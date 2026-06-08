package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Purity(double purity) {
    public static final String TAG = "purity";
    public static final double MIN = 0.0;
    public static final double DEFAULT = 75;
    public static final double MAX = 100.0;

    public static final Codec<Purity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf(TAG).forGetter(Purity::purity)
    ).apply(instance, Purity::new));

    public Purity {
        purity = Mth.clamp(purity, MIN, MAX);
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

        public @Nullable Purity get() {
            CompoundTag tag = stack.getTag();
            return tag != null && tag.contains(TAG) ? new Purity(tag.getDouble(TAG)) : null;
        }

        public Purity getOrDefault(Purity fallback) {
            Purity value = get();
            return value == null ? fallback : value;
        }

        @Contract("_ -> this")
        public Stack set(@NotNull Purity purity) {
            stack.getOrCreateTag().putDouble(TAG, purity.purity());
            return this;
        }

        public Stack set(double purity) {
            return set(new Purity(purity));
        }

        public Stack remove() {
            CompoundTag tag = stack.getTag();
            if (tag != null) tag.remove(TAG);
            return this;
        }
    }
}
