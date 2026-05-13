package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class PhysicalComponent<A extends Material> extends Component<PhysicalComponent<A>> {
    private int color;
    private double density;
    private int harvestLevel;

    private Integer defaultQuantity;
    private Integer defaultQuality;
    private Double defaultPurity;
    private Integer maxQuantity;
    private Integer maxQuality;
    private Double maxPurity;

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

    public PhysicalComponent<A> setDefaultQuantity(Integer defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
        return this;
    }

    public PhysicalComponent<A> setDefaultQuality(Integer defaultQuality) {
        this.defaultQuality = defaultQuality;
        return this;
    }

    public PhysicalComponent<A> setDefaultPurity(Double defaultPurity) {
        this.defaultPurity = defaultPurity;
        return this;
    }

    public PhysicalComponent<A> setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public PhysicalComponent<A> setMaxQuality(Integer maxQuality) {
        this.maxQuality = maxQuality;
        return this;
    }

    public PhysicalComponent<A> setMaxPurity(Double maxPurity) {
        this.maxPurity = maxPurity;
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

    public Integer getDefaultQuantity() {
        return defaultQuantity;
    }

    public Integer getDefaultQuality() {
        return defaultQuality;
    }

    public Double getDefaultPurity() {
        return defaultPurity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public Integer getMaxQuality() {
        return maxQuality;
    }

    public Double getMaxPurity() {
        return maxPurity;
    }

    public A end() {
        return material;
    }
}