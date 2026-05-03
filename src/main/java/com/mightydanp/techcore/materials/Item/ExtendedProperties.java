package com.mightydanp.techcore.materials.Item;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class ExtendedProperties extends Item.Properties {
    private String symbol;
    private int color;


    private double boilingPoint;
    private double meltingPoint;

    private int maxQuantity = 144;


    public ExtendedProperties symbol(String value) {
        this.symbol = value;
        return this;
    }

    public ExtendedProperties color(int color) {
        this.color = color;
        return this;
    }

    public ExtendedProperties boilingPoint(double value) {
        this.boilingPoint = value;
        return this;
    }

    public ExtendedProperties meltingPoint(double value) {
        this.meltingPoint = value;
        return this;
    }

    public ExtendedProperties maxQuantity(int value) {
        this.maxQuantity = value;
        return this;
    }



    public String getSymbol() {
        return symbol;
    }

    public int getColor() {return color;
    }

    public double getBoilingPoint() {
        return boilingPoint;
    }

    public double getMeltingPoint() {
        return meltingPoint;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    // --- Item.Properties overrides ---

    @Override
    public @NotNull ExtendedProperties food(@NotNull FoodProperties value) {
        super.food(value);
        return this;
    }

    @Override
    public @NotNull ExtendedProperties stacksTo(int value) {
        super.stacksTo(value);
        return this;
    }

    @Override
    public @NotNull ExtendedProperties durability(int value) {
        super.durability(value);
        return this;
    }

    @Override
    public @NotNull ExtendedProperties craftRemainder(@NotNull Item item) {
        super.craftRemainder(item);
        return this;
    }

    @Override
    public @NotNull ExtendedProperties rarity(@NotNull Rarity value) {
        super.rarity(value);
        return this;
    }

    @Override
    public @NotNull ExtendedProperties fireResistant() {
        super.fireResistant();
        return this;
    }

    @Override
    public @NotNull ExtendedProperties setNoRepair() {
        super.setNoRepair();
        return this;
    }

    @Override
    public @NotNull ExtendedProperties requiredFeatures(FeatureFlag @NotNull ... flags) {
        super.requiredFeatures(flags);
        return this;
    }

}