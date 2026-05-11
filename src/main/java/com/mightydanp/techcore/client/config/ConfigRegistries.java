package com.mightydanp.techcore.client.config;

import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.config.MaterialConfig;
import com.mightydanp.techcore.traits.config.BlockTraitConfig;
import com.mightydanp.techcore.traits.config.ItemTraitConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigRegistries {

    public static void registerConfigs(FMLJavaModLoadingContext context) {
        TechCore.LOGGER.info("Tech Core config registry is starting.");

        context.registerConfig(ModConfig.Type.CLIENT, MaterialConfig.SPEC, CoreRef.MOD_ID + "-material.toml");

        ClientConfig.registerConfigs(context);
        BlockTraitConfig.registerConfigs(context);
        ItemTraitConfig.registerConfigs(context);

        TechCore.LOGGER.info("Tech Core config registry has completed.");
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        TechCore.LOGGER.info("Tech Core configs are loading.");

        if (event.getConfig().getSpec() == ClientConfig.SPEC) ClientConfig.load();
        BlockTraitConfig.load();
        ItemTraitConfig.load();

        TechCore.LOGGER.info("Tech Core configs are done loading.");
    }
}
