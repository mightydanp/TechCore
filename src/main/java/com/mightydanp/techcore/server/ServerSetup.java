package com.mightydanp.techcore.server;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.block.OreBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.jetbrains.annotations.NotNull;

import static com.mightydanp.techcore.TechCore.LOGGER;


@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class ServerSetup {

    @SubscribeEvent
    public static void onServerSetup(final @NotNull FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Tech Core server setup is starting");

        //enqueueWork is only needed for things that aren't thread-safe
        event.enqueueWork(() -> {

        });

        LOGGER.info("Tech Core server setup is finished ");
    }

    @SubscribeEvent
    public void onServerTick(@NotNull TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            OreBlock.expireStaleHarvests(event.getServer());
        }
    }

    @SubscribeEvent
    public void onLevelUnload(@NotNull LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            OreBlock.repairLevelUnload(serverLevel);
        }
    }

    @SubscribeEvent
    public void onServerStopping(@NotNull ServerStoppingEvent event) {
        OreBlock.repairServerStop(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopped(@NotNull ServerStoppedEvent event) {
        OreBlock.clearTransientHarvestState();
    }
}
