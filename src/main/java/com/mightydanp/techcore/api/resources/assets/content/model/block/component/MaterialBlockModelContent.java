package com.mightydanp.techcore.api.resources.assets.content.model.block.component;

import com.mightydanp.techcore.api.resources.assets.content.model.block.MCBlockModelContent;
import net.minecraft.resources.ResourceLocation;

public class MaterialBlockModelContent extends MCBlockModelContent {
    public MaterialBlockModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public MaterialBlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }


}
