package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
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

        ResourceLocation resourceLocation = new ResourceLocation(CoreRef.MOD_ID, "test");

        ConfigRegistries.saveBlockTrait(resourceLocation, ConfigRegistries.getBlockTrait(resourceLocation), false);

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
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Tech Core server is starting.");
    }
}
