package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.client.ref.CreativeTabsRef;
import com.mightydanp.techcore.materials.item.OreItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quality;
import com.mightydanp.techcore.world.item.properties.Quantity;
import com.mightydanp.techcore.world.item.properties.Temperature;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CreativeModeTabRegistry implements BaseRegistries<CreativeModeTabRegistry> {
    public static final Supplier<CreativeModeTab> BLOCK_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.block_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.block_tab_translatable))
            .icon(() -> new ItemStack(Blocks.STONE))
            .displayItems((params, output) -> {
                // Add block items here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> ITEM_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.item_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.item_tab_translatable))
            .icon(() -> new ItemStack(Items.STICK))
            .displayItems((params, output) -> {
                // Add item items here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> ORE_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.ore_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.ore_tab_translatable))
            .icon(() -> new ItemStack(Blocks.IRON_ORE))
            .displayItems((params, output) -> RegistriesHandler.getMaterialObjects().forEach(matObj -> {
                Material mat = matObj.get();

                addItemStacks(output, mat.ore.sparseOreBlockItems.values());
                addItemStacks(output, mat.ore.oreBlockItems.values());
                addItemStacks(output, mat.ore.denseOreBlockItems.values());
                addItemStacks(output, mat.ore.bedrockOreBlockItems.values());

                addOreStacks(output, mat.ore.rawOreItems.values());
                addOreStacks(output, mat.ore.centrifugedOreItems.values());
                addOreStacks(output, mat.ore.crushedOreItems.values());
                addOreStacks(output, mat.ore.purifiedOreItems.values());
            }))
            .build()
    );

    public static final Supplier<CreativeModeTab> PLANT_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.plant_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.plant_tab_translatable))
            .icon(() -> new ItemStack(Items.WHEAT))
            .displayItems((params, output) -> {
                // Add plants here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> GEM_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.gem_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.gem_tab_translatable))
            .icon(() -> new ItemStack(Items.DIAMOND))
            .displayItems((params, output) -> RegistriesHandler.getMaterialObjects().forEach(matObj -> {
                Material mat = matObj.get();
                int maxQuality = Quality.MAX;

                addGemStack(output, mat.ore.chippedGem, maxQuality / 5);
                addGemStack(output, mat.ore.flawedGem, maxQuality * 2 / 5);
                addGemStack(output, mat.ore.gem, maxQuality * 3 / 5);
                addGemStack(output, mat.ore.flawlessGem, maxQuality * 4 / 5);
                addGemStack(output, mat.ore.legendaryGem, maxQuality);
            }))
            .build()
    );

    public static final Supplier<CreativeModeTab> ORE_PRODUCTS_TAB =
            RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.ore_products_tab, () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable(CreativeTabsRef.ore_products_tab_translatable))
                            .icon(() -> new ItemStack(Items.GLOWSTONE_DUST))
                            .displayItems((params, output) -> {
                                RegistriesHandler.getMaterialObjects().forEach(matObj -> {
                                    Material mat = matObj.get();

                                    addDustStack(output, mat.processed.dust, Purity.MAX);

                                    addDustStacks(output, mat.processed.impureDustItems.values(), 25);
                                    addDustStacks(output, mat.processed.dustItems.values(), Purity.DEFAULT);
                                    addDustStack(output, mat.processed.pureDust, Purity.MAX);
                                });
                            })
                            .build()
            );

    public static final Supplier<CreativeModeTab> STONE_LAYER_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.stone_layer_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.stone_layer_tab_translatable))
            .icon(() -> new ItemStack(Items.STONE))
            .displayItems((params, output) -> RegistriesHandler.getMaterialObjects().forEach(matObj -> {
                Material mat = matObj.get();

                if (mat.rockLayer.isRockLayer) {

                    output.accept(mat.rockLayer.tilesItemBlock.get());
                    output.accept(mat.rockLayer.smallTilesItemBlock.get());
                    output.accept(mat.rockLayer.smallBricksItemBlock.get());
                    output.accept(mat.rockLayer.squareBricksItemBlock.get());
                    output.accept(mat.rockLayer.crackedBricksItemBlock.get());
                    output.accept(mat.rockLayer.chiseledBricksItemBlock.get());
                    output.accept(mat.rockLayer.windmillTilesAItemBlock.get());
                    output.accept(mat.rockLayer.windmillTilesBItemBlock.get());
                    output.accept(mat.rockLayer.mossyCobbleItemBlock.get());
                    output.accept(mat.rockLayer.mossyBricksItemBlock.get());
                    output.accept(mat.rockLayer.reinforcedBricksItemBlock.get());

                    if (!mat.rockLayer.useExistingRockLayerTexture) {
                        output.accept(mat.rockLayer.stoneItemBlock.get());
                        output.accept(mat.rockLayer.stoneButtonItemBlock.get());
                        output.accept(mat.rockLayer.stonePressurePlateItemBlock.get());
                        output.accept(mat.rockLayer.polishedSlabItemBlock.get());
                        output.accept(mat.rockLayer.polishedStairsItemBlock.get());
                        output.accept(mat.rockLayer.bricksItemBlock.get());
                        output.accept(mat.rockLayer.cobbleItemBlock.get());
                        output.accept(mat.rockLayer.polishedItemBlock.get());

                    }
                }
            }))
            .build()
    );

    public static final Supplier<CreativeModeTab> TOOL_PARTS_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.tool_parts_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.tool_parts_tab_translatable))
            .icon(() -> new ItemStack(Items.STICK))
            .displayItems((params, output) -> {
                // Add tool parts here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TOOL_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.tool_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.tool_tab_translatable))
            .icon(() -> new ItemStack(Items.STONE_AXE))
            .displayItems((params, output) -> {
                // Add tools here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TREE_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.tree_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.tree_tab_translatable))
            .icon(() -> new ItemStack(Items.OAK_LOG))
            .displayItems((params, output) -> {
                // Add trees and logs here
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> FLUID_TAB = RegistriesHandler.CREATIVE_MODE_TABS.register(CreativeTabsRef.fluid_tab, () -> CreativeModeTab.builder()
            .title(Component.translatable(CreativeTabsRef.fluid_tab_translatable))
            .icon(() -> new ItemStack(Items.BUCKET))
            .displayItems((params, output) -> {
                // Add fluid items here
            })
            .build()
    );

    private static void addGemStack(CreativeModeTab.Output output, Supplier<Item> supplier, int quality) {
        if (supplier == null) {
            return;
        }

        ItemStack stack = new ItemStack(supplier.get());
        Quality.stack(stack).set(quality);
        Quantity.stack(stack).set(Quantity.DEFAULT_MAX, Quantity.DEFAULT_MAX);
        output.accept(stack);
    }

    private static void addDustStacks(CreativeModeTab.Output output, @NotNull Iterable<Supplier<Item>> suppliers, double purity) {
        for (Supplier<Item> supplier : suppliers) {
            addDustStack(output, supplier, purity);
        }
    }

    private static void addDustStack(CreativeModeTab.Output output, Supplier<Item> supplier, double purity) {
        if (supplier == null) {
            return;
        }

        ItemStack stack = new ItemStack(supplier.get());
        Quantity.stack(stack).set(Quantity.DEFAULT_MAX, Quantity.DEFAULT_MAX);
        Purity.stack(stack).set(purity);
        output.accept(stack);
    }

    private static void addOreStacks(CreativeModeTab.Output output, @NotNull Iterable<Supplier<Item>> ores) {
        for (Supplier<Item> ore : ores) {
            if (ore != null) {
                OreItem oreItem = (OreItem) ore.get();
                addOreStack(output, oreItem, Purity.DEFAULT, null);
                addOreStack(output, oreItem, Purity.MAX, null);
            }
        }
    }

    private static void addOreStack(CreativeModeTab.Output output, OreItem oreItem, double purity, Integer temperature) {
        ItemStack stack = new ItemStack(oreItem);
        Quantity.stack(stack).set(Quantity.DEFAULT_MAX, Quantity.DEFAULT_MAX);
        Purity.stack(stack).set(purity);

        if (temperature != null) {
            Temperature.setTemperature(stack, temperature);
        }

        output.accept(stack);
    }

    private static void addItemStacks(CreativeModeTab.Output output, @NotNull Iterable<Supplier<Item>> suppliers) {
        for (Supplier<Item> supplier : suppliers) {
            if (supplier != null) {
                output.accept(new ItemStack(supplier.get()));
            }
        }
    }

    @Override
    public CreativeModeTabRegistry init() {
        return this;
    }

    @Override
    public CreativeModeTabRegistry initClient() {
        return this;
    }

    @Override
    public CreativeModeTabRegistry initLanguages() {
        CreativeTabsRef.initLanguages();
        return this;
    }
}
