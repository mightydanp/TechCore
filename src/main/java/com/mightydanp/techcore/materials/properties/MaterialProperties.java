package com.mightydanp.techcore.materials.properties;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class MaterialProperties extends Item.Properties {
    private String symbol;
    private int color;

    private Double boilingPoint;
    private Double meltingPoint;

    private Integer maxQuantity;
    private Integer maxQuality;
    private Double maxPurity;


    public MaterialProperties symbol(String value) {
        this.symbol = value;
        return this;
    }

    public MaterialProperties color(int color) {
        this.color = color;
        return this;
    }

    public MaterialProperties boilingPoint(double value) {
        this.boilingPoint = value;
        return this;
    }

    public MaterialProperties meltingPoint(double value) {
        this.meltingPoint = value;
        return this;
    }

    public MaterialProperties maxQuantity(int value) {
        this.maxQuantity = value;
        return this;
    }

    public MaterialProperties maxQuality(int value) {
        this.maxQuality = value;
        return this;
    }

    public MaterialProperties maxPurity(double value) {
        this.maxPurity = value;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getColor() {return color;
    }

    public Double getBoilingPoint() {
        return boilingPoint;
    }

    public Double getMeltingPoint() {
        return meltingPoint;
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

    // --- Item.Properties overrides ---

    @Override
    public @NotNull MaterialProperties food(@NotNull FoodProperties value) {
        super.food(value);
        return this;
    }

    @Override
    public @NotNull MaterialProperties stacksTo(int value) {
        super.stacksTo(value);
        return this;
    }

    @Override
    public @NotNull MaterialProperties durability(int value) {
        super.durability(value);
        return this;
    }

    @Override
    public @NotNull MaterialProperties craftRemainder(@NotNull Item item) {
        super.craftRemainder(item);
        return this;
    }

    @Override
    public @NotNull MaterialProperties rarity(@NotNull Rarity value) {
        super.rarity(value);
        return this;
    }

    @Override
    public @NotNull MaterialProperties fireResistant() {
        super.fireResistant();
        return this;
    }

    @Override
    public @NotNull MaterialProperties setNoRepair() {
        super.setNoRepair();
        return this;
    }

    @Override
    public @NotNull MaterialProperties requiredFeatures(FeatureFlag @NotNull ... flags) {
        super.requiredFeatures(flags);
        return this;
    }

}