package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class OreVeinChunkPlanner {
    // Use the normal planner dependencies by default
    private static final CandidateProvider PRODUCTION_CANDIDATE_PROVIDER = OreVeinCandidateLookup::candidatesForChunk;
    private static final EvaluationBoundsProvider PRODUCTION_BOUNDS_PROVIDER = OreVeinCandidateLookup::evaluationBounds;
    private static final ResolvedCellProvider PRODUCTION_RESOLVED_CELL_PROVIDER = OreVeinResolvedCellResolver::resolve;
    private static final Comparator<ChunkPlan.PlannedReplacement> REPLACEMENT_ORDER = Comparator.comparingInt((ChunkPlan.PlannedReplacement replacement) -> replacement.position().getY()).thenComparingInt(replacement -> replacement.position().getZ()).thenComparingInt(replacement -> replacement.position().getX());

    public static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive) {
        // Plan this chunk with the normal production lookup and resolution providers.
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, PRODUCTION_CANDIDATE_PROVIDER, PRODUCTION_BOUNDS_PROVIDER, PRODUCTION_RESOLVED_CELL_PROVIDER);
    }

    private static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, CandidateProvider candidateProvider, EvaluationBoundsProvider boundsProvider, ResolvedCellProvider resolvedCellProvider) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(candidateProvider, "candidateProvider");
        Objects.requireNonNull(boundsProvider, "boundsProvider");
        Objects.requireNonNull(resolvedCellProvider, "resolvedCellProvider");

        // Get the candidate veins for this chunk
        List<OreVeinInstanceDescriptor> candidates = candidateProvider.candidatesForChunk(worldSeed, dimension, chunkPos, minY, maxYExclusive);
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, boundsProvider, resolvedCellProvider);
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates) {
        // Reuse the same planning pipeline when the caller already has candidate veins.
        return plan(worldSeed, dimension, chunkPos, minY, maxYExclusive, candidates, PRODUCTION_BOUNDS_PROVIDER, PRODUCTION_RESOLVED_CELL_PROVIDER);
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    private static @NotNull ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive, List<OreVeinInstanceDescriptor> candidates, EvaluationBoundsProvider boundsProvider, ResolvedCellProvider resolvedCellProvider) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(boundsProvider, "boundsProvider");
        Objects.requireNonNull(resolvedCellProvider, "resolvedCellProvider");

        // Get the amount of visited bits needed for this chunk height
        int bitCount = logicalBitCount(minY, maxYExclusive);

        // If there are no candidates return an empty plan
        if (candidates.isEmpty()) return new ChunkPlan(chunkPos, dimension, List.of());

        BitSet visited = new BitSet(bitCount);
        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMaxX = chunkPos.getMaxBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();
        int chunkMaxZ = chunkPos.getMaxBlockZ();
        OreVeinBounds chunkBounds = new OreVeinBounds(chunkMinX, minY, chunkMinZ, chunkMaxX, maxYExclusive - 1, chunkMaxZ);
        List<OreVeinBounds> evaluationBounds = collectEvaluationBounds(candidates, boundsProvider);
        List<ChunkPlan.PlannedReplacement> replacements = collectReplacements(
                worldSeed,
                dimension,
                candidates,
                collectClippedRegions(evaluationBounds, chunkBounds),
                resolvedCellProvider,
                visited,
                chunkMinX,
                chunkMinZ,
                minY
        );

        replacements.sort(REPLACEMENT_ORDER);

        // Return the chunk plan with all replacements in sorted order
        return new ChunkPlan(chunkPos, dimension, replacements);
    }

    private static @NotNull List<OreVeinBounds> collectEvaluationBounds(List<OreVeinInstanceDescriptor> candidates, EvaluationBoundsProvider boundsProvider) {
        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        // Get the evaluation bounds for each candidate first
        for (OreVeinInstanceDescriptor candidate : candidates)
            evaluationBounds.add(boundsProvider.evaluationBounds(candidate));

        return evaluationBounds;
    }

    private static @NotNull List<@Nullable OreVeinBounds> collectClippedRegions(List<OreVeinBounds> evaluationBounds, OreVeinBounds chunkBounds) {
        List<@Nullable OreVeinBounds> clippedRegions = new ArrayList<>(evaluationBounds.size());

        // Clip every evaluation area down to the current chunk bounds.
        for (OreVeinBounds evaluationBound : evaluationBounds)
            clippedRegions.add(evaluationBound.intersect(chunkBounds));

        return clippedRegions;
    }

    private static @NotNull List<ChunkPlan.PlannedReplacement> collectReplacements(long worldSeed, ResourceKey<Level> dimension, List<OreVeinInstanceDescriptor> candidates, List<@Nullable OreVeinBounds> clippedRegions, ResolvedCellProvider resolvedCellProvider, BitSet visited, int chunkMinX, int chunkMinZ, int minY) {
        List<ChunkPlan.PlannedReplacement> replacements = new ArrayList<>();

        // Loop through each candidate veins clipped area
        for (int i = 0; i < candidates.size(); i++) {
            OreVeinBounds clipped = clippedRegions.get(i);

            if (clipped == null) continue;

            for (int y = clipped.minY(); y <= clipped.maxY(); y++) {
                for (int z = clipped.minZ(); z <= clipped.maxZ(); z++) {
                    for (int x = clipped.minX(); x <= clipped.maxX(); x++) {
                        int index = bitIndex(x, y, z, chunkMinX, chunkMinZ, minY);

                        if (visited.get(index)) continue;

                        // If this block position was already checked skip it
                        visited.set(index);

                        BlockPos position = new BlockPos(x, y, z);

                        resolvedCellProvider.resolve(worldSeed, dimension, position, candidates)
                                .filter(OreVeinResolvedCellResolver.ResolvedCell::replacement)
                                .ifPresent(resolvedCell -> replacements.add(new ChunkPlan.PlannedReplacement(position, resolvedCell)));
                    }
                }
            }
        }

        return replacements;
    }

    private static int logicalBitCount(int minY, int maxYExclusive) {
        // Convert the chunk height into one visited bit per block position.
        int height = Math.subtractExact(maxYExclusive, minY);

        if (height <= 0) throw new IllegalArgumentException("maxYExclusive must be greater than minY");

        return Math.multiplyExact(16 * 16, height);
    }

    private static int bitIndex(int x, int y, int z, int chunkMinX, int chunkMinZ, int minY) {
        // Convert the local chunk position into one visited-bit index
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
