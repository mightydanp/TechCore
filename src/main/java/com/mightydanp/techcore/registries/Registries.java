package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.resources.ResourcePackRegistry;

public class Registries {
    public static CreativeModeTabRegistries creativeModeTabRegistries = new CreativeModeTabRegistries().init();
    public static RockLayerWorldGenRegistries rockLayerWorldGenRegistries = new RockLayerWorldGenRegistries().init();
    public static VeinFeaturesRegistries veinFeaturesRegistries = new VeinFeaturesRegistries().init();
    public static VanillaWorldGenOverrides vanillaWorldGenOverrides = new VanillaWorldGenOverrides().init();
    public static OreVeinWorldGenRegistries oreVeinWorldGenRegistries = new OreVeinWorldGenRegistries().init();

    public static void init() {
        ResourcePackRegistry.addInit(creativeModeTabRegistries);
        ResourcePackRegistry.addInit(rockLayerWorldGenRegistries);
        ResourcePackRegistry.addInit(veinFeaturesRegistries);
        ResourcePackRegistry.addInit(vanillaWorldGenOverrides);
        ResourcePackRegistry.addInit(oreVeinWorldGenRegistries);
    }
}
