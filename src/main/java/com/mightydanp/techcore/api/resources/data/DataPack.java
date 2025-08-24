package com.mightydanp.techcore.api.resources.data;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.TechCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;

public class DataPack implements PackResources {
    public Map<ResourceLocation, IoSupplier<InputStream>> HOLDER = new HashMap<>();

    static boolean firstLoad = false;
    public final int version = 15;
    public final PackType type = PackType.SERVER_DATA;
    public final PackSource source = PackSource.DEFAULT;

    public String namespace;
    public String name;

    public boolean required;
    public Pack.Position defaultPosition;
    public boolean fixedPosition;

    public DataPack(String namespace, String name, boolean required, Pack.Position defaultPosition, boolean fixedPosition) {
        this.namespace = namespace;
        this.name = name;
        this.required = required;
        this.defaultPosition = defaultPosition;
        this.fixedPosition = fixedPosition;
    }

    public void addToResources(ResourceLocation location, JsonObject jsonObject) {
        byte[] bytes = jsonObject.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        HOLDER.put(location, () -> new java.io.ByteArrayInputStream(bytes));
        if (!firstLoad) {
            TechCore.LOGGER.warn("[{}] already exists in resource data's: ", location);
            firstLoad = true;
        }
    }

    public void removeFromResources(ResourceLocation location) {
        if (HOLDER.remove(location) == null) {
            TechCore.LOGGER.warn("[{}] does not exist in resource data's", location);
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String @NotNull ... locations) {
        for (String l : locations) {
            if ("pack.mcmeta".equals(l)) {
                String meta = """
            {"pack":{"pack_format":15,"description":"dynamically generated resource data"}}
            """;
                byte[] bytes = meta.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                return () -> new java.io.ByteArrayInputStream(bytes);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location) {
        if (packType != type) return null;
        return HOLDER.get(location);
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput resourceOutput) {
        if (packType != type) return;

        // Only expose files under the requested namespace and path prefix.
        HOLDER.forEach((loc, supplier) -> {
            if (!loc.getNamespace().equals(namespace)) return;
            String p = loc.getPath();
            if (p.startsWith(path + "/")) {
                TechCore.LOGGER.warn(p, supplier); // <-- critical: publish the resource
            }
        });
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType packType) {
        if (packType != type) return java.util.Collections.emptySet();
        java.util.Set<String> set = new java.util.HashSet<>();
        HOLDER.keySet().forEach(loc -> set.add(loc.getNamespace()));
        return set;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        if ("pack".equals(serializer.getMetadataSectionName())) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", version);
            object.addProperty("description", "dynamically generated resource data");
            return serializer.fromJson(object);
        }

        return null;
    }

    @Override
    public @NotNull String packId() {
        return namespace + ":" + name;
    }

    @Override
    public void close() {
        HOLDER.clear();
    }

    public PackResources open() {
        return this;
    }

    @Nullable
    public Pack createPack() {
        Pack.ResourcesSupplier supplier = (a) -> this;
        // Create the pack
        return Pack.readMetaAndCreate(
                packId(),                   // String packId
                Component.literal(packId()),// Display name
                required,                   // boolean, whether enabled
                supplier,                   // ResourcesSupplier
                type,                       // PackType
                defaultPosition,            // Pack.Position
                source                      // PackSource
        );
    }
}