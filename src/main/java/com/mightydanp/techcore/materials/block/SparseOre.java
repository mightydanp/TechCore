package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SparseOre extends MaterialBlock {
    public SparseOre(MaterialBlockProperties properties) {
        super(properties);
    }

    @Override
    public void destroy(@NotNull LevelAccessor worldIn, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(worldIn, pos, state);
    }
}