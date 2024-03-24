package com.mightydanp.techcore.api.traits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.data.DataGen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Trait<A extends Trait<A>> extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final ResourceKey<? extends Registry<?>> registryKey;
    private final Map<ResourceLocation, A> traits = new HashMap<>();

    public Trait(ResourceKey<? extends Registry<?>> resourceKey) {
        super(GSON, getDir(resourceKey));
        this.registryKey = resourceKey;
        DataGen.listeners.add(this);
    }

    public Codec<A> codec(){
        return null;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        resourceLocationJsonElementMap.forEach((resourceLocation, jsonElement) -> traits.put(resourceLocation, Util.getOrThrow(codec().decode(JsonOps.INSTANCE, jsonElement), IllegalStateException::new).getFirst()));
    }

    public A getTrait(ResourceLocation resourceLocation) {
        return traits.get(resourceLocation);
    }

    public ResourceKey<? extends Registry<?>> registryKey() {
        return registryKey;
    }

    public JsonObject json(Codec<A> codec, A builder){
        return Util.getOrThrow(codec.encodeStart(JsonOps.INSTANCE, builder), IllegalStateException::new).getAsJsonObject();
    }

    public A trait(Codec<A> codec, JsonObject jsonObject){
        return Util.getOrThrow(codec.decode(JsonOps.INSTANCE, jsonObject), IllegalStateException::new).getFirst();
    }

    public static String getDir(ResourceKey<? extends Registry<?>> resourceKey) {
        return "traits/" + CommonHooks.prefixNamespace(resourceKey.location());
    }


}
