package com.mightydanp.techcore.client.config;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TEMPERATURE_BLACKLIST;
    private static final Set<Item> blacklisted_items = new HashSet<>();

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("temperatureGlow");
        TEMPERATURE_BLACKLIST = builder
                .comment("Item registry names excluded from temperature glow (e.g. 'minecraft:iron_ingot').")
                .defineListAllowEmpty("blacklist", List.of(), o -> o instanceof String);
        builder.pop();
        SPEC = builder.build();
    }

    public static void registerConfigs(@NotNull FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, SPEC, CoreRef.MOD_ID + "-client.toml");
    }

    public static void load() {
        TEMPERATURE_BLACKLIST.get().forEach(id -> {
            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) return;
            Item item = ForgeRegistries.ITEMS.getValue(rl);
            if (item != null) blacklist(item);
        });
    }

    public static void blacklist(ItemLike @NotNull ... items) {
        for (ItemLike itemLike : items) {
            Item item = itemLike.asItem();
            blacklisted_items.add(item);
        }
    }

    public static boolean isBlacklisted(Item item) {
        return blacklisted_items.contains(item);
    }
}