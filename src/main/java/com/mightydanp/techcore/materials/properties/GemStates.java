package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum GemStates {
    CHIPPED  (new GemStates.GemState("chipped")),
    FLAWED   (new GemStates.GemState("flawed")),
    GEM      (new GemStates.GemState("gem")),
    FLAWLESS (new GemStates.GemState("flawless")),
    LEGENDARY(new GemStates.GemState("legendary"));

    public final GemStates.GemState gemState;

    GemStates(GemStates.GemState fluidState) {
        this.gemState = fluidState;
    }

    @Override
    public String toString() {
        return gemState.label;
    }

    public record GemState(String label) {
        // Regular Codec for FluidState
        public static final Codec<GemStates.GemState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("label").forGetter(GemStates.GemState::label)
                ).apply(instance, GemStates.GemState::new)
        );
    }
}
