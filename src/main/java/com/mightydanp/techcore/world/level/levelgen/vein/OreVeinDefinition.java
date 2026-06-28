package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record OreVeinDefinition(ResourceLocation id, List<ResourceKey<Level>> dimensions, int generationWeight, int minCenterY, int maxCenterYExclusive, int minSizeX, int maxSizeX, int minSizeY, int maxSizeY, int minSizeZ, int maxSizeZ, boolean rotationEnabled, double maxPitchDegrees, double maxRollDegrees, List<ConfiguredVeinFeature> features, List<OreEntry> oreEntries) {

    public OreVeinDefinition {
        // Validate the full definition data before storing it
        Objects.requireNonNull(id, "id");
        dimensions = copyNonEmptyList(dimensions, "dimensions");
        validateGenerationWeight(generationWeight);
        validateCenterYRange(minCenterY, maxCenterYExclusive);
        validateSizeRange(minSizeX, maxSizeX, "sizeX");
        validateSizeRange(minSizeY, maxSizeY, "sizeY");
        validateSizeRange(minSizeZ, maxSizeZ, "sizeZ");
        validateSizeAcceptanceRange(minSizeX, minSizeY, minSizeZ);
        validateTilt(maxPitchDegrees, "maxPitchDegrees");
        validateTilt(maxRollDegrees, "maxRollDegrees");
        features = List.copyOf(Objects.requireNonNull(features, "features"));
        oreEntries = copyNonEmptyList(oreEntries, "oreEntries");
        validateTotalDistributionWeight(oreEntries);
    }

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
        if (minCenterY >= maxCenterYExclusive) throw new IllegalArgumentException("minCenterY must be less than maxCenterYExclusive");
    }

    private static void validateSizeRange(int minSize, int maxSize, String name) {
        if (minSize < 6) throw new IllegalArgumentException("min" + name + " must be at least 6");
        if (maxSize < minSize) throw new IllegalArgumentException("max" + name + " must be at least min" + name);
    }

    private static void validateSizeAcceptanceRange(int minSizeX, int minSizeY, int minSizeZ) {
        int smallestPossibleLargestSize = Math.max(minSizeX, Math.max(minSizeY, minSizeZ));

        if (smallestPossibleLargestSize > OreVeinGenerationMath.MAX_ACCEPTED_VEIN_SIZE_BLOCKS)
            throw new IllegalArgumentException("vein size range has no size with a positive acceptance chance; at least one possible size must be 96 blocks or smaller");
    }

    private static void validateTilt(double maxTiltDegrees, String name) {
        if (!Double.isFinite(maxTiltDegrees) || maxTiltDegrees < 0.0D || maxTiltDegrees > 90.0D)
            throw new IllegalArgumentException(name + " must be finite and in [0, 90]");
    }

    private static void validateTotalDistributionWeight(List<OreEntry> oreEntries) {
        if (calculateTotalDistributionWeight(oreEntries) <= 0L) throw new IllegalArgumentException("total distribution weight must be positive");
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

    public static final class Builder {
        private final ResourceLocation id;
        private List<ResourceKey<Level>> dimensions;
        private Integer generationWeight;
        private Integer minCenterY;
        private Integer maxCenterY;
        private Integer minSizeX = 16;
        private Integer maxSizeX = 96;
        private Integer minSizeY;
        private Integer maxSizeY;
        private Integer minSizeZ = 16;
        private Integer maxSizeZ = 96;
        private Double maxPitchDegrees = 12.0D;
        private Double maxRollDegrees = 12.0D;
        private boolean rotationEnabled = true;
        private final List<ConfiguredVeinFeature> features = new ArrayList<>();
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

        public Builder minSizeX(int minSizeX) {
            this.minSizeX = minSizeX;
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

        public Builder minSizeZ(int minSizeZ) {
            this.minSizeZ = minSizeZ;
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

        public Builder disableRotation() {
            this.rotationEnabled = false;
            return this;
        }

        public Builder addVeinFeature(int loadOrder, ConfiguredVeinFeature veinFeature) {
            Objects.requireNonNull(veinFeature, "veinFeature");
            if (loadOrder < 0) throw new IllegalArgumentException("loadOrder cannot be negative: " + loadOrder);

            int insertIndex = Math.min(loadOrder, features.size());
            features.add(insertIndex, new ConfiguredVeinFeature(veinFeature.featureId(), loadOrder, veinFeature.configuration()));
            return this;
        }

        public Builder addVeinFeature(ConfiguredVeinFeature veinFeature) {
            features.add(Objects.requireNonNull(veinFeature, "veinFeature"));
            return this;
        }

        public Builder features(ConfiguredVeinFeature... features) {
            return features(List.of(features));
        }

        public Builder features(List<ConfiguredVeinFeature> features) {
            this.features.clear();
            for (ConfiguredVeinFeature feature : features) addVeinFeature(feature);
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
                    rotationEnabled,
                    maxPitchDegrees,
                    maxRollDegrees,
                    features,
                    oreEntries
            );
        }
    }

    public record OreEntry(Supplier<com.mightydanp.techcore.materials.Material> oreMaterial, int distributionWeight) {
        public OreEntry {
            Objects.requireNonNull(oreMaterial, "oreMaterial");
            if (distributionWeight <= 0) throw new IllegalArgumentException("distributionWeight must be positive");
        }
    }
}
