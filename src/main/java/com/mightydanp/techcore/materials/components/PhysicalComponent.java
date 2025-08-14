package com.mightydanp.techcore.materials.components;

public class PhysicalComponent extends Component {
    public double density;
    public int harvestLevel;

    public PhysicalComponent() {
        super("physical", "component");
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