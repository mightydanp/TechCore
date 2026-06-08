package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Quality(int quality) {
    public static final String TAG = "quality";
    public static final int MIN = 0;
    public static final int DEFAULT = 60;
    public static final int MAX = 100;

    public static final Codec<Quality> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf(TAG).forGetter(Quality::quality)
    ).apply(instance, Quality::new));

    public Quality {
        quality = Mth.clamp(quality, MIN, MAX);
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

        public @Nullable Quality get() {
            CompoundTag tag = stack.getTag();
            return tag != null && tag.contains(TAG) ? new Quality(tag.getInt(TAG)) : null;
        }

        public Quality getOrDefault(Quality fallback) {
            Quality value = get();
            return value == null ? fallback : value;
        }

        @Contract("_ -> this")
        public Stack set(@NotNull Quality quality) {
            stack.getOrCreateTag().putInt(TAG, quality.quality());
            return this;
        }

        public Stack set(int quality) {
            return set(new Quality(quality));
        }

        public Stack remove() {
            CompoundTag tag = stack.getTag();
            if (tag != null) tag.remove(TAG);
            return this;
        }
    }
}

