package com.mightydanp.techcore.api.resources.data.tag;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public class TagContent<A> {
    private final String modid;
    private final String name;
    private final TagBuilder builder = new TagBuilder();
    private final TagKey<A> tagKey;
    private final ResourceKey<? extends Registry<A>> resourceKey;

    private final @Nullable Registry<A> registry;
    private final @Nullable IForgeRegistry<A> forgeRegistry;

    public TagContent(@NotNull ResourceLocation resourceLocation, ResourceKey<? extends Registry<A>> resourceKey) {
        this(resourceLocation, resourceKey, null, null);
    }

    public TagContent(String modid, String name, ResourceKey<? extends Registry<A>> resourceKey) {
        this(ResourceLocation.fromNamespaceAndPath(modid, name), resourceKey);
    }

    public TagContent(@NotNull ResourceLocation resourceLocation, ResourceKey<? extends Registry<A>> resourceKey, @Nullable Registry<A> registry) {
        this(resourceLocation, resourceKey, registry, null);
    }

    public TagContent(String modid, String name, ResourceKey<? extends Registry<A>> resourceKey, @Nullable Registry<A> registry) {
        this(ResourceLocation.fromNamespaceAndPath(modid, name), resourceKey, registry);
    }

    public TagContent(@NotNull ResourceLocation resourceLocation, ResourceKey<? extends Registry<A>> resourceKey, @Nullable IForgeRegistry<A> forgeRegistry) {
        this(resourceLocation, resourceKey, null, forgeRegistry);
    }

    public TagContent(String modid, String name, ResourceKey<? extends Registry<A>> resourceKey, @Nullable IForgeRegistry<A> forgeRegistry) {
        this(ResourceLocation.fromNamespaceAndPath(modid, name), resourceKey, forgeRegistry);
    }

    private TagContent(@NotNull ResourceLocation resourceLocation, ResourceKey<? extends Registry<A>> resourceKey, @Nullable Registry<A> registry, @Nullable IForgeRegistry<A> forgeRegistry) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
        this.tagKey = TagKey.create(resourceKey, resourceLocation);
        this.resourceKey = resourceKey;
        this.registry = registry;
        this.forgeRegistry = forgeRegistry;
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

    public TagKey<A> getTagKey() {
        return tagKey;
    }

    public ResourceKey<? extends Registry<A>> getResourceKey() {
        return resourceKey;
    }

    public TagContent<A> replace(boolean replace) {
        builder.replace(replace);
        return this;
    }

    private @Nullable ResourceLocation key(A object) {
        if (forgeRegistry != null) {
            return forgeRegistry.getKey(object);
        }

        return registry == null ? null : registry.getKey(object);
    }

    private ResourceLocation requireKey(A object, String action) {
        ResourceLocation location = key(object);

        if (location == null) {
            throw new IllegalStateException(
                    "Cannot " + action + " object in tag '" + tagKey.location() + "' because no registry was supplied. " +
                            "Use " + action + "(ResourceLocation) or " + action + "(String, String) instead."
            );
        }

        return location;
    }

    public TagContent<A> add(A object) {
        return add(requireKey(object, "add"));
    }

    public TagContent<A> add(ResourceLocation resourceLocation) {
        builder.addElement(resourceLocation);
        return this;
    }

    public TagContent<A> add(String modid, String name) {
        return add(ResourceLocation.fromNamespaceAndPath(modid, name));
    }

    public TagContent<A> addTag(ResourceLocation resourceLocation) {
        builder.addTag(resourceLocation);
        return this;
    }

    public TagContent<A> addTag(String modid, String name) {
        return addTag(ResourceLocation.fromNamespaceAndPath(modid, name));
    }

    @SafeVarargs
    public final TagContent<A> addAll(A... objects) {
        Arrays.stream(objects).forEach(this::add);
        return this;
    }

    public TagContent<A> remove(A object, String modid) {
        return remove(requireKey(object, "remove"), modid);
    }

    public TagContent<A> remove(ResourceLocation resourceLocation, String modid) {
        builder.removeElement(resourceLocation, modid);
        return this;
    }

    public TagContent<A> remove(String targetModid, String name, String removingModid) {
        return remove(ResourceLocation.fromNamespaceAndPath(targetModid, name), removingModid);
    }

    public final TagContent<A> removeAll(Map<A, String> list) {
        list.forEach(this::remove);
        return this;
    }

    public final TagContent<A> removeAllById(Map<ResourceLocation, String> list) {
        list.forEach(this::remove);
        return this;
    }

    public TagContent<A> addExisting() {
        if (registry != null) {
            registry.getTag(tagKey).ifPresent(holders ->
                    holders.forEach(holder -> add(holder.value()))
            );
        } else if (forgeRegistry != null) {
            ITagManager<A> tags = forgeRegistry.tags();

            if (tags != null) {
                tags.getTag(tagKey).stream().forEach(this::add);
            }
        }

        return this;
    }

    public JsonObject json() {
        return TagFile.CODEC
                .encodeStart(
                        JsonOps.INSTANCE,
                        new TagFile(
                                builder.build(),
                                builder.isReplace(),
                                builder.getRemoveEntries().toList()
                        )
                )
                .getOrThrow(false, error -> {
                    throw new IllegalStateException(
                            "Failed to encode TagFile '" + tagKey.location() + "': " + error
                    );
                })
                .getAsJsonObject();
    }
}