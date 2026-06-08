package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.guitab.registries.ScreenTabRegistries;

public class Registries {
    public static ScreenRegistries screenRegistries = new ScreenRegistries().init();
    public static ScreenTabRegistries screenTabRegistries = new ScreenTabRegistries().init();
    public static CreativeModeTabRegistry creativeModeTabRegistry = new CreativeModeTabRegistry().init();

    public static void init(){
        ResourcePackRegistry.addInit(screenRegistries);
        ResourcePackRegistry.addInit(screenTabRegistries);
        ResourcePackRegistry.addInit(creativeModeTabRegistry);
    }
}