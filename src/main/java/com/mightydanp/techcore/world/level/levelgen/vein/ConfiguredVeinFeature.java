package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record ConfiguredVeinFeature(ResourceLocation featureId, Integer loadOrder, Object configuration) {
    public ConfiguredVeinFeature(ResourceLocation featureId, Object configuration) {
        this(featureId, null, configuration);
    }

    public ConfiguredVeinFeature {
        Objects.requireNonNull(featureId, "featureId");
        Objects.requireNonNull(configuration, "configuration");
        if (loadOrder != null && loadOrder < 0) throw new IllegalArgumentException("loadOrder cannot be negative: " + loadOrder);
    }

    public <T> T configuration(Class<T> type) {
        Objects.requireNonNull(type, "type");
        return type.cast(configuration);
    }
}
