package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.properties.FluidStates;
import com.mightydanp.techcore.materials.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidComponent<A extends Material> extends Component<FluidComponent<A>> {
    private Float acceleration = 0.014F;
    private FluidStates.FluidState state = null;
    private Integer density = null;
    private Integer viscosity = null;
    private Integer luminosity = null;

    public FlowingFluid fluid, fluid_flowing;
    public Block fluidBlock;

    private final A material;

    public FluidComponent(A material) {
        super("fluid", "component");
        this.material = material;
    }

    public FluidComponent<A> setAcceleration(float acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    public FluidComponent<A> setState(FluidStates.FluidState state) {
        this.state = state;
        return this;
    }

    public FluidComponent<A> setDensity(int density) {
        this.density = density;
        return this;
    }

    public FluidComponent<A> setViscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public FluidComponent<A> setLuminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    public Float getAcceleration() {
        return acceleration;
    }

    public Integer getLuminosity() {
        return luminosity;
    }

    public Integer getViscosity() {
        return viscosity;
    }

    public Integer getDensity() {
        return density;
    }

    public FluidStates.FluidState getState() {
        return state;
    }

    public A end(){
        return material;
    }
}