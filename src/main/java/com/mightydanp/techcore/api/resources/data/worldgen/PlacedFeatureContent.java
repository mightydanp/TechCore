package com.mightydanp.techcore.api.resources.data.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlacedFeatureContent {
    private final String modid;
    private final String name;
    private final ResourceLocation configuredFeature;
    private final List<PlacementModifier> modifiers = new ArrayList<>();

    public PlacedFeatureContent(String modid, String name, ResourceLocation configuredFeature) {
        this.modid = modid;
        this.name = name;
        this.configuredFeature = configuredFeature;
    }

    public PlacedFeatureContent(@NotNull ResourceLocation name, ResourceLocation configuredFeature) {
        this.modid = name.getNamespace();
        this.name = name.getPath();
        this.configuredFeature = configuredFeature;
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public ResourceLocation configuredFeature() {
        return configuredFeature;
    }

    public PlacedFeatureContent addModifier(PlacementModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public PlacedFeatureContent addModifiers(List<PlacementModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        return this;
    }

    public PlacedFeatureContent count(int count) {
        return addModifier(CountPlacement.of(count));
    }

    public PlacedFeatureContent inSquare() {
        return addModifier(InSquarePlacement.spread());
    }

    public PlacedFeatureContent heightRange(VerticalAnchor min, VerticalAnchor max) {
        return addModifier(HeightRangePlacement.uniform(min, max));
    }

    public PlacedFeatureContent biomeFilter() {
        return addModifier(BiomeFilter.biome());
    }

    public JsonObject json() {
        JsonObject json = new JsonObject();

        json.addProperty("feature", configuredFeature.toString());

        JsonArray placement = new JsonArray();

        for (PlacementModifier modifier : modifiers) {
            placement.add(
                    PlacementModifier.CODEC
                            .encodeStart(JsonOps.INSTANCE, modifier)
                            .getOrThrow(false, error -> {
                                throw new IllegalStateException("Failed to encode placement modifier: " + error);
                            })
            );
        }

        json.add("placement", placement);

        return json;
    }
}
