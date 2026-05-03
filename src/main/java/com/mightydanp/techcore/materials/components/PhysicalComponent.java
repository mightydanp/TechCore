package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class PhysicalComponent<A extends Material> extends Component<PhysicalComponent<A>> {
    private int color;
    private double density;
    private int harvestLevel;

    private final A material;

    public PhysicalComponent(A material) {
        super("physical", "component");
        this.material = material;
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
        return color;
    }

    public double getDensity() {
        return density;
    }

    public int getHarvestLevel() {
        return harvestLevel;
    }

    public A end() {
        return material;
    }
}