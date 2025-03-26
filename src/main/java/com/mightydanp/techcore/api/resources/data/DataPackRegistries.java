package com.mightydanp.techcore.api.resources.data;

import com.mightydanp.techcore.api.resources.data.loottable.LootTableContent;
import com.mightydanp.techcore.api.resources.data.recipe.RecipeContent;
import com.mightydanp.techcore.api.resources.data.tag.TagContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;

public class DataPackRegistries {
    //private static final Map<ResourceLocation, TagContent<BannerPattern>> bannerPatternTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<Block>> blockTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<Block>> blockMineableTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<CatVariant>> catVariantTag = new HashMap<>();

    //private static final Map<ResourceLocation, TagContent<DamageType>> damageTypeTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<EntityType<?>>> entityTypeTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<Fluid>> fluidTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<GameEvent>> gameEventTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<Item>> itemTag = new HashMap<>();
    private static final Map<ResourceLocation, TagContent<Instrument>> instrumentTag = new HashMap<>();

    //private static final Map<ResourceLocation, TagContent<PaintingVariant>> paintingVariantTag = new HashMap<>();
    //private static final Map<ResourceLocation, TagContent<Biome>> biomeTag = new HashMap<>();
    //private static final Map<ResourceLocation, TagContent<ConfiguredFeature<?, ?>>> configuredFeatureTag = new HashMap<>();
    private static final Map<ResourceLocation, LootTableContent> blockLootTable = new HashMap<>();
    private static final Map<ResourceLocation, LootTableContent> chestLootTable = new HashMap<>();
    private static final Map<ResourceLocation, LootTableContent> entityLootTable = new HashMap<>();
    private static final Map<ResourceLocation, LootTableContent> gameplayLootTable = new HashMap<>();
    public static final Map<ResourceLocation, RecipeContent> recipe = new HashMap<>();

    public static void init() {
        //bannerPatternTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/banner_pattern/" + s.getPath() + ".json"), b.json()));
        blockTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/blocks/" + s.getPath() + ".json"), b.json()));
        blockMineableTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/blocks/mineable/" + s.getPath() + ".json"), b.json()));
        catVariantTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/cat_variant/" + s.getPath() + ".json"), b.json()));
        //damageTypeTag.forEach((s, b) -> DataPackRegistry.dataHolder.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/damage_type/" +  s.getPath() + ".json"), b.json()));
        entityTypeTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/entity_type/" + s.getPath() + ".json"), b.json()));
        fluidTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/fluids/" + s.getPath() + ".json"), b.json()));
        gameEventTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/game_events/" + s.getPath() + ".json"), b.json()));
        itemTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/items/" + s.getPath() + ".json"), b.json()));
        instrumentTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/instrument/" + s.getPath() + ".json"), b.json()));
        //paintingVariantTag.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/painting_variant/" + s.getPath() + ".json"), b.json()));
        //biomeTag.forEach((s, b) -> DataPackRegistry.dataHolder.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/worldgen/biome/" +  s.getPath() + ".json"), b.json()));
        //configuredFeatureTag.forEach((s, b) -> DataPackRegistry.dataHolder.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "tags/worldgen/configured_structure_feature/" +  s.getPath() + ".json"), b.json()));

        blockLootTable.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "loot_tables/blocks/" + s.getPath() + ".json"), b.json()));
        chestLootTable.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "loot_tables/chests/" + s.getPath() + ".json"), b.json()));
        entityLootTable.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "loot_tables/entities/" + s.getPath() + ".json"), b.json()));
        gameplayLootTable.forEach((s, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(s.getNamespace(), "loot_tables/gameplay/" + s.getPath() + ".json"), b.json()));

        recipe.forEach((r, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(r.getNamespace(), "recipes/" + r.getPath() + ".json"), b.recipeJson()));
        recipe.forEach((r, b) -> DataPackRegistry.dataPack.addToResources(ResourceLocation.fromNamespaceAndPath(r.getNamespace(), "advancements/" + r.getPath() + ".json"), b.advancementJson()));
    }

    //------------------------------------------------------------------------------------------------------------------
/*
    public static boolean saveBannerPatternTag(TagContent<BannerPattern> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.bannerPatternTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.bannerPatternTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<BannerPattern> getBannerPatternTag(ResourceLocation name) {
        return bannerPatternTag.getOrDefault(name, new TagContent<>(name, Registries.BANNER_PATTERN, BuiltInRegistries.BANNER_PATTERN));
    }

    public static TagContent<BannerPattern> getBannerPatternTag(String modid, String name) {
        return bannerPatternTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.BANNER_PATTERN, BuiltInRegistries.BANNER_PATTERN));
    }
*/
    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveBlockTag(TagContent<Block> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.blockTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.blockTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<Block> getBlockTag(ResourceLocation name) {
        return blockTag.getOrDefault(name, new TagContent<>(name, Registries.BLOCK, BuiltInRegistries.BLOCK));
    }

    public static TagContent<Block> getBlockTag(String modid, String name) {
        return blockTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.BLOCK, BuiltInRegistries.BLOCK));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveBlockMineableTag(TagContent<Block> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.blockMineableTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.blockMineableTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<Block> getBlockMineableTag(ResourceLocation name) {
        return blockMineableTag.getOrDefault(name, new TagContent<>(name, Registries.BLOCK, BuiltInRegistries.BLOCK));
    }

    public static TagContent<Block> getBlockMineableTag(String modid, String name) {
        return blockMineableTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.BLOCK, BuiltInRegistries.BLOCK));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveCatVariantTag(TagContent<CatVariant> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.catVariantTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.catVariantTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<CatVariant> getCatVariantTag(ResourceLocation name) {
        return catVariantTag.getOrDefault(name, new TagContent<>(name, Registries.CAT_VARIANT, BuiltInRegistries.CAT_VARIANT));
    }

    public static TagContent<CatVariant> getCatVariantTag(String modid, String name) {
        return catVariantTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.CAT_VARIANT, BuiltInRegistries.CAT_VARIANT));
    }

    //------------------------------------------------------------------------------------------------------------------
/*
    public static boolean saveDamageTypeTag(TagContent<DamageType> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if(!override && DataPackRegistries.damageTypeTag.containsKey(resourceLocation)){
            return false;
        }

        DataPackRegistries.damageTypeTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<DamageType> getDamageTypeTag(ResourceLocation name) {
        return damageTypeTag.getOrDefault(name, new TagContent<>(name, Registries.DAMAGE_TYPE, BuiltInRegistries.));
    }

    public static TagContent<DamageType> getDamageTypeTag(String modid, String name) {
        return damageTypeTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.DAMAGE_TYPE, BuiltInRegistries.));
    }
*/
    //----------------------------------------------------------------------------------------------------------------------
    public static boolean saveEntityTypeTag(TagContent<EntityType<?>> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.entityTypeTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.entityTypeTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<EntityType<?>> getEntityTypeTag(ResourceLocation name) {
        return entityTypeTag.getOrDefault(name, new TagContent<>(name, Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE));
    }

    public static TagContent<EntityType<?>> getEntityTypeTag(String modid, String name) {
        return entityTypeTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveFluidTag(TagContent<Fluid> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.fluidTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.fluidTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<Fluid> getFluidTag(ResourceLocation name) {
        return fluidTag.getOrDefault(name, new TagContent<>(name, Registries.FLUID, BuiltInRegistries.FLUID));
    }

    public static TagContent<Fluid> getFluidTag(String modid, String name) {
        return fluidTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.FLUID, BuiltInRegistries.FLUID));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveGameEventTag(TagContent<GameEvent> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.gameEventTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.gameEventTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<GameEvent> getGameEventTag(ResourceLocation name) {
        return gameEventTag.getOrDefault(name, new TagContent<>(name, Registries.GAME_EVENT, BuiltInRegistries.GAME_EVENT));
    }

    public static TagContent<GameEvent> getGameEventTag(String modid, String name) {
        return gameEventTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.GAME_EVENT, BuiltInRegistries.GAME_EVENT));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveInstrumentTag(TagContent<Instrument> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.instrumentTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.instrumentTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<Instrument> getInstrumentTag(ResourceLocation name) {
        return instrumentTag.getOrDefault(name, new TagContent<>(name, Registries.INSTRUMENT, BuiltInRegistries.INSTRUMENT));
    }

    public static TagContent<Instrument> getInstrumentTag(String modid, String name) {
        return instrumentTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.INSTRUMENT, BuiltInRegistries.INSTRUMENT));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveItemTag(TagContent<Item> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.itemTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.itemTag.put(resourceLocation, content);
        return true;
    }


    public static TagContent<Item> getItemTag(ResourceLocation name) {
        return itemTag.getOrDefault(name, new TagContent<>(name, Registries.ITEM, BuiltInRegistries.ITEM));
    }

    public static TagContent<Item> getItemTag(String modid, String name) {
        return itemTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.ITEM, BuiltInRegistries.ITEM));
    }

    //------------------------------------------------------------------------------------------------------------------
/*
    public static boolean savePaintVariantTag(TagContent<PaintingVariant> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.paintingVariantTag.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.paintingVariantTag.put(resourceLocation, content);
        return true;
    }


    public static TagContent<PaintingVariant> getPaintVariantTag(ResourceLocation name) {
        return paintingVariantTag.getOrDefault(name, new TagContent<>(name, Registries.PAINTING_VARIANT, BuiltInRegistries.PAINTING_VARIANT));
    }

    public static TagContent<PaintingVariant> getPaintVariantTag(String modid, String name) {
        return paintingVariantTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.PAINTING_VARIANT, BuiltInRegistries.PAINTING_VARIANT));
    }
*/
    //------------------------------------------------------------------------------------------------------------------
/*
    public static boolean saveBiomeTag(TagContent<Biome> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if(!override && DataPackRegistries.biomeTag.containsKey(resourceLocation)){
            return false;
        }

        DataPackRegistries.biomeTag.put(resourceLocation, content);
        return true;
    }

    public static TagContent<Biome> getBiomeTag(ResourceLocation name) {
        return biomeTag.getOrDefault(name, new TagContent<>(name, Registries.BIOME, BuiltInRegistries.));
    }

    public static TagContent<Biome> getBiomeTag(String modid, String name) {
        return biomeTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.BIOME, BuiltInRegistries.));
    }
*/
    //------------------------------------------------------------------------------------------------------------------
/*
    public static boolean saveConfiguredFeatureTag(TagContent<ConfiguredFeature<?, ?>> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if(!override && DataPackRegistries.configuredFeatureTag.containsKey(resourceLocation)){
            return false;
        }

        DataPackRegistries.configuredFeatureTag.put(resourceLocation, content);
        return true;
    }


    public static TagContent<ConfiguredFeature<?, ?>> getConfiguredFeatureTag(ResourceLocation name) {
        return configuredFeatureTag.getOrDefault(name, new TagContent<>(name, Registries.CONFIGURED_FEATURE, BuiltInRegistries));
    }

    public static TagContent<ConfiguredFeature<?, ?>> getBiomeTag(String modid, String name) {
        return configuredFeatureTag.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new TagContent<>(modid, name, Registries.CONFIGURED_FEATURE, BuiltInRegistries.));
    }
*/
    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveBlockLootTable(LootTableContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.blockLootTable.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.blockLootTable.put(resourceLocation, content);
        return true;
    }

    public static LootTableContent getBlockLootTable(ResourceLocation name) {
        return blockLootTable.getOrDefault(name, new LootTableContent(name));
    }

    public static LootTableContent getBlockLootTable(String modid, String name) {
        return blockLootTable.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new LootTableContent(modid, name));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveChestLootTable(LootTableContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.chestLootTable.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.chestLootTable.put(resourceLocation, content);
        return true;
    }

    public static LootTableContent getChestLootTable(ResourceLocation name) {
        return chestLootTable.getOrDefault(name, new LootTableContent(name));
    }

    public static LootTableContent getChestLootTable(String modid, String name) {
        return chestLootTable.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new LootTableContent(modid, name));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveEntityLootTable(LootTableContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.entityLootTable.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.entityLootTable.put(resourceLocation, content);
        return true;
    }

    public static LootTableContent getEntityLootTable(ResourceLocation name) {
        return entityLootTable.getOrDefault(name, new LootTableContent(name));
    }

    public static LootTableContent getEntityLootTable(String modid, String name) {
        return entityLootTable.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new LootTableContent(modid, name));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveGameplayLootTable(LootTableContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.gameplayLootTable.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.gameplayLootTable.put(resourceLocation, content);
        return true;
    }

    public static LootTableContent getGameplayLootTable(ResourceLocation name) {
        return gameplayLootTable.getOrDefault(name, new LootTableContent(name));
    }

    public static LootTableContent getGameplayLootTable(String modid, String name) {
        return gameplayLootTable.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new LootTableContent(modid, name));
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean saveRecipe(RecipeContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && DataPackRegistries.recipe.containsKey(resourceLocation)) {
            return false;
        }

        DataPackRegistries.recipe.put(resourceLocation, content);

        return true;
    }

    public static RecipeContent getRecipe(ResourceLocation name) {
        return recipe.getOrDefault(name, new RecipeContent(name));
    }

    public static RecipeContent getRecipe(String modid, String name) {
        return recipe.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new RecipeContent(modid, name));
    }
}