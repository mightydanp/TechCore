package com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition;

import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo.OreVeinSparseHaloEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator.OreCellResult.OreVariant.REGULAR_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator.OreCellResult.OreVariant.SPARSE_ORE;

public final class OreVeinSparseTransitionEvaluator {
    private static final long TRANSITION_VARIANT_SALT = 0xC6A4A7935BD1E995L;

    public static boolean isInsideTransitionHalf(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        if (transitionWidthBlocks <= 0.0D) return false;

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;

        return signedBoundaryDistanceBlocks > -halfTransitionWidth && signedBoundaryDistanceBlocks <= 0.0D;
    }

    public static OreVeinOreCellEvaluator.OreCellResult.OreVariant insideTransitionVariant(OreVeinInstanceDescriptor descriptor, BlockPos position, double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double insideTransitionProgress = Mth.clamp((signedBoundaryDistanceBlocks + halfTransitionWidth) / halfTransitionWidth, 0.0D, 1.0D);
        int sparseFormThreshold = Mth.floor(insideTransitionProgress * OreVeinSparseHaloEvaluator.sparseOccupancyDenominator());
        int variantRoll = OreVeinOreCellEvaluator.occupancyRoll(descriptor, position, TRANSITION_VARIANT_SALT, OreVeinSparseHaloEvaluator.sparseOccupancyDenominator());

        return variantRoll < sparseFormThreshold ? SPARSE_ORE : REGULAR_ORE;
    }

    public static int transitionShellThreshold(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        if (transitionWidthBlocks <= 0.0D) return sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double outsideTransitionProgress = Mth.clamp(signedBoundaryDistanceBlocks / halfTransitionWidth, 0.0D, 1.0D);
        int baseExteriorThreshold = sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        return Mth.floor(Mth.lerp(outsideTransitionProgress, OreVeinSparseHaloEvaluator.sparseOccupancyDenominator(), baseExteriorThreshold));
    }

    private static int sparseChanceDivisor(double distanceFromMainBody) {
        if (!Double.isFinite(distanceFromMainBody) || distanceFromMainBody < 0.0D) throw new IllegalArgumentException("distanceFromMainBody must be finite and non-negative");

        return 2 + Mth.floor((distanceFromMainBody * (distanceFromMainBody + 2.0D)) / 4.0D);
    }

    private static int sparseOccupancyThreshold(double distanceFromMainBody) {
        return Math.floorDiv(OreVeinSparseHaloEvaluator.sparseOccupancyDenominator(), sparseChanceDivisor(distanceFromMainBody));
    }
}
