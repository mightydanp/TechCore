package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class ThermalComponent<A extends Material> extends Component<A, ThermalComponent<A>> {
    private Double meltingPoint;
    private Double boilingPoint;
    private double heatCapacity;

    public ThermalComponent(A material) {
        super("thermal", "component", material);
    }

    public ThermalComponent<A> setMeltingPoint(Double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }

    public ThermalComponent<A> setBoilingPoint(Double boilingPoint) {
        this.boilingPoint = boilingPoint;
        return this;
    }

    public ThermalComponent<A> setHeatCapacity(double heatCapacity) {
        this.heatCapacity = heatCapacity;
        return this;
    }

    public Double getMeltingPoint() {
        return meltingPoint;
    }

    public Double getBoilingPoint() {
        return boilingPoint;
    }

    public double getHeatCapacity() {
        return heatCapacity;
    }
}