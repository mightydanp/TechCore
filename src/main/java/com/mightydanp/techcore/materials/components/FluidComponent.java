package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.FluidStates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidComponent<A extends Material> extends Component<A, FluidComponent<A>> {
    public FlowingFluid fluid, fluid_flowing;
    public Block fluidBlock;
    private Float acceleration = 0.014F;
    private FluidStates.FluidState state = null;
    private Integer density = null;
    private Integer viscosity = null;
    private Integer luminosity = null;

    public FluidComponent(A material) {
        super("fluid", "component", material);
    }

    public Float getAcceleration() {
        return acceleration;
    }

    public FluidComponent<A> setAcceleration(float acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    public Integer getLuminosity() {
        return luminosity;
    }

    public FluidComponent<A> setLuminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    public Integer getViscosity() {
        return viscosity;
    }

    public FluidComponent<A> setViscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public Integer getDensity() {
        return density;
    }

    public FluidComponent<A> setDensity(int density) {
        this.density = density;
        return this;
    }

    public FluidStates.FluidState getState() {
        return state;
    }

    public FluidComponent<A> setState(FluidStates.FluidState state) {
        this.state = state;
        return this;
    }
}