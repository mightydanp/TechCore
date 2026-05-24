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