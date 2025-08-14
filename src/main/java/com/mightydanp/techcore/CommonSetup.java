package com.mightydanp.techcore;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.mightydanp.techcore.TechCore.LOGGER;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class CommonSetup {

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Tech Core common setup is starting");

        RegistriesHandler.MATERIALS.getEntries().forEach(m -> m.get().initServer());

        LOGGER.info("Tech Core common setup is finished ");
    }
}
