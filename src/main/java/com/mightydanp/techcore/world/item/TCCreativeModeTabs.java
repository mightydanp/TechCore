package com.mightydanp.techcore.world.item;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreCreativeTabsRef;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class TCCreativeModeTabs {

    public static void init(){

    }

    public static final Supplier<CreativeModeTab> BLOCK_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("block_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.block_tab))
            .icon(() -> new ItemStack(Blocks.STONE))
            .displayItems((params, output) -> {
                // Add block items here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> ITEM_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("item_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.item_tab))
            .icon(() -> new ItemStack(Items.STICK))
            .displayItems((params, output) -> {
                // Add item items here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> ORE_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("ore_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.ore_tab))
            .icon(() -> new ItemStack(Blocks.IRON_ORE))
            .displayItems((params, output) -> {
                // Add ores here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> PLANT_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("plant_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.plant_tab))
            .icon(() -> new ItemStack(Items.WHEAT))
            .displayItems((params, output) -> {
                // Add plants here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> GEM_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("gem_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.gem_tab))
            .icon(() -> new ItemStack(Items.DIAMOND))
            .displayItems((params, output) -> {
                // Add gems here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> ORE_PRODUCTS_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("ore_products_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.ore_products_tab))
            .icon(() -> new ItemStack(Items.GLOWSTONE_DUST))
            .displayItems((params, output) -> {
                // Add processed ore products here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> STONE_LAYER_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("stone_layer_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.stone_layer_tab))
            .icon(() -> new ItemStack(Items.STONE))
            .displayItems((params, output) -> {
                // Add stone layers here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TOOL_PARTS_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("tool_parts_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.tool_parts_tab))
            .icon(() -> new ItemStack(Items.STICK))
            .displayItems((params, output) -> {
                // Add tool parts here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TOOL_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("tool_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.tool_tab))
            .icon(() -> new ItemStack(Items.STONE_AXE))
            .displayItems((params, output) -> {
                // Add tools here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TREE_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("tree_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.tree_tab))
            .icon(() -> new ItemStack(Items.OAK_LOG))
            .displayItems((params, output) -> {
                // Add trees and logs here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> FLUID_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register("fluid_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(CoreCreativeTabsRef.fluid_tab))
            .icon(() -> new ItemStack(Items.BUCKET))
            .displayItems((params, output) -> {
                // Add fluid items here
            })
            .build()
    );


}
