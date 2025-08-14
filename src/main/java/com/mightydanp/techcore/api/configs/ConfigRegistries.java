package com.mightydanp.techcore.api.configs;

import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.client.trait.block.BlockTraitConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigRegistries {
    private static final Map<ResourceLocation, BlockTraitConfig> blockTrait = new HashMap<>();

    public static void registerConfigs(FMLJavaModLoadingContext context) {
        TechCore.LOGGER.info("Tech Core config registry is starting.");

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        blockTrait.forEach((resourceLocation, config) -> {
            context.registerConfig(
                    ModConfig.Type.COMMON,
                    config.build(),
                    CoreRef.MOD_ID + "/blockTrait/" + resourceLocation.getPath() + ".toml"
            );
        });

        TechCore.LOGGER.info("Tech Core config registry has completed.");
    }



    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        TechCore.LOGGER.info("Tech Core configs are loading.");
        blockTrait.forEach((resourceLocation, config) -> config.load());

        TechCore.LOGGER.info("Tech Core configs are done loading.");
    }

    public static BlockTraitConfig getBlockTrait(ResourceLocation resourceLocation) {
        return blockTrait.getOrDefault(resourceLocation, new BlockTraitConfig(resourceLocation.toString(), 0, 0.0, false));
    }

    public static boolean saveBlockTrait(ResourceLocation resourceLocation, BlockTraitConfig config, boolean override) {
        if (!override && ConfigRegistries.blockTrait.containsKey(resourceLocation)) {
            return false;
        }

        ConfigRegistries.blockTrait.put(resourceLocation, config);
        return true;
    }
}
