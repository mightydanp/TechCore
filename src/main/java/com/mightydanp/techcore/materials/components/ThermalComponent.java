package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class ThermalComponent<A extends Material> extends Component<ThermalComponent<A>> {
    private double meltingPoint;
    private double boilingPoint;
    private double heatCapacity;

    private final A material;

    public ThermalComponent(A material) {
        super("thermal", "component");
        this.material = material;
    }

    public ThermalComponent<A> setMeltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }

    public ThermalComponent<A> setBoilingPoint(double boilingPoint) {
        this.boilingPoint = boilingPoint;
        return this;
    }

    public ThermalComponent<A> setHeatCapacity(double heatCapacity) {
        this.heatCapacity = heatCapacity;
        return this;
    }

    public double getMeltingPoint() {
        return meltingPoint;
    }

    public double getBoilingPoint() {
        return boilingPoint;
    }

    public double getHeatCapacity() {
        return heatCapacity;
    }

    public A end(){
        return material;
    }
}