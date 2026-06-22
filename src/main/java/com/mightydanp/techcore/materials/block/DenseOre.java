package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
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
    protected void afterSuccessfulHarvest(@NotNull Level level, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        if (level.isClientSide()) return;

        int density = state.getValue(DENSITY);
        if (density > 1) level.setBlock(pos, state.setValue(DENSITY, density - 1), 2);
        else level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
    }

    @Override
    protected int explosionCandidateCount(@NotNull BlockState state, LootParams.Builder params) {
        return params.getLevel().random.nextInt(state.getValue(DENSITY) + 1);
    }

    public int getMaxDensity() {
        return maxDensity;
    }
}


