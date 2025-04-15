package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.data.data.DataGen;
import com.mightydanp.techcore.data.event.TCInventoryEvent;
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
        RegistriesHandler.init(bus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(TCInventoryEvent::onTick);
        NeoForge.EVENT_BUS.addListener(TCInventoryEvent::onRightClick);
        NeoForge.EVENT_BUS.addListener(TCInventoryEvent::addSlotServerSide);
        NeoForge.EVENT_BUS.addListener(TCInventoryEvent::addTabsToInventoryScreen);

        ConfigRegistries.registerConfigs();
    }

    @SubscribeEvent
    private void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Tech Core server is starting.");
    }
}
