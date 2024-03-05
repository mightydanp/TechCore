package com.mightydanp.techcore.api.resources.assets;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AssetHolder implements PackResources {

    public Map<ResourceLocation, IoSupplier<InputStream>> ASSET_HOLDER = new HashMap<>();

    private static final int PACK_VERSION = 8;

    public void addToResources(ResourceLocation location, JsonObject jsonObject){
        if (!ASSET_HOLDER.containsKey(location)) {
            InputStream inputStream = new ByteArrayInputStream(jsonObject.toString().getBytes());
            ASSET_HOLDER.put(location, ()-> inputStream);
        }else{
            TechCore.LOGGER.warn("[" + location + "] already exists in assets.");
        }
    }

    public void removeToResources(ResourceLocation location){
        if (ASSET_HOLDER.containsKey(location)) {
            ASSET_HOLDER.remove(location);
        }else{
            TechCore.LOGGER.warn("[" + location + "] does not already exists in assets.");
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String @NotNull ... locations) {
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location){
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
        if (packType == PackType.CLIENT_RESOURCES) {
            if (ASSET_HOLDER.containsKey(location)) {
                IoSupplier<java.io.InputStream> resource = ASSET_HOLDER.get(location);
                try {
                    resource.get();
                    return resource;
                } catch (IOException ignored){}
            }
        }

        throw new RuntimeException(new Throwable("Could not find resource in generated resources: " + location));
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput resourceOutput) {
        if (packType == PackType.CLIENT_RESOURCES){
            Map<String, IoSupplier<InputStream>> filteredMap = new HashMap<>();

            ASSET_HOLDER.forEach((resource, stream) -> {
                if(resource.getNamespace().equals(namespace)){
                    filteredMap.put(resource.getPath(), stream);
                }
            });

            if(filteredMap.isEmpty()){
                TechCore.LOGGER.error("Invalid path + " + path);
            }
        }
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType packType) {
        Set<String> namespaces = new HashSet<>();
        if (packType == PackType.CLIENT_RESOURCES) {
            for (ResourceLocation resource : ASSET_HOLDER.keySet()) {
                namespaces.add(resource.getNamespace());
            }
        }
        return namespaces;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        if(serializer.getMetadataSectionName().equals("pack")) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", PACK_VERSION);
            object.addProperty("description", "dynamically generated assets");
            return serializer.fromJson(object);
        }

        return serializer.fromJson(new JsonObject());
    }

    @Override
    public @NotNull String packId() {
        return CoreRef.MOD_ID + ":assets";
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isBuiltin() {
        return true;
    }
}