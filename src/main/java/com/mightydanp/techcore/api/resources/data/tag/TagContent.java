package com.mightydanp.techcore.api.resources.data.tag;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TagContent<A> {
    private final String modid;
    private final String name;
    private final TagBuilder builder = new TagBuilder();
    private final TagKey<A> tagKey;
    private final Set<ResourceLocation> values = new HashSet<>();
    private final @Nullable Registry<A> registry;
    private final ResourceKey<? extends Registry<A>> resourceKey;

    public TagContent(ResourceLocation resourceLocation, ResourceKey<? extends Registry<A>> ResourceKey, @Nullable Registry<A> registry) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
        tagKey = TagKey.create(ResourceKey, resourceLocation);
        this.registry = registry;
        this.resourceKey = ResourceKey;
    }

    public TagContent(String modid, String name, ResourceKey<? extends Registry<A>> ResourceKey, @Nullable Registry<A> registry) {
        this.modid = modid;
        this.name = name;
        tagKey = TagKey.create(ResourceKey, ResourceLocation.fromNamespaceAndPath(modid, name));
        this.registry = registry;
        this.resourceKey = ResourceKey;
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public TagContent<A> replace(boolean replace) {
        builder.replace(replace);
        return this;
    }

    public TagContent<A> add(A object) {
        if (registry != null) {
            builder.addElement(Objects.requireNonNull(registry.getKey(object)));
        }

        return this;
    }

    @SafeVarargs
    public final TagContent<A> addAll(A... objects) {
        if (registry != null) {
            Arrays.stream(objects).forEach(object -> builder.addElement(Objects.requireNonNull(registry.getKey(object))));
        }

        return this;
    }

    public TagContent<A> remove(A object) {
        if (registry != null) {
            //re-look builder.removeElement
            builder.removeElement(registry.getKey(object));
        }

        return this;
    }

    @SafeVarargs
    public final TagContent<A> removeAll(A... objects) {
        if (registry != null) {
            //re-look builder.removeElement
            Arrays.stream(objects).forEach(object -> builder.removeElement(Objects.requireNonNull(registry.getKey(object))));
        }

        return this;
    }

    public TagKey<A> getTagKey() {
        return tagKey;
    }

    public TagContent<A> addExisting() {
        if (registry != null) {
            registry.getTag(tagKey).ifPresent(holders -> holders.forEach(aHolder -> add(aHolder.value())));
        }
        return this;
    }

    public JsonObject json() {
        return TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(builder.build(), builder.isReplace(), builder.getRemoveEntries().toList())).getOrThrow(IllegalStateException::new).getAsJsonObject();
    }
}