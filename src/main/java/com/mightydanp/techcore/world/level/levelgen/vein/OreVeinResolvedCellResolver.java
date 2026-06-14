package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.DENSE_ORE;

public final class OreVeinResolvedCellResolver {
    static final long WINNER_POSITION_SALT = 0xE9C56AF8D1B54A32L;
    static final long OVERLAP_GAP_SALT = 0xA24BAED4963EE407L;

    private OreVeinResolvedCellResolver() {
    }

    public static Optional<OreVeinResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        if (!OreVeinDefinitions.isSupportedDimension(dimension)) {
            return Optional.empty();
        }

        Material originalHostMaterial = RockLayerFeature.getOriginalRockMaterial(worldSeed, dimension, position);
        BlockState originalHostState = RockLayerFeature.getOriginalRockState(worldSeed, dimension, position);

        if (originalHostMaterial == null || originalHostState == null) {
            throw new IllegalStateException("Unable to resolve original geology for supported dimension:"
                    + " dimension=" + dimension.location()
                    + ", position=" + position
                    + ", host=" + materialName(originalHostMaterial)
                    + ", veinId=none"
                    + ", oreMaterial=none"
                    + ", variant=HOST_ROCK"
                    + ", density=0");
        }

        return resolve(
                worldSeed,
                dimension,
                position,
                OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position),
                originalHostMaterial,
                originalHostState
        );
    }

    public static Optional<OreVeinResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");

        if (!OreVeinDefinitions.isSupportedDimension(dimension)) {
            return Optional.empty();
        }

        Material originalHostMaterial = RockLayerFeature.getOriginalRockMaterial(worldSeed, dimension, position);
        BlockState originalHostState = RockLayerFeature.getOriginalRockState(worldSeed, dimension, position);

        if (originalHostMaterial == null || originalHostState == null) {
            throw new IllegalStateException("Unable to resolve original geology for supported dimension:"
                    + " dimension=" + dimension.location()
                    + ", position=" + position
                    + ", host=" + materialName(originalHostMaterial)
                    + ", veinId=none"
                    + ", oreMaterial=none"
                    + ", variant=HOST_ROCK"
                    + ", density=0");
        }

        return resolve(worldSeed, dimension, position, candidates, originalHostMaterial, originalHostState);
    }

    private static Optional<OreVeinResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates, Material originalHostMaterial, BlockState originalHostState) {
        return Optional.of(resolve(
                worldSeed,
                dimension,
                position,
                originalHostMaterial,
                originalHostState,
                OreVeinOreCellLookup.resultsForPosition(worldSeed, dimension, position, candidates)
        ));
    }

    static OreVeinResolvedCell resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, BlockState originalHostState, List<OreVeinOreCellResult> results) {
        List<OreVeinOreCellResult> compatibleOreResults = compatibleOreResults(results, originalHostMaterial);

        if (compatibleOreResults.isEmpty()) {
            return hostResult(position, originalHostMaterial, originalHostState, null, false, false, List.of());
        }

        OreVeinOreCellResult winner = pickWinner(worldSeed, dimension, position, compatibleOreResults);
        List<Long> participatingGapIds = eligibleGapInstanceIds(results, originalHostMaterial, winner.instanceId());
        boolean overlapGapEvaluated = !participatingGapIds.isEmpty();
        boolean overlapGapWon = overlapGapEvaluated && overlapGapWins(worldSeed, dimension, position, winner.instanceId(), participatingGapIds);

        if (overlapGapWon) {
            return hostResult(position, originalHostMaterial, originalHostState, winner, true, true, participatingGapIds);
        }

        BlockState resolvedBlockState = resolveOreState(dimension, position, originalHostMaterial, winner);
        return new OreVeinResolvedCell(position, originalHostMaterial, originalHostState, winner, resolvedBlockState, true, overlapGapEvaluated, false, participatingGapIds);
    }

    static List<OreVeinOreCellResult> compatibleOreResults(List<OreVeinOreCellResult> results, Material originalHostMaterial) {
        List<OreVeinOreCellResult> compatible = new ArrayList<>();

        for (OreVeinOreCellResult result : results) {
            if (result.finalDensity() <= 0) {
                continue;
            }

            if (OreVeinDefinitions.isOreCompatibleWithHost(result.selectedMaterial(), originalHostMaterial)) {
                compatible.add(result);
            }
        }

        return compatible;
    }

    static OreVeinOreCellResult pickWinner(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinOreCellResult> compatibleOreResults) {
        return compatibleOreResults.stream()
                .sorted((left, right) -> {
                    int byVariant = Integer.compare(variantRank(right.variant()), variantRank(left.variant()));

                    if (byVariant != 0) {
                        return byVariant;
                    }

                    int byDensity = Integer.compare(right.finalDensity(), left.finalDensity());

                    if (byDensity != 0) {
                        return byDensity;
                    }

                    int byDistance = Double.compare(left.shapeContribution().signedBoundaryDistanceBlocks(), right.shapeContribution().signedBoundaryDistanceBlocks());

                    if (byDistance != 0) {
                        return byDistance;
                    }

                    long leftHash = winnerHash(worldSeed, dimension, position, left.instanceId());
                    long rightHash = winnerHash(worldSeed, dimension, position, right.instanceId());
                    int byHash = Long.compareUnsigned(rightHash, leftHash);

                    if (byHash != 0) {
                        return byHash;
                    }

                    int byInstance = Long.compare(left.instanceId(), right.instanceId());

                    if (byInstance != 0) {
                        return byInstance;
                    }

                    return left.definitionId().toString().compareTo(right.definitionId().toString());
                })
                .findFirst()
                .orElseThrow();
    }

    private static long winnerHash(long worldSeed, ResourceKey<Level> dimension, BlockPos position, long instanceId) {
        long hash = OreVeinGenerationMath.hashSeedAndDimension(worldSeed, dimension, WINNER_POSITION_SALT);
        hash = OreVeinGenerationMath.mix64(hash ^ position.getX());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getY());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getZ());
        return OreVeinGenerationMath.mix64(hash ^ instanceId);
    }

    static List<Long> eligibleGapInstanceIds(List<OreVeinOreCellResult> results, Material originalHostMaterial, long winningInstanceId) {
        Set<Long> gapIds = new LinkedHashSet<>();

        for (OreVeinOreCellResult result : results) {
            if (result.instanceId() == winningInstanceId) {
                continue;
            }

            if (result.variant() != OreVeinOreCellResult.OreVariant.HOST_ROCK) {
                continue;
            }

            if (result.shapeContribution().signedBoundaryDistanceBlocks() > 0.0D) {
                continue;
            }

            if (!OreVeinDefinitions.isOreCompatibleWithHost(result.selectedMaterial(), originalHostMaterial)) {
                continue;
            }

            gapIds.add(result.instanceId());
        }

        return gapIds.stream().sorted(Long::compare).toList();
    }

    static boolean overlapGapWins(long worldSeed, ResourceKey<Level> dimension, BlockPos position, long winningInstanceId, List<Long> participatingGapIds) {
        OreVeinOverlapSettings settings = OreVeinDefinitions.getOverlapSettings(dimension);

        if (settings == null) {
            throw new IllegalStateException("Missing ore vein overlap settings for dimension " + dimension.location());
        }
        long hash = OreVeinGenerationMath.hashSeedAndDimension(worldSeed, dimension, OVERLAP_GAP_SALT);
        hash = OreVeinGenerationMath.mix64(hash ^ position.getX());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getY());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getZ());
        hash = OreVeinGenerationMath.mix64(hash ^ winningInstanceId);

        for (long gapId : participatingGapIds) {
            hash = OreVeinGenerationMath.mix64(hash ^ gapId);
        }

        return Math.floorMod(hash, settings.denominator()) < settings.mainBodyGapNumerator();
    }

    private static BlockState resolveOreState(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellResult winner) {
        Supplier<Block> supplier = switch (winner.variant()) {
            case REGULAR_ORE -> supplierFor(winner.selectedMaterial().ore.getOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case DENSE_ORE -> supplierFor(winner.selectedMaterial().ore.getDenseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case SPARSE_ORE -> supplierFor(winner.selectedMaterial().ore.getSparseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case HOST_ROCK -> throw invalidReplacement("host-rock winner is not replaceable", dimension, position, originalHostMaterial, winner);
        };
        Block block = supplier.get();

        if (block == null) {
            throw invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);
        }

        BlockState state = block.defaultBlockState();

        if (winner.variant() != DENSE_ORE) {
            return state;
        }

        if (!state.hasProperty(DenseOre.DENSITY)) {
            throw invalidReplacement("dense block missing DenseOre.DENSITY", dimension, position, originalHostMaterial, winner);
        }

        if (!DenseOre.DENSITY.getPossibleValues().contains(winner.finalDensity())) {
            throw invalidReplacement("unsupported exact density value", dimension, position, originalHostMaterial, winner);
        }

        return state.setValue(DenseOre.DENSITY, winner.finalDensity());
    }

    private static Supplier<Block> supplierFor(Map<String, Supplier<Block>> blocks, String hostName, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellResult winner) {
        if (!blocks.containsKey(hostName)) {
            throw invalidReplacement("missing block mapping for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);
        }

        Supplier<Block> supplier = blocks.get(hostName);
        if (supplier == null) {
            throw invalidReplacement("null block supplier for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);
        }

        return supplier;
    }

    private static IllegalStateException invalidReplacement(String reason, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellResult winner) {
        return new IllegalStateException("Invalid resolved ore replacement:"
                + " dimension=" + dimension.location()
                + ", position=" + position
                + ", host=" + materialName(originalHostMaterial)
                + ", veinId=" + winner.definitionId()
                + ", oreMaterial=" + materialName(winner.selectedMaterial())
                + ", variant=" + winner.variant()
                + ", density=" + winner.finalDensity()
                + ", reason=" + reason);
    }

    private static OreVeinResolvedCell hostResult(BlockPos position, Material originalHostMaterial, BlockState originalHostState, OreVeinOreCellResult winner, boolean overlapGapEvaluated, boolean overlapGapWon, List<Long> participatingGapIds) {
        return new OreVeinResolvedCell(position, originalHostMaterial, originalHostState, winner, originalHostState, false, overlapGapEvaluated, overlapGapWon, participatingGapIds);
    }

    static int variantRank(OreVeinOreCellResult.OreVariant variant) {
        return switch (variant) {
            case DENSE_ORE -> 3;
            case REGULAR_ORE -> 2;
            case SPARSE_ORE -> 1;
            case HOST_ROCK -> 0;
        };
    }

    private static String materialName(Material material) {
        return material == null ? "null" : material.name;
    }
}
