package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.*;

public final class OreVeinChunkPlanner {
    static final PlanningDependencies PRODUCTION_DEPENDENCIES = new PlanningDependencies(
            OreVeinCandidateLookup::candidatesForChunk,
            OreVeinCandidateLookup::evaluationBounds,
            OreVeinResolvedCellResolver::resolve
    );

    private OreVeinChunkPlanner() {
    }

    public static OreVeinChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive) {
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, PRODUCTION_DEPENDENCIES);
    }

    static OreVeinChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, PlanningDependencies dependencies) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(dependencies, "dependencies");

        List<OreVeinInstanceDescriptor> candidates = dependencies.candidateProvider().candidatesForChunk(worldSeed, dimension, chunkPos, minY, maxYExclusive);
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, dependencies);
    }

    public static OreVeinChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates) {
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, PRODUCTION_DEPENDENCIES);
    }

    static OreVeinChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates, PlanningDependencies dependencies) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(dependencies, "dependencies");

        int bitCount = logicalBitCount(minY, maxYExclusive);

        if (candidates.isEmpty()) {
            return new OreVeinChunkPlan(chunkPos, dimension, List.of());
        }

        BitSet visited = new BitSet(bitCount);
        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMaxX = chunkPos.getMaxBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();
        int chunkMaxZ = chunkPos.getMaxBlockZ();
        OreVeinBounds chunkBounds = new OreVeinBounds(chunkMinX, minY, chunkMinZ, chunkMaxX, maxYExclusive - 1, chunkMaxZ);
        List<OreVeinChunkPlan.PlannedReplacement> replacements = new ArrayList<>();
        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        for (OreVeinInstanceDescriptor candidate : candidates) {
            evaluationBounds.add(dependencies.boundsProvider().evaluationBounds(candidate));
        }

        for (int i = 0; i < candidates.size(); i++) {
            OreVeinBounds clipped = evaluationBounds.get(i).intersect(chunkBounds);

            if (clipped == null) {
                continue;
            }

            for (int y = clipped.minY(); y <= clipped.maxY(); y++) {
                for (int z = clipped.minZ(); z <= clipped.maxZ(); z++) {
                    for (int x = clipped.minX(); x <= clipped.maxX(); x++) {
                        int index = bitIndex(x, y, z, chunkMinX, chunkMinZ, minY);

                        if (visited.get(index)) {
                            continue;
                        }

                        visited.set(index);

                        Optional<OreVeinResolvedCell> resolvedCell = dependencies.resolvedCellProvider().resolve(
                                worldSeed,
                                dimension,
                                new BlockPos(x, y, z),
                                candidates
                        );

                        if (resolvedCell.isPresent() && resolvedCell.get().replacement()) {
                            replacements.add(new OreVeinChunkPlan.PlannedReplacement(new BlockPos(x, y, z), resolvedCell.get()));
                        }
                    }
                }
            }
        }

        replacements.sort((left, right) -> {
            int byY = Integer.compare(left.position().getY(), right.position().getY());
            if (byY != 0) {
                return byY;
            }

            int byZ = Integer.compare(left.position().getZ(), right.position().getZ());
            if (byZ != 0) {
                return byZ;
            }

            return Integer.compare(left.position().getX(), right.position().getX());
        });
        return new OreVeinChunkPlan(chunkPos, dimension, replacements);
    }

    static int logicalBitCount(int minY, int maxYExclusive) {
        int height = Math.subtractExact(maxYExclusive, minY);

        if (height <= 0) {
            throw new IllegalArgumentException("maxYExclusive must be greater than minY");
        }

        return Math.multiplyExact(16 * 16, height);
    }

    static int bitIndex(int x, int y, int z, int chunkMinX, int chunkMinZ, int minY) {
        return Math.addExact(
                Math.multiplyExact(
                        Math.addExact(
                                Math.multiplyExact(y - minY, 16),
                                z - chunkMinZ
                        ),
                        16
                ),
                x - chunkMinX
        );
    }

    @FunctionalInterface
    interface CandidateProvider {
        List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive);
    }

    @FunctionalInterface
    interface EvaluationBoundsProvider {
        OreVeinBounds evaluationBounds(OreVeinInstanceDescriptor descriptor);
    }

    @FunctionalInterface
    interface ResolvedCellProvider {
        Optional<OreVeinResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates);
    }

    record PlanningDependencies(
            CandidateProvider candidateProvider,
            EvaluationBoundsProvider boundsProvider,
            ResolvedCellProvider resolvedCellProvider
    ) {
        PlanningDependencies {
            Objects.requireNonNull(candidateProvider, "candidateProvider");
            Objects.requireNonNull(boundsProvider, "boundsProvider");
            Objects.requireNonNull(resolvedCellProvider, "resolvedCellProvider");
        }
    }
}
