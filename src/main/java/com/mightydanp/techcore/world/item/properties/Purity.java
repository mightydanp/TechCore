package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public record Purity(double purity) {
    public static final String TAG = "purity";

    public static final Codec<Purity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf(TAG).forGetter(Purity::purity)
    ).apply(instance, Purity::new));

    public static boolean hasPurity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static Double getPurity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG) ? tag.getDouble(TAG) : null;
    }

    public static Double getPurityOrDefault(ItemStack itemStack, Double defaultQuantity) {
        if(hasPurity(itemStack)) return getPurity(itemStack);

        if(defaultQuantity == null) return null;

        setPurity(itemStack, defaultQuantity);
        return defaultQuantity;
    }

    public static void setPurity(ItemStack itemStack, double purity) {
        itemStack.getOrCreateTag().putDouble(TAG, purity);
    }

    public static void setPurity(ItemStack itemStack, double purity, Double maxPurity) {
        itemStack.getOrCreateTag().putDouble(TAG, maxPurity != null ? Mth.clamp(purity, 0, maxPurity) : purity);
    }

    public static Purity fromStack(ItemStack itemStack) {
        Double purity = getPurity(itemStack);

        return purity != null ? new Purity(purity) : null;
    }
}