package com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class OreVeinSparseHaloEvaluator {
    private static final long HALO_OCCUPANCY_SALT = 0xDB4F0B9175AE2165L;
    private static final int SPARSE_OCCUPANCY_DENOMINATOR = 1024;
    private static final int SPARSE_HALO_PRIORITY = 10;

    public static OreVeinOreCellEvaluator.OreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, @NotNull SparseHaloVeinFeature.Config config, BlockPos position, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry) {
        double distanceFromMainBody = contribution.signedBoundaryDistanceBlocks();

        if (distanceFromMainBody > config.reachBlocks()) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        int threshold = sparseOccupancyThreshold(distanceFromMainBody);

        if (threshold == 0) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        int sparseOccupancyRoll = OreVeinOreCellEvaluator.occupancyRoll(
                descriptor,
                position,
                HALO_OCCUPANCY_SALT,
                SPARSE_OCCUPANCY_DENOMINATOR
        );

        if (sparseOccupancyRoll >= threshold) return OreVeinOreCellEvaluator.hostRockResult(descriptor, selectedOreEntry, contribution);

        return sparseOreResult(descriptor, selectedOreEntry, contribution);
    }

    public static int sparseOccupancyDenominator() {
        return SPARSE_OCCUPANCY_DENOMINATOR;
    }

    @Contract("_, _, _ -> new")
    private static OreVeinOreCellEvaluator.@NotNull OreCellResult sparseOreResult(OreVeinInstanceDescriptor descriptor, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinShapeEvaluator.ShapeContribution contribution) {
        return OreVeinOreCellEvaluator.createResult(
                descriptor,
                selectedOreEntry,
                contribution,
                1,
                1,
                OreVeinOreCellEvaluator.OreCellResult.OreVariant.FEATURE_ORE,
                SparseHaloOreReplacement.INSTANCE
        );
    }

    private static int sparseChanceDivisor(double distanceFromMainBody) {
        if (!Double.isFinite(distanceFromMainBody) || distanceFromMainBody < 0.0D)
            throw new IllegalArgumentException("distanceFromMainBody must be finite and non-negative");

        return 2 + Mth.floor((distanceFromMainBody * (distanceFromMainBody + 2.0D)) / 4.0D);
    }

    private static int sparseOccupancyThreshold(double distanceFromMainBody) {
        return Math.floorDiv(SPARSE_OCCUPANCY_DENOMINATOR, sparseChanceDivisor(distanceFromMainBody));
    }

    private enum SparseHaloOreReplacement implements OreVeinOreCellEvaluator.OreReplacement {
        INSTANCE;

        @Override
        public int priority() {
            return SPARSE_HALO_PRIORITY;
        }

        @Override
        public @NotNull BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellEvaluator.OreCellResult winner) {
            Supplier<Block> supplier = OreVeinOreCellEvaluator.supplierFor(winner.selectedMaterial().ore.getSparseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            Block block = supplier.get();

            if (block == null) throw OreVeinOreCellEvaluator.invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);

            return block.defaultBlockState();
        }
    }

    @FunctionalInterface
    private interface HaloOccupancyRollProvider {
        int roll(OreVeinInstanceDescriptor descriptor, BlockPos position, int fillDenominator);
    }
}
