package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.data.data.DataGen;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CoreRef.MOD_ID)
public class TechCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public TechCore(IEventBus bus){
        //IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus(), forge = NeoForge.EVENT_BUS;
        RegistriesHandler.init(bus);

        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.addListener(DataGen::addListeners);

        ConfigRegistries.registerConfigs();
    }

    private void onCommonSetup(final FMLCommonSetupEvent event){
        LOGGER.info("Tech Core common setup is starting");
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        LOGGER.info("Tech Core client setup is starting");
    }

    @SubscribeEvent
    private void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Tech Core server is starting.");
    }
}
