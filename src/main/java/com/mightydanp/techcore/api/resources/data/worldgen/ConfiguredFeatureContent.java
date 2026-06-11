package com.mightydanp.techcore.api.resources.data.worldgen;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfiguredFeatureContent {
    private final String modid;
    private final String name;

    private @Nullable ConfiguredFeature<?, ?> configuredFeature;

    public ConfiguredFeatureContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public ConfiguredFeatureContent(@NotNull ResourceLocation resourceLocation) {
        this(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public ResourceLocation id() {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    public ConfiguredFeatureContent feature(ConfiguredFeature<?, ?> configuredFeature) {
        this.configuredFeature = configuredFeature;
        return this;
    }

    public <FC extends FeatureConfiguration> ConfiguredFeatureContent feature(Feature<FC> feature, FC configuration) {
        return feature(new ConfiguredFeature<>(feature, configuration));
    }

    public JsonObject json() {
        if (configuredFeature == null) {
            throw new IllegalStateException(
                    "Configured feature has not been set: " + modid + ":" + name
            );
        }

        return ConfiguredFeature.DIRECT_CODEC
                .encodeStart(JsonOps.INSTANCE, configuredFeature)
                .getOrThrow(false, error -> {
                    throw new IllegalStateException(
                            "Failed to encode configured feature '" + id() + "': " + error
                    );
                })
                .getAsJsonObject();
    }
}
