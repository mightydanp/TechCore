package com.mightydanp.techcore.traits.config;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemTraitConfig {
    private static final Map<ResourceLocation, com.mightydanp.techcore.client.trait.item.ItemTraitConfig> itemTrait = new ConcurrentHashMap<>();

    public static void registerConfigs(FMLJavaModLoadingContext context) {
        itemTrait.forEach((resourceLocation, config) -> {
            context.registerConfig(
                    ModConfig.Type.COMMON,
                    config.build(),
                    CoreRef.MOD_ID + "/itemTrait/" + resourceLocation.getPath() + ".toml"
            );
        });
    }

    public static void load() {
        itemTrait.forEach((resourceLocation, config) -> config.load());
    }

    public static com.mightydanp.techcore.client.trait.item.ItemTraitConfig get(ResourceLocation resourceLocation) {
        return itemTrait.getOrDefault(resourceLocation, new com.mightydanp.techcore.client.trait.item.ItemTraitConfig(resourceLocation.toString(), 0, 0, "", 0.0, 0.0));
    }

    public static boolean save(ResourceLocation resourceLocation, com.mightydanp.techcore.client.trait.item.ItemTraitConfig config, boolean override) {
        if (!override && itemTrait.containsKey(resourceLocation)) {
            return false;
        }
        itemTrait.put(resourceLocation, config);
        return true;
    }
}
