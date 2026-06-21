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

        OreVeinDefinition definition = Objects.requireNonNull(
                OreVeinDefinitions.getDefinition(descriptor.definitionId()),
                "Missing ore vein definition: " + descriptor.definitionId()
        );

        return evaluateCell(descriptor, definition, position, contribution);
    }

    public static OreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution) {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(contribution, "contribution");

        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position);

        if (contribution.signedBoundaryDistanceBlocks() <= 0.0D)
            return OreVeinDenseNodeEvaluator.evaluateMainBodyCell(descriptor, definition, position, contribution, selectedOreEntry);

        return OreVeinSparseHaloEvaluator.evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull SelectedOreEntry selectOreEntry(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, @NotNull BlockPos position) {
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

    public static int occupancyRoll(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull BlockPos position, long salt, int fillDenominator) {
        long hash = descriptor.instanceSeed() ^ salt;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;

        return Math.floorMod(mix64(hash), fillDenominator);
    }

    public static double sampleDouble(long hash, double minValue, double maxValue) {
        if (minValue == maxValue) return minValue;

        return minValue + unit(hash) * (maxValue - minValue);
    }

    public static int sampleIntInclusive(long hash, int minValue, int maxValue) {
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
        return OCCUPANCY_SALT;
    }

    public static long xHashMultiplier() {
        return X_HASH_MULTIPLIER;
    }

    public static long yHashMultiplier() {
        return Y_HASH_MULTIPLIER;
    }

    private static double unit(long hash) {
        return (OreVeinShapeEvaluator.hashToSignedUnit(hash) + 1.0D) * 0.5D;
    }

    public record CellResultFactory(OreVeinInstanceDescriptor descriptor, SelectedOreEntry selectedOreEntry,
                                    OreVeinShapeEvaluator.ShapeContribution contribution) {
        public CellResultFactory {
            Objects.requireNonNull(descriptor, "descriptor");
            Objects.requireNonNull(selectedOreEntry, "selectedOreEntry");
            Objects.requireNonNull(contribution, "contribution");
        }

        @Contract("_, _, _, _, _ -> new")
        public @NotNull OreCellResult create(int candidateDensity, int finalDensity, OreCellResult.OreVariant variant, long nodeId, double nodeInfluence) {
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

        @Contract(" -> new")
        public @NotNull OreCellResult hostRock() {
            return create(1, 0, OreCellResult.OreVariant.HOST_ROCK, 0L, 0.0D);
        }

        @Contract(" -> new")
        public @NotNull OreCellResult sparseOre() {
            return create(1, 1, OreCellResult.OreVariant.SPARSE_ORE, 0L, 0.0D);
        }
    }

    public record OreCellResult(long instanceId, net.minecraft.resources.ResourceLocation definitionId,
                                Material selectedMaterial, int selectedDistributionWeight,
                                OreVeinShapeEvaluator.ShapeContribution shapeContribution, int candidateDensity,
                                int finalDensity, OreVariant variant, long winningNodeId, double winningNodeInfluence) {
        public OreCellResult {
            Objects.requireNonNull(definitionId, "definitionId");
            Objects.requireNonNull(selectedMaterial, "selectedMaterial");
            Objects.requireNonNull(shapeContribution, "shapeContribution");
            Objects.requireNonNull(variant, "variant");

            if (selectedDistributionWeight <= 0)
                throw new IllegalArgumentException("selectedDistributionWeight must be positive");
            if (candidateDensity < 1) throw new IllegalArgumentException("candidateDensity must be at least 1");
            if (finalDensity < 0) throw new IllegalArgumentException("finalDensity must be non-negative");
            if (!Double.isFinite(winningNodeInfluence))
                throw new IllegalArgumentException("winningNodeInfluence must be finite");
        }

        public enum OreVariant {
            HOST_ROCK,
            REGULAR_ORE,
            DENSE_ORE,
            SPARSE_ORE
        }
    }

    public record SelectedOreEntry(OreVeinDefinition.OreEntry entry, Material material) {
    }
}
