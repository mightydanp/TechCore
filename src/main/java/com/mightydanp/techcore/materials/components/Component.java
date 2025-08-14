package com.mightydanp.techcore.materials.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Component extends AbstractComponent<Component> {
    public Component(String prefix, String suffix) {
        super(prefix, suffix);
    }

    private static final Codec<Component> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("prefix").forGetter(Component::prefix),
                    Codec.STRING.fieldOf("suffix").forGetter(Component::suffix)
            ).apply(instance, Component::new)
    );
}
