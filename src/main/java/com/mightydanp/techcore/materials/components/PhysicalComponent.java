package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class PhysicalComponent<A extends Material> extends Component<A, PhysicalComponent<A>> {
    private int color;
    private double density;
    private int harvestLevel;

    public PhysicalComponent(A material) {
        super("physical", "component", material);
    }

    public PhysicalComponent<A> setColor(int color) {
        this.color = color;
        return this;
    }

    public PhysicalComponent<A> setDensity(double density) {
        this.density = density;
        return this;
    }

    public PhysicalComponent<A> setHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
        return this;
    }


    public int getColor() {
        return this.color;
    }

    public double getDensity() {
        return this.density;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }
}