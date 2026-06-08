package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class DenseOre extends MaterialBlock {
    public static final int MAX_DENSITY_PROPERTY = 64;
    public static final IntegerProperty DENSITY = IntegerProperty.create("density", 0, MAX_DENSITY_PROPERTY);

    private final int maxDensity;

    public DenseOre(MaterialBlockProperties properties, int maxDensity) {
        super(properties);

        this.maxDensity = Math.min(maxDensity, MAX_DENSITY_PROPERTY);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DENSITY, this.maxDensity)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(DENSITY);
    }

    @Override
    public void destroy(@NotNull LevelAccessor worldIn, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(worldIn, pos, state);

        int density = state.getValue(DENSITY);

        if (density > 1) {
            worldIn.setBlock(pos, state.setValue(DENSITY, density - 1), 2);
        } else {
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    public int getMaxDensity() {
        return maxDensity;
    }
}
