package com.mightydanp.techcore;

import com.mightydanp.techcore.api.configs.ConfigRegistries;
import com.mightydanp.techcore.api.guitabs.event.TCInventoryEvent;
import com.mightydanp.techcore.api.guitabs.registries.GuiTabRegistries;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistry;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.component.TCDataComponents;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CoreRef.MOD_ID)
public class TechCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public TechCore(IEventBus bus) {
        RegistriesHandler.init(bus);

        TCDataComponents.init();

        GuiTabRegistries.init();
        AssetPackRegistry.init.add(new GuiTabRegistries());

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(TCInventoryEvent::addTabsToInventoryScreen);
        ConfigRegistries.registerConfigs();
    }

    @SubscribeEvent
    private void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Tech Core server is starting.");
    }
}
