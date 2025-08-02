package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum OreTypes {
    ORE(new OreType("ore")),
    DENSE(new OreType("dense")),
    GEM(new OreType("gem")),
    CRYSTAL(new OreType("crystal"));

    private final OreType oreType;

    OreTypes(OreType oreType) {
        this.oreType = oreType;
    }

    public String getName() {
        return oreType.name();
    }

    @Override
    public String toString() {
        return oreType.name();
    }

    public OreType oreType() {
        return oreType;
    }

    public record OreType(String name) {
        public static final Codec<OreType> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(OreType::name)
                ).apply(instance, OreType::new)
        );

        public static final StreamCodec<FriendlyByteBuf, OreType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                OreType::name,
                OreType::new
        );
    }
}