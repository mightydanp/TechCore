package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Item.FluidStates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidComponent extends Component {
    public Float acceleration = 0.014F;
    public FluidStates.FluidState state = null;
    public Integer density = null;
    public Integer viscosity = null;
    public Integer luminosity = null;

    public FlowingFluid fluid, fluid_flowing;
    public Block fluidBlock;

    public FluidComponent() {
        super("fluid", "component");
    }

    public FluidComponent setAcceleration(float acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    public FluidComponent setState(FluidStates.FluidState state) {
        this.state = state;
        return this;
    }

    public FluidComponent setDensity(int density) {
        this.density = density;
        return this;
    }

    public FluidComponent setViscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public FluidComponent setLuminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }
}