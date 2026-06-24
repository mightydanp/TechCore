package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.world.level.levelgen.vein.densenode.OreVeinDenseNodeLayout;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.math.BigInteger;
import java.util.*;

public final class OreVeinCandidateLookup {
    private static final Comparator<OreVeinInstanceDescriptor> CANDIDATE_ORDER = Comparator.comparingInt(OreVeinInstanceDescriptor::originRegionX).thenComparingInt(OreVeinInstanceDescriptor::originRegionZ).thenComparingInt(OreVeinInstanceDescriptor::originIndex).thenComparing(descriptor -> descriptor.definitionId().toString());

    public static @NotNull @Unmodifiable List<OreVeinInstanceDescriptor> candidatesForBlock(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(position, "position");

        // Reuse the area lookup with the same block position as both corners.
        return candidatesForArea(worldSeed, dimension, position, position);
    }

    public static @NotNull @Unmodifiable List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunk) {
        Objects.requireNonNull(chunk, "chunk");

        // Use an effectively unbounded Y range when the caller wants the full chunk.
        return candidatesForChunk(worldSeed, dimension, chunk,
                Integer.MIN_VALUE / 4,
                Integer.MAX_VALUE / 4 + 1
        );
    }

    public static @NotNull @Unmodifiable List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive) {
        Objects.requireNonNull(chunkPos, "chunkPos");

        if (maxYExclusive <= minY) throw new IllegalArgumentException("maxYExclusive must be greater than minY");

        // Convert the chunk and vertical range into one inclusive block-area query.
        return candidatesForArea(worldSeed, dimension,
                new BlockPos(chunkPos.getMinBlockX(), minY, chunkPos.getMinBlockZ()),
                new BlockPos(chunkPos.getMaxBlockX(), maxYExclusive - 1, chunkPos.getMaxBlockZ())
        );
    }

    public static @NotNull @Unmodifiable List<OreVeinInstanceDescriptor> candidatesForArea(long worldSeed, ResourceKey<Level> dimension, BlockPos minInclusive, BlockPos maxInclusive) {
        Objects.requireNonNull(dimension, "dimension");

        // Expand the area by the maximum vein reach so nearby origins are not missed
        OreVeinBounds area = OreVeinBounds.from(minInclusive, maxInclusive);
        int reach = maxCandidateReach(dimension);
        OreVeinBounds expanded = area.inflate(reach);
        int minRegionX = OreVeinGenerationMath.regionCoordinateForBlock(expanded.minX());
        int maxRegionX = OreVeinGenerationMath.regionCoordinateForBlock(expanded.maxX());
        int minRegionZ = OreVeinGenerationMath.regionCoordinateForBlock(expanded.minZ());
        int maxRegionZ = OreVeinGenerationMath.regionCoordinateForBlock(expanded.maxZ());
        List<OreVeinInstanceDescriptor> candidates = new ArrayList<>();

        for (int originRegionX = minRegionX; originRegionX <= maxRegionX; originRegionX++) {
            for (int originRegionZ = minRegionZ; originRegionZ <= maxRegionZ; originRegionZ++) {
                descriptorForOrigin(worldSeed, dimension, originRegionX, originRegionZ, OreVeinGenerationMath.ORIGIN_INDEX)
                        .filter(descriptor -> evaluationBounds(descriptor).intersects(area))
                        .ifPresent(candidates::add);
            }
        }

        candidates.sort(CANDIDATE_ORDER);

        return List.copyOf(candidates);
    }

    public static Optional<OreVeinInstanceDescriptor> descriptorForOrigin(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        Objects.requireNonNull(dimension, "dimension");

        // Get the generation settings for this dimension before rolling a definition.
        OreVeinDefinitions.DimensionGenerationSettings settings = OreVeinDefinitions.getGenerationSettings(dimension);

        if (settings == null) return Optional.empty();

        BigInteger budgetQ16 = OreVeinGenerationMath.budgetQ16(settings);
        BigInteger roll = OreVeinGenerationMath.rollQ16(worldSeed, dimension, originRegionX, originRegionZ, originIndex, budgetQ16);
        BigInteger cursor = BigInteger.ZERO;

        // Loop through the weighted definitions until the roll selects one
        for (OreVeinDefinition definition : OreVeinDefinitions.getDefinitions()) {
            if (!definition.dimensions().contains(dimension)) continue;

            cursor = cursor.add(OreVeinGenerationMath.effectiveWeightQ16(definition));

            if (roll.compareTo(cursor) < 0) return createDescriptor(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        }

        return Optional.empty();
    }

    private static Optional<OreVeinInstanceDescriptor> createDescriptor(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, OreVeinDefinition definition) {
        // Sample the final size and rotation values for this origin and definition.
        int sizeX = OreVeinGenerationMath.sizeX(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        int sizeY = OreVeinGenerationMath.sizeY(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        int sizeZ = OreVeinGenerationMath.sizeZ(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        double yaw = OreVeinGenerationMath.yaw(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        double pitch = OreVeinGenerationMath.pitch(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        double roll = OreVeinGenerationMath.roll(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        OreVeinShapeEvaluator.HalfExtents halfExtents = OreVeinShapeEvaluator.rotatedHalfExtents(sizeX, sizeY, sizeZ, yaw, pitch, roll);
        OreVeinDefinitions.DimensionHeight height = OreVeinDefinitions.dimensionHeight(dimension);

        if (height == null) return Optional.empty();

        // Clamp the legal center Y range so the rotated shape stays inside the dimension.
        int minLegalCenterY = Math.max(definition.minCenterY(), height.minY() + halfExtents.y());
        int maxLegalCenterYExclusive = Math.min(definition.maxCenterYExclusive(), height.maxYExclusive() - halfExtents.y());

        if (minLegalCenterY >= maxLegalCenterYExclusive) return Optional.empty();

        int centerX = OreVeinGenerationMath.centerX(worldSeed, dimension, originRegionX, originRegionZ, originIndex);
        int centerY = OreVeinGenerationMath.randomCenterY(worldSeed, dimension, originRegionX, originRegionZ, originIndex, minLegalCenterY, maxLegalCenterYExclusive);
        int centerZ = OreVeinGenerationMath.centerZ(worldSeed, dimension, originRegionX, originRegionZ, originIndex);
        OreVeinBounds bounds = OreVeinShapeEvaluator.bounds(centerX, centerY, centerZ, halfExtents);

        // Create the descriptor first, then attach any generated dense nodes.
        OreVeinInstanceDescriptor provisional = new OreVeinInstanceDescriptor(
                OreVeinGenerationMath.instanceId(worldSeed, dimension, originRegionX, originRegionZ, originIndex),
                OreVeinGenerationMath.instanceSeed(worldSeed, dimension, originRegionX, originRegionZ, originIndex),
                OreVeinGenerationMath.shapeSeed(worldSeed, dimension, originRegionX, originRegionZ, originIndex),
                definition.id(),
                new BlockPos(centerX, centerY, centerZ),
                sizeX,
                sizeY,
                sizeZ,
                yaw,
                pitch,
                roll,
                originRegionX,
                originRegionZ,
                originIndex,
                bounds,
                List.of()
        );

        if (!definition.denseNodeEnabled()) return Optional.of(provisional);

        return Optional.of(provisional.withDenseNodes(OreVeinDenseNodeLayout.generate(provisional, definition)));

    }

    public static @NotNull OreVeinBounds evaluationBounds(OreVeinInstanceDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor");

        // Expand the core bounds by distortion and sparse reach so evaluation can include halo cells.
        OreVeinDefinition definition = OreVeinDefinitions.requireDefinition(descriptor);
        int distortionReach = OreVeinDefinitions.checkedCeilToInt(
                OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS,
                "MAX_BOUNDARY_DISTORTION_BLOCKS"
        );
        int sparseReach = exteriorSparseReach(definition);

        return descriptor.bounds().inflate(
                Math.addExact(distortionReach, sparseReach)
        );
    }

    private static int exteriorSparseReach(@NotNull OreVeinDefinition definition) {
        int fadeReach = definition.effectiveSparseHaloReachBlocks();

        int transitionReach = definition.hasSparseTransition() ?
                OreVeinDefinitions.checkedCeilToInt(
                        definition.haloSettings().transitionWidthBlocks() * 0.5D,
                        "sparse transition exterior reach")
                : 0;

        return Math.max(fadeReach, transitionReach);
    }

    private static int maxCandidateReach(ResourceKey<Level> dimension) {
        int shapeReach = 0;
        int sparseReach = 0;

        // Track the largest shape and sparse reach used by any definition in this dimension.
        for (OreVeinDefinition definition : OreVeinDefinitions.getDefinitions()) {
            if (!definition.dimensions().contains(dimension)) continue;

            shapeReach = Math.max(shapeReach, maxReach(definition));
            sparseReach = Math.max(sparseReach, definition.sparseReachBlocks());
        }

        int distortionReach = OreVeinDefinitions.checkedCeilToInt(
                OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS,
                "MAX_BOUNDARY_DISTORTION_BLOCKS"
        );

        return Math.addExact(shapeReach, Math.addExact(distortionReach, sparseReach));
    }

    private static int maxReach(@NotNull OreVeinDefinition definition) {
        if (!definition.rotationEnabled()) {
            OreVeinShapeEvaluator.HalfExtents halfExtents =
                    OreVeinShapeEvaluator.rotatedHalfExtents(
                            definition.maxSizeX(),
                            definition.maxSizeY(),
                            definition.maxSizeZ(),
                            0.0D,
                            0.0D,
                            0.0D
                    );

            return Math.max(halfExtents.x(), halfExtents.z());
        }

        int maxReach = 0;

        // Test the extreme pitch and roll combinations to find the farthest horizontal extent.
        for (double pitch : List.of(-definition.maxPitchDegrees(), definition.maxPitchDegrees())) {
            for (double roll : List.of(-definition.maxRollDegrees(), definition.maxRollDegrees())) {
                OreVeinShapeEvaluator.HalfExtents halfExtents = OreVeinShapeEvaluator.rotatedHalfExtents(
                        definition.maxSizeX(),
                        definition.maxSizeY(),
                        definition.maxSizeZ(),
                        45.0D,
                        pitch,
                        roll
                );

                maxReach = Math.max(maxReach, Math.max(halfExtents.x(), halfExtents.z()));
            }
        }

        return maxReach;
    }
}
