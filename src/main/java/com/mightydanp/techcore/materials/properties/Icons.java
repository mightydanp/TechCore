package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum Icons {
    DIAMOND(new Icon("diamond")),
    DULL(new Icon("dull")),
    EMERALD(new Icon("emerald")),
    FINE(new Icon("fine")),
    FLINT(new Icon("flint")),
    GEM_HORIZONTAL(new Icon("gem_horizontal")),
    GEM_VERTICAL(new Icon("gem_vertical")),
    LAPIS(new Icon("lapis")),
    LIGNITE(new Icon("lignite")),
    MAGNETIC(new Icon("magnetic")),
    METALLIC(new Icon("metallic")),
    QUARTZ(new Icon("quartz")),
    REDSTONE(new Icon("redstone")),
    ROUGH(new Icon("rough")),
    RUBY(new Icon("ruby")),
    SHINY(new Icon("shiny"));

    public final Icon icon;

    Icons(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return icon.label;
    }

    public record Icon(String label) {
        public static final Codec<Icon> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("label").forGetter(Icon::label)
                ).apply(instance, Icon::new)
        );

        public static final StreamCodec<FriendlyByteBuf, Icon> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                Icon::label,
                Icon::new
        );
    }
}