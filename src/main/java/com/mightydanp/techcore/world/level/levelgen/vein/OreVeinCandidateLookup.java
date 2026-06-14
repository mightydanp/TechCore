package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class OreVeinCandidateLookup {
    private OreVeinCandidateLookup() {
    }

    public static List<OreVeinInstanceDescriptor> candidatesForBlock(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(position, "position");

        return candidatesForArea(worldSeed, dimension, position, position);
    }

    public static List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunk) {
        Objects.requireNonNull(chunk, "chunk");

        return candidatesForArea(worldSeed, dimension, new BlockPos(chunk.getMinBlockX(), Integer.MIN_VALUE / 4, chunk.getMinBlockZ()),
                new BlockPos(chunk.getMaxBlockX(), Integer.MAX_VALUE / 4, chunk.getMaxBlockZ()));
    }

    public static List<OreVeinInstanceDescriptor> candidatesForChunk(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive) {
        Objects.requireNonNull(chunkPos, "chunkPos");

        if (maxYExclusive <= minY) {
            throw new IllegalArgumentException("maxYExclusive must be greater than minY");
        }

        return candidatesForArea(
                worldSeed,
                dimension,
                new BlockPos(chunkPos.getMinBlockX(), minY, chunkPos.getMinBlockZ()),
                new BlockPos(chunkPos.getMaxBlockX(), maxYExclusive - 1, chunkPos.getMaxBlockZ())
        );
    }

    public static List<OreVeinInstanceDescriptor> candidatesForArea(long worldSeed, ResourceKey<Level> dimension, BlockPos minInclusive, BlockPos maxInclusive) {
        Objects.requireNonNull(dimension, "dimension");

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

        candidates.sort(Comparator
                .comparingInt(OreVeinInstanceDescriptor::originRegionX)
                .thenComparingInt(OreVeinInstanceDescriptor::originRegionZ)
                .thenComparingInt(OreVeinInstanceDescriptor::originIndex)
                .thenComparing(descriptor -> descriptor.definitionId().toString()));

        return List.copyOf(candidates);
    }

    public static Optional<OreVeinInstanceDescriptor> descriptorForOrigin(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        Objects.requireNonNull(dimension, "dimension");

        OreVeinDimensionGenerationSettings settings = OreVeinDefinitions.getGenerationSettings(dimension);

        if (settings == null) {
            return Optional.empty();
        }

        BigInteger budgetQ16 = OreVeinGenerationMath.budgetQ16(settings);
        BigInteger roll = OreVeinGenerationMath.rollQ16(worldSeed, dimension, originRegionX, originRegionZ, originIndex, budgetQ16);
        BigInteger cursor = BigInteger.ZERO;

        for (OreVeinDefinition definition : OreVeinDefinitions.getDefinitions()) {
            if (!definition.dimensions().contains(dimension)) {
                continue;
            }

            cursor = cursor.add(OreVeinGenerationMath.effectiveWeightQ16(definition));

            if (roll.compareTo(cursor) < 0) {
                return createDescriptor(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
            }
        }

        return Optional.empty();
    }

    static Optional<OreVeinInstanceDescriptor> createDescriptor(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, OreVeinDefinition definition) {
        int sizeX = OreVeinGenerationMath.sizeX(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        int sizeY = OreVeinGenerationMath.sizeY(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        int sizeZ = OreVeinGenerationMath.sizeZ(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        double yaw = OreVeinGenerationMath.yaw(worldSeed, dimension, originRegionX, originRegionZ, originIndex);
        double pitch = OreVeinGenerationMath.pitch(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        double roll = OreVeinGenerationMath.roll(worldSeed, dimension, originRegionX, originRegionZ, originIndex, definition);
        OreVeinShapeEvaluator.HalfExtents halfExtents = OreVeinShapeEvaluator.rotatedHalfExtents(sizeX, sizeY, sizeZ, yaw, pitch, roll);
        OreVeinDefinitions.DimensionHeight height = OreVeinDefinitions.dimensionHeight(dimension);

        if (height == null) {
            return Optional.empty();
        }

        int minLegalCenterY = Math.max(definition.minCenterY(), height.minY() + halfExtents.y());
        int maxLegalCenterYExclusive = Math.min(definition.maxCenterYExclusive(), height.maxYExclusive() - halfExtents.y());

        if (minLegalCenterY >= maxLegalCenterYExclusive) {
            return Optional.empty();
        }

        int centerX = OreVeinGenerationMath.centerX(worldSeed, dimension, originRegionX, originRegionZ, originIndex);
        int centerY = OreVeinGenerationMath.randomCenterY(worldSeed, dimension, originRegionX, originRegionZ, originIndex, minLegalCenterY, maxLegalCenterYExclusive);
        int centerZ = OreVeinGenerationMath.centerZ(worldSeed, dimension, originRegionX, originRegionZ, originIndex);
        OreVeinBounds bounds = OreVeinShapeEvaluator.bounds(centerX, centerY, centerZ, halfExtents);

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
        return Optional.of(new OreVeinInstanceDescriptor(
                provisional.instanceId(),
                provisional.instanceSeed(),
                provisional.shapeSeed(),
                provisional.definitionId(),
                provisional.center(),
                provisional.sizeX(),
                provisional.sizeY(),
                provisional.sizeZ(),
                provisional.yaw(),
                provisional.pitch(),
                provisional.roll(),
                provisional.originRegionX(),
                provisional.originRegionZ(),
                provisional.originIndex(),
                provisional.bounds(),
                OreVeinDenseNodeLayout.generate(provisional, definition)
        ));
    }

    public static OreVeinBounds evaluationBounds(OreVeinInstanceDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor");

        OreVeinDefinition definition = Objects.requireNonNull(
                OreVeinDefinitions.getDefinition(descriptor.definitionId()),
                "Missing ore vein definition: " + descriptor.definitionId()
        );
        int distortionReach = OreVeinDefinitions.checkedCeilToInt(
                OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS,
                "MAX_BOUNDARY_DISTORTION_BLOCKS"
        );
        int sparseReach = definition.sparseReachBlocks();

        return descriptor.bounds().inflate(Math.addExact(distortionReach, sparseReach));
    }

    private static int maxCandidateReach(ResourceKey<Level> dimension) {
        int reach = 0;

        for (OreVeinDefinition definition : OreVeinDefinitions.getDefinitions()) {
            if (!definition.dimensions().contains(dimension)) {
                continue;
            }

            reach = Math.max(reach, maxReach(definition));
        }

        return Math.addExact(
                reach,
                Math.addExact(
                        OreVeinDefinitions.checkedCeilToInt(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS, "MAX_BOUNDARY_DISTORTION_BLOCKS"),
                        maxSparseReach(dimension)
                )
        );
    }

    private static int maxReach(OreVeinDefinition definition) {
        int maxReach = 0;

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

    private static int maxSparseReach(ResourceKey<Level> dimension) {
        int maxSparseReach = 0;

        for (OreVeinDefinition definition : OreVeinDefinitions.getDefinitions()) {
            if (!definition.dimensions().contains(dimension)) {
                continue;
            }

            maxSparseReach = Math.max(maxSparseReach, definition.sparseReachBlocks());
        }

        return maxSparseReach;
    }
}
