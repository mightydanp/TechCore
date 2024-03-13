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
    public TCItemModelContent tcBlockItem(TCBlockModelContent modelContent) {
        return new TCItemModelContent(modid(), name(), "")
                .model().parent(modelContent.model()).end()
                .save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcBoat(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcButton(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcChestBoat(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcDoor(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFence(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcFenceGate(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcHangingSign(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLeaves(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcLog(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPane(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPlanks(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcPressurePlate(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSapling(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSign(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcSlab(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStairs(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcStick(Map<Integer, ResourceLocation> textureMap){
        return tcItem(textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcTrapDoor(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcWall(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------
    public TCItemModelContent tcWood(TCBlockModelContent modelContent){
        return tcBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------

}