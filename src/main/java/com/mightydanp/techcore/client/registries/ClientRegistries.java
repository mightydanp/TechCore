package com.mightydanp.techcore.client.registries;

import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.guitab.registries.ScreenTabRegistries;

public class ClientRegistries {
    public static ScreenRegistries screenRegistries = new ScreenRegistries().init();
    public static ScreenTabRegistries screenTabRegistries = new ScreenTabRegistries().init();

    public static void init() {
        ResourcePackRegistry.addInit(screenRegistries);
        ResourcePackRegistry.addInit(screenTabRegistries);
    }
}
