package com.mightydanp.techcore.api.resources.data;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.api.resources.data.biomemodifier.BiomeModifierContent;
import com.mightydanp.techcore.api.resources.data.loottable.LootTableContent;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import com.mightydanp.techcore.api.resources.data.recipe.RecipeContent;
import com.mightydanp.techcore.api.resources.data.tag.TagContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataPackRegistries {
    private static final Map<ResourceLocation, TagContent<BannerPattern>> bannerPatternTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Block>> blockTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Block>> blockMineableTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<CatVariant>> catVariantTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<DamageType>> damageTypeTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<EntityType<?>>> entityTypeTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Fluid>> fluidTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<GameEvent>> gameEventTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Item>> itemTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Instrument>> instrumentTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<PaintingVariant>> paintingVariantTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<Biome>> biomeTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, TagContent<ConfiguredFeature<?, ?>>> configuredFeatureTag = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, ConfiguredFeatureContent> configuredFeature = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, PlacedFeatureContent> placedFeature = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BiomeModifierContent> biomeModifier = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, LootTableContent> blockLootTable = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, LootTableContent> chestLootTable = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, LootTableContent> entityLootTable = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, LootTableContent> gameplayLootTable = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, RecipeContent> recipe = new ConcurrentHashMap<>();

    public static void init() {
        addData(bannerPatternTag, "tags/banner_pattern", TagContent::json);
        addData(blockTag, "tags/blocks", TagContent::json);
        addData(blockMineableTag, "tags/blocks/mineable", TagContent::json);
        addData(catVariantTag, "tags/cat_variant", TagContent::json);
        addData(damageTypeTag, "tags/damage_type", TagContent::json);
        addData(entityTypeTag, "tags/entity_type", TagContent::json);
        addData(fluidTag, "tags/fluids", TagContent::json);
        addData(gameEventTag, "tags/game_events", TagContent::json);
        addData(itemTag, "tags/items", TagContent::json);
        addData(instrumentTag, "tags/instrument", TagContent::json);
        addData(paintingVariantTag, "tags/painting_variant", TagContent::json);
        addData(biomeTag, "tags/worldgen/biome", TagContent::json);
        addData(configuredFeatureTag, "tags/worldgen/configured_feature", TagContent::json);
        addData(configuredFeature, "worldgen/configured_feature", ConfiguredFeatureContent::json);
        addData(placedFeature, "worldgen/placed_feature", PlacedFeatureContent::json);
        addData(biomeModifier, "forge/biome_modifier", BiomeModifierContent::json);
        addData(blockLootTable, "loot_tables/blocks", LootTableContent::json);
        addData(chestLootTable, "loot_tables/chests", LootTableContent::json);
        addData(entityLootTable, "loot_tables/entities", LootTableContent::json);
        addData(gameplayLootTable, "loot_tables/gameplay", LootTableContent::json);
        addData(recipe, "recipes", RecipeContent::recipeJson);
        addData(recipe, "advancements", RecipeContent::advancementJson);
    }

    private static <T> void addData(Map<ResourceLocation, T> map, String folder, Function<T, JsonObject> jsonGetter) {
        map.forEach((location, content) -> ResourcePackRegistry.PACK.addData(
                ResourceLocation.fromNamespaceAndPath(
                        location.getNamespace(),
                        folder + "/" + location.getPath() + ".json"
                ),

                jsonGetter.apply(content)
        ));
    }

    ///
    public static boolean saveBannerPatternTag(@NotNull TagContent<BannerPattern> content, boolean override) {
        return save(bannerPatternTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<BannerPattern> getBannerPatternTag(ResourceLocation name) {
        return get(bannerPatternTag, name, () -> new TagContent<>(name, Registries.BANNER_PATTERN, BuiltInRegistries.BANNER_PATTERN));
    }

    public static TagContent<BannerPattern> getBannerPatternTag(String modid, String name) {
        return getBannerPatternTag(location(modid, name));
    }

    ///
    public static boolean saveBlockTag(@NotNull TagContent<Block> content, boolean override) {
        return save(blockTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Block> getBlockTag(ResourceLocation name) {
        return get(blockTag, name, () -> new TagContent<>(name, Registries.BLOCK, ForgeRegistries.BLOCKS)
        );
    }

    public static TagContent<Block> getBlockTag(String modid, String name) {
        return getBlockTag(location(modid, name));
    }

    ///
    public static boolean saveBlockMineableTag(@NotNull TagContent<Block> content, boolean override) {
        return save(blockMineableTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Block> getBlockMineableTag(ResourceLocation name) {
        return get(blockMineableTag, name, () -> new TagContent<>(name, Registries.BLOCK, ForgeRegistries.BLOCKS));
    }

    public static TagContent<Block> getBlockMineableTag(String modid, String name) {
        return getBlockMineableTag(location(modid, name));
    }

    ///
    public static boolean saveCatVariantTag(@NotNull TagContent<CatVariant> content, boolean override) {
        return save(catVariantTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<CatVariant> getCatVariantTag(ResourceLocation name) {
        return get(catVariantTag, name, () -> new TagContent<>(name, Registries.CAT_VARIANT, BuiltInRegistries.CAT_VARIANT));
    }

    public static TagContent<CatVariant> getCatVariantTag(String modid, String name) {
        return getCatVariantTag(location(modid, name));
    }

    ///
    public static boolean saveDamageTypeTag(@NotNull TagContent<DamageType> content, boolean override) {
        return save(damageTypeTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<DamageType> getDamageTypeTag(ResourceLocation name) {
        return get(damageTypeTag, name, () -> new TagContent<>(name, Registries.DAMAGE_TYPE)
        );
    }

    public static TagContent<DamageType> getDamageTypeTag(String modid, String name) {
        return getDamageTypeTag(location(modid, name));
    }

    ///
    public static boolean saveEntityTypeTag(@NotNull TagContent<EntityType<?>> content, boolean override) {
        return save(entityTypeTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<EntityType<?>> getEntityTypeTag(ResourceLocation name) {
        return get(entityTypeTag, name, () -> new TagContent<>(name, Registries.ENTITY_TYPE, ForgeRegistries.ENTITY_TYPES)
        );
    }

    public static TagContent<EntityType<?>> getEntityTypeTag(String modid, String name) {
        return getEntityTypeTag(location(modid, name));
    }

    ///
    public static boolean saveFluidTag(@NotNull TagContent<Fluid> content, boolean override) {
        return save(fluidTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Fluid> getFluidTag(ResourceLocation name) {
        return get(fluidTag, name, () -> new TagContent<>(name, Registries.FLUID, ForgeRegistries.FLUIDS)
        );
    }

    public static TagContent<Fluid> getFluidTag(String modid, String name) {
        return getFluidTag(location(modid, name));
    }

    ///
    public static boolean saveGameEventTag(@NotNull TagContent<GameEvent> content, boolean override) {
        return save(gameEventTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<GameEvent> getGameEventTag(ResourceLocation name) {
        return get(gameEventTag, name, () -> new TagContent<>(name, Registries.GAME_EVENT, BuiltInRegistries.GAME_EVENT)
        );
    }

    public static TagContent<GameEvent> getGameEventTag(String modid, String name) {
        return getGameEventTag(location(modid, name));
    }

    ///
    public static boolean saveInstrumentTag(@NotNull TagContent<Instrument> content, boolean override) {
        return save(instrumentTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Instrument> getInstrumentTag(ResourceLocation name) {
        return get(instrumentTag, name, () -> new TagContent<>(name, Registries.INSTRUMENT, BuiltInRegistries.INSTRUMENT)
        );
    }

    public static TagContent<Instrument> getInstrumentTag(String modid, String name) {
        return getInstrumentTag(location(modid, name));
    }

    ///
    public static boolean saveItemTag(@NotNull TagContent<Item> content, boolean override) {
        return save(itemTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Item> getItemTag(ResourceLocation name) {
        return get(itemTag, name, () -> new TagContent<>(name, Registries.ITEM, ForgeRegistries.ITEMS)
        );
    }

    public static TagContent<Item> getItemTag(String modid, String name) {
        return getItemTag(location(modid, name));
    }

    ///
    public static boolean savePaintVariantTag(@NotNull TagContent<PaintingVariant> content, boolean override) {
        return save(paintingVariantTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<PaintingVariant> getPaintVariantTag(ResourceLocation name) {
        return get(paintingVariantTag, name, () -> new TagContent<>(name, Registries.PAINTING_VARIANT)
        );
    }

    public static TagContent<PaintingVariant> getPaintVariantTag(String modid, String name) {
        return getPaintVariantTag(location(modid, name));
    }

    ///
    public static boolean saveBiomeTag(@NotNull TagContent<Biome> content, boolean override) {
        return save(biomeTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<Biome> getBiomeTag(ResourceLocation name) {
        return get(biomeTag, name, () -> new TagContent<>(name, Registries.BIOME)
        );
    }

    public static TagContent<Biome> getBiomeTag(String modid, String name) {
        return getBiomeTag(location(modid, name));
    }

    ///
    public static boolean saveConfiguredFeatureTag(@NotNull TagContent<ConfiguredFeature<?, ?>> content, boolean override) {
        return save(configuredFeatureTag, location(content.modid(), content.name()), content, override);
    }

    public static TagContent<ConfiguredFeature<?, ?>> getConfiguredFeatureTag(ResourceLocation name) {
        return get(configuredFeatureTag, name, () -> new TagContent<>(name, Registries.CONFIGURED_FEATURE)
        );
    }

    public static TagContent<ConfiguredFeature<?, ?>> getConfiguredFeatureTag(String modid, String name) {
        return getConfiguredFeatureTag(location(modid, name));
    }

    ///
    public static boolean saveConfiguredFeature(@NotNull ConfiguredFeatureContent content, boolean override) {
        return save(configuredFeature, location(content.modid(), content.name()), content, override);
    }

    public static ConfiguredFeatureContent getConfiguredFeature(ResourceLocation name) {
        return get(configuredFeature, name, () -> new ConfiguredFeatureContent(name)
        );
    }

    public static ConfiguredFeatureContent getConfiguredFeature(String modid, String name) {
        return getConfiguredFeature(location(modid, name));
    }

    ///
    public static boolean savePlacedFeature(@NotNull PlacedFeatureContent content, boolean override) {
        return save(placedFeature, location(content.modid(), content.name()), content, override);
    }

    public static PlacedFeatureContent getPlacedFeature(ResourceLocation name, ResourceLocation configuredFeature) {
        return get(placedFeature, name, () -> new PlacedFeatureContent(name, configuredFeature)
        );
    }

    public static PlacedFeatureContent getPlacedFeature(String modid, String name, ResourceLocation configuredFeature) {
        return getPlacedFeature(location(modid, name), configuredFeature);
    }

    ///
    public static boolean saveBiomeModifier(@NotNull BiomeModifierContent content, boolean override) {
        return save(biomeModifier, location(content.modid(), content.name()), content, override);
    }

    public static BiomeModifierContent getBiomeModifier(ResourceLocation name) {
        return get(biomeModifier, name, () -> new BiomeModifierContent(name)
        );
    }

    public static BiomeModifierContent getBiomeModifier(String modid, String name) {
        return getBiomeModifier(location(modid, name));
    }

    public static boolean removeBiomeModifier(ResourceLocation name) {
        return biomeModifier.remove(name) != null;
    }

    public static boolean removeBiomeModifier(String modid, String name) {
        return removeBiomeModifier(location(modid, name));
    }

    public static boolean saveAddFeaturesBiomeModifier(String modid, String name, String biomes, List<String> features, String step, boolean override) {
        return saveBiomeModifier(new BiomeModifierContent(modid, name).addFeatures(biomes, features, step), override);
    }

    public static boolean saveRemoveFeaturesBiomeModifier(String modid, String name, String biomes, List<String> features, List<String> steps, boolean override) {
        return saveBiomeModifier(new BiomeModifierContent(modid, name).removeFeatures(biomes, features, steps), override);
    }

    ///
    public static boolean saveBlockLootTable(@NotNull LootTableContent content, boolean override) {
        return save(blockLootTable, location(content.modid(), content.name()), content, override);
    }

    public static LootTableContent getBlockLootTable(ResourceLocation name) {
        return get(blockLootTable, name, () -> new LootTableContent(name));
    }

    public static LootTableContent getBlockLootTable(String modid, String name) {
        return getBlockLootTable(location(modid, name));
    }

    ///
    public static boolean saveChestLootTable(@NotNull LootTableContent content, boolean override) {
        return save(chestLootTable, location(content.modid(), content.name()), content, override);
    }

    public static LootTableContent getChestLootTable(ResourceLocation name) {
        return get(chestLootTable, name, () -> new LootTableContent(name));
    }

    public static LootTableContent getChestLootTable(String modid, String name) {
        return getChestLootTable(location(modid, name));
    }

    ///
    public static boolean saveEntityLootTable(@NotNull LootTableContent content, boolean override) {
        return save(entityLootTable, location(content.modid(), content.name()), content, override);
    }

    public static LootTableContent getEntityLootTable(ResourceLocation name) {
        return get(entityLootTable, name, () -> new LootTableContent(name));
    }

    public static LootTableContent getEntityLootTable(String modid, String name) {
        return getEntityLootTable(location(modid, name));
    }

    ///
    public static boolean saveGameplayLootTable(@NotNull LootTableContent content, boolean override) {
        return save(gameplayLootTable, location(content.modid(), content.name()), content, override);
    }

    public static LootTableContent getGameplayLootTable(ResourceLocation name) {
        return get(gameplayLootTable, name, () -> new LootTableContent(name));
    }

    public static LootTableContent getGameplayLootTable(String modid, String name) {
        return getGameplayLootTable(location(modid, name));
    }

    ///
    public static boolean saveRecipe(@NotNull RecipeContent content, boolean override) {
        return save(recipe, location(content.modid(), content.name()), content, override);
    }

    public static RecipeContent getRecipe(ResourceLocation name) {
        return get(recipe, name, () -> new RecipeContent(name));
    }

    public static RecipeContent getRecipe(String modid, String name) {
        return getRecipe(location(modid, name));
    }

    @Contract("_, _ -> new")
    private static @NotNull ResourceLocation location(String modid, String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    ///
    private static <T> boolean save(Map<ResourceLocation, T> map, ResourceLocation location, T content, boolean override) {
        if (!override && map.containsKey(location)) {
            return false;
        }

        map.put(location, content);
        return true;
    }

    private static <T> T get(Map<ResourceLocation, T> map, ResourceLocation location, Supplier<T> fallback) {
        return map.getOrDefault(location, fallback.get());
    }
}
