package com.mightydanp.techcore.client.config;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ClientConfig {

    public static final int TEMPERATURE_TINT_INDEX = 100;
    private static final Set<Item> registered_items = new LinkedHashSet<>();
    private static final Set<Item> blacklisted_items = new HashSet<>();

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue TEMPERATURE_ALL_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TEMPERATURE_BLACKLIST;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("temperatureGlow");
        TEMPERATURE_ALL_ITEMS = builder
                .comment("Applies temperature color glow to all items in the game, not just registered ones.")
                .define("allItems", false);
        TEMPERATURE_BLACKLIST = builder
                .comment("Item registry names excluded from temperature glow (e.g. 'minecraft:iron_ingot').")
                .defineListAllowEmpty("blacklist", List.of(), o -> o instanceof String);
        builder.pop();
        SPEC = builder.build();
    }

    public static void registerConfigs(FMLJavaModLoadingContext context) {
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

    public static void register(ItemLike... items) {
        for (ItemLike itemLike : items) {
            Item item = itemLike.asItem();
            if (!blacklisted_items.contains(item))
                registered_items.add(item);
        }
    }

    public static void blacklist(ItemLike... items) {
        for (ItemLike itemLike : items) {
            Item item = itemLike.asItem();
            blacklisted_items.add(item);
            registered_items.remove(item);
        }
    }

    public static Set<Item> getRegistered() {
        return Collections.unmodifiableSet(registered_items);
    }

    public static boolean isRegistered(Item item) {
        return registered_items.contains(item);
    }

    public static boolean isBlacklisted(Item item) {
        return blacklisted_items.contains(item);
    }

    private static boolean vanillaDefaultsLoaded = false;
    private static boolean techCoreDefaultsLoaded = false;

    public static void registerVanillaDefaults() {
        if (vanillaDefaultsLoaded) return;
        vanillaDefaultsLoaded = true;
        List.of("leather_helmet", "leather_chestplate", "leather_leggings", "leather_boots",
                        "leather_horse_armor", "potion", "splash_potion", "lingering_potion",
                        "tipped_arrow", "filled_map", "firework_star", "shield")
                .forEach(name -> {
                    Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("minecraft", name));
                    if (item != null) blacklisted_items.add(item);
                });
        ForgeRegistries.ITEMS.getKeys().stream()
                .filter(k -> k.getNamespace().equals("minecraft"))
                .map(ForgeRegistries.ITEMS::getValue)
                .filter(i -> i != null && !(i instanceof SpawnEggItem) && !blacklisted_items.contains(i))
                .forEach(registered_items::add);
    }

    public static void registerTechCoreDefaults() {
        if (techCoreDefaultsLoaded) return;
        techCoreDefaultsLoaded = true;
        List.of()
                .forEach(name -> {
                    Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, (String) name));
                    if (item != null) blacklisted_items.add(item);
                });
        ForgeRegistries.ITEMS.getKeys().stream()
                .filter(k -> k.getNamespace().equals(CoreRef.MOD_ID))
                .map(ForgeRegistries.ITEMS::getValue)
                .filter(i -> i != null && !(i instanceof SpawnEggItem) && !blacklisted_items.contains(i))
                .forEach(registered_items::add);
    }
}