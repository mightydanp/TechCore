package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record OreVeinDefinition(ResourceLocation id, List<ResourceKey<Level>> dimensions, int generationWeight, int minCenterY, int maxCenterYExclusive, int minSizeX, int maxSizeX, int minSizeY, int maxSizeY, int minSizeZ, int maxSizeZ, int sparseReachBlocks, boolean sparseHaloEnabled, boolean sparseTransitionEnabled, boolean denseNodeEnabled, boolean rotationEnabled, double maxPitchDegrees, double maxRollDegrees, DensitySettings densitySettings, HaloSettings haloSettings, List<OreEntry> oreEntries) {
    private static final DensitySettings DEFAULT_DENSITY_SETTINGS = new DensitySettings(
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
    private static final HaloSettings DEFAULT_HALO_SETTINGS = new HaloSettings(4.0D);

    public OreVeinDefinition {
        // Validate the full definition data before storing it
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
        // Return a builder for this vein definition id
        return new Builder(id);
    }

    private static <T> @Unmodifiable @NotNull List<T> copyNonEmptyList(List<T> values, String name) {
        Objects.requireNonNull(values, name);

        // Empty lists are not valid for required definition fields.
        if (values.isEmpty()) throw new IllegalArgumentException(name + " cannot be empty");

        return List.copyOf(values);
    }

    private static void validateGenerationWeight(int generationWeight) {
        if (generationWeight <= 0) throw new IllegalArgumentException("generationWeight must be positive");
    }

    private static void validateCenterYRange(int minCenterY, int maxCenterYExclusive) {
        if (minCenterY >= maxCenterYExclusive)
            throw new IllegalArgumentException("minCenterY must be less than maxCenterYExclusive");
    }

    private static void validateSizeRange(int minSize, int maxSize, String name) {
        if (minSize < 6) throw new IllegalArgumentException("min" + name + " must be at least 6");
        if (maxSize < minSize) throw new IllegalArgumentException("max" + name + " must be at least min" + name);
    }

    private static void validateTilt(double maxTiltDegrees, String name) {
        if (!Double.isFinite(maxTiltDegrees) || maxTiltDegrees < 0.0D || maxTiltDegrees > 90.0D)
            throw new IllegalArgumentException(name + " must be finite and in [0, 90]");
    }

    private static void validateSparseReachBlocks(int sparseReachBlocks) {
        if (sparseReachBlocks < 0) throw new IllegalArgumentException("sparseReachBlocks must be at least 0");
    }

    private static void validateTotalDistributionWeight(List<OreEntry> oreEntries) {
        if (calculateTotalDistributionWeight(oreEntries) <= 0L)
            throw new IllegalArgumentException("total distribution weight must be positive");
    }

    private static long calculateTotalDistributionWeight(@NotNull List<OreEntry> oreEntries) {
        long totalWeight = 0L;

        for (OreEntry entry : oreEntries)
            try {
                totalWeight = Math.addExact(totalWeight, entry.distributionWeight());
            } catch (ArithmeticException exception) {
                throw new IllegalArgumentException("total distribution weight is too large", exception);
            }

        return totalWeight;
    }

    public long totalDistributionWeight() {
        // Recalculate the total weight from the current immutable ore-entry list.
        return calculateTotalDistributionWeight(oreEntries);
    }

    public int effectiveSparseHaloReachBlocks() {
        return sparseHaloEnabled ? sparseReachBlocks : 0;
    }

    public boolean hasSparseTransition() {
        return sparseTransitionEnabled
                && haloSettings.transitionWidthBlocks() > 0.0D;
    }

    public boolean canGenerateSparseOre() {
        return effectiveSparseHaloReachBlocks() > 0
                || hasSparseTransition();
    }


    public static final class Builder {
        private final ResourceLocation id;
        private List<ResourceKey<Level>> dimensions;
        private Integer generationWeight;
        private Integer minCenterY;
        private Integer maxCenterY;
        private Integer minSizeX = 8;
        private Integer maxSizeX;
        private Integer minSizeY;
        private Integer maxSizeY;
        private Integer minSizeZ = 8;
        private Integer maxSizeZ;
        private Double maxPitchDegrees = 12.0D;
        private Double maxRollDegrees = 12.0D;
        private Integer sparseReachBlocks = 32;
        private boolean sparseHaloEnabled = true;
        private boolean sparseTransitionEnabled = true;
        private boolean denseNodeEnabled = true;
        private boolean rotationEnabled = true;
        private List<OreEntry> oreEntries;

        private Builder(ResourceLocation id) {
            // Store the definition id now so every later builder step can reuse it.
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
            this.maxCenterY = maxCenterYExclusive;
            return this;
        }

        public Builder sizeX(int minSizeX, int maxSizeX) {
            this.minSizeX = minSizeX;
            this.maxSizeX = maxSizeX;
            return this;
        }

        public Builder maxSizeX(int maxSizeX) {
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

        public Builder maxSizeZ(int maxSizeZ) {
            this.maxSizeZ = maxSizeZ;
            return this;
        }

        public Builder maxPitchDegrees(double maxPitchDegrees) {
            this.maxPitchDegrees = maxPitchDegrees;
            return this;
        }

        public Builder maxRollDegrees(double maxRollDegrees) {
            this.maxRollDegrees = maxRollDegrees;
            return this;
        }

        public Builder sparseReachBlocks(int sparseReachBlocks) {
            this.sparseReachBlocks = sparseReachBlocks;
            return this;
        }

        public Builder disableSparseFade() {
            this.sparseHaloEnabled = false;
            return this;
        }

        public Builder disableSparseTransition() {
            this.sparseTransitionEnabled = false;
            return this;
        }

        public Builder disableDenseNode() {
            this.denseNodeEnabled = false;
            return this;
        }

        public Builder disableRotation() {
            this.rotationEnabled = false;
            return this;
        }

        public Builder ores(OreEntry... oreEntries) {
            return ores(List.of(oreEntries));
        }

        public Builder ores(List<OreEntry> oreEntries) {
            this.oreEntries = List.copyOf(oreEntries);
            return this;
        }

        @Contract(" -> new")
        public @NotNull OreVeinDefinition build() {
            // Build the final definition and fill in default values where needed
            return new OreVeinDefinition(
                    id,
                    dimensions,
                    generationWeight,
                    minCenterY,
                    maxCenterY,
                    minSizeX,
                    maxSizeX,
                    minSizeY,
                    maxSizeY,
                    minSizeZ,
                    maxSizeZ,
                    sparseReachBlocks,
                    sparseHaloEnabled,
                    sparseTransitionEnabled,
                    denseNodeEnabled,
                    rotationEnabled,
                    maxPitchDegrees,
                    maxRollDegrees,
                    DEFAULT_DENSITY_SETTINGS,
                    DEFAULT_HALO_SETTINGS,
                    oreEntries
            );
        }
    }

    public record DensitySettings(int regularFillNumerator, int maximumFillNumerator, int fillDenominator, long blocksPerDenseNode, int minNodeCount, int maxNodeCount, double minNodeRadiusX, double maxNodeRadiusX, double minNodeRadiusY, double maxNodeRadiusY, double minNodeRadiusZ, double maxNodeRadiusZ, int minPeakDensity, int maxPeakDensity) {
        public DensitySettings {
            if (fillDenominator <= 0) throw new IllegalArgumentException("fillDenominator must be positive");
            if (regularFillNumerator < 0 || regularFillNumerator > fillDenominator)
                throw new IllegalArgumentException("regularFillNumerator must be in [0, fillDenominator]");
            if (maximumFillNumerator < regularFillNumerator || maximumFillNumerator > fillDenominator)
                throw new IllegalArgumentException("maximumFillNumerator must be in [regularFillNumerator, fillDenominator]");
            if (blocksPerDenseNode <= 0L) throw new IllegalArgumentException("blocksPerDenseNode must be positive");
            if (minNodeCount < 0 || maxNodeCount < minNodeCount)
                throw new IllegalArgumentException("node-count range is invalid");
            validateRadiusRange(minNodeRadiusX, maxNodeRadiusX, "X");
            validateRadiusRange(minNodeRadiusY, maxNodeRadiusY, "Y");
            validateRadiusRange(minNodeRadiusZ, maxNodeRadiusZ, "Z");
            if (minPeakDensity < 1 || maxPeakDensity < minPeakDensity)
                throw new IllegalArgumentException("peak-density range is invalid");
        }

        private static void validateRadiusRange(double minRadius, double maxRadius, String axis) {
            if (!Double.isFinite(minRadius) || !Double.isFinite(maxRadius) || minRadius <= 0.0D || maxRadius < minRadius) throw new IllegalArgumentException("node-radius " + axis + " range is invalid");
        }
    }

    public record HaloSettings(double transitionWidthBlocks) {
        public HaloSettings {
            if (!Double.isFinite(transitionWidthBlocks) || transitionWidthBlocks < 0.0D) throw new IllegalArgumentException("transitionWidthBlocks must be finite and non-negative");
        }
    }

    public record OreEntry(Supplier<com.mightydanp.techcore.materials.Material> oreMaterial, int distributionWeight) {
        public OreEntry {
            Objects.requireNonNull(oreMaterial, "oreMaterial");
            if (distributionWeight <= 0) throw new IllegalArgumentException("distributionWeight must be positive");
        }
    }
}
