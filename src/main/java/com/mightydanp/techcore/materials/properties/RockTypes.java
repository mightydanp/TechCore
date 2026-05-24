package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum RockTypes {
    PLUTONIC_IGNEOUS(new RockType("plutonic_igneous")),
    VOLCANIC_IGNEOUS(new RockType("volcanic_igneous")),
    CLASTIC_SEDIMENTARY(new RockType("clastic_sedimentary")),
    CHEMICAL_SEDIMENTARY(new RockType("chemical_sedimentary")),
    ORGANIC_SEDIMENTARY(new RockType("organic_sedimentary")),
    FOLIATED_METAMORPHIC(new RockType("foliated_metamorphic")),
    NON_FOLIATED_METAMORPHIC(new RockType("non_foliated_metamorphic")),
    GENERIC(new RockType("generic"));

    private final RockType type;

    RockTypes(RockType type){
        this.type = type;
    }

    public RockType getType(){
        return type;
    }

    @Override
    public String toString() {
        return getType().name();
    }

    public record RockType(String name){
        public static final Codec<OreTypeStates.OreTypeState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(OreTypeStates.OreTypeState::name)
                ).apply(instance, OreTypeStates.OreTypeState::new)
        );
    }
}
