package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.OUTSIDE;

public final class OreVeinShapeEvaluator {
    private static final double COARSE_FREQUENCY = 1.0D / 32.0D;
    private static final double MEDIUM_FREQUENCY = 1.0D / 14.0D;
    private static final double DETAIL_FREQUENCY = 1.0D / 6.0D;

    private static final double COARSE_AMPLITUDE_BLOCKS = 4.0D;
    private static final double MEDIUM_AMPLITUDE_BLOCKS = 2.0D;
    private static final double DETAIL_AMPLITUDE_BLOCKS = 1.0D;

    private static final double VERTICAL_DISTORTION_SCALE = 0.35D;

    private static final long COARSE_SALT = 0x6A09E667F3BCC909L;
    private static final long MEDIUM_SALT = 0xBB67AE8584CAA73BL;
    private static final long DETAIL_SALT = 0x3C6EF372FE94F82BL;

    public static @NotNull OreVeinContribution evaluate(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull BlockPos position) {
        double worldX = position.getX() + 0.5D;
        double worldY = position.getY() + 0.5D;
        double worldZ = position.getZ() + 0.5D;
        double centerX = descriptor.center().getX() + 0.5D;
        double centerY = descriptor.center().getY() + 0.5D;
        double centerZ = descriptor.center().getZ() + 0.5D;
        RotatedVector local = inverseRotate(
                worldX - centerX,
                worldY - centerY,
                worldZ - centerZ,
                descriptor.yaw(),
                descriptor.pitch(),
                descriptor.roll()
        );
        return evaluateLocalPoint(descriptor, local.x(), local.y(), local.z());
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull OreVeinContribution evaluateLocalPoint(@NotNull OreVeinInstanceDescriptor descriptor, double localX, double localY, double localZ) {
        double halfX = descriptor.sizeX() / 2.0D;
        double halfY = descriptor.sizeY() / 2.0D;
        double halfZ = descriptor.sizeZ() / 2.0D;
        double radialDistance = length(localX, localY, localZ);

        if (radialDistance == 0.0D) {
            return new OreVeinContribution(
                    descriptor.instanceId(),
                    descriptor.definitionId(),
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D,
                    clampDistortion(0.0D),
                    -Math.min(halfX, Math.min(halfY, halfZ)),
                    INSIDE_MAIN_BODY
            );
        }

        double normalizedRadius = normalizedRadius(localX, localY, localZ, halfX, halfY, halfZ);
        double baseDistanceBlocks = baseDistanceBlocks(localX, localY, localZ, halfX, halfY, halfZ);
        double distortionBlocks = distortionBlocks(descriptor.shapeSeed(), localX, localY, localZ);
        double signedBoundaryDistanceBlocks = baseDistanceBlocks - distortionBlocks;

        return new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                localX,
                localY,
                localZ,
                normalizedRadius,
                distortionBlocks,
                signedBoundaryDistanceBlocks,
                signedBoundaryDistanceBlocks <= 0.0D ? INSIDE_MAIN_BODY : OUTSIDE
        );
    }

