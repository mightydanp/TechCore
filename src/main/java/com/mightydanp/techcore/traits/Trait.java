package com.mightydanp.techcore.traits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Trait<A extends Trait<A>> extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final ResourceKey<? extends Registry<?>> registryKey;
    private final Map<ResourceLocation, A> traits = new ConcurrentHashMap<>();

    public Trait(ResourceKey<? extends Registry<?>> resourceKey) {
        super(GSON, getDir(resourceKey));
        this.registryKey = resourceKey;
    }

    public Codec<A> codec() {
        return null;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        Codec<A> codec = codec();
        if (codec == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " must override codec() before traits can be loaded.");
        }
        resourceLocationJsonElementMap.forEach((resourceLocation, jsonElement) -> traits.put(resourceLocation, codec.decode(JsonOps.INSTANCE, jsonElement)
                .getOrThrow(false, msg -> { throw new IllegalStateException("Failed to decode: " + msg); })
                .getFirst()));
    }

    public A getTrait(ResourceLocation resourceLocation) {
        return traits.get(resourceLocation);
    }

    public ResourceKey<? extends Registry<?>> registryKey() {
        return registryKey;
    }

    public JsonObject json(Codec<A> codec, A builder) {
        return codec.encodeStart(JsonOps.INSTANCE, builder)
                .getOrThrow(false, msg -> { throw new IllegalStateException("Failed to encode: " + msg); })
                .getAsJsonObject();
    }

    public A trait(Codec<A> codec, JsonObject jsonObject) {
        return codec.decode(JsonOps.INSTANCE, jsonObject)
                .getOrThrow(false, msg -> { throw new IllegalStateException("Failed to decode: " + msg); })
                .getFirst();
    }

    public static String prefixNamespace(ResourceLocation registryKey) {
        return registryKey.getNamespace().equals("minecraft") ? registryKey.getPath() : registryKey.getNamespace() + "/" + registryKey.getPath();
    }

    public static String getDir(ResourceKey<? extends Registry<?>> resourceKey) {
        return "traits/" + prefixNamespace(resourceKey.location());
    }

}
