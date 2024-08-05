package com.mightydanp.techcore.api.configs;

import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.trait.block.BlockTraitConfig;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigRegistries {
    private static final Map<ResourceLocation, BlockTraitConfig> blockTrait = new HashMap<>();

    public static void registerConfigs(){
        TechCore.LOGGER.info("Tech Core config registry is starting.");

        blockTrait.forEach((resourceLocation, config) -> ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, config.build(), CoreRef.MOD_ID + "/blockTrait/" + resourceLocation.getPath() + ".toml"));

        TechCore.LOGGER.info("Tech Core config registry has completed.");
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event){
        TechCore.LOGGER.info("Tech Core configs are loading.");
        blockTrait.forEach((resourceLocation, config) -> config.load());

        TechCore.LOGGER.info("Tech Core configs are done loading.");
    }

    public static BlockTraitConfig getBlockTrait(ResourceLocation resourceLocation) {
        return blockTrait.getOrDefault(resourceLocation, new BlockTraitConfig(resourceLocation.toString(), 0, 0.0, false));
    }

    public static boolean saveBlockTrait(ResourceLocation resourceLocation, BlockTraitConfig config, boolean override) {
        if(!override && ConfigRegistries.blockTrait.containsKey(resourceLocation)){
            return false;
        }

        ConfigRegistries.blockTrait.put(resourceLocation, config);
        return true;
    }
}
