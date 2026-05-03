package com.mightydanp.techcore;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mightydanp.techcore.TechCore.LOGGER;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Tech Core client setup is starting");

        event.enqueueWork(() -> {
            RegistriesHandler.getMaterials().forEach(m -> m.get().initItemProperties());
        });

        LOGGER.info("Tech Core client setup has finished");
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        RegistriesHandler.getMaterials().forEach(m -> m.get().initClientRenderLayers(event));
    }
}
