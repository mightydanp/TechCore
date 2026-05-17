package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public record Quantity(int quantity) {
    public static final String TAG = "quantity";

    public static final Codec<Quantity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf(TAG).forGetter(Quantity::quantity)
    ).apply(instance, Quantity::new));

    public static boolean hasQuantity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static Integer getQuantity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG) ? tag.getInt(TAG) : null;
    }

    public static Integer getQuantityOrDefault(ItemStack itemStack, Integer defaultQuantity) {
        if(hasQuantity(itemStack)) return getQuantity(itemStack);

        if(defaultQuantity == null) return null;

        setQuantity(itemStack, defaultQuantity);
        return defaultQuantity;
    }

    public static void setQuantity(ItemStack itemStack, int quantity) {
        itemStack.getOrCreateTag().putInt(TAG, quantity);
    }

    public static void setQuantity(ItemStack itemStack, int quantity, Integer maxQuantity) {
        itemStack.getOrCreateTag().putInt(TAG, maxQuantity != null ? Mth.clamp(quantity, 0, maxQuantity) : quantity);
    }

    public static Quantity fromStack(ItemStack itemStack, Integer maxQuantity) {
        Integer quantity = getQuantity(itemStack);

        return quantity != null ? new Quantity(quantity) : null;
    }
}
