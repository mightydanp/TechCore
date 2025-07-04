package com.mightydanp.techcore.materials.components;

public class PhysicalComponent extends Component {
    public double meltingPoint;
    public double boilingPoint;
    public double density;
    public int harvestLevel;

    public PhysicalComponent() {
        super("physical", "component",
                m -> { //server

            },  m -> { //client

        });
    }

    public PhysicalComponent setMeltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }

    public PhysicalComponent setBoilingPoint(double boilingPoint) {
        this.boilingPoint = boilingPoint;
        return this;
    }

    public PhysicalComponent setDensity(double density) {
        this.density = density;
        return this;
    }

    public PhysicalComponent setHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
        return this;
    }
}