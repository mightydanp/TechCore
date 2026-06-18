package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public record OreVeinDefinition(ResourceLocation id, List<ResourceKey<Level>> dimensions, int generationWeight, int minCenterY, int maxCenterYExclusive, int minSizeX, int maxSizeX, int minSizeY, int maxSizeY, int minSizeZ, int maxSizeZ, int sparseReachBlocks, double maxPitchDegrees, double maxRollDegrees, OreVeinDensitySettings densitySettings, OreVeinHaloSettings haloSettings, List<VeinOreEntry> oreEntries) {
    private static final int DEFAULT_SPARSE_REACH_BLOCKS = 32;
    private static final double DEFAULT_MAX_PITCH_DEGREES = 12.0D;
    private static final double DEFAULT_MAX_ROLL_DEGREES = 12.0D;

    //private Double maxPitchDegrees;
    //private Double maxRollDegrees;
    
    private static final OreVeinDensitySettings DEFAULT_DENSITY_SETTINGS = new OreVeinDensitySettings(
            704,
            960,
            1024,
            8192L,
            0,
            5,
            4.0D,
            8.5D,
            3.0D,
            3.5D,
            4.0D,
            8.5D,
            2,
            4
    );
    private static final OreVeinHaloSettings DEFAULT_HALO_SETTINGS =
            new OreVeinHaloSettings(
                    4.0D
            );

    public OreVeinDefinition {
        Objects.requireNonNull(id, "id");
        dimensions = copyNonEmptyList(dimensions, "dimensions");
        validateGenerationWeight(generationWeight);
        validateCenterYRange(minCenterY, maxCenterYExclusive);
        validateSizeRange(minSizeX, maxSizeX, "sizeX");
        validateSizeRange(minSizeY, maxSizeY, "sizeY");
        validateSizeRange(minSizeZ, maxSizeZ, "sizeZ");
        validateSparseReachBlocks(sparseReachBlocks);
        validateTilt(maxPitchDegrees, "maxPitchDegrees");
        validateTilt(maxRollDegrees, "maxRollDegrees");
        Objects.requireNonNull(densitySettings, "densitySettings");
        Objects.requireNonNull(haloSettings, "haloSettings");
        oreEntries = copyNonEmptyList(oreEntries, "oreEntries");
        validateTotalDistributionWeight(oreEntries);
    }

    public OreVeinDefinition(ResourceLocation id, List<ResourceKey<Level>> dimensions, int generationWeight, int minCenterY, int maxCenterYExclusive, int minSizeX, int maxSizeX, int minSizeY, int maxSizeY, int minSizeZ, int maxSizeZ, double maxPitchDegrees, double maxRollDegrees, OreVeinDensitySettings densitySettings, OreVeinHaloSettings haloSettings, List<VeinOreEntry> oreEntries) {
        this(id, dimensions, generationWeight, minCenterY, maxCenterYExclusive, minSizeX, maxSizeX, minSizeY, maxSizeY, minSizeZ, maxSizeZ, DEFAULT_SPARSE_REACH_BLOCKS, maxPitchDegrees, maxRollDegrees, densitySettings, haloSettings, oreEntries);
    }

    /*
    public Builder maxPitchDegrees(double maxPitchDegrees) {
        this.maxPitchDegrees = maxPitchDegrees;
        return this;
    }

    public Builder maxRollDegrees(double maxRollDegrees) {
        this.maxRollDegrees = maxRollDegrees;
        return this;
    }
    */

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Builder builder(ResourceLocation id) {
        return new Builder(id);
    }

    private static <T> @Unmodifiable @NotNull List<T> copyNonEmptyList(List<T> values, String name) {
        Objects.requireNonNull(values, name);

        if (values.isEmpty()) throw new IllegalArgumentException(name + " cannot be empty");

        return List.copyOf(values);
    }

    private static void validateGenerationWeight(int generationWeight) {
        if (generationWeight <= 0) throw new IllegalArgumentException("generationWeight must be positive");
    }

    private static void validateCenterYRange(int minCenterY, int maxCenterYExclusive) {
        if (minCenterY >= maxCenterYExclusive) throw new IllegalArgumentException("minCenterY must be less than maxCenterYExclusive");
    }

    private static void validateSizeRange(int minSize, int maxSize, String name) {
        if (minSize < 6) throw new IllegalArgumentException("min" + name + " must be at least 6");
        if (maxSize < minSize) throw new IllegalArgumentException("max" + name + " must be at least min" + name);
    }

    private static void validateTilt(double maxTiltDegrees, String name) {
        if (!Double.isFinite(maxTiltDegrees) || maxTiltDegrees < 0.0D || maxTiltDegrees > 90.0D) throw new IllegalArgumentException(name + " must be finite and in [0, 90]");
    }

    private static void validateSparseReachBlocks(int sparseReachBlocks) {
        if (sparseReachBlocks < 0) throw new IllegalArgumentException("sparseReachBlocks must be at least 0");
    }

    private static void validateTotalDistributionWeight(List<VeinOreEntry> oreEntries) {
        if (calculateTotalDistributionWeight(oreEntries) <= 0L)
            throw new IllegalArgumentException("total distribution weight must be positive");
    }

    private static long calculateTotalDistributionWeight(@NotNull List<VeinOreEntry> oreEntries) {
        long totalWeight = 0L;

        for (VeinOreEntry entry : oreEntries) totalWeight = addDistributionWeight(totalWeight, entry);

        return totalWeight;
    }

    private static long addDistributionWeight(long totalWeight, @NotNull VeinOreEntry entry) {
        try {
            return Math.addExact(totalWeight, entry.distributionWeight());
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("total distribution weight is too large", exception);
        }
    }

    public long totalDistributionWeight() {
        return calculateTotalDistributionWeight(oreEntries);
    }


    public static final class Builder {
        private final ResourceLocation id;
        private List<ResourceKey<Level>> dimensions;
        private Integer generationWeight;
        private Integer minCenterY;
        private Integer maxCenterYExclusive;
        private Integer minSizeX;
        private Integer maxSizeX;
        private Integer minSizeY;
        private Integer maxSizeY;
        private Integer minSizeZ;
        private Integer maxSizeZ;
        private Integer sparseReachBlocks;
        private List<VeinOreEntry> oreEntries;

        private Builder(ResourceLocation id) {
            this.id = Objects.requireNonNull(id, "id");
        }

        @SafeVarargs
        public final Builder dimensions(ResourceKey<Level>... dimensions) {
            return dimensions(List.of(dimensions));
        }

        public Builder dimensions(List<ResourceKey<Level>> dimensions) {
            this.dimensions = List.copyOf(dimensions);
            return this;
        }

        public Builder generationWeight(int generationWeight) {
            this.generationWeight = generationWeight;
            return this;
        }

        public Builder centerY(int minCenterY, int maxCenterYExclusive) {
            this.minCenterY = minCenterY;
            this.maxCenterYExclusive = maxCenterYExclusive;
            return this;
        }

        public Builder sizeX(int minSizeX, int maxSizeX) {
            this.minSizeX = minSizeX;
            this.maxSizeX = maxSizeX;
            return this;
        }

        public Builder sizeY(int minSizeY, int maxSizeY) {
            this.minSizeY = minSizeY;
            this.maxSizeY = maxSizeY;
            return this;
        }

        public Builder sizeZ(int minSizeZ, int maxSizeZ) {
            this.minSizeZ = minSizeZ;
            this.maxSizeZ = maxSizeZ;
            return this;
        }

        public Builder sparseReachBlocks(int sparseReachBlocks) {
            this.sparseReachBlocks = sparseReachBlocks;
            return this;
        }

        public Builder ores(VeinOreEntry... oreEntries) {
            return ores(List.of(oreEntries));
        }

        public Builder ores(List<VeinOreEntry> oreEntries) {
            this.oreEntries = List.copyOf(oreEntries);
            return this;
        }

        @Contract(" -> new")
        public @NotNull OreVeinDefinition build() {
            return new OreVeinDefinition(
                    id,
                    dimensions,
                    generationWeight,
                    minCenterY,
                    maxCenterYExclusive,
                    minSizeX,
                    maxSizeX,
                    minSizeY,
                    maxSizeY,
                    minSizeZ,
                    maxSizeZ,
                    sparseReachBlocks != null ? sparseReachBlocks : DEFAULT_SPARSE_REACH_BLOCKS,
                    DEFAULT_MAX_PITCH_DEGREES,//maxPitchDegrees != null ? maxPitchDegrees : DEFAULT_MAX_PITCH_DEGREES,
                    DEFAULT_MAX_ROLL_DEGREES,//maxRollDegrees != null ? maxRollDegrees : DEFAULT_MAX_ROLL_DEGREES,
                    DEFAULT_DENSITY_SETTINGS,
                    DEFAULT_HALO_SETTINGS,
                    oreEntries
            );
        }
    }
}
