package com.mightydanp.techcore.api.resources.assets.contents;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistry;
import com.mightydanp.techcore.api.resources.assets.contents.blockstate.BlockStateContent;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.ItemModelContent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class AssetPackRegistries {
    public static Map<ResourceLocation, BlockStateContent<?>> blockState = new HashMap<>();
    public static Map<ResourceLocation, BlockModelContent<?>> blockModel = new HashMap<>();
    public static Map<ResourceLocation, ItemModelContent<?>> itemModel = new HashMap<>();
    public static Map<ResourceLocation, LanguageContent> language = new HashMap<>();

    public static void init() {
        blockState.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "blockstates/" + r.getPath() + ".json"), b.createJson()));
        blockModel.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(),    "models/" + b.getModelType() + "/" + (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/")  + r.getPath() + ".json"), b.createJson()));
        itemModel.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "models/" + b.getModelType() + "/" + (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/")  + (r.getPath().contains(":") ? r.getPath().split(":")[1] : r.getPath()) + ".json"), b.createJson()));
        language.forEach((r, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(r.getNamespace(), "lang/" + r + ".json"), b.translations()));
    }

    public static boolean saveBlockState(BlockStateContent<?> blockState, boolean override) {
        ResourceLocation resourceLocation = toResourceLocation(blockState);

        if(!override && AssetPackRegistries.blockState.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.blockState.put(resourceLocation, blockState);

        return true;
    }

    public static boolean saveBlockModel(BlockModelContent<?> blockModel, boolean override) {
        ResourceLocation resourceLocation = toResourceLocation(blockModel);

        if(!override && AssetPackRegistries.blockModel.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.blockModel.put(resourceLocation, blockModel);
        return true;
    }

    public static boolean saveItemModel(ItemModelContent<?> itemModel, boolean override) {
        ResourceLocation resourceLocation = toResourceLocation(itemModel);

        if(!override && AssetPackRegistries.itemModel.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.itemModel.put(resourceLocation, itemModel);
        return true;
    }

    public static boolean saveLanguage(LanguageContent language, boolean override) {
        ResourceLocation resourceLocation = toResourceLocation(language);

        if(!override && AssetPackRegistries.language.containsKey(resourceLocation)){
            return false;
        }

        AssetPackRegistries.language.put(resourceLocation, language);
        return true;
    }

    public static ResourceLocation toResourceLocation(Object asset){
        if (asset instanceof BlockStateContent<?> Content) {
            return new ResourceLocation(Content.modid(), Content.name());
        }

        if (asset instanceof BlockModelContent<?> Content) {
            return new ResourceLocation(Content.modid(), Content.name());
        }

        if (asset instanceof ItemModelContent<?> Content) {
            return new ResourceLocation(Content.modid(), Content.name());
        }

        if (asset instanceof LanguageContent Content) {
            return new ResourceLocation(Content.modid(), Content.name());
        }

        return null;
    }
}