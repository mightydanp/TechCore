package com.mightydanp.techcore.materials.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum Icons {
    DIAMOND(new Icon("diamond", "shiny")),
    DULL(new Icon("dull")),
    EMERALD(new Icon("emerald", "shiny")),
    FINE(new Icon("fine")),
    FLINT(new Icon("flint", "rough")),
    GEM_HORIZONTAL(new Icon("gem_horizontal", "shiny")),
    GEM_VERTICAL(new Icon("gem_vertical", "shiny")),
    LAPIS(new Icon("lapis", "shiny")),
    LIGNITE(new Icon("lignite", "rough")),
    MAGNETIC(new Icon("magnetic", "metallic")),
    METALLIC(new Icon("metallic", "shiny")),
    QUARTZ(new Icon("quartz", "shiny")),
    REDSTONE(new Icon("redstone", "shiny")),
    ROUGH(new Icon("rough")),
    RUBY(new Icon("ruby", "shiny")),
    SHINY(new Icon("shiny"));

    public static final String DEFAULT_FOLDER = "default";

    private final Icon icon;

    Icons(Icon icon) {
        this.icon = icon;
    }

    public Icon icon() {
        return icon;
    }

    @Override
    public String toString() {
        return icon.label();
    }

    public record Icon(String label, String fallback) {
        public Icon(String label) {
            this(label, null);
        }

        public static final Codec<Icon> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("label").forGetter(Icon::label),
                        Codec.STRING.optionalFieldOf("fallback", "").forGetter(icon ->
                                icon.fallback() == null ? "" : icon.fallback()
                        )
                ).apply(instance, (label, fallback) ->
                        new Icon(label, fallback.isBlank() ? null : fallback)
                )
        );

        public boolean hasFallback() {
            return fallback != null && !fallback.isBlank();
        }
    }
}