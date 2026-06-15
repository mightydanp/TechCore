package com.mightydanp.techcore.api.resources.data.biomemodifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BiomeModifierContent {
    private final String modid;
    private final String name;
    private JsonObject json = new JsonObject();

    public BiomeModifierContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public BiomeModifierContent(@NotNull ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    private static JsonArray stringArray(List<String> values) {
        JsonArray array = new JsonArray();

        // Convert each string into a JsonPrimitive entry in order.
        values.forEach(array::add);
        return array;
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public BiomeModifierContent setJson(JsonObject json) {
        this.json = json;
        return this;
    }

    public BiomeModifierContent addFeatures(String biomes, List<String> features, String step) {
        JsonObject json = new JsonObject();

        // Match Forge's add_features biome modifier codec.
        json.addProperty("type", "forge:add_features");
        json.addProperty("biomes", biomes);
        json.add("features", stringArray(features));
        json.addProperty("step", step);

        return setJson(json);
    }

    public BiomeModifierContent removeFeatures(String biomes, List<String> features, List<String> steps) {
        JsonObject json = new JsonObject();

        // Match Forge's remove_features biome modifier codec.
        json.addProperty("type", "forge:remove_features");
        json.addProperty("biomes", biomes);
        json.add("features", stringArray(features));
        json.add("steps", stringArray(steps));

        return setJson(json);
    }

    public JsonObject json() {
        return json;
    }
}
