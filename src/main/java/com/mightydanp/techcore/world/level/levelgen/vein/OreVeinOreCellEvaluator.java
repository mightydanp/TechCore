package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class OreVeinOreCellEvaluator {
    private static final int BASE_MAIN_BODY_FILL_NUMERATOR = 704;
    private static final int BASE_MAIN_BODY_FILL_DENOMINATOR = 1024;
    private static final int HOST_ROCK_PRIORITY = 0;
    private static final int REGULAR_ORE_PRIORITY = 20;

    private static final long MATERIAL_SELECTION_SALT = 0xD1B54A32D192ED03L;
    private static final long OCCUPANCY_SALT = 0x8CB92BA72F3D8DD7L;

    private static final long X_HASH_MULTIPLIER = 0x9E3779B97F4A7C15L;
    private static final long Y_HASH_MULTIPLIER = 0xC2B2AE3D27D4EB4FL;
    private static final long Z_HASH_MULTIPLIER = 0x165667B19E3779F9L;

    public static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution) {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(contribution, "contribution");

        // Resolve the definition once, then evaluate the cell with the full overload.
        return evaluateCell(descriptor, OreVeinDefinitions.requireDefinition(descriptor), position, contribution);
    }

    public static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(contribution, "contribution");

        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position);
        return evaluateCell(descriptor, definition, position, contribution, selectedOreEntry);
    }

    public static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, @NotNull Material hostRockMaterial) {
        Objects.requireNonNull(hostRockMaterial, "hostRockMaterial");

        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position, hostRockMaterial);
        return evaluateCell(descriptor, definition, position, contribution, selectedOreEntry);
    }

    private static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, SelectedOreEntry selectedOreEntry) {
        OreCellResult currentResult = contribution.signedBoundaryDistanceBlocks() <= 0.0D
                ? baseMainBodyResult(descriptor, position, contribution, selectedOreEntry)
                : hostRockResult(descriptor, selectedOreEntry, contribution);

        for (OreVeinDefinitions.ResolvedFeature resolvedFeature : OreVeinDefinitions.resolvedFeatures(definition))
            currentResult = contribution.signedBoundaryDistanceBlocks() <= 0.0D
                    ? resolvedFeature.feature().applyMainBody(descriptor, definition, resolvedFeature.configuredFeature(), position, contribution, selectedOreEntry, currentResult)
                    : resolvedFeature.feature().applyExterior(descriptor, definition, resolvedFeature.configuredFeature(), position, contribution, selectedOreEntry, currentResult);

        return currentResult;
    }

    private static OreCellResult baseMainBodyResult(OreVeinInstanceDescriptor descriptor, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, SelectedOreEntry selectedOreEntry) {
        int occupancyRoll = occupancyRoll(descriptor, position, OCCUPANCY_SALT, BASE_MAIN_BODY_FILL_DENOMINATOR);

        if (occupancyRoll >= BASE_MAIN_BODY_FILL_NUMERATOR) return hostRockResult(descriptor, selectedOreEntry, contribution);

        return createResult(descriptor, selectedOreEntry, contribution, 1, 1, OreCellResult.OreVariant.REGULAR_ORE, regularOreReplacement(), false);
    }

    public static boolean hasCompatibleOreEntry(@NotNull OreVeinDefinition definition, @NotNull Material hostRockMaterial) {
        for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
            Material material = Objects.requireNonNull(entry.oreMaterial().get(), "oreMaterial supplier returned null");

            if (OreVeinDefinitions.isOreCompatibleWithHost(material, hostRockMaterial)) return true;
        }

        return false;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull SelectedOreEntry selectOreEntry(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, @NotNull BlockPos position) {
        // Hash the world position and use it to pick an ore entry from the weighted list
        long hash = descriptor.instanceId() ^ MATERIAL_SELECTION_SALT;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;
        hash = mix64(hash);

        long totalWeight = definition.totalDistributionWeight();
        long roll = Math.floorMod(hash, totalWeight);
        long cursor = 0L;

        for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
            cursor += entry.distributionWeight();

            if (roll < cursor) {
                Material material = Objects.requireNonNull(entry.oreMaterial().get(), "oreMaterial supplier returned null");
                return new SelectedOreEntry(entry, material);
            }
        }

        throw new IllegalStateException("Failed to select ore entry for " + definition.id());
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull SelectedOreEntry selectOreEntry(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, @NotNull BlockPos position, @NotNull Material hostRockMaterial) {
        long hash = descriptor.instanceId() ^ MATERIAL_SELECTION_SALT;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;
        hash = mix64(hash);

        long totalWeight = 0L;

        for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
            Material material = Objects.requireNonNull(entry.oreMaterial().get(), "oreMaterial supplier returned null");

            if (OreVeinDefinitions.isOreCompatibleWithHost(material, hostRockMaterial))
                totalWeight = Math.addExact(totalWeight, entry.distributionWeight());
        }

        if (totalWeight <= 0L)
            throw new IllegalStateException("No compatible ore entries for " + definition.id() + " and host " + hostRockMaterial.name);

        long roll = Math.floorMod(hash, totalWeight);
        long cursor = 0L;

        for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
            Material material = Objects.requireNonNull(entry.oreMaterial().get(), "oreMaterial supplier returned null");

            if (!OreVeinDefinitions.isOreCompatibleWithHost(material, hostRockMaterial)) continue;

            cursor += entry.distributionWeight();

            if (roll < cursor) return new SelectedOreEntry(entry, material);
        }

        throw new IllegalStateException("Failed to select compatible ore entry for " + definition.id());
    }

    public static int occupancyRoll(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull BlockPos position, long salt, int fillDenominator) {
        // Hash the instance and block position into one bounded occupancy roll.
        long hash = descriptor.instanceSeed() ^ salt;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;

        return Math.floorMod(mix64(hash), fillDenominator);
    }

    public static double sampleDouble(long hash, double minValue, double maxValue) {
        // Return the exact value when the sampled range has no width.
        if (minValue == maxValue) return minValue;

        return minValue + unit(hash) * (maxValue - minValue);
    }

    public static int sampleIntInclusive(long hash, int minValue, int maxValue) {
        // Return the exact value when the sampled inclusive range has no width.
        if (minValue == maxValue) return minValue;

        return minValue + Math.floorMod(hash, maxValue - minValue + 1);
    }

    public static long mix64(long value) {
        long mixed = value;
        mixed ^= mixed >>> 30;
        mixed *= 0xBF58476D1CE4E5B9L;
        mixed ^= mixed >>> 27;
        mixed *= 0x94D049BB133111EBL;
        mixed ^= mixed >>> 31;
        return mixed;
    }

    public static int baseMainBodyFillDenominator() {
        return BASE_MAIN_BODY_FILL_DENOMINATOR;
    }

    public static long occupancySalt() {
        // Expose the main-body occupancy salt for shared deterministic tests and helpers.
        return OCCUPANCY_SALT;
    }

    public static long xHashMultiplier() {
        return X_HASH_MULTIPLIER;
    }

    public static long yHashMultiplier() {
        return Y_HASH_MULTIPLIER;
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static @NotNull OreCellResult createResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, int candidateDensity, int finalDensity, OreCellResult.OreVariant variant, OreReplacement replacement) {
        return createResult(descriptor, selectedOreEntry, contribution, candidateDensity, finalDensity, variant, replacement, false);
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static @NotNull OreCellResult createResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, int candidateDensity, int finalDensity, OreCellResult.OreVariant variant, OreReplacement replacement, boolean terminalDecision) {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(selectedOreEntry, "selectedOreEntry");
        Objects.requireNonNull(contribution, "contribution");

        return new OreCellResult(
                descriptor.instanceId(),
                descriptor.definitionId(),
                selectedOreEntry.material(),
                selectedOreEntry.entry().distributionWeight(),
                contribution,
                candidateDensity,
                finalDensity,
                variant,
                terminalDecision,
                replacement
        );
    }

    @Contract("_, _, _ -> new")
    public static @NotNull OreCellResult hostRockResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        return createResult(descriptor, selectedOreEntry, contribution, 1, 0, OreCellResult.OreVariant.HOST_ROCK, NoOreReplacement.INSTANCE);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull OreCellResult terminalHostRockResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        return createResult(descriptor, selectedOreEntry, contribution, 1, 0, OreCellResult.OreVariant.HOST_ROCK, NoOreReplacement.INSTANCE, true);
    }

    public static OreReplacement regularOreReplacement() {
        return RegularOreReplacement.INSTANCE;
    }

    public static @NotNull Supplier<Block> supplierFor(@NotNull Map<String, Supplier<Block>> blocks, String hostName, ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreCellResult winner) {
        if (!blocks.containsKey(hostName))
            throw invalidReplacement("missing block mapping for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);

        Supplier<Block> supplier = blocks.get(hostName);

        if (supplier == null)
            throw invalidReplacement("null block supplier for host key '" + hostName + "'", dimension, position, originalHostMaterial, winner);

        return supplier;
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull IllegalStateException invalidReplacement(String reason, @NotNull ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, @NotNull OreCellResult winner) {
        return new IllegalStateException("Invalid resolved ore replacement:"
                + " dimension=" + dimension.location()
                + ", position=" + position
                + ", host=" + materialName(originalHostMaterial)
                + ", veinId=" + winner.definitionId()
                + ", oreMaterial=" + materialName(winner.selectedMaterial())
                + ", variant=" + winner.variant()
                + ", density=" + winner.finalDensity()
                + ", reason=" + reason);
    }

    private static String materialName(Material material) {
        return material == null ? "null" : material.name;
    }

    private static double unit(long hash) {
        return (OreVeinShapeEvaluator.hashToSignedUnit(hash) + 1.0D) * 0.5D;
    }

    public interface OreReplacement {
        int priority();

        BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreCellResult winner);
    }

    private enum NoOreReplacement implements OreReplacement {
        INSTANCE;

        @Override
        public int priority() {
            return HOST_ROCK_PRIORITY;
        }

        @Override
        public BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreCellResult winner) {
            throw invalidReplacement("host-rock winner is not replaceable", dimension, position, originalHostMaterial, winner);
        }
    }

    private enum RegularOreReplacement implements OreReplacement {
        INSTANCE;

        @Override
        public int priority() {
            return REGULAR_ORE_PRIORITY;
        }

        @Override
        public BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, @NotNull OreCellResult winner) {
            Supplier<Block> supplier = supplierFor(winner.selectedMaterial().ore.getOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            Block block = supplier.get();

            if (block == null) throw invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);

            return block.defaultBlockState();
        }
    }

    public record OreCellResult(long instanceId, net.minecraft.resources.ResourceLocation definitionId, Material selectedMaterial, int selectedDistributionWeight, OreVeinShapeEvaluator.ShapeContribution shapeContribution, int candidateDensity, int finalDensity, OreVariant variant, boolean terminalDecision, OreReplacement replacement) {
        public OreCellResult {
            Objects.requireNonNull(definitionId, "definitionId");
            Objects.requireNonNull(selectedMaterial, "selectedMaterial");
            Objects.requireNonNull(shapeContribution, "shapeContribution");
            Objects.requireNonNull(variant, "variant");
            Objects.requireNonNull(replacement, "replacement");

            if (selectedDistributionWeight <= 0) throw new IllegalArgumentException("selectedDistributionWeight must be positive");
            if (candidateDensity < 1) throw new IllegalArgumentException("candidateDensity must be at least 1");
            if (finalDensity < 0) throw new IllegalArgumentException("finalDensity must be non-negative");
        }

        public enum OreVariant {
            HOST_ROCK,
            REGULAR_ORE,
            FEATURE_ORE
        }
    }

    public record SelectedOreEntry(OreVeinDefinition.OreEntry entry, Material material) {}
}
