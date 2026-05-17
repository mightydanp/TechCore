package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public record Quality(int quality) {
    public static final String TAG = "quality";

    public static final Codec<Quality> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf(TAG).forGetter(Quality::quality)
    ).apply(instance, Quality::new));

    public static boolean hasQuality(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static Integer getQuality(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG) ? tag.getInt(TAG) : null;
    }

    public static Integer getQualityOrDefault(ItemStack itemStack, Integer defaultValue) {
        if(hasQuality(itemStack)) return getQuality(itemStack);

        if(defaultValue == null) return null;

        setQuality(itemStack, defaultValue);
        return defaultValue;
    }

    public static void setQuality(ItemStack itemStack, int quality) {
        itemStack.getOrCreateTag().putInt(TAG, quality);
    }

    public static void setQuality(ItemStack itemStack, int quality, Integer maxQuality) {
        itemStack.getOrCreateTag().putInt(TAG, maxQuality != null ? Mth.clamp(quality, 0, maxQuality) : quality);
    }

    public static Quality fromStack(ItemStack itemStack) {
        Integer quality = getQuality(itemStack);

        return quality != null ? new Quality(quality) : null;
    }
}
