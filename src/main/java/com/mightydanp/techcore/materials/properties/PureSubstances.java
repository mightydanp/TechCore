package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum PureSubstances {
    COMPOUND(new PureSubstance("compound")),
    ELEMENT(new PureSubstance("element"));

    public final PureSubstance pureSubstance;

    PureSubstances(PureSubstance pureSubstance) {
        this.pureSubstance = pureSubstance;
    }

    public String getName() {
        return pureSubstance.name();
    }

    @Override
    public String toString() {
        return pureSubstance.name();
    }

    public record PureSubstance(String name) {
        public static final Codec<PureSubstance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(PureSubstance::name)
                ).apply(instance, PureSubstance::new)
        );
    }
}