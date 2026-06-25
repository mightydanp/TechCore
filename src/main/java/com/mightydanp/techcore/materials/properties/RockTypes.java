package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum RockTypes {
    SEDIMENTARY(new RockType("clastic_sedimentary")),
    IGNEOUS(new RockType("plutonic_igneous")),
    METAMORPHIC(new RockType("foliated_metamorphic")),
    GENERIC(new RockType("generic"));

    private final RockType type;

    RockTypes(RockType type) {
        this.type = type;
    }

    public RockType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType().name();
    }

    public record RockType(String name) {
        public static final Codec<RockType> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(RockType::name)
                ).apply(instance, RockType::new)
        );
    }

}
