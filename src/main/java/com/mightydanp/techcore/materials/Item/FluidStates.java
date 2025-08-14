package com.mightydanp.techcore.materials.Item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    }
}