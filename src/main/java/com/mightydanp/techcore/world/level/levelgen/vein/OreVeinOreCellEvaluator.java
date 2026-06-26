package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.vein.densenode.OreVeinDenseNodeEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo.OreVeinSparseHaloEvaluator;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class OreVeinOreCellEvaluator {
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

        // Pick the ore entry for this position before deciding between main-body and halo logic.
        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position);

        // If the position is inside the main body use dense node logic, otherwise use sparse halo logic
        if (contribution.signedBoundaryDistanceBlocks() <= 0.0D)
            return OreVeinDenseNodeEvaluator.evaluateMainBodyCell(descriptor, definition, position, contribution, selectedOreEntry);

        return OreVeinSparseHaloEvaluator.evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry);
    }

    public static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, @NotNull Material hostRockMaterial) {
        Objects.requireNonNull(hostRockMaterial, "hostRockMaterial");

        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position, hostRockMaterial);

        if (contribution.signedBoundaryDistanceBlocks() <= 0.0D)
            return OreVeinDenseNodeEvaluator.evaluateMainBodyCell(descriptor, definition, position, contribution, selectedOreEntry);

        return OreVeinSparseHaloEvaluator.evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry);
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

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static @NotNull OreCellResult createResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, int candidateDensity, int finalDensity, OreCellResult.OreVariant variant, long nodeId, double nodeInfluence) {
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
                nodeId,
                nodeInfluence
        );
    }

    @Contract("_, _, _ -> new")
    public static @NotNull OreCellResult hostRockResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        return createResult(descriptor, selectedOreEntry, contribution, 1, 0, OreCellResult.OreVariant.HOST_ROCK, 0L, 0.0D);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull OreCellResult sparseOreResult(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull SelectedOreEntry selectedOreEntry, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        return createResult(descriptor, selectedOreEntry, contribution, 1, 1, OreCellResult.OreVariant.SPARSE_ORE, 0L, 0.0D);
    }

    private static double unit(long hash) {
        return (OreVeinShapeEvaluator.hashToSignedUnit(hash) + 1.0D) * 0.5D;
    }

    public record OreCellResult(long instanceId, net.minecraft.resources.ResourceLocation definitionId, Material selectedMaterial, int selectedDistributionWeight, OreVeinShapeEvaluator.ShapeContribution shapeContribution, int candidateDensity, int finalDensity, OreVariant variant, long winningNodeId, double winningNodeInfluence) {
        public OreCellResult {
            Objects.requireNonNull(definitionId, "definitionId");
            Objects.requireNonNull(selectedMaterial, "selectedMaterial");
            Objects.requireNonNull(shapeContribution, "shapeContribution");
            Objects.requireNonNull(variant, "variant");

            if (selectedDistributionWeight <= 0) throw new IllegalArgumentException("selectedDistributionWeight must be positive");
            if (candidateDensity < 1) throw new IllegalArgumentException("candidateDensity must be at least 1");
            if (finalDensity < 0) throw new IllegalArgumentException("finalDensity must be non-negative");
            if (!Double.isFinite(winningNodeInfluence)) throw new IllegalArgumentException("winningNodeInfluence must be finite");
        }

        public enum OreVariant {
            HOST_ROCK,
            REGULAR_ORE,
            DENSE_ORE,
            SPARSE_ORE
        }
    }

    public record SelectedOreEntry(OreVeinDefinition.OreEntry entry, Material material) {}
}
