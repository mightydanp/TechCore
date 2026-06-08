package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public record Impurities(Map<ResourceLocation, Double> impurities) {
    public static final String TAG = "impurities";

    public static final Codec<Impurities> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.DOUBLE).fieldOf(TAG).forGetter(Impurities::impurities)
    ).apply(instance, Impurities::new));

    public Impurities {
        impurities = Map.copyOf(impurities);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Stack stack(ItemStack stack) {
        return new Stack(stack);
    }

    public static final class Stack {
        private final ItemStack stack;

        private Stack(ItemStack stack) {
            this.stack = stack;
        }

        public boolean has() {
            CompoundTag tag = stack.getTag();
            return tag != null && tag.contains(TAG, Tag.TAG_COMPOUND);
        }

        public @Nullable Impurities get() {
            CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains(TAG, Tag.TAG_COMPOUND)) return null;

            CompoundTag impuritiesTag = tag.getCompound(TAG);
            Map<ResourceLocation, Double> impurities = new TreeMap<>();

            for (String key : impuritiesTag.getAllKeys()) {
                ResourceLocation impurity = ResourceLocation.tryParse(key);
                if (impurity != null) {
                    impurities.put(impurity, impuritiesTag.getDouble(key));
                }
            }

            return new Impurities(impurities);
        }

        @Contract("_ -> this")
        public Stack set(@NotNull Impurities impurities) {
            CompoundTag impuritiesTag = new CompoundTag();

            impurities.impurities().forEach((impurity, amount) ->
                    impuritiesTag.putDouble(impurity.toString(), amount));

            stack.getOrCreateTag().put(TAG, impuritiesTag);
            return this;
        }

        public Stack set(Map<ResourceLocation, Double> impurities) {
            return set(new Impurities(impurities));
        }

        public Stack remove() {
            CompoundTag tag = stack.getTag();
            if (tag != null) tag.remove(TAG);
            return this;
        }
    }
}