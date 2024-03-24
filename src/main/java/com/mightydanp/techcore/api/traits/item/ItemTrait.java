package com.mightydanp.techcore.api.traits.item;

import com.mightydanp.techcore.api.traits.Trait;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;

public class ItemTrait extends Trait<ItemTrait> {
    private int color = 0;
    private int maxDamage = 0;
    private String textureIcon = "";
    private Double kilograms = 0D;
    private Double meters = 0D;

    public ItemTrait() {
        super(Registries.ITEM);
    }

    public ItemTrait(int color, int maxDamage, String textureIcon, Double kilograms, Double meters) {
        super(Registries.ITEM);
        this.color = color;
        this.maxDamage = maxDamage;
        this.textureIcon = textureIcon;
        this.kilograms = kilograms;
        this.meters = meters;
    }

    @Override
    public Codec<ItemTrait> codec() {
        return RecordCodecBuilder.create((instance) -> instance.group(
                Codec.INT.optionalFieldOf("color", color).forGetter(ItemTrait::color),
                Codec.INT.optionalFieldOf("max_damage", maxDamage).forGetter(ItemTrait::maxDamage),
                Codec.STRING.optionalFieldOf("texture_icon", textureIcon).forGetter(ItemTrait::textureIcon),
                Codec.DOUBLE.optionalFieldOf("kilograms", kilograms).forGetter(ItemTrait::kilograms),
                Codec.DOUBLE.optionalFieldOf("meters", meters).forGetter(ItemTrait::meters)
        ).apply(instance, ItemTrait::new));
    }

    public static ItemTrait getInstance(){
        return new ItemTrait();
    }

    public ItemTrait color(int color) {
        this.color= color;
        return this;
    }
    public int color() {
        return color;
    }

    public ItemTrait maxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public int maxDamage() {
        return maxDamage;
    }

    public ItemTrait textureIcon(String textureIcon) {
        this.textureIcon = textureIcon;
        return this;
    }

    public String textureIcon() {
        return textureIcon;
    }

    public ItemTrait kilograms(Double kilograms) {
        this.kilograms = kilograms;
        return this;
    }

    public Double kilograms() {
        return kilograms;
    }

    public ItemTrait meters(Double meters) {
        this.meters = meters;
        return this;
    }

    public Double meters() {
        return meters;
    }
}
