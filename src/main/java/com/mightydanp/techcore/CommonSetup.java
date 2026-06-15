package com.mightydanp.techcore;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.network.TCNetworkChannel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import static com.mightydanp.techcore.TechCore.LOGGER;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

    @SubscribeEvent
    public static void onCommonSetup(final @NotNull FMLCommonSetupEvent event) {
        LOGGER.info("Tech Core common setup is starting");

        //enqueueWork is only needed for things that aren't thread-safe
        event.enqueueWork(() -> {

        });

        TCNetworkChannel.init();

        LOGGER.info("Tech Core common setup is finished ");
    }
}