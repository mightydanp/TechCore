package com.mightydanp.techcore;

import com.mightydanp.techcore.api.guitabs.registries.GuiTabRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mightydanp.techcore.TechCore.LOGGER;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    private static void onClientSetup(FMLClientSetupEvent event){
        LOGGER.info("Tech Core client setup is starting");
        GuiTabRegistries.initResource();
        LOGGER.info("Tech Core client setup has finished");
    }
}