    public static @NotNull HalfExtents rotatedHalfExtents(int sizeX, int sizeY, int sizeZ, double yawDegrees, double pitchDegrees, double rollDegrees) {
        double halfX = sizeX / 2.0D;
        double halfY = sizeY / 2.0D;
        double halfZ = sizeZ / 2.0D;
        RotationMatrix matrix = forwardRotation(yawDegrees, pitchDegrees, rollDegrees);

        return new HalfExtents(
                ceilExtent(matrix.m00(), halfX, matrix.m01(), halfY, matrix.m02(), halfZ),
                ceilExtent(matrix.m10(), halfX, matrix.m11(), halfY, matrix.m12(), halfZ),
                ceilExtent(matrix.m20(), halfX, matrix.m21(), halfY, matrix.m22(), halfZ)
        );
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull OreVeinBounds bounds(int centerX, int centerY, int centerZ, @NotNull HalfExtents halfExtents) {
        return new OreVeinBounds(
                centerX - halfExtents.x(),
                centerY - halfExtents.y(),
                centerZ - halfExtents.z(),
                centerX + halfExtents.x(),
                centerY + halfExtents.y(),
                centerZ + halfExtents.z()
        );
    }

    private static double normalizedRadius(double localX, double localY, double localZ, double halfX, double halfY, double halfZ) {
        return Math.sqrt(square(localX / halfX) + square(localY / halfY) + square(localZ / halfZ));
    }

    private static double baseDistanceBlocks(double localX, double localY, double localZ, double halfX, double halfY, double halfZ) {
        double radialDistance = length(localX, localY, localZ);

        if (radialDistance == 0.0D) return -Math.min(halfX, Math.min(halfY, halfZ));


        double directionX = localX / radialDistance;
        double directionY = localY / radialDistance;
        double directionZ = localZ / radialDistance;
        double ellipsoidSurfaceRadius = 1.0D / Math.sqrt(
                square(directionX / halfX)
                        + square(directionY / halfY)
                        + square(directionZ / halfZ)
        );

        return radialDistance - ellipsoidSurfaceRadius;
    }

    private static double distortionBlocks(long shapeSeed, double localX, double localY, double localZ) {
        double coarse = noiseLayer(shapeSeed, COARSE_SALT, localX, localY, localZ, COARSE_FREQUENCY, COARSE_AMPLITUDE_BLOCKS);
        double medium = noiseLayer(shapeSeed, MEDIUM_SALT, localX, localY, localZ, MEDIUM_FREQUENCY, MEDIUM_AMPLITUDE_BLOCKS);
        double detail = noiseLayer(shapeSeed, DETAIL_SALT, localX, localY, localZ, DETAIL_FREQUENCY, DETAIL_AMPLITUDE_BLOCKS);

        double radialDistance = length(localX, localY, localZ);

        double verticalShare = radialDistance == 0.0D ?
                0.0D : Math.abs(localY) / radialDistance;

        double horizontalStrength = 1.0D - square(verticalShare);

        double directionalScale = VERTICAL_DISTORTION_SCALE + (1.0D - VERTICAL_DISTORTION_SCALE) * horizontalStrength;

        return clampDistortion(
                (coarse + medium + detail) * directionalScale
        );
    }

    private static long latticeHash(long shapeSeed, long layerSalt, long x, long y, long z) {
        long hash = shapeSeed ^ layerSalt;
        hash ^= x * 0x9E3779B97F4A7C15L;
        hash ^= y * 0xC2B2AE3D27D4EB4FL;
        hash ^= z * 0x165667B19E3779F9L;
        return OreVeinGenerationMath.mix64(hash);
    }


    public static double hashToSignedUnit(long hash) {
        double unit = (hash >>> 11) * 0x1.0p-53;
        return unit * 2.0D - 1.0D;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0D - 15.0D) + 10.0D);
    }

    private static @NotNull RotatedVector inverseRotate(double x, double y, double z, double yawDegrees, double pitchDegrees, double rollDegrees) {
        RotationMatrix matrix = inverseRotation(yawDegrees, pitchDegrees, rollDegrees);
        return matrix.apply(x, y, z);
    }

    @Contract("_, _, _ -> new")
    private static @NotNull RotationMatrix forwardRotation(double yawDegrees, double pitchDegrees, double rollDegrees) {
        double yaw = Math.toRadians(yawDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double roll = Math.toRadians(rollDegrees);
        double cy = Math.cos(yaw);
        double sy = Math.sin(yaw);
        double cp = Math.cos(pitch);
        double sp = Math.sin(pitch);
        double cr = Math.cos(roll);
        double sr = Math.sin(roll);

        return new RotationMatrix(
                cy * cr + sy * sp * sr,
                -cy * sr + sy * sp * cr,
                sy * cp,
                cp * sr,
                cp * cr,
                -sp,
                -sy * cr + cy * sp * sr,
                sy * sr + cy * sp * cr,
                cy * cp
        );
    }

    private static @NotNull RotationMatrix inverseRotation(double yawDegrees, double pitchDegrees, double rollDegrees) {
        RotationMatrix forward = forwardRotation(yawDegrees, pitchDegrees, rollDegrees);
        return new RotationMatrix(
                forward.m00(), forward.m10(), forward.m20(),
                forward.m01(), forward.m11(), forward.m21(),
                forward.m02(), forward.m12(), forward.m22()
        );
    }

    public static @NotNull RotatedVector forwardRotate(double x, double y, double z, double yawDegrees, double pitchDegrees, double rollDegrees) {
        RotationMatrix matrix = forwardRotation(
                yawDegrees,
                pitchDegrees,
                rollDegrees);

        return matrix.apply(x, y, z);
    }

    private static double noiseLayer(long shapeSeed, long layerSalt, double localX, double localY, double localZ, double frequency, double amplitude) {
        return sampleNoise(shapeSeed, layerSalt, localX, localY, localZ, frequency) * amplitude;
    }

    private static double sampleNoise(long shapeSeed, long layerSalt, double localX, double localY, double localZ, double frequency) {
        double scaledX = localX * frequency;
        double scaledY = localY * frequency;
        double scaledZ = localZ * frequency;
        long minX = fastFloor(scaledX);
        long minY = fastFloor(scaledY);
        long minZ = fastFloor(scaledZ);
        long maxX = minX + 1L;
        long maxY = minY + 1L;
        long maxZ = minZ + 1L;
        double tx = scaledX - minX;
        double ty = scaledY - minY;
        double tz = scaledZ - minZ;
        double ux = fade(tx);
        double uy = fade(ty);
        double uz = fade(tz);

        double v000 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, minX, minY, minZ));
        double v100 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, maxX, minY, minZ));
        double v010 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, minX, maxY, minZ));
        double v110 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, maxX, maxY, minZ));
        double v001 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, minX, minY, maxZ));
        double v101 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, maxX, minY, maxZ));
        double v011 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, minX, maxY, maxZ));
        double v111 = hashToSignedUnit(latticeHash(shapeSeed, layerSalt, maxX, maxY, maxZ));

        double x00 = lerp(v000, v100, ux);
        double x10 = lerp(v010, v110, ux);
        double x01 = lerp(v001, v101, ux);
        double x11 = lerp(v011, v111, ux);
        double y0 = lerp(x00, x10, uy);
        double y1 = lerp(x01, x11, uy);
        return lerp(y0, y1, uz);
    }

    private static double clampDistortion(double distortionBlocks) {
        return Math.max(-OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS,
                Math.min(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS, distortionBlocks));
    }

    private static long fastFloor(double value) {
        long floor = (long) value;
        return value < floor ? floor - 1L : floor;
    }

    private static int ceilExtent(double r0, double h0, double r1, double h1, double r2, double h2) {
        return (int) Math.ceil(Math.abs(r0) * h0 + Math.abs(r1) * h1 + Math.abs(r2) * h2);
    }

    private static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    private static double square(double value) {
        return value * value;
    }

    private static double length(double x, double y, double z) {
        return Math.sqrt(square(x) + square(y) + square(z));
    }

    public record HalfExtents(int x, int y, int z) {
    }

    private record RotationMatrix(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        RotatedVector apply(double x, double y, double z) {
            return new RotatedVector(
                    m00 * x + m01 * y + m02 * z,
                    m10 * x + m11 * y + m12 * z,
                    m20 * x + m21 * y + m22 * z
            );
        }
    }

    public record RotatedVector(double x, double y, double z) { }
}
