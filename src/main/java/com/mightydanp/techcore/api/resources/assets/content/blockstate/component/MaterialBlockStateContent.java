package com.mightydanp.techcore.api.resources.assets.content.blockstate.component;

import com.mightydanp.techcore.api.resources.assets.content.blockstate.MCBlockStateContent;
import net.minecraft.resources.ResourceLocation;

public class MaterialBlockStateContent extends MCBlockStateContent {
    public MaterialBlockStateContent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public MaterialBlockStateContent(String modid, String name) {
        super(modid, name);
    }
}
