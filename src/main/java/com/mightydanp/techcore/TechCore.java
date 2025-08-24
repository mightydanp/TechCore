package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.guitabs.event.TCInventoryEvent;
import com.mightydanp.techcore.guitabs.registries.GuiTabRegistries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CoreRef.MOD_ID)
public class TechCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public TechCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        RegistriesHandler.init(modEventBus);

        //RegistriesHandler.MATERIALS.getEntries().forEach(c -> ResourcePackRegistry.init.add(c.get()));

        GuiTabRegistries.init();
        ResourcePackRegistry.init.add(new GuiTabRegistries());

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(TCInventoryEvent::addTabsToInventoryScreen);

        ConfigRegistries.registerConfigs(context);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Tech Core server is starting.");
    }
}
