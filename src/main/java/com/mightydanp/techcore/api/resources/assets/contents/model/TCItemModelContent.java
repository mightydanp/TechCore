package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Map;

public class TCItemModelContent extends ModelContent{
    public static final String ITEM_FOLDER = "item";

    public TCItemModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public TCItemModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }

    public TCItemModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcItem(String baseName, String category, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;
        TCItemModelContent model = new TCItemModelContent(name, ITEM_FOLDER, "");

        model.model().parent(new ModelFile.UncheckedModelFile("item/generated"));

        textureMap.forEach((integer, resourceLocation) -> model.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBlockItem(String baseName, String category, TCBlockModelContent modelContent) {
        String name = baseName + "_" + category;
        TCItemModelContent model = new TCItemModelContent(name, ITEM_FOLDER, "");

        model.model().parent(modelContent);

        AssetPackRegistries.saveItemModel(name, model, true);
        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBoat(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcButton(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcChestBoat(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcDoor(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFence(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFenceGate(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcHangingSign(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLeaves(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLog(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPlanks(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPressurePlate(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSapling(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSign(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSlab(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStairs(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStick(String baseName, String category, Map<Integer, ResourceLocation> textureMap){
        String name = baseName + "_" + category;
        TCItemModelContent model = tcItem(baseName, category, textureMap);

        AssetPackRegistries.saveItemModel(name, model, true);

        return model;
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcTrapDoor(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcWood(String baseName, String category, TCBlockModelContent modelContent){
        return tcBlockItem(baseName, category, modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------

}