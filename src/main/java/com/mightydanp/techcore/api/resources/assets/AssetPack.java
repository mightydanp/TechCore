package com.mightydanp.techcore.api.resources.assets;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AssetPack implements PackResources {
    public Map<ResourceLocation, IoSupplier<InputStream>> ASSET_HOLDER = new HashMap<>();

    static boolean firstLoad = false;
    public final int version = 14;
    public final PackType type = PackType.CLIENT_RESOURCES;
    public final PackSource source = PackSource.DEFAULT;

    public String namespace;
    public String name;

    public boolean required;
    public Pack.Position defaultPosition;
    public boolean fixedPosition;

    public AssetPack(String namespace, String name, boolean required, Pack.Position defaultPosition, boolean fixedPosition) {
        this.namespace = namespace;
        this.name = name;
        this.required = required;
        this.defaultPosition = defaultPosition;
        this.fixedPosition = fixedPosition;
    }

    public void addToResources(ResourceLocation location, JsonObject jsonObject) {
        if (!ASSET_HOLDER.containsKey(location)) {
            InputStream inputStream = new ByteArrayInputStream(jsonObject.toString().getBytes());
            ASSET_HOLDER.put(location, () -> inputStream);
            firstLoad = true;
        } else {
            if (!firstLoad)
                TechCore.LOGGER.warn("[{}] already exists in resource assets: ", location);
        }
    }

    public void removeFromResources(ResourceLocation location) {
        if (ASSET_HOLDER.remove(location) == null) {
            TechCore.LOGGER.warn("[{}] does not exist in resource assets", location);
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String @NotNull ... locations) {
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location) {
        /*
        return FileUtil.decomposePath(p_251554_.getPath()).get().map(p_248224_ -> {
            String s = p_251554_.getNamespace();

            for(Path path : this.pathsForType.get(p_250512_)) {
                Path path1 = FileUtil.resolvePath(path.resolve(s), p_248224_);
                if (Files.exists(path1) && PathPackResources.validatePath(path1)) {
                    return IoSupplier.create(path1);
                }
            }

            return null;
        }, p_248230_ -> {
            LOGGER.error("Invalid path {}: {}", p_251554_, p_248230_.message());
            return null;
        });
         */
        if (packType == type) {
            if (ASSET_HOLDER.containsKey(location)) {
                IoSupplier<InputStream> resource = ASSET_HOLDER.get(location);
                try {
                    resource.get();
                    return resource;
                } catch (IOException ignored) {
                }
            }
        }

        return null;
        //throw new RuntimeException(new Throwable("Could not find resource in generated resource assets: " + location));
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput resourceOutput) {
        if (packType == type) {
            Map<String, IoSupplier<InputStream>> filteredMap = new HashMap<>();

            ASSET_HOLDER.forEach((resource, stream) -> {
                if (resource.getNamespace().equals(namespace)) {
                    filteredMap.put(resource.getPath(), stream);
                }
            });

            if (filteredMap.isEmpty()) {
                TechCore.LOGGER.error("Invalid path + {}", path);
            }
        }
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType packType) {
        Set<String> namespaces = new HashSet<>();
        if (packType == type) {
            for (ResourceLocation resource : ASSET_HOLDER.keySet()) {
                namespaces.add(resource.getNamespace());
            }
        }
        return namespaces;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        if ("pack".equals(serializer.getMetadataSectionName())) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", version);
            object.addProperty("description", "dynamically generated resource assets");
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
        ASSET_HOLDER.clear();
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