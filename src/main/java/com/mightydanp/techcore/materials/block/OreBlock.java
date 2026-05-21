package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.properties.MaterialBlock;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import com.mightydanp.techcore.materials.properties.OreTypeStates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class OreBlock extends MaterialBlock {
    public int maxDensity;

    public static EnumProperty<OreTypeStates> oreTypeStateProperty;
    public static IntegerProperty densityProperty;

    public OreBlock(MaterialBlockProperties properties, int maxDensity) {
        super(properties);

        this.maxDensity = maxDensity;
        oreTypeStateProperty = EnumProperty.create("ore_type_state", OreTypeStates.class);
        densityProperty = IntegerProperty.create("density", 0, maxDensity);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(densityProperty, maxDensity)
                .setValue(oreTypeStateProperty, OreTypeStates.NORMAL)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(densityProperty).add(oreTypeStateProperty);
    }

    @Override
    public void destroy(@NotNull LevelAccessor worldIn, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(worldIn, pos, state);
        int randomDensity = worldIn.getRandom().nextInt(maxDensity);
        String oreState = worldIn.getBlockState(pos).getValue(oreTypeStateProperty).toString();


        if(oreState.equals(OreTypeStates.SMALL.toString()) || oreState.equals(OreTypeStates.NORMAL.toString())){
            if(state.getValue(densityProperty) > maxDensity) {
                worldIn.setBlock(pos, state.setValue(densityProperty, state.getValue(densityProperty) - 1), 2);
            }else{
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }

        if(state.getValue(densityProperty) > 1) {
            worldIn.setBlock(pos, state.setValue(densityProperty, state.getValue(densityProperty) - 1), 2);
        }else{
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
    public int getMaxDensity(){
        return maxDensity;
    }



}
