package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum OreTypeStates implements StringRepresentable {
    SPARE(new OreTypeState("sparse")),
    NORMAL(new OreTypeState("normal")),
    DENSE(new OreTypeState("dense"));

    private final OreTypeState oreTypeState;

    OreTypeStates(OreTypeState oreTypeState) {
        this.oreTypeState = oreTypeState;
    }

    public String getName() {
        return oreTypeState.name();
    }

    @Override
    public String toString() {
        return oreTypeState.name();
    }

    public OreTypeState oreTypeState() {
        return oreTypeState;
    }

    @Override
    public @NotNull String getSerializedName() {
        return toString();
    }

    public record OreTypeState(String name) {
        public static final Codec<OreTypeState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(OreTypeState::name)
                ).apply(instance, OreTypeState::new)
        );
    }
}
