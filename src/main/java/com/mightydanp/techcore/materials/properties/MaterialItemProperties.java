package com.mightydanp.techcore.materials.properties;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class MaterialItemProperties extends Item.Properties {
    private String symbol;
    private int color;

    private Double boilingPoint;
    private Double meltingPoint;

    private Integer defaultQuantity;
    private Integer defaultQuality;
    private Double defaultPurity;
    private Integer maxQuantity;
    private Integer maxQuality;
    private Double maxPurity;


    public MaterialItemProperties symbol(String value) {
        this.symbol = value;
        return this;
    }

    public MaterialItemProperties color(int color) {
        this.color = color;
        return this;
    }

    public MaterialItemProperties boilingPoint(Double value) {
        this.boilingPoint = value;
        return this;
    }

    public MaterialItemProperties meltingPoint(Double value) {
        this.meltingPoint = value;
        return this;
    }

    public MaterialItemProperties defaultQuantity(Integer value) {
        this.defaultQuantity = value;
        return this;
    }

    public MaterialItemProperties defaultQuality(Integer value) {
        this.defaultQuality = value;
        return this;
    }

    public MaterialItemProperties defaultPurity(Double value) {
        this.defaultPurity = value == null ? null : Math.min(value, 100);
        return this;
    }

    public MaterialItemProperties maxQuantity(Integer value) {
        this.maxQuantity = value;
        return this;
    }

    public MaterialItemProperties maxQuality(Integer value) {
        this.maxQuality = value;
        return this;
    }

    public MaterialItemProperties maxPurity(Double value) {
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

    // --- Item.Properties overrides ---

    @Override
    public @NotNull MaterialItemProperties food(@NotNull FoodProperties value) {
        super.food(value);
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties stacksTo(int value) {
        super.stacksTo(value);
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties durability(int value) {
        super.durability(value);
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties craftRemainder(@NotNull Item item) {
        super.craftRemainder(item);
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties rarity(@NotNull Rarity value) {
        super.rarity(value);
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties fireResistant() {
        super.fireResistant();
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties setNoRepair() {
        super.setNoRepair();
        return this;
    }

    @Override
    public @NotNull MaterialItemProperties requiredFeatures(FeatureFlag @NotNull ... flags) {
        super.requiredFeatures(flags);
        return this;
    }

}