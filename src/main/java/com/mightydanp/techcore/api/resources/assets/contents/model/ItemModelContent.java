package com.mightydanp.techcore.api.resources.assets.contents.model;

import net.minecraft.resources.ResourceLocation;

public class ItemModelContent extends ModelContent{
    public ItemModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public ItemModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }

    public ItemModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }
}
