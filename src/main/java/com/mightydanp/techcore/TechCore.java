package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.guitabs.event.ScreenTabEvent;
import com.mightydanp.techcore.guitabs.registries.ScreenTabRegistries;
import com.mightydanp.techcore.registries.ScreenRegistries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CoreRef.MOD_ID)
public class TechCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public TechCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        RegistriesHandler.init(modEventBus);

        ScreenRegistries screenRegistries = new ScreenRegistries();
        screenRegistries.init();
        ResourcePackRegistry.init.add(screenRegistries);

        ScreenTabRegistries screenTabRegistries = new ScreenTabRegistries();
        screenTabRegistries.init();
        ResourcePackRegistry.init.add(screenTabRegistries);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(ScreenTabEvent::addTabsToInventoryScreen);

        ConfigRegistries.registerConfigs(context);
    }
}
