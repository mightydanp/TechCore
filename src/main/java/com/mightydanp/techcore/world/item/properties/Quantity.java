package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Quantity(int quantity, int maxQuantity) {
    public static final String TAG = "quantity";
    public static final String MAX_TAG = "max_quantity";
    public static final int DEFAULT_MAX = 144;

    public static final Codec<Quantity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf(TAG).forGetter(Quantity::quantity),
            Codec.INT.optionalFieldOf(MAX_TAG, DEFAULT_MAX).forGetter(Quantity::maxQuantity)
    ).apply(instance, Quantity::new));

    public Quantity {
        maxQuantity = Math.max(0, maxQuantity);
        quantity = Mth.clamp(quantity, 0, maxQuantity);
    }

    public float level() {
        return maxQuantity <= 0 ? 0.0f : (float) quantity / maxQuantity;
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
            return tag != null && tag.contains(TAG);
        }

        public @Nullable Quantity get() {
            CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains(TAG)) return null;

            int quantity = tag.getInt(TAG);
            int maxQuantity = tag.contains(MAX_TAG) ? tag.getInt(MAX_TAG) : DEFAULT_MAX;

            return new Quantity(quantity, maxQuantity);
        }

        public Stack set(int quantity, int maxQuantity) {
            Quantity value = new Quantity(quantity, maxQuantity);
            CompoundTag tag = stack.getOrCreateTag();

            tag.putInt(TAG, value.quantity());
            tag.putInt(MAX_TAG, value.maxQuantity());

            return this;
        }

        public Stack setQuantity(int quantity) {
            Quantity current = get();
            int maxQuantity = current == null ? DEFAULT_MAX : current.maxQuantity();

            return set(quantity, maxQuantity);
        }

        public Stack setMaxQuantity(int maxQuantity) {
            Quantity current = get();
            int quantity = current == null ? maxQuantity : current.quantity();

            return set(quantity, maxQuantity);
        }

        public Stack remove() {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                tag.remove(TAG);
                tag.remove(MAX_TAG);
            }

            return this;
        }
    }
}
