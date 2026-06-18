package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.materials.properties.RockTypes;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class OreVeinDefinitions {
    public static final double MAX_BOUNDARY_DISTORTION_BLOCKS = 7.0D;

    private static final Map<ResourceLocation, OreVeinDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static final Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> GENERATION_SETTINGS = new LinkedHashMap<>();
    private static final Map<ResourceKey<Level>, OreVeinOverlapSettings> OVERLAP_SETTINGS = new LinkedHashMap<>();

    @Contract("_, _, _, _ -> param3")
    private static <K, V> V registerUnique(@NotNull Map<K, V> registry, K key, V value, Function<K, String> duplicateMessage) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(duplicateMessage, "duplicateMessage");

        V previous = registry.putIfAbsent(key, value);

        if (previous != null) throw new IllegalArgumentException(
                duplicateMessage.apply(key)
        );

        return value;
    }

    public static OreVeinDefinition register(OreVeinDefinition definition) {
        Objects.requireNonNull(definition, "definition");

        return registerUnique(
                DEFINITIONS,
                definition.id(),
                definition,
                id -> "Duplicate ore vein definition ID: " + id
        );
    }

    public static OreVeinDimensionGenerationSettings registerGenerationSettings(OreVeinDimensionGenerationSettings settings) {
        Objects.requireNonNull(settings, "settings");

        return registerUnique(
                GENERATION_SETTINGS,
                settings.dimension(),
                settings,
                dimension -> "Duplicate ore vein dimension generation settings: " + dimension.location()
        );
    }

    public static @NotNull @Unmodifiable List<OreVeinDefinition> getDefinitions() {
        return List.copyOf(DEFINITIONS.values());
    }

    public static OreVeinDefinition getDefinition(ResourceLocation id) {
        return DEFINITIONS.get(id);
    }

    public static @NotNull @Unmodifiable List<OreVeinDimensionGenerationSettings> getGenerationSettings() {
        return List.copyOf(GENERATION_SETTINGS.values());
    }

    public static OreVeinDimensionGenerationSettings getGenerationSettings(ResourceKey<Level> dimension) {
        return GENERATION_SETTINGS.get(dimension);
    }

    public static OreVeinOverlapSettings registerOverlapSettings(ResourceKey<Level> dimension, OreVeinOverlapSettings settings) {
        return registerUnique(
                OVERLAP_SETTINGS,
                dimension,
                settings,
                key -> "Duplicate ore vein overlap settings: " + key.location()
        );
    }

    public static OreVeinOverlapSettings getOverlapSettings(ResourceKey<Level> dimension) {
        return OVERLAP_SETTINGS.get(dimension);
    }

    public static boolean isUnsupportedDimension(ResourceKey<Level> dimension) {
        Objects.requireNonNull(dimension, "dimension");

        return !GENERATION_SETTINGS.containsKey(dimension) || !OVERLAP_SETTINGS.containsKey(dimension) || DEFINITIONS.values().stream().noneMatch(definition -> definition.dimensions().contains(dimension));
    }

    public static void validateAll() {
        validate(getDefinitions(), getGenerationSettings());
    }

    public static void validate(Collection<OreVeinDefinition> definitions) {
        validate(definitions, getGenerationSettings());
    }

    public static void validate(Collection<OreVeinDefinition> definitions, Collection<OreVeinDimensionGenerationSettings> settings) {
        Objects.requireNonNull(definitions, "definitions");
        Objects.requireNonNull(settings, "settings");

        List<String> problems = new ArrayList<>();
        Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> settingsByDimension = validateSettings(settings, problems);

        for (OreVeinDefinition definition : definitions) {
            if (definition == null) {
                problems.add("unknown\n  reason: null ore vein definition");
                continue;
            }

            validateDefinition(definition, settingsByDimension, problems);
        }

        validateDimensionBudgets(definitions, settingsByDimension, problems);
        validateOverlapSettings(definitions, settingsByDimension, problems);

        if (!problems.isEmpty()) {
            throw new IllegalStateException("Invalid ore vein definitions:\n\n" + String.join("\n\n", problems));
        }
    }

    public static boolean isOreCompatibleWithHost(Material oreMaterial, Material hostRockMaterial) {
        Objects.requireNonNull(oreMaterial, "oreMaterial");
        Objects.requireNonNull(hostRockMaterial, "hostRockMaterial");

        return oreMaterial.ore.getRockTypes().contains(hostRockMaterial.rockLayer.rockType);
    }

    public static void assertOreCompatibleWithHost(Material oreMaterial, Material hostRockMaterial) {
        assertOreCompatibleWithHost(null, oreMaterial, hostRockMaterial);
    }

    public static void assertOreCompatibleWithHost(ResourceLocation veinDefinitionId, Material oreMaterial, Material hostRockMaterial) {
        if (isOreCompatibleWithHost(oreMaterial, hostRockMaterial)) return;

        throw new IllegalStateException("Incompatible ore vein host rock:\n"
                + "  vein definition: " + (veinDefinitionId == null ? "unknown" : veinDefinitionId) + "\n"
                + "  ore: " + materialName(oreMaterial) + "\n"
                + "  host rock: " + materialName(hostRockMaterial) + "\n"
                + "  host RockType: " + rockTypeName(hostRockMaterial) + "\n"
                + "  allowed ore RockTypes: " + rockTypes(oreMaterial) + "\n"
                + "  reason: incompatible RockType");
    }

    public static @Nullable DimensionHeight dimensionHeight(@NotNull ResourceKey<Level> dimension) {
        ResourceLocation location = dimension.location();

        if (ResourceLocation.withDefaultNamespace("overworld").equals(location)) return new DimensionHeight(-64, 320);
        if (ResourceLocation.withDefaultNamespace("the_nether").equals(location) || ResourceLocation.withDefaultNamespace("the_end").equals(location)) return new DimensionHeight(0, 256);

        return null;
    }

    private static @NotNull Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> validateSettings(@NotNull Collection<OreVeinDimensionGenerationSettings> settings, List<String> problems) {
        Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> settingsByDimension = new LinkedHashMap<>();

        for (OreVeinDimensionGenerationSettings setting : settings) {
            if (setting == null) {
                problems.add("unknown\n  reason: null ore vein dimension generation settings");
                continue;
            }

            if (setting.originWeightBudget() <= 0) {
                problems.add(setting.dimension().location() + "\n  originWeightBudget: " + setting.originWeightBudget() + "\n  reason: originWeightBudget must be positive");
            }

            OreVeinDimensionGenerationSettings previous = settingsByDimension.putIfAbsent(setting.dimension(), setting);

            if (previous != null) problems.add(setting.dimension().location() + "\n  reason: duplicate dimension generation settings");
        }

        return settingsByDimension;
    }

    private static void validateDefinition(OreVeinDefinition definition, Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> settingsByDimension, List<String> problems) {
        validateGenerationFields(definition, problems);

        List<ResolvedOreEntry> oreEntries = resolveOreEntries(definition, problems);
        validateDistributionWeights(definition, problems);
        validateOreMaterials(definition, oreEntries, problems);
        validateDuplicateOreMaterials(definition, oreEntries, problems);

        Set<RockTypes.RockType> commonRockTypes = commonRockTypes(oreEntries);

        if (!hasResolvedOreMaterial(oreEntries)) problems.add(problem(definition, null, null, null, null, null, commonRockTypes, "no resolved ore materials"));
        if (commonRockTypes.isEmpty()) problems.add(problem(definition, null, null, null, null, null, commonRockTypes, "no common RockType across vein ores"));

        for (ResourceKey<Level> dimension : definition.dimensions()) {
            if (!settingsByDimension.containsKey(dimension)) problems.add(problem(definition, dimension, null, null, null, null, commonRockTypes, "missing dimension generation settings"));

            DimensionHeight height = dimensionHeight(dimension);

            if (height == null) problems.add(problem(definition, dimension, null, null, null, null, commonRockTypes, "missing dimension height limits"));
            else validateLegalCenterRange(definition, dimension, height, commonRockTypes, problems);

            validateDimensionHosts(definition, dimension, oreEntries, commonRockTypes, problems);
        }
    }

    private static void validateGenerationFields(@NotNull OreVeinDefinition definition, List<String> problems) {
        if (definition.generationWeight() <= 0) problems.add(problem(definition, null, null, null, null, null, null, "generationWeight must be positive"));

        if (definition.minCenterY() >= definition.maxCenterYExclusive()) problems.add(problem(definition, null, null, null, null, null, null, "minCenterY must be less than maxCenterYExclusive"));

        validateSizeRange(definition, definition.minSizeX(), definition.maxSizeX(), "X", problems);
        validateSizeRange(definition, definition.minSizeY(), definition.maxSizeY(), "Y", problems);
        validateSizeRange(definition, definition.minSizeZ(), definition.maxSizeZ(), "Z", problems);
        validateTilt(definition, definition.maxPitchDegrees(), "maxPitchDegrees", problems);
        validateTilt(definition, definition.maxRollDegrees(), "maxRollDegrees", problems);
        validateDensitySettings(definition, problems);
        validateHaloSettings(definition, problems);
    }

    private static void validateHaloSettings(@NotNull OreVeinDefinition definition, List<String> problems) {
        OreVeinHaloSettings haloSettings = definition.haloSettings();

        if (!Double.isFinite(haloSettings.transitionWidthBlocks()) || haloSettings.transitionWidthBlocks() < 0.0D) problems.add(problem(definition, null, null, null, null, null, null, "transitionWidthBlocks must be finite and non-negative"));
    }


    private static void validateDensitySettings(@NotNull OreVeinDefinition definition, List<String> problems) {
        OreVeinDensitySettings settings = definition.densitySettings();

        validateNodeRadius(definition, settings.maxNodeRadiusX(), definition.minSizeX(), "X", problems);
        validateNodeRadius(definition, settings.maxNodeRadiusY(), definition.minSizeY(), "Y", problems);
        validateNodeRadius(definition, settings.maxNodeRadiusZ(), definition.minSizeZ(), "Z", problems);
    }

    private static void validateNodeRadius(OreVeinDefinition definition, double maxRadius, int minSize, String axis, List<String> problems) {
        if (maxRadius > minSize / 2.0D) problems.add(
                problem(
                        definition,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "maxNodeRadius" + axis + " exceeds minSize" + axis + " / 2.0")
        );
    }

    private static void validateSizeRange(OreVeinDefinition definition, int minSize, int maxSize, String axis, List<String> problems) {
        if (minSize < 6) problems.add(problem(definition, null, null, null, null, null, null, "minSize" + axis + " must be at least 6"));

        if (maxSize < minSize) problems.add(problem(definition, null, null, null, null, null, null, "maxSize" + axis + " must be at least minSize" + axis));
    }

    private static void validateTilt(OreVeinDefinition definition, double value, String name, List<String> problems) {
        if (!Double.isFinite(value) || value < 0.0D || value > 90.0D) problems.add(problem(definition, null, null, null, null, null, null, name + " must be finite and in [0, 90]"));

    }

    private static void validateLegalCenterRange(OreVeinDefinition definition, ResourceKey<Level> dimension, @NotNull DimensionHeight height, Set<RockTypes.RockType> commonRockTypes, List<String> problems) {
        int maxHalfY = maxAllowedHalfY(definition);
        int minLegalCenterY = Math.max(definition.minCenterY(), height.minY() + maxHalfY);
        int maxLegalCenterYExclusive = Math.min(definition.maxCenterYExclusive(), height.maxYExclusive() - maxHalfY);

        if (minLegalCenterY >= maxLegalCenterYExclusive) problems.add(problem(definition, dimension, null, null, null, null, commonRockTypes, "no legal center Y can keep rotated bounds inside dimension"));
    }

    private static int maxAllowedHalfY(@NotNull OreVeinDefinition definition) {
        int maxHalfY = 0;

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

                maxHalfY = Math.max(maxHalfY, halfExtents.y());
            }
        }

        return maxHalfY + (int) Math.ceil(MAX_BOUNDARY_DISTORTION_BLOCKS);
    }

    private static void validateDimensionHosts(OreVeinDefinition definition, ResourceKey<Level> dimension, List<ResolvedOreEntry> oreEntries, Set<RockTypes.RockType> commonRockTypes, List<String> problems) {
        List<Material> dimensionHosts = RockLayerFeature.getAllowedMaterials(dimension);

        if (dimensionHosts.isEmpty()) {
            problems.add(problem(definition, dimension, null, null, null, null, commonRockTypes, "dimension has no rock layers"));
            return;
        }

        List<Material> eligibleHosts = eligibleHosts(dimensionHosts, commonRockTypes);

        if (eligibleHosts.isEmpty()) {
            problems.add(problem(definition, dimension, null, null, null, null, commonRockTypes, "no rock layer in the dimension matches the common RockTypes"));
            return;
        }

        for (Material hostRockMaterial : eligibleHosts) {
            for (ResolvedOreEntry resolvedOreEntry : oreEntries) {
                Material oreMaterial = resolvedOreEntry.material();

                if (isInvalidOre(oreMaterial)) continue;

                validateOreEntry(definition, dimension, resolvedOreEntry.entry(), oreMaterial, hostRockMaterial, commonRockTypes, problems);
            }
        }
    }

    private static void validateDimensionBudgets(Collection<OreVeinDefinition> definitions, @NotNull Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> settingsByDimension, List<String> problems) {
        for (OreVeinDimensionGenerationSettings settings : settingsByDimension.values()) {
            List<OreVeinDefinition> eligibleDefinitions = definitions.stream()
                    .filter(Objects::nonNull)
                    .filter(definition -> definition.dimensions().contains(settings.dimension()))
                    .toList();

            BigInteger totalQ16 = OreVeinGenerationMath.totalEffectiveWeightQ16(eligibleDefinitions);
            BigInteger budgetQ16 = OreVeinGenerationMath.budgetQ16(settings);

            if (totalQ16.compareTo(budgetQ16) > 0) {
                problems.add(settings.dimension().location() + "\n"
                        + "  originWeightBudget: " + settings.originWeightBudget() + "\n"
                        + "  total effective weight Q16: " + totalQ16 + "\n"
                        + "  budget Q16: " + budgetQ16 + "\n"
                        + "  reason: total effective generation weight exceeds dimension budget");
            }
        }
    }

    private static void validateOverlapSettings(Collection<OreVeinDefinition> definitions, Map<ResourceKey<Level>, OreVeinDimensionGenerationSettings> settingsByDimension, List<String> problems) {
        for (Map.Entry<ResourceKey<Level>, OreVeinOverlapSettings> entry : OVERLAP_SETTINGS.entrySet()) {
            OreVeinOverlapSettings settings = entry.getValue();

            if (settings.denominator() <= 0) problems.add(entry.getKey().location() + "\n  reason: overlap denominator must be positive");
            if (settings.mainBodyGapNumerator() < 0 || settings.mainBodyGapNumerator() > settings.denominator()) problems.add(entry.getKey().location() + "\n  reason: overlap mainBodyGapNumerator must be in [0, denominator]");
        }

        for (OreVeinDefinition definition : definitions) {
            if (definition == null) continue;

            for (ResourceKey<Level> dimension : definition.dimensions())
                if (settingsByDimension.containsKey(dimension) && !OVERLAP_SETTINGS.containsKey(dimension)) problems.add(problem(definition, dimension, null, null, null, null, null, "missing dimension overlap settings"));
        }
    }

    private static void validateDistributionWeights(@NotNull OreVeinDefinition definition, List<String> problems) {
        long totalWeight = 0L;

        for (VeinOreEntry entry : definition.oreEntries()) {
            if (entry.distributionWeight() <= 0) {
                problems.add(problem(definition, null, entry, null, null, null, null, "distribution weight must be positive"));
                continue;
            }

            try {
                totalWeight = Math.addExact(totalWeight, entry.distributionWeight());
            } catch (ArithmeticException exception) {
                problems.add(problem(definition, null, entry, null, null, null, null, "distribution-weight overflow"));
            }
        }

        if (totalWeight <= 0L) problems.add(problem(definition, null, null, null, null, null, null, "total distribution weight must be positive"));
    }

    private static void validateOreMaterials(OreVeinDefinition definition, @NotNull List<ResolvedOreEntry> oreEntries, List<String> problems) {
        for (ResolvedOreEntry resolvedOreEntry : oreEntries) {
            Material oreMaterial = resolvedOreEntry.material();

            if (oreMaterial == null) continue;

            if (isInvalidOre(oreMaterial)) {
                problems.add(problem(definition, null, resolvedOreEntry.entry(), oreMaterial, null, null, null, "invalid ore material"));
                continue;
            }

            if (oreMaterial.ore.getMaxDensity() < 1) problems.add(problem(definition, null, resolvedOreEntry.entry(), oreMaterial, null, null, null, "ore max density must be at least 1"));
        }
    }

    private static void validateDuplicateOreMaterials(OreVeinDefinition definition, @NotNull List<ResolvedOreEntry> oreEntries, List<String> problems) {
        Map<Material, VeinOreEntry> seen = new IdentityHashMap<>();

        for (ResolvedOreEntry resolvedOreEntry : oreEntries) {
            Material oreMaterial = resolvedOreEntry.material();

            if (oreMaterial == null) continue;

            VeinOreEntry previous = seen.putIfAbsent(oreMaterial, resolvedOreEntry.entry());

            if (previous != null) problems.add(problem(definition, null, resolvedOreEntry.entry(), oreMaterial, null, null, null, "duplicate ore material"));
        }
    }

    private static Set<RockTypes.RockType> commonRockTypes(@NotNull List<ResolvedOreEntry> oreEntries) {
        LinkedHashSet<RockTypes.RockType> commonRockTypes = null;

        for (ResolvedOreEntry resolvedOreEntry : oreEntries) {
            Material oreMaterial = resolvedOreEntry.material();

            if (isInvalidOre(oreMaterial)) continue;

            LinkedHashSet<RockTypes.RockType> oreRockTypes = new LinkedHashSet<>(oreMaterial.ore.getRockTypes());

            if (commonRockTypes == null) commonRockTypes = oreRockTypes;
            else commonRockTypes.retainAll(oreRockTypes);
        }

        return commonRockTypes == null ? Set.of() : Collections.unmodifiableSet(commonRockTypes);
    }

    private static boolean hasResolvedOreMaterial(@NotNull List<ResolvedOreEntry> oreEntries) {
        return oreEntries.stream().anyMatch(entry -> entry.material() != null);
    }

    private static @NotNull @Unmodifiable List<Material> eligibleHosts(@NotNull List<Material> dimensionHosts, Set<RockTypes.RockType> commonRockTypes) {
        return dimensionHosts.stream()
                .filter(OreVeinDefinitions::isConfiguredHostRock)
                .filter(material -> commonRockTypes.contains(material.rockLayer.rockType))
                .toList();
    }


    private static void validateOreEntry(OreVeinDefinition definition, ResourceKey<Level> dimension, VeinOreEntry entry, Material oreMaterial, Material hostRockMaterial, Set<RockTypes.RockType> commonRockTypes, List<String> problems) {
        if (!isOreCompatibleWithHost(oreMaterial, hostRockMaterial)) problems.add(problem(definition, dimension, entry, oreMaterial, hostRockMaterial, null, commonRockTypes, "internal validation inconsistency: incompatible RockType"));

        validateRequiredBlock(definition, dimension, entry, oreMaterial, hostRockMaterial, "oreBlocks", oreMaterial.ore.getOreBlocks(), commonRockTypes, problems);
        validateRequiredBlock(definition, dimension, entry, oreMaterial, hostRockMaterial, "sparseOreBlocks", oreMaterial.ore.getSparseOreBlocks(), commonRockTypes, problems);

        if (maxReachableDensity(definition, oreMaterial) >= 2) validateRequiredBlock(definition, dimension, entry, oreMaterial, hostRockMaterial, "denseOreBlocks", oreMaterial.ore.getDenseOreBlocks(), commonRockTypes, problems);

    }

    private static int maxReachableDensity(@NotNull OreVeinDefinition definition, @NotNull Material oreMaterial) {
        return Math.min(
                definition.densitySettings().maxPeakDensity(),
                Math.min(DenseOre.MAX_DENSITY_PROPERTY, oreMaterial.ore.getMaxDensity())
        );
    }

    private static void validateRequiredBlock(OreVeinDefinition definition, ResourceKey<Level> dimension, VeinOreEntry entry, Material oreMaterial, @NotNull Material hostRockMaterial, String requiredMap, @NotNull Map<String, Supplier<Block>> blocks, Set<RockTypes.RockType> commonRockTypes, List<String> problems) {
        Supplier<Block> block = blocks.get(hostRockMaterial.name);

        if (block == null) problems.add(problem(definition, dimension, entry, oreMaterial, hostRockMaterial, requiredMap, commonRockTypes, "missing " + requiredMap + " mapping"));
    }

    private static @NotNull List<ResolvedOreEntry> resolveOreEntries(@NotNull OreVeinDefinition definition, List<String> problems) {
        List<ResolvedOreEntry> entries = new ArrayList<>();

        for (VeinOreEntry entry : definition.oreEntries()) entries.add(new ResolvedOreEntry(entry, resolveOreMaterial(definition, entry, problems)));

        return entries;
    }

    private static @Nullable Material resolveOreMaterial(OreVeinDefinition definition, VeinOreEntry entry, List<String> problems) {
        try {
            Material material = entry.oreMaterial().get();

            if (material == null) problems.add(problem(definition, null, entry, null, null, null, null, "ore supplier returned null"));

            return material;
        } catch (RuntimeException exception) {
            problems.add(problem(definition, null, entry, null, null, null, null, "ore supplier threw: " + exception.getClass().getSimpleName() + ": " + exception.getMessage()));
            return null;
        }
    }

    private static boolean isConfiguredHostRock(Material material) {
        return material != null && material.rockLayer != null && material.rockLayer.isRockLayer && material.rockLayer.rockType != null;
    }

    private static boolean isInvalidOre(Material material) {
        return material == null || material.ore == null || material.ore.getOreType() == null || material.ore.getRockTypes().isEmpty();
    }

    private static @NotNull String problem(@NotNull OreVeinDefinition definition, ResourceKey<Level> dimension, VeinOreEntry entry, Material oreMaterial, Material hostRockMaterial, String requiredMap, Set<RockTypes.RockType> commonRockTypes, String reason) {
        StringBuilder message = new StringBuilder();
        message.append(definition.id()).append("\n");
        message.append("  dimension: ").append(dimension == null ? "unknown" : dimension.location()).append("\n");
        message.append("  generationWeight: ").append(definition.generationWeight()).append("\n");
        message.append("  centerY range: [").append(definition.minCenterY()).append(", ").append(definition.maxCenterYExclusive()).append(")\n");
        message.append("  size ranges: X[").append(definition.minSizeX()).append(", ").append(definition.maxSizeX()).append("] Y[").append(definition.minSizeY()).append(", ").append(definition.maxSizeY()).append("] Z[").append(definition.minSizeZ()).append(", ").append(definition.maxSizeZ()).append("]\n");

        if (entry != null) message.append("  ore distribution weight: ").append(entry.distributionWeight()).append("\n");

        if (oreMaterial != null || entry != null) message.append("  ore: ").append(materialName(oreMaterial)).append("\n");

        message.append("  vein ore RockTypes: ").append(veinOreRockTypes(definition)).append("\n");

        if (commonRockTypes != null) message.append("  common RockTypes: ").append(rockTypes(commonRockTypes)).append("\n");

        if (hostRockMaterial != null) {
            message.append("  host rock: ").append(materialName(hostRockMaterial)).append("\n");
            message.append("  host RockType: ").append(rockTypeName(hostRockMaterial)).append("\n");
        }

        if (oreMaterial != null) message.append("  allowed ore RockTypes: ").append(rockTypes(oreMaterial)).append("\n");


        if (requiredMap != null) message.append("  required map: ").append(requiredMap).append("\n");

        message.append("  reason: ").append(reason);

        return message.toString();
    }

    private static String materialName(Material material) {
        return material == null ? "unknown" : material.name;
    }

    @Contract(pure = true)
    private static String rockTypeName(@NotNull Material hostRockMaterial) {
        RockTypes.RockType rockType = hostRockMaterial.rockLayer == null ? null : hostRockMaterial.rockLayer.rockType;
        return rockType == null ? "unknown" : rockType.name();
    }

    private static String rockTypes(Material oreMaterial) {
        if (oreMaterial == null || oreMaterial.ore == null) return "unknown";

        return rockTypes(oreMaterial.ore.getRockTypes());
    }

    private static String rockTypes(@NotNull Collection<RockTypes.RockType> rockTypes) {
        return rockTypes.stream()
                .map(rockType -> rockType == null ? "unknown" : rockType.name())
                .toList()
                .toString();
    }

    private static String veinOreRockTypes(@NotNull OreVeinDefinition definition) {
        List<String> values = new ArrayList<>();

        for (VeinOreEntry entry : definition.oreEntries()) {
            Material oreMaterial;

            try {
                oreMaterial = entry.oreMaterial().get();
            } catch (RuntimeException exception) {
                values.add("unknown=supplier threw");
                continue;
            }

            values.add(materialName(oreMaterial) + "=" + rockTypes(oreMaterial));
        }

        return values.toString();
    }

    public static int checkedCeilToInt(double value, String name) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException(name + " must be finite");

        double ceil = Math.ceil(value);

        if (ceil > Integer.MAX_VALUE) throw new IllegalArgumentException(name + " is too large");

        return Math.toIntExact((long) ceil);
    }

    public record DimensionHeight(int minY, int maxYExclusive) {
    }

    private record ResolvedOreEntry(VeinOreEntry entry, Material material) {
    }
}
