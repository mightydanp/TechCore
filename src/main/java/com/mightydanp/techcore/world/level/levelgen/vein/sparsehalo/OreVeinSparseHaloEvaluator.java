package com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo;

import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinDefinition;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition.OreVeinSparseTransitionEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class OreVeinSparseHaloEvaluator {
    private static final long HALO_OCCUPANCY_SALT = 0xDB4F0B9175AE2165L;
    private static final int SPARSE_OCCUPANCY_DENOMINATOR = 1024;

    public static OreVeinOreCellEvaluator.OreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry) {
        // Use the normal halo occupancy roll unless another one is passed in
        return evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry, (rollDescriptor, rollPosition, fillDenominator) -> OreVeinOreCellEvaluator.occupancyRoll(rollDescriptor, rollPosition, HALO_OCCUPANCY_SALT, fillDenominator));
    }

    public static int sparseOccupancyDenominator() {
        return SPARSE_OCCUPANCY_DENOMINATOR;
    }

    private static OreVeinOreCellEvaluator.OreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, HaloOccupancyRollProvider occupancyRollProvider) {
        // Use the transition shell rules near the boundary and the sparse halo rules farther out.
        double signedBoundaryDistanceBlocks = contribution.signedBoundaryDistanceBlocks();
        double transitionWidthBlocks = definition.haloSettings().transitionWidthBlocks();
        double halfTransitionWidth = transitionWidthBlocks * 0.5D;

        if (transitionWidthBlocks <= 0.0D || signedBoundaryDistanceBlocks > halfTransitionWidth) return sparseResult(descriptor, definition, position, contribution, selectedOreEntry, occupancyRollProvider);

        int threshold = OreVeinSparseTransitionEvaluator.transitionShellThreshold(signedBoundaryDistanceBlocks, transitionWidthBlocks);
        int sparseOccupancyRoll = occupancyRollProvider.roll(descriptor, position, SPARSE_OCCUPANCY_DENOMINATOR);

        if (sparseOccupancyRoll >= threshold) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        return OreVeinOreCellEvaluator.sparseOreResult(descriptor, selectedOreEntry, contribution);
    }

    private static OreVeinOreCellEvaluator.OreCellResult sparseResult(OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, HaloOccupancyRollProvider occupancyRollProvider) {
        // Outside the transition shell, fall back to the normal sparse halo occupancy curve.
        double distanceFromMainBody = contribution.signedBoundaryDistanceBlocks();
        int sparseReachBlocks = definition.sparseReachBlocks();

        if (distanceFromMainBody > sparseReachBlocks) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        int threshold = sparseOccupancyThreshold(distanceFromMainBody);

        if (threshold == 0) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        int sparseOccupancyRoll = occupancyRollProvider.roll(descriptor, position, SPARSE_OCCUPANCY_DENOMINATOR);

        if (sparseOccupancyRoll >= threshold) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        return OreVeinOreCellEvaluator.sparseOreResult(descriptor, selectedOreEntry, contribution);
    }

    private static int sparseChanceDivisor(double distanceFromMainBody) {
        if (!Double.isFinite(distanceFromMainBody) || distanceFromMainBody < 0.0D)
            throw new IllegalArgumentException("distanceFromMainBody must be finite and non-negative");

        return 2 + Mth.floor((distanceFromMainBody * (distanceFromMainBody + 2.0D)) / 4.0D);
    }

    private static int sparseOccupancyThreshold(double distanceFromMainBody) {
        return Math.floorDiv(SPARSE_OCCUPANCY_DENOMINATOR, sparseChanceDivisor(distanceFromMainBody));
    }

    @FunctionalInterface
    private interface HaloOccupancyRollProvider {
        int roll(OreVeinInstanceDescriptor descriptor, BlockPos position, int fillDenominator);
    }
}
