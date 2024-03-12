package com.mightydanp.techcore.api.resources.assets.contents.model;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Map;

public class TCItemModelContent extends ItemModelContent<TCItemModelContent>{
    public static final String ITEM_FOLDER = "item";

    public TCItemModelContent(String modelName, String modid, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public TCItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcItem(Map<Integer, ResourceLocation> textureMap) {
        ItemModelContent<TCItemModelContent> model = new TCItemModelContent(modid(), name(), "")
                .model().parent(new ModelFile.UncheckedModelFile("item/generated")).end();

        textureMap.forEach((integer, resourceLocation) -> model.model().texture("texture_" + integer, resourceLocation));

        return model.save(false);


    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBlockItem(TCItemModelContent modelContent) {
        return new TCItemModelContent(modid(), name(), "")
                .model().parent(modelContent).end()
                .save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBoat(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcButton(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcChestBoat(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcDoor(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFence(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFenceGate(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcHangingSign(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLeaves(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLog(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPlanks(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPressurePlate(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSapling(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSign(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSlab(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStairs(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStick(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcTrapDoor(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcWood(TCItemModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------

}