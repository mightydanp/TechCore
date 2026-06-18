package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class OreVeinChunkPlanner {
    private static final PlanningDependencies PRODUCTION_DEPENDENCIES = new PlanningDependencies(OreVeinCandidateLookup::candidatesForChunk, OreVeinCandidateLookup::evaluationBounds, OreVeinResolvedCellResolver::resolve);
    private static final Comparator<ChunkPlan.PlannedReplacement> REPLACEMENT_ORDER = Comparator.comparingInt((ChunkPlan.PlannedReplacement replacement) -> replacement.position().getY())
                    .thenComparingInt(replacement -> replacement.position().getZ())
                    .thenComparingInt(replacement -> replacement.position().getX());

    public static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive) {
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, PRODUCTION_DEPENDENCIES);
    }

    private static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, PlanningDependencies dependencies) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(dependencies, "dependencies");

        List<OreVeinInstanceDescriptor> candidates = dependencies.candidateProvider().candidatesForChunk(worldSeed, dimension, chunkPos, minY, maxYExclusive);
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, dependencies);
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates) {
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, PRODUCTION_DEPENDENCIES);
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    private static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates, PlanningDependencies dependencies) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(dependencies, "dependencies");

        int bitCount = logicalBitCount(minY, maxYExclusive);

        if (candidates.isEmpty()) return new ChunkPlan(chunkPos, dimension, List.of());

        BitSet visited = new BitSet(bitCount);
        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMaxX = chunkPos.getMaxBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();
        int chunkMaxZ = chunkPos.getMaxBlockZ();
        OreVeinBounds chunkBounds = new OreVeinBounds(chunkMinX, minY, chunkMinZ, chunkMaxX, maxYExclusive - 1, chunkMaxZ);
        List<ChunkPlan.PlannedReplacement> replacements = new ArrayList<>();
        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        for (OreVeinInstanceDescriptor candidate : candidates) evaluationBounds.add(dependencies.boundsProvider().evaluationBounds(candidate));


        for (int i = 0; i < candidates.size(); i++) {
            OreVeinBounds clipped = evaluationBounds.get(i).intersect(chunkBounds);

            if (clipped == null) continue;

            for (int y = clipped.minY(); y <= clipped.maxY(); y++) {
                for (int z = clipped.minZ(); z <= clipped.maxZ(); z++) {
                    for (int x = clipped.minX(); x <= clipped.maxX(); x++) {
                        int index = bitIndex(x, y, z, chunkMinX, chunkMinZ, minY);

                        if (visited.get(index)) continue;

                        visited.set(index);

                        BlockPos position = new BlockPos(x, y, z);

                        dependencies.resolvedCellProvider()
                                .resolve(worldSeed, dimension, position, candidates)
                                .filter(OreVeinResolvedCellResolver.ResolvedCell::replacement)
                                .ifPresent(resolvedCell -> replacements.add(new ChunkPlan.PlannedReplacement(position, resolvedCell)));
                    }
                }
            }
        }

        replacements.sort(REPLACEMENT_ORDER);

        return new ChunkPlan(chunkPos, dimension, replacements);
    }

    private static int logicalBitCount(int minY, int maxYExclusive) {
        int height = Math.subtractExact(maxYExclusive, minY);

        if (height <= 0) throw new IllegalArgumentException("maxYExclusive must be greater than minY");

        return Math.multiplyExact(16 * 16, height);
    }

    private static int bitIndex(int x, int y, int z, int chunkMinX, int chunkMinZ, int minY) {
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
    private interface CandidateProvider {
        List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive);
    }

    @FunctionalInterface
    private interface EvaluationBoundsProvider {
        OreVeinBounds evaluationBounds(OreVeinInstanceDescriptor descriptor);
    }

    @FunctionalInterface
    private interface ResolvedCellProvider {
        Optional<OreVeinResolvedCellResolver.ResolvedCell> resolve(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates);
    }

    private record PlanningDependencies(CandidateProvider candidateProvider, EvaluationBoundsProvider boundsProvider, ResolvedCellProvider resolvedCellProvider) {
        PlanningDependencies {
            Objects.requireNonNull(candidateProvider, "candidateProvider");
            Objects.requireNonNull(boundsProvider, "boundsProvider");
            Objects.requireNonNull(resolvedCellProvider, "resolvedCellProvider");
        }
    }

    public record ChunkPlan(ChunkPos chunkPos, ResourceKey<Level> dimension, List<PlannedReplacement> replacements) {
        public ChunkPlan {
            Objects.requireNonNull(chunkPos, "chunkPos");
            Objects.requireNonNull(dimension, "dimension");
            replacements = List.copyOf(Objects.requireNonNull(replacements, "replacements"));
        }

        public boolean isEmpty() {
            return replacements.isEmpty();
        }

        public record PlannedReplacement(BlockPos position, OreVeinResolvedCellResolver.ResolvedCell resolvedCell) {
            public PlannedReplacement {
                position = Objects.requireNonNull(position, "position").immutable();
                Objects.requireNonNull(resolvedCell, "resolvedCell");
            }
        }
    }
}
