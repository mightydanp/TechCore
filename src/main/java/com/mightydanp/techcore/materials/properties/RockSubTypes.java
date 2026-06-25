package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum RockSubTypes {
    CLASTIC(new RockSubType("clastic")),
    CHEMICAL(new RockSubType("chemical")),
    ORGANIC(new RockSubType("organic")),
    INTRUSIVE(new RockSubType("intrusive")),
    EXTRUSIVE(new RockSubType("extrusive")),
    FOLIATED(new RockSubType("foliated")),
    NON_FOLIATED(new RockSubType("non_foliated")),
    GENERIC(new RockSubType("generic"));

    private final RockSubType subType;

    RockSubTypes(RockSubType rockSubType) {
        this.subType = rockSubType;
    }

    public RockSubType getSubType() {
        return subType;
    }

    @Override
    public String toString() {
        return getSubType().name();
    }

    public record RockSubType(String name) {
        public static final Codec<RockSubType> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(RockSubType::name)
                ).apply(instance, RockSubType::new)
        );
    }
}
