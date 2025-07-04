package com.mightydanp.techcore.materials.components;

public class ThermalComponent extends Component {
    public double meltingPoint;
    public double boilingPoint;
    public double heatCapacity;

    public ThermalComponent() {
        super("thermal", "component",
                m -> { //server

            },  m -> { //client

        });
    }

    public ThermalComponent setMeltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }

    public ThermalComponent setBoilingPoint(double boilingPoint) {
        this.boilingPoint = boilingPoint;
        return this;
    }

    public ThermalComponent setHeatCapacity(double heatCapacity) {
        this.heatCapacity = heatCapacity;
        return this;
    }
}