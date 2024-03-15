package com.mightydanp.techcore.api.resources.assets;

import com.mightydanp.techcore.api.resources.assets.contents.blockstate.BlockStateContent;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.ItemModelContent;
import com.mightydanp.techcore.api.resources.data.tag.TagContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class AssetPackRegistries {
    public static Map<ResourceLocation, BlockStateContent<?>> blockState = new HashMap<>();
    public static Map<ResourceLocation, BlockModelContent<?>> blockModel = new HashMap<>();
    public static Map<ResourceLocation, ItemModelContent<?>> itemModel = new HashMap<>();
    public static Map<ResourceLocation, LanguageContent> language = new HashMap<>();

    public static void init() {
        blockState.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "blockstates/" + r.getPath() + ".json"), b.json()));
        blockModel.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(),    "models/" + b.getModelType() + "/" + (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/")  + r.getPath() + ".json"), b.json()));
        itemModel.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "models/" + b.getModelType() + "/" + (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/")  + (r.getPath().contains(":") ? r.getPath().split(":")[1] : r.getPath()) + ".json"), b.json()));
        language.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "lang/" + r + ".json"), b.json()));
    }

    public static boolean saveBlockState(BlockStateContent<?> content, boolean override) {
        ResourceLocation resourceLocation = new ResourceLocation(content.modid(), content.name());

        if(!override && AssetPackRegistries.blockState.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.blockState.put(resourceLocation, content);

        return true;
    }

    public static BlockStateContent<?> getBlockState(ResourceLocation name) {
        return blockState.getOrDefault(name, new BlockStateContent<>(name));
    }

    public static BlockStateContent<?> getBlockState(String modid, String name) {
        return blockState.getOrDefault(new ResourceLocation(modid, name), new BlockStateContent<>(modid, name));
    }

    public static boolean saveBlockModel(BlockModelContent<?> content, boolean override) {
        ResourceLocation resourceLocation = new ResourceLocation(content.modid(), content.name());

        if(!override && AssetPackRegistries.blockModel.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.blockModel.put(resourceLocation, content);
        return true;
    }

    public static BlockModelContent<?> getBlockModel(ResourceLocation name) {
        return blockModel.getOrDefault(name, new BlockModelContent<>(name, ""));
    }

    public static BlockModelContent<?> getBlockModel(String modid, String name) {
        return blockModel.getOrDefault(new ResourceLocation(modid, name), new BlockModelContent<>(modid, name, ""));
    }

    public static boolean saveItemModel(ItemModelContent<?> content, boolean override) {
        ResourceLocation resourceLocation = new ResourceLocation(content.modid(), content.name());

        if(!override && AssetPackRegistries.itemModel.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.itemModel.put(resourceLocation, content);
        return true;
    }

    public static ItemModelContent<?> getItemModel(ResourceLocation name) {
        return itemModel.getOrDefault(name, new ItemModelContent<>(name, ""));
    }

    public static ItemModelContent<?> getItemModel(String modid, String name) {
        return itemModel.getOrDefault(new ResourceLocation(modid, name), new ItemModelContent<>(modid, name, ""));
    }

    public static boolean saveLanguage(LanguageContent content, boolean override) {
        ResourceLocation resourceLocation = new ResourceLocation(content.modid(), content.name());

        if(!override && AssetPackRegistries.language.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.language.put(resourceLocation, content);
        return true;
    }

    public static LanguageContent getLanguage(ResourceLocation name) {
        return language.getOrDefault(name, new LanguageContent(name));
    }

    public static LanguageContent getLanguage(String modid, String name) {
        return language.getOrDefault(new ResourceLocation(modid, name), new LanguageContent(modid, name));
    }
}