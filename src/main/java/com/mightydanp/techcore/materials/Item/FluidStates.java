package com.mightydanp.techcore.materials.Item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum FluidStates {
    FLUID(new FluidState("fluid")),
    GAS(new FluidState("gas"));

    public final FluidState fluidState;

    FluidStates(FluidState fluidState) {
        this.fluidState = fluidState;
    }

    @Override
    public String toString() {
        return fluidState.label;
    }

    public record FluidState(String label) {
        // Regular Codec for FluidState
        public static final Codec<FluidState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("label").forGetter(FluidState::toString)
                ).apply(instance, FluidState::new)
        );

        // StreamCodec for FluidState
        public static final StreamCodec<FriendlyByteBuf, FluidState> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,  // Serializes the String value of FluidState
                FluidState::toString,  // To retrieve the label
                FluidState::new  // Convert string to FluidState
        );
    }
}