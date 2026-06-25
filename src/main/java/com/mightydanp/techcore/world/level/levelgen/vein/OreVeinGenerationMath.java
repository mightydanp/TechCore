package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public final class OreVeinGenerationMath {
    public static final int GENERATOR_VERSION = 2;
    public static final int REGION_CHUNKS = 1;
    public static final int REGION_BLOCKS = REGION_CHUNKS * 16;
    public static final int AVERAGE_CHUNKS_PER_VEIN_ORIGIN = 9;
    public static final int ORIGIN_WEIGHT_SCALE = 9360;
    public static final int MAX_ORIGINS_PER_CHUNK = 64;
    private static final double MAX_SUPPORTED_ORIGIN_LAMBDA = 16.0D;
    public static final int FUTURE_BOUNDARY_MARGIN = 0;
    public static final BigInteger Q16 = BigInteger.ONE.shiftLeft(16);
    public static final int LARGE_VEIN_THRESHOLD_BLOCKS = 48;
    public static final int MAX_ACCEPTED_VEIN_SIZE_BLOCKS = 96;
    private static final int SIZE_CHANCE_SCALE = 10_000;

    private static final long SALT_DEFINITION_SELECTION = 0x4d7f4a7c15d9e377L;
    private static final long SALT_INSTANCE_ID = 0x1f123bb5a9f04731L;
    private static final long SALT_INSTANCE_SEED = 0x6a09e667f3bcc909L;
    private static final long SALT_ORIGIN_COUNT = 0x6e624eb7f5a2d9c3L;
    private static final long SALT_SHAPE_SEED = 0xbb67ae8584caa73bL;
    private static final long SALT_CENTER_X = 0x3c6ef372fe94f82bL;
    private static final long SALT_CENTER_Y = 0xa54ff53a5f1d36f1L;
    private static final long SALT_CENTER_Z = 0x510e527fade682d1L;
    private static final long SALT_SIZE_X = 0x9b05688c2b3e6c1fL;
    private static final long SALT_SIZE_Y = 0x1f83d9abfb41bd6bL;
    private static final long SALT_SIZE_Z = 0x5be0cd19137e2179L;
    private static final long SALT_SIZE_ACCEPTANCE = 0xd1b54a32d192ed03L;
    private static final long SIZE_ATTEMPT_STEP = 0x9e3779b97f4a7c15L;
    private static final long SALT_YAW = 0xcbbb9d5dc1059ed8L;
    private static final long SALT_PITCH = 0x629a292a367cd507L;
    private static final long SALT_ROLL = 0x9159015a3070dd17L;

    public static @NotNull BigInteger effectiveWeightQ16(@NotNull OreVeinDefinition definition) {
        // Vein size does not affect whether this definition is selected.
        return BigInteger.valueOf(definition.generationWeight()).multiply(Q16);
    }

    public static BigInteger totalEffectiveWeightQ16(@NotNull List<OreVeinDefinition> definitions) {
        BigInteger total = BigInteger.ZERO;

        // Sum the effective weights for every eligible definition.
        for (OreVeinDefinition definition : definitions) total = total.add(effectiveWeightQ16(definition));

        return total;
    }

    public static int originCount( long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, @NotNull BigInteger totalWeightQ16 ) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(totalWeightQ16, "totalWeightQ16");

        if (totalWeightQ16.signum() <= 0) return 0;

        double lambda = totalWeightQ16.doubleValue() / Q16.doubleValue() / ORIGIN_WEIGHT_SCALE;

        if (!Double.isFinite(lambda) || lambda > MAX_SUPPORTED_ORIGIN_LAMBDA) throw new IllegalStateException( "Ore vein origin rate is too large: " + lambda + "; totalWeightQ16=" + totalWeightQ16 );

        double countRoll = randomUnit( worldSeed, dimension, originRegionX, originRegionZ, 0, SALT_ORIGIN_COUNT );
        int count = 0;
        double probability = Math.exp(-lambda);
        double cumulativeProbability = probability;

        while (countRoll >= cumulativeProbability) {
            count++;
            if (count >= MAX_ORIGINS_PER_CHUNK) return MAX_ORIGINS_PER_CHUNK;

            probability *= lambda / count; cumulativeProbability += probability;
        }

        return count;
    }

    public static @NotNull BigInteger rollQ16(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, BigInteger budgetQ16) {
        // Build a 128-bit deterministic roll and clamp it into the dimension budget.
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
        // Pick one block position inside the origin region along this axis.
        int regionStart = Math.multiplyExact(axisRegionCoordinate, REGION_BLOCKS);
        int regionOffset = randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt, REGION_BLOCKS);
        return Math.addExact(regionStart, regionOffset);
    }

    public static @NotNull SampledSize sampledSize(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
         // Every attempt is derived from the world seed, dimension, origin coordinates, origin index, and attempt number.
         // The same world seed therefore produces the same rejected candidates and the same final accepted size.
        for (long attempt = 0L; ; attempt++) {
            long attemptSalt = mix64(SIZE_ATTEMPT_STEP * (attempt + 1L));

            int sizeX = randomRangeInclusive(worldSeed, dimension, originRegionX, originRegionZ, originIndex,
                    SALT_SIZE_X ^ attemptSalt,
                    definition.minSizeX(),
                    definition.maxSizeX()
            );

            int sizeY = randomRangeInclusive(worldSeed, dimension, originRegionX, originRegionZ, originIndex,
                    SALT_SIZE_Y ^ attemptSalt,
                    definition.minSizeY(),
                    definition.maxSizeY()
            );

            int sizeZ = randomRangeInclusive(worldSeed, dimension, originRegionX, originRegionZ,
                    originIndex,
                    SALT_SIZE_Z ^ attemptSalt,
                    definition.minSizeZ(),
                    definition.maxSizeZ()
            );

            SampledSize candidate = new SampledSize(sizeX, sizeY, sizeZ);
            int acceptanceChance = sizeAcceptanceChance(candidate);


            //Sizes above the accepted limit have a zero chance. Immediately move to the next deterministic attempt.
            if (acceptanceChance <= 0) continue;

            // A chance of 100% does not need an additional roll.
            if (acceptanceChance >= SIZE_CHANCE_SCALE) return candidate;

            int acceptanceRoll = randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex,
                    SALT_SIZE_ACCEPTANCE ^ attemptSalt,
                    SIZE_CHANCE_SCALE
            );
            if (acceptanceRoll < acceptanceChance) return candidate;

        }
    }
    private static int sizeAcceptanceChance(@NotNull SampledSize size) {
        int largestSize = Math.max(
                size.x(),

                Math.max(size.y(), size.z())
        );

        if (largestSize <= 48) return 10_000;

        if (largestSize <= 64) return interpolateChance(largestSize,
                48,
                64,
                10_000,
                5_000
        );

        if (largestSize <= 80) return interpolateChance(largestSize,
                64,
                80,
                5_000,
                1_000
        );

        if (largestSize <= 96) return interpolateChance(largestSize,
                80,
                96,
                1_000,
                500
        );

        return 0;
    }

    private static int interpolateChance(int size, int startSize, int endSize, int startChance, int endChance) {
        int sizeOffset = size - startSize;

        int sizeSpan = endSize - startSize;
        int chanceDrop = startChance - endChance;
        long roundedDrop =
                ((long) chanceDrop * sizeOffset
                        + sizeSpan / 2L)
                        / sizeSpan;
        return startChance - (int) roundedDrop;
    }

    public static double yaw(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        if (!definition.rotationEnabled()) return 0.0D;

        return randomUnit(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_YAW) * 360.0D;
    }

    public static double pitch(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        if (!definition.rotationEnabled()) return 0.0D;

        return randomSigned(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_PITCH, definition.maxPitchDegrees());
    }

    public static double roll(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, @NotNull OreVeinDefinition definition) {
        if (!definition.rotationEnabled()) return 0.0D;

        return randomSigned(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_ROLL, definition.maxRollDegrees());
    }

    public static int randomCenterY(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, int minInclusive, int maxExclusive) {
        return minInclusive + randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, SALT_CENTER_Y, maxExclusive - minInclusive);
    }

    public static int regionCoordinateForBlock(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, REGION_BLOCKS);
    }

    public static long hashSeedAndDimension(long worldSeed, ResourceKey<Level> dimension, long salt) {
        // Fold the world seed, generator version, salt, and dimension identity into one hash seed.
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
        // Convert the inclusive bounds into the exclusive-size form used by randomInt.
        return minInclusive + randomInt(worldSeed, dimension, originRegionX, originRegionZ, originIndex, salt, maxInclusive - minInclusive + 1);
    }

    private static int randomInt(long worldSeed, ResourceKey<Level> dimension, int originRegionX, int originRegionZ, int originIndex, long salt, int bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        // Use floorMod so every bound works correctly with signed long hashes.
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

    public record SampledSize(int x, int y, int z) {}

    private static void writeLong(byte[] bytes, int offset, long value) {
        for (int i = 7; i >= 0; i--) {
            bytes[offset + i] = (byte) value;
            value >>>= 8;
        }
    }
}
