package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DenseOre extends OreBlock {
    public static final int MAX_DENSITY_PROPERTY = 64;
    public static final IntegerProperty DENSITY = IntegerProperty.create("density", 0, MAX_DENSITY_PROPERTY);

    private final int maxDensity;

    public DenseOre(MaterialBlockProperties properties, Material oreMaterial, Material hostMaterial, Supplier<Item> rawOreItem, int maxDensity) {
        super(properties, oreMaterial, hostMaterial, rawOreItem);
        this.maxDensity = Math.min(maxDensity, MAX_DENSITY_PROPERTY);
        this.registerDefaultState(this.stateDefinition.any().setValue(DENSITY, this.maxDensity));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(DENSITY);
    }

    @Override
    protected double baseRockMultiplier() {
        return 0.95D;
    }

    @Override
    protected void afterSuccessfulHarvest(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ServerPlayer player, @NotNull ItemStack tool, @NotNull PendingHarvestOperation operation) {
        int density = state.getValue(DENSITY);
        if (density <= 1) {
            finishPermanentHarvest(operation);
            return;
        }

        BlockState restoredState = state.setValue(DENSITY, density - 1);
        suppressRestoration(operation, restoredState);

        boolean restored;
        try {
            restored = level.setBlock(pos, restoredState, 2);
        } catch (RuntimeException | Error exception) {
            failHarvest(operation);
            throw exception;
        }

        finishDenseHarvest(operation, restoredState, restored);
    }

    public int getMaxDensity() {
        return maxDensity;
    }
}
