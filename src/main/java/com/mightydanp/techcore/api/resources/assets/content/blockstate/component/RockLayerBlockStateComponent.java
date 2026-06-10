package com.mightydanp.techcore.api.resources.assets.content.blockstate.component;

import net.minecraft.resources.ResourceLocation;

public class RockLayerBlockStateComponent  extends MaterialBlockStateContent{
    public RockLayerBlockStateComponent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public RockLayerBlockStateComponent(String modid, String name) {
        super(modid, name);
    }
}
