package com.mightydanp.techcore.api.resources.data.worldgen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class NoiseSettingsContent {
    private final String modid;
    private final String name;
    private JsonObject json = new JsonObject();

    public NoiseSettingsContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public NoiseSettingsContent(@NotNull ResourceLocation resourceLocation) {
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

    public NoiseSettingsContent setJson(JsonObject json) {
        this.json = json.deepCopy();
        return this;
    }

    public JsonObject json() {
        return json.deepCopy();
    }
}
