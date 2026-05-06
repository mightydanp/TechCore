package com.mightydanp.techcore.traits.config;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockTraitConfig {
    private static final Map<ResourceLocation, com.mightydanp.techcore.client.trait.block.BlockTraitConfig> blockTrait = new ConcurrentHashMap<>();

    public static void registerConfigs(FMLJavaModLoadingContext context) {
        blockTrait.forEach((resourceLocation, config) -> {
            context.registerConfig(
                    ModConfig.Type.COMMON,
                    config.build(),
                    CoreRef.MOD_ID + "/blockTrait/" + resourceLocation.getPath() + ".toml"
            );
        });
    }

    public static void load() {
        blockTrait.forEach((resourceLocation, config) -> config.load());
    }

    public static com.mightydanp.techcore.client.trait.block.BlockTraitConfig get(ResourceLocation resourceLocation) {
        return blockTrait.getOrDefault(resourceLocation, new com.mightydanp.techcore.client.trait.block.BlockTraitConfig(resourceLocation.toString(), 0, 0.0, false));
    }

    public static boolean save(ResourceLocation resourceLocation, com.mightydanp.techcore.client.trait.block.BlockTraitConfig config, boolean override) {
        if (!override && blockTrait.containsKey(resourceLocation)) {
            return false;
        }
        blockTrait.put(resourceLocation, config);
        return true;
    }
}
