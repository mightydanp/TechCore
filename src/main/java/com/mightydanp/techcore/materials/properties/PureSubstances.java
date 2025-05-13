package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

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

        public static final StreamCodec<FriendlyByteBuf, PureSubstance> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                PureSubstance::name,
                PureSubstance::new
        );
    }
}