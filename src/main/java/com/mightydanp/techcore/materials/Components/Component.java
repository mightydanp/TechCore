package com.mightydanp.techcore.materials.Components;

import com.mightydanp.techcore.materials.Material;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Consumer;

public class Component extends AbstractComponent<Component> {
    public Component(String prefix, String suffix, Consumer<Material> onServerApply, Consumer<Material> onClientApply) {
        super(prefix, suffix, onServerApply, onClientApply);
    }

    protected static final Codec<Component> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("prefix").forGetter(Component::prefix),
                    Codec.STRING.fieldOf("suffix").forGetter(Component::suffix)
            ).apply(instance, (prefix, suffix) -> new Component(prefix, suffix, m -> {}, m -> {}))
    );

    protected static final StreamCodec<FriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Component::prefix,
            ByteBufCodecs.STRING_UTF8, Component::suffix,
            (prefix, suffix) -> new Component(prefix, suffix, m -> {}, m -> {})
    );
}
