package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Map;

public class TCItemModelContent extends ModelContent{
    public static final String ITEM_FOLDER = "item";

    public TCItemModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }

    public TCItemModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcItem(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;
        TCItemModelContent model = new TCItemModelContent(modid, name, ITEM_FOLDER, "");

        model.model().parent(new ModelFile.UncheckedModelFile("item/generated"));

        textureMap.forEach((integer, resourceLocation) -> model.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBlockItem(String modid, String baseName, String category, TCBlockModelContent modelContent) {
        String name = baseName + "_" + category;
        TCItemModelContent model = new TCItemModelContent(modid, name, ITEM_FOLDER, "");

        model.model().parent(modelContent);

        AssetPackRegistries.saveItemModel(name, model, true);
        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBoat(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcButton(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcChestBoat(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcDoor(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFence(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFenceGate(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcHangingSign(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLeaves(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLog(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPlanks(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPressurePlate(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSapling(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSign(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSlab(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStairs(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStick(String modid, String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(modid, baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcTrapDoor(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcWood(String modid, String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(modid, baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------

}