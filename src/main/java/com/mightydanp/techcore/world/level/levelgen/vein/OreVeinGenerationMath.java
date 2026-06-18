package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class OreVeinGenerationMath {
    public static final int GENERATOR_VERSION = 1;
    public static final int REGION_CHUNKS = 3;
    public static final int REGION_BLOCKS = REGION_CHUNKS * 16;
    public static final int ORIGIN_INDEX = 0;
    public static final int FUTURE_BOUNDARY_MARGIN = 0;
    public static final BigInteger Q16 = BigInteger.ONE.shiftLeft(16);
    public static final BigInteger REFERENCE_VOLUME_8 = BigInteger.valueOf(32L * 32L * 32L);

    private static final long SALT_DEFINITION_SELECTION = 0x4d7f4a7c15d9e377L;
    private static final long SALT_INSTANCE_ID = 0x1f123bb5a9f04731L;
    private static final long SALT_INSTANCE_SEED = 0x6a09e667f3bcc909L;
    private static final long SALT_SHAPE_SEED = 0xbb67ae8584caa73bL;
    private static final long SALT_CENTER_X = 0x3c6ef372fe94f82bL;
    private static final long SALT_CENTER_Y = 0xa54ff53a5f1d36f1L;
    private static final long SALT_CENTER_Z = 0x510e527fade682d1L;
    private static final long SALT_SIZE_X = 0x9b05688c2b3e6c1fL;
    private static final long SALT_SIZE_Y = 0x1f83d9abfb41bd6bL;
    private static final long SALT_SIZE_Z = 0x5be0cd19137e2179L;
    private static final long SALT_YAW = 0xcbbb9d5dc1059ed8L;
    private static final long SALT_PITCH = 0x629a292a367cd507L;
    private static final long SALT_ROLL = 0x9159015a3070dd17L;

    public static BigInteger budgetQ16(OreVeinDimensionGenerationSettings settings) {
        return BigInteger.valueOf(settings.originWeightBudget()).multiply(Q16);
    }

    public static BigInteger effectiveWeightQ16(OreVeinDefinition definition) {
        BigInteger expectedVolume8 = expectedVolume8(definition);
        BigInteger volumeScaleQ16 = ceilDiv(expectedVolume8.multiply(Q16), REFERENCE_VOLUME_8);
        BigInteger penaltyQ16 = sqrtFloor(volumeScaleQ16.multiply(Q16)).max(Q16);
        BigInteger numerator = BigInteger.valueOf(definition.generationWeight()).multiply(Q16).multiply(Q16);

        return numerator.divide(penaltyQ16).max(BigInteger.ONE);
    }

    public static BigInteger totalEffectiveWeightQ16(List<OreVeinDefinition> definitions) {
        BigInteger total = BigInteger.ZERO;

        for (OreVeinDefinition definition : definitions) total = total.add(effectiveWeightQ16(definition));

        return total;
    }

    public static BigInteger rollQ16(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, BigInteger budgetQ16) {
        long low = hash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_DEFINITION_SELECTION);
        long high = hash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_DEFINITION_SELECTION ^ 0x9e3779b97f4a7c15L);
        byte[] bytes = new byte[16];

        writeLong(bytes, 0, high);
        writeLong(bytes, 8, low);

        return new BigInteger(1, bytes).mod(budgetQ16);
    }

    public static long instanceId(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return instanceHash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_INSTANCE_ID);
    }

    public static long instanceSeed(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return instanceHash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_INSTANCE_SEED);
    }

    public static long shapeSeed(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return instanceHash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_SHAPE_SEED);
    }

    private static long instanceHash(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt) {
        return hash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt);
    }

    public static int centerX(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return centerCoordinate(worldSeed, dimension, originRegionX, originRegionZ, originIndex, originRegionX, SALT_CENTER_X);
    }

    public static int centerZ(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return centerCoordinate(worldSeed, dimension, originRegionX, originRegionZ, originIndex, originRegionZ, SALT_CENTER_Z);
    }

    private static int centerCoordinate(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, int axisRegionCoordinate, long salt) {
        int regionStart = Math.multiplyExact(axisRegionCoordinate, REGION_BLOCKS);
        int regionOffset = randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt, REGION_BLOCKS);
        return Math.addExact(regionStart, regionOffset);
    }

    public static int sizeX(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        return size(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_SIZE_X, definition.minSizeX(), definition.maxSizeX());
    }

    public static int sizeY(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        return size(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_SIZE_Y, definition.minSizeY(), definition.maxSizeY());
    }

    public static int sizeZ(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        return size(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_SIZE_Z, definition.minSizeZ(), definition.maxSizeZ());
    }

    private static int size(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt, int minSize, int maxSize) {
        return randomRangeInclusive(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt, minSize, maxSize);
    }

    public static double yaw(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex) {
        return randomUnit(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_YAW) * 360.0D;
    }

    public static double pitch(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        return randomSigned(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_PITCH, definition.maxPitchDegrees());
    }

    public static double roll(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        return randomSigned(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_ROLL, definition.maxRollDegrees());
    }

    public static int randomCenterY(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, int minInclusive, int maxExclusive) {
        return minInclusive + randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_CENTER_Y, maxExclusive - minInclusive);
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull HalfExtents rotatedHalfExtents(int sizeX, int sizeY, int sizeZ, double yawDegrees, double pitchDegrees, double rollDegrees) {
        double hx = (sizeX - 1) / 2.0D;
        double hy = (sizeY - 1) / 2.0D;
        double hz = (sizeZ - 1) / 2.0D;
        double yaw = Math.toRadians(yawDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double roll = Math.toRadians(rollDegrees);
        double cy = Math.cos(yaw);
        double sy = Math.sin(yaw);
        double cp = Math.cos(pitch);
        double sp = Math.sin(pitch);
        double cr = Math.cos(roll);
        double sr = Math.sin(roll);

        double r00 = cy * cr + sy * sp * sr;
        double r01 = -cy * sr + sy * sp * cr;
        double r02 = sy * cp;
        double r10 = cp * sr;
        double r11 = cp * cr;
        double r12 = -sp;
        double r20 = -sy * cr + cy * sp * sr;
        double r21 = sy * sr + cy * sp * cr;
        double r22 = cy * cp;

        return new HalfExtents(
                ceilExtent(r00, hx, r01, hy, r02, hz),
                ceilExtent(r10, hx, r11, hy, r12, hz),
                ceilExtent(r20, hx, r21, hy, r22, hz)
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

    public static int regionCoordinateForBlock(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, REGION_BLOCKS);
    }

    public static @NotNull BigInteger expectedVolume8(@NotNull OreVeinDefinition definition) {
        BigInteger mid2X = BigInteger.valueOf((long) definition.minSizeX() + definition.maxSizeX());
        BigInteger mid2Y = BigInteger.valueOf((long) definition.minSizeY() + definition.maxSizeY());
        BigInteger mid2Z = BigInteger.valueOf((long) definition.minSizeZ() + definition.maxSizeZ());

        return mid2X.multiply(mid2Y).multiply(mid2Z);
    }

    public static BigInteger ceilDiv(@NotNull BigInteger value, BigInteger divisor) {
        BigInteger[] divided = value.divideAndRemainder(divisor);

        return divided[1].signum() == 0 ? divided[0] : divided[0].add(BigInteger.ONE);
    }

    @Contract(pure = true)
    public static BigInteger sqrtFloor(@NotNull BigInteger value) {
        return value.sqrt();
    }

    public static long hashSeedAndDimension(long worldSeed, ResourceKey<Level> dimension, long salt) {
        long value = 0xcbf29ce484222325L;
        value = mix64(value ^ worldSeed);
        value = mix64(value ^ GENERATOR_VERSION);
        value = mix64(value ^ salt);
        return mix64(foldDimensionIdentity(value, dimension));
    }

    private static long foldDimensionIdentity(long value, @NotNull ResourceKey<Level> dimension) {
        ResourceLocation location = dimension.location();
        byte[] namespace = location.getNamespace().getBytes(StandardCharsets.UTF_8);
        byte[] path = location.getPath().getBytes(StandardCharsets.UTF_8);

        for (byte b : namespace) value = mix64(value ^ b);

        value = mix64(value ^ ':');

        for (byte b : path) value = mix64(value ^ b);

        return value;
    }

    private static int randomRangeInclusive(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt, int minInclusive, int maxInclusive) {
        return minInclusive + randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt, maxInclusive - minInclusive + 1);
    }

    private static int randomInt(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt, int bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        return Math.floorMod(hash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt), bound);
    }

    private static double randomSigned(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt, double maxAbsolute) {
        if (maxAbsolute == 0.0D) return 0.0D;

        return (randomUnit(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt) * 2.0D - 1.0D) * maxAbsolute;
    }

    private static double randomUnit(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt) {
        return (hash(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt) >>> 11) * 0x1.0p-53D;
    }

    private static long hash(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt) {
        long value = hashSeedAndDimension(worldSeed, dimension, salt);
        value = mix64(value ^ originRegionX);
        value = mix64(value ^ originRegionZ);
        value = mix64(value ^ originIndex);
        return mix64(value);
    }

    public static long mix64(long value) {
        value ^= value >>> 30;
        value *= 0xbf58476d1ce4e5b9L;
        value ^= value >>> 27;
        value *= 0x94d049bb133111ebL;
        value ^= value >>> 31;
        return value;
    }

    private static int ceilExtent(double r0, double h0, double r1, double h1, double r2, double h2) {
        return (int) Math.ceil(Math.abs(r0) * h0 + Math.abs(r1) * h1 + Math.abs(r2) * h2);
    }

    private static void writeLong(byte[] bytes, int offset, long value) {
        for (int i = 7; i >= 0; i--) {
            bytes[offset + i] = (byte) value;
            value >>>= 8;
        }
    }

    public record HalfExtents(int x, int y, int z) {}
}
