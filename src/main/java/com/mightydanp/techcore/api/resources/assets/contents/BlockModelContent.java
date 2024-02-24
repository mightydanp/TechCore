package com.mightydanp.techcore.api.resources.assets.contents;

import org.jetbrains.annotations.NotNull;

public class BlockModelContent extends ModelContent{
    public BlockModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public BlockModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }
}
