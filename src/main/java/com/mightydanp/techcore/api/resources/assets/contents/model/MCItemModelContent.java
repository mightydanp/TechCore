package com.mightydanp.techcore.api.resources.assets.contents.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Map;

public class MCItemModelContent extends ItemModelContent<MCItemModelContent> {
    public static final String ITEM_FOLDER = "item";

    public MCItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public MCItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent saveItem(Map<Integer, ResourceLocation> textureMap) {
        ItemModelContent<MCItemModelContent> model = this.model().parent(new ModelFile.UncheckedModelFile("item/generated")).end();

        textureMap.forEach((integer, resourceLocation) -> model.model().texture("texture_" + integer, resourceLocation));

        return model.save(false).end();

    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent saveBlockItem(MCBlockModelContent modelContent) {
        return this
                .model().parent(modelContent.model()).end()
                .save(false).end();
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcBoat(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcButton(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcChestBoat(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcDoor(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcFence(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcFenceGate(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcHangingSign(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcLeaves(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcLog(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcPane(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcPlanks(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcPressurePlate(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcSapling(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcSign(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcSlab(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcStairs(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcStick(Map<Integer, ResourceLocation> textureMap) {
        return saveItem(textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcTrapDoor(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcWall(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }

    //------------------------------------------------------------------------------------------------------------------
    public MCItemModelContent mcWood(MCBlockModelContent modelContent) {
        return saveBlockItem(modelContent);
    }
    //------------------------------------------------------------------------------------------------------------------

}