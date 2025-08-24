package com.mightydanp.techcore.api.resources;

import com.google.gson.JsonObject;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ResourcePack implements PackResources {
    // Holders for each side
    private final Map<ResourceLocation, IoSupplier<InputStream>> assets = new HashMap<>();
    private final Map<ResourceLocation, IoSupplier<InputStream>> data   = new HashMap<>();

    // Pack meta
    public final int version = 15; // 1.20.1
    public final PackSource source = PackSource.DEFAULT;

    // Identity
    public final String namespace;
    public final String name;

    // Repository prefs
    public final boolean required;
    public final Pack.Position defaultPosition;
    public final boolean fixedPosition;

    public ResourcePack(String namespace, String name, boolean required, Pack.Position defaultPosition, boolean fixedPosition) {
        this.namespace = namespace;
        this.name = name;
        this.required = required;
        this.defaultPosition = defaultPosition;
        this.fixedPosition = fixedPosition;
    }

    /* ------------ API to add/remove ------------- */

    public void addAsset(ResourceLocation location, JsonObject json) {
        put(assets, location, json);
    }

    public void addData(ResourceLocation location, JsonObject json) {
        put(data, location, json);
    }

    public void removeAsset(ResourceLocation location) { assets.remove(location); }
    public void removeData(ResourceLocation location)  { data.remove(location);  }

    private static void put(Map<ResourceLocation, IoSupplier<InputStream>> map, ResourceLocation loc, JsonObject json) {
        byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
        map.put(loc, () -> new ByteArrayInputStream(bytes));
    }

    /* ------------ PackResources impl ------------- */

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String @NotNull ... locations) {
        for (String l : locations) {
            if ("pack.mcmeta".equals(l)) {
                String meta = "{\"pack\":{\"pack_format\":15,\"description\":\"dynamic generated assets and data\"}}";
                byte[] b = meta.getBytes(StandardCharsets.UTF_8);
                return () -> new ByteArrayInputStream(b);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location) {
        Map<ResourceLocation, IoSupplier<InputStream>> map = (packType == PackType.CLIENT_RESOURCES) ? assets
                : (packType == PackType.SERVER_DATA) ? data : null;
        return map != null ? map.get(location) : null;
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String ns, @NotNull String path, @NotNull ResourceOutput out) {
        Map<ResourceLocation, IoSupplier<InputStream>> map = (packType == PackType.CLIENT_RESOURCES) ? assets
                : (packType == PackType.SERVER_DATA) ? data : null;
        if (map == null) return;

        String norm = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        map.forEach((rl, supplier) -> {
            if (!rl.getNamespace().equals(ns)) return;
            String p = rl.getPath();
            if (p.equals(norm) || p.startsWith(norm + "/")) out.accept(rl, supplier);
        });
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType packType) {
        Map<ResourceLocation, IoSupplier<InputStream>> map = (packType == PackType.CLIENT_RESOURCES) ? assets
                : (packType == PackType.SERVER_DATA) ? data : null;
        if (map == null) return Set.of();

        Set<String> set = new HashSet<>();
        set.add(namespace); // advertise even if empty
        map.keySet().forEach(rl -> set.add(rl.getNamespace()));
        return set;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        if ("pack".equals(serializer.getMetadataSectionName())) {
            JsonObject obj = new JsonObject();
            obj.addProperty("pack_format", version);
            obj.addProperty("description", "dynamically generated assets + data");
            return serializer.fromJson(obj);
        }
        return null;
    }

    @Override
    public @NotNull String packId() {
        return namespace + ":" + name;
    }

    @Override
    public void close() {
        // no-op (keep content across reloads)
    }

    public PackResources open() { return this; }

    /* ------------ Pack registry helpers ------------- */

    /** Create the visual "Assets" pack (CLIENT_RESOURCES) backed by this UnifiedPack. */
    @Nullable
    public Pack createAssetPack() {
        Pack.ResourcesSupplier supplier = (a) -> this;
        return Pack.readMetaAndCreate(
                packId() + "_assets",
                Component.literal(packId() + " assets"),
                required,
                supplier,
                PackType.CLIENT_RESOURCES,
                defaultPosition,
                source
        );
    }

    /** Create the visual "Data" pack (SERVER_DATA) backed by this UnifiedPack. */
    @Nullable
    public Pack createDataPack() {
        Pack.ResourcesSupplier supplier = (a) -> this;
        return Pack.readMetaAndCreate(
                packId() + "_data",
                Component.literal(packId() + " data"),
                required,
                supplier,
                PackType.SERVER_DATA,
                defaultPosition,
                source
        );
    }
}