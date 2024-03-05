package com.mightydanp.techcore.api.resources.assets.contents;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistry;
import com.mightydanp.techcore.api.resources.assets.contents.blockstate.BlockStateContent;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.ModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class AssetPackRegistries {
    public static Map<String, BlockStateContent> blockState = new HashMap<>();
    public static Map<String, ModelContent> blockModel = new HashMap<>();
    public static Map<String, ModelContent> itemModel = new HashMap<>();
    public static Map<String, LanguageContent> language = new HashMap<>();

    public static void init() {
        blockState.forEach((s, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(CoreRef.MOD_ID, "blockstates/" + s + ".json"), b.createJson()));
        blockModel.forEach((s, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(CoreRef.MOD_ID,    "models/" + b.getModelFolder() + "/" + (b.getParentFolder() == null ? "" : b.getParentFolder() + "/")  + s + ".json"), b.createJson()));
        itemModel.forEach((s, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(CoreRef.MOD_ID, "models/" + b.getModelFolder() + "/" + (b.getParentFolder() == null ? "" : b.getParentFolder() + "/")  + (s.contains(":") ? s.split(":")[1] : s) + ".json"), b.createJson()));
        language.forEach((s, b) -> AssetPackRegistry.assetHolder.addToResources(new ResourceLocation(CoreRef.MOD_ID, "lang/" + s + ".json"), b.translations));
    }

    public static void saveBlockState(String name, BlockStateContent blockStateData, boolean existCheck) {
        if(existCheck){
            if(!blockState.containsKey(name)){
                blockState.put(name, blockStateData);
            }
        }else{
            blockState.put(name, blockStateData);
        }
    }

    public static void saveBlockModel(String name,  ModelContent blockModelData, boolean existCheck) {
        if(existCheck){
            if(!blockModel.containsKey(name)){
                blockModel.put(name, blockModelData);
            }
        }else{
            blockModel.put(name, blockModelData);
        }
    }

    public static void saveItemModel(String name,  ModelContent itemModelData, boolean existCheck) {
        if(existCheck){
            if(!itemModel.containsKey(name)){
                itemModel.put(name, itemModelData);
            }
        }else{
            itemModel.put(name, itemModelData);
        }
    }

    public static void saveLanguage(String name,  LanguageContent langData, boolean existCheck) {
        if(existCheck){
            if(!language.containsKey(name)){
                language.put(name, langData);
            }
        }else{
            language.put(name, langData);
        }
    }
}