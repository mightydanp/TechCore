package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    }
}