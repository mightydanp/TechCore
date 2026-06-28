package com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class OreVeinSparseTransitionEvaluator {
    private static final long TRANSITION_VARIANT_SALT = 0xC6A4A7935BD1E995L;
    private static final int SPARSE_OCCUPANCY_DENOMINATOR = 1024;
    private static final int SPARSE_TRANSITION_PRIORITY = 10;

    public static boolean isInsideTransitionHalf(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        // Check if the position is in the inner half of the transition shell
        if (transitionWidthBlocks <= 0.0D) return false;

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;

        return signedBoundaryDistanceBlocks > -halfTransitionWidth && signedBoundaryDistanceBlocks <= 0.0D;
    }

    public static boolean insideTransitionSelectsSparseOre(OreVeinInstanceDescriptor descriptor, BlockPos position, double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        // Blend between regular ore and sparse ore as the cell approaches the boundary.
        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double insideTransitionProgress = Mth.clamp((signedBoundaryDistanceBlocks + halfTransitionWidth) / halfTransitionWidth, 0.0D, 1.0D);
        int sparseFormThreshold = Mth.floor(insideTransitionProgress * SPARSE_OCCUPANCY_DENOMINATOR);
        int variantRoll = OreVeinOreCellEvaluator.occupancyRoll(descriptor, position, TRANSITION_VARIANT_SALT, SPARSE_OCCUPANCY_DENOMINATOR);

        return variantRoll < sparseFormThreshold;
    }

    public static OreVeinOreCellEvaluator.OreReplacement sparseTransitionOreReplacement() {
        return SparseTransitionOreReplacement.INSTANCE;
    }

    public static long transitionVariantSalt() {
        return TRANSITION_VARIANT_SALT;
    }

    public static int sparseOccupancyDenominator() {
        return SPARSE_OCCUPANCY_DENOMINATOR;
    }

    public static int transitionShellThreshold(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        // Fade the sparse shell threshold between the outer shell and the boundary edge.
        if (transitionWidthBlocks <= 0.0D) return sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double outsideTransitionProgress = Mth.clamp(signedBoundaryDistanceBlocks / halfTransitionWidth, 0.0D, 1.0D);
        int baseExteriorThreshold = sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        return Mth.floor(Mth.lerp(outsideTransitionProgress, SPARSE_OCCUPANCY_DENOMINATOR, baseExteriorThreshold));
    }

    private static int sparseChanceDivisor(double distanceFromMainBody) {
        if (!Double.isFinite(distanceFromMainBody) || distanceFromMainBody < 0.0D)
            throw new IllegalArgumentException("distanceFromMainBody must be finite and non-negative");

        return 2 + Mth.floor((distanceFromMainBody * (distanceFromMainBody + 2.0D)) / 4.0D);
    }

    private static int sparseOccupancyThreshold(double distanceFromMainBody) {
        return Math.floorDiv(SPARSE_OCCUPANCY_DENOMINATOR, sparseChanceDivisor(distanceFromMainBody));
    }

    private enum SparseTransitionOreReplacement implements OreVeinOreCellEvaluator.OreReplacement {
        INSTANCE;

        @Override
        public int priority() {
            return SPARSE_TRANSITION_PRIORITY;
        }

        @Override
        public @NotNull BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellEvaluator.OreCellResult winner) {
            Supplier<Block> supplier = OreVeinOreCellEvaluator.supplierFor(winner.selectedMaterial().ore.getSparseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            Block block = supplier.get();

            if (block == null) throw OreVeinOreCellEvaluator.invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);

            return block.defaultBlockState();
        }
    }
}
