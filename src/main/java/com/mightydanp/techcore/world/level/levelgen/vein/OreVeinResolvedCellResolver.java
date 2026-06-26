package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Supplier;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator.OreCellResult.OreVariant.DENSE_ORE;

public final class OreVeinResolvedCellResolver {
    private static final long WINNER_POSITION_SALT = 0xE9C56AF8D1B54A32L;
    private static final long OVERLAP_GAP_SALT = 0xA24BAED4963EE407L;
    private static final Comparator<OreVeinOreCellEvaluator.OreCellResult> RESULT_ORDER = Comparator.comparingLong(OreVeinOreCellEvaluator.OreCellResult::instanceId)
            .thenComparing(result -> result.definitionId().toString());

    public static Optional<ResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        // If the dimension is unsupported return nothing
        if (OreVeinDefinitions.isUnsupportedDimension(dimension)) return Optional.empty();

        Material originalHostMaterial = requireOriginalHostMaterial(worldSeed, dimension, position);
        BlockState originalHostState = requireOriginalHostState(worldSeed, dimension, position, originalHostMaterial);

        return resolve(
                worldSeed,
                dimension,
                position,
                OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position),
                originalHostMaterial,
                originalHostState
        );
    }

    public static Optional<ResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");

        // Unsupported dimensions never produce ore-cell replacements.
        if (OreVeinDefinitions.isUnsupportedDimension(dimension)) return Optional.empty();

        Material originalHostMaterial = requireOriginalHostMaterial(worldSeed, dimension, position);
        BlockState originalHostState = requireOriginalHostState(worldSeed, dimension, position, originalHostMaterial);

        return resolve(worldSeed, dimension, position, candidates, originalHostMaterial, originalHostState);
    }


    @Contract("_, _, _, _, _, _ -> new")
    private static @NotNull Optional<ResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates, Material originalHostMaterial, BlockState originalHostState) {
        return Optional.of(resolve(worldSeed, dimension, position, originalHostMaterial, originalHostState, oreCellResultsForPosition(worldSeed, dimension, position, candidates)));
    }

    public static @NotNull @Unmodifiable List<OreVeinOreCellEvaluator.OreCellResult> oreCellResultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        return oreCellResultsForPosition(worldSeed, dimension, position, OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position));
    }

    public static @NotNull @Unmodifiable List<OreVeinOreCellEvaluator.OreCellResult> oreCellResultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");

        // Collect the evaluation bounds once so each candidate can be filtered consistently.
        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        for (OreVeinInstanceDescriptor candidate : candidates) evaluationBounds.add(OreVeinCandidateLookup.evaluationBounds(candidate));

        Material originalHostMaterial = requireOriginalHostMaterial(worldSeed, dimension, position);

        return oreCellResultsForPosition(position, candidates, evaluationBounds, new OreCellEvaluator() {
            @Override
            public OreVeinOreCellEvaluator.OreCellResult evaluate(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos evaluatedPosition, OreVeinShapeEvaluator.ShapeContribution contribution) {
                return OreVeinOreCellEvaluator.evaluateCell(descriptor, definition, evaluatedPosition, contribution, originalHostMaterial);
            }

            @Override
            public boolean canEvaluate(OreVeinDefinition definition) {
                return OreVeinOreCellEvaluator.hasCompatibleOreEntry(definition, originalHostMaterial);
            }
        });
    }

    private static @NotNull @Unmodifiable List<OreVeinOreCellEvaluator.OreCellResult> oreCellResultsForPosition(BlockPos position, List<OreVeinInstanceDescriptor> candidates, List<OreVeinBounds> evaluationBounds, OreCellEvaluator oreCellEvaluator) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(evaluationBounds, "evaluationBounds");
        Objects.requireNonNull(oreCellEvaluator, "oreCellEvaluator");

        if (candidates.size() != evaluationBounds.size()) throw new IllegalArgumentException("candidates and evaluationBounds must have the same size");

        List<OreVeinOreCellEvaluator.OreCellResult> results = new ArrayList<>();

        for (int i = 0; i < candidates.size(); i++) {
            OreVeinInstanceDescriptor descriptor = candidates.get(i);

            if (!evaluationBounds.get(i).contains(position)) continue;

            OreVeinShapeEvaluator.ShapeContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);
            OreVeinDefinition definition = OreVeinDefinitions.requireDefinition(descriptor);

            if (contribution.signedBoundaryDistanceBlocks() > definition.sparseReachBlocks()) continue;
            if (!oreCellEvaluator.canEvaluate(definition)) continue;

            results.add(oreCellEvaluator.evaluate(descriptor, definition, position, contribution));
        }

        results.sort(RESULT_ORDER);
        return List.copyOf(results);
    }

    private static @NotNull OreVeinResolvedCellResolver.ResolvedCell resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, BlockState originalHostState, List<OreVeinOreCellEvaluator.OreCellResult> results) {
        // Get only ore results that can replace the original host rock
        List<OreVeinOreCellEvaluator.OreCellResult> compatibleOreResults = compatibleOreResults(results, originalHostMaterial);

        if (compatibleOreResults.isEmpty())
            return hostResult(position, originalHostMaterial, originalHostState, null, false, false, List.of());

        OreVeinOreCellEvaluator.OreCellResult winner = pickWinner(worldSeed, dimension, position, compatibleOreResults);
        List<Long> participatingGapIds = eligibleGapInstanceIds(results, originalHostMaterial, winner.instanceId());
        boolean overlapGapEvaluated = !participatingGapIds.isEmpty();
        boolean overlapGapWon = overlapGapEvaluated && overlapGapWins(worldSeed, dimension, position, winner.instanceId(), participatingGapIds);

        if (overlapGapWon)
            return hostResult(position, originalHostMaterial, originalHostState, winner, true, true, participatingGapIds);

        BlockState resolvedBlockState = resolveOreState(dimension, position, originalHostMaterial, winner);

        return new ResolvedCell(position, originalHostMaterial, originalHostState, winner, resolvedBlockState, true, overlapGapEvaluated, false, participatingGapIds);
    }

    private static @NotNull List<OreVeinOreCellEvaluator.OreCellResult> compatibleOreResults(@NotNull List<OreVeinOreCellEvaluator.OreCellResult> results, Material originalHostMaterial) {
        List<OreVeinOreCellEvaluator.OreCellResult> compatible = new ArrayList<>();

        for (OreVeinOreCellEvaluator.OreCellResult result : results) {
            if (result.finalDensity() <= 0) continue;

            if (OreVeinDefinitions.isOreCompatibleWithHost(result.selectedMaterial(), originalHostMaterial)) compatible.add(result);
        }

        return compatible;
    }

    private static @NotNull OreVeinOreCellEvaluator.OreCellResult pickWinner(long worldSeed, ResourceKey<Level> dimension, BlockPos position, @NotNull List<OreVeinOreCellEvaluator.OreCellResult> compatibleOreResults) {
        // Rank compatible results by variant, density, distance, and deterministic tie breakers.
        return compatibleOreResults.stream().min((left, right) -> {
            int byVariant = Integer.compare(variantRank(right.variant()), variantRank(left.variant()));

            if (byVariant != 0) return byVariant;

            int byDensity = Integer.compare(right.finalDensity(), left.finalDensity());

            if (byDensity != 0) return byDensity;

            int byDistance = Double.compare(left.shapeContribution().signedBoundaryDistanceBlocks(), right.shapeContribution().signedBoundaryDistanceBlocks());

            if (byDistance != 0) return byDistance;

            long leftHash = winnerHash(worldSeed, dimension, position, left.instanceId());
            long rightHash = winnerHash(worldSeed, dimension, position, right.instanceId());
            int byHash = Long.compareUnsigned(rightHash, leftHash);

            if (byHash != 0) return byHash;

            int byInstance = Long.compare(left.instanceId(), right.instanceId());

            if (byInstance != 0) return byInstance;

            return left.definitionId().toString().compareTo(right.definitionId().toString());
        }).orElseThrow();
    }

    private static long winnerHash(long worldSeed, ResourceKey<Level> dimension, BlockPos position, long instanceId) {
        return OreVeinGenerationMath.mix64(
                positionHash(worldSeed, dimension, position, WINNER_POSITION_SALT) ^ instanceId
        );
    }

    private static long positionHash(long worldSeed, ResourceKey<Level> dimension, @NotNull BlockPos position, long salt) {
        long hash = OreVeinGenerationMath.hashSeedAndDimension(worldSeed, dimension, salt);
        hash = OreVeinGenerationMath.mix64(
                hash ^ position.getX()
        );
        hash = OreVeinGenerationMath.mix64(
                hash ^ position.getY()
        );

        return OreVeinGenerationMath.mix64(
                hash ^ position.getZ()
        );
    }


    private static @NotNull @Unmodifiable List<Long> eligibleGapInstanceIds(@NotNull List<OreVeinOreCellEvaluator.OreCellResult> results, Material originalHostMaterial, long winningInstanceId) {
        Set<Long> gapIds = new LinkedHashSet<>();

        for (OreVeinOreCellEvaluator.OreCellResult result : results) {
            if (result.instanceId() == winningInstanceId) continue;


            if (result.variant() != OreVeinOreCellEvaluator.OreCellResult.OreVariant.HOST_ROCK) continue;


            if (result.shapeContribution().signedBoundaryDistanceBlocks() > 0.0D) continue;


            if (!OreVeinDefinitions.isOreCompatibleWithHost(result.selectedMaterial(), originalHostMaterial)) continue;

            gapIds.add(result.instanceId());
        }

        return gapIds.stream().sorted(Long::compare).toList();
    }

    private static boolean overlapGapWins(long worldSeed, ResourceKey<Level> dimension, BlockPos position, long winningInstanceId, List<Long> participatingGapIds) {
        OreVeinDefinitions.OverlapSettings settings = OreVeinDefinitions.getOverlapSettings(dimension);

        if (settings == null) throw new IllegalStateException("Missing ore vein overlap settings for dimension " + dimension.location());

        long hash = positionHash(worldSeed, dimension, position, OVERLAP_GAP_SALT);
        hash = OreVeinGenerationMath.mix64(hash ^ winningInstanceId);

        for (long gapId : participatingGapIds) hash = OreVeinGenerationMath.mix64(hash ^ gapId);

        return Math.floorMod(hash, settings.denominator()) < settings.mainBodyGapNumerator();
    }

    private static BlockState resolveOreState(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, @NotNull OreVeinOreCellEvaluator.OreCellResult winner) {
        // Select the correct replacement map for the winning ore variant.
        Supplier<Block> supplier = switch (winner.variant()) {
            case REGULAR_ORE ->
                    supplierFor(winner.selectedMaterial().ore.getOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case DENSE_ORE ->
                    supplierFor(winner.selectedMaterial().ore.getDenseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case SPARSE_ORE ->
                    supplierFor(winner.selectedMaterial().ore.getSparseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            case HOST_ROCK ->
                    throw invalidReplacement("host-rock winner is not replaceable", dimension, position, originalHostMaterial, winner);
        };

        Block block = supplier.get();

        if (block == null) throw invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);

        BlockState state = block.defaultBlockState();

        if (winner.variant() != DENSE_ORE) return state;

        if (!state.hasProperty(DenseOre.DENSITY))
            throw invalidReplacement("dense block missing DenseOre.DENSITY", dimension, position, originalHostMaterial, winner);

        if (!DenseOre.DENSITY.getPossibleValues().contains(winner.finalDensity()))
            throw invalidReplacement("unsupported exact density value", dimension, position, originalHostMaterial, winner);

        return state.setValue(DenseOre.DENSITY, winner.finalDensity());
    }

    private static @NotNull Supplier<Block> supplierFor(@NotNull Map<String, Supplier<Block>> blocks, String hostName, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellEvaluator.OreCellResult winner) {
        if (!blocks.containsKey(hostName))
            throw invalidReplacement("missing block mapping for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);

        Supplier<Block> supplier = blocks.get(hostName);

        if (supplier == null)
            throw invalidReplacement("null block supplier for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);

        return supplier;
    }

    @Contract("_, _, _, _, _ -> new")
    private static @NotNull IllegalStateException invalidReplacement(String reason, @NotNull ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, @NotNull OreVeinOreCellEvaluator.OreCellResult winner) {
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

    @Contract("_, _, _, _, _, _, _ -> new")
    private static @NotNull OreVeinResolvedCellResolver.ResolvedCell hostResult(BlockPos position, Material originalHostMaterial, BlockState originalHostState, OreVeinOreCellEvaluator.OreCellResult winner, boolean overlapGapEvaluated, boolean overlapGapWon, List<Long> participatingGapIds) {
        return new ResolvedCell(position, originalHostMaterial, originalHostState, winner, originalHostState, false, overlapGapEvaluated, overlapGapWon, participatingGapIds);
    }

    @Contract(pure = true)
    static int variantRank(OreVeinOreCellEvaluator.OreCellResult.@NotNull OreVariant variant) {
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

    private static @NotNull Material requireOriginalHostMaterial(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Material material = RockLayerFeature.getOriginalRockMaterial(worldSeed, dimension, position);

        if (material == null) throw missingOriginalGeology(dimension, position, null);

        return material;
    }

    private static @NotNull BlockState requireOriginalHostState(long worldSeed, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial) {
        BlockState state = RockLayerFeature.getOriginalRockState(worldSeed, dimension, position);

        if (state == null) throw missingOriginalGeology(dimension, position, originalHostMaterial);

        return state;
    }

    @Contract("_, _, _ -> new")
    private static @NotNull IllegalStateException missingOriginalGeology(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial) {
        return new IllegalStateException(
                "Unable to resolve original geology for supported dimension:"
                        + " dimension=" + dimension.location()
                        + ", position=" + position
                        + ", host=" + materialName(originalHostMaterial)
                        + ", veinId=none"
                        + ", oreMaterial=none"
                        + ", variant=HOST_ROCK"
                        + ", density=0"
        );
    }

    @FunctionalInterface
    private interface OreCellEvaluator {
        OreVeinOreCellEvaluator.OreCellResult evaluate(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution);

        default boolean canEvaluate(OreVeinDefinition definition) {
            return true;
        }
    }

    public record ResolvedCell(BlockPos position, Material originalHostMaterial, BlockState originalHostState, OreVeinOreCellEvaluator.OreCellResult winningOreCellResult, BlockState resolvedBlockState, boolean replacement, boolean overlapGapEvaluated, boolean overlapGapWon, List<Long> participatingMainBodyGapInstanceIds) {
        public ResolvedCell {
            Objects.requireNonNull(position, "position");
            Objects.requireNonNull(originalHostMaterial, "originalHostMaterial");
            Objects.requireNonNull(originalHostState, "originalHostState");
            Objects.requireNonNull(resolvedBlockState, "resolvedBlockState");
            participatingMainBodyGapInstanceIds = List.copyOf(participatingMainBodyGapInstanceIds);
        }
    }
}
