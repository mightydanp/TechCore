package com.mightydanp.techcore;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistry;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.mightydanp.techcore.TechCore.LOGGER;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class CommonSetup {

    @SubscribeEvent
    private static void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Tech Core common setup is starting");

        RegistriesHandler.MATERIALS.getEntries().forEach(m -> m.get().initServer());

        LOGGER.info("Tech Core common setup is finished ");
    }
}
