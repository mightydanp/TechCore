package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.api.resources.data.biomemodifier.BiomeModifierContent;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.NoiseSettingsContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public final class RockLayerWorldGenRegistries implements BaseRegistries<RockLayerWorldGenRegistries> {
    private static boolean initialized;
    private static final RockLayerFeature SERIALIZATION_FALLBACK_FEATURE = new RockLayerFeature(NoneFeatureConfiguration.CODEC);

    static RockLayerFeature featureTypeForContent() {
        return RegistriesHandler.ROCK_LAYER_FEATURE.isPresent()
                ? RegistriesHandler.ROCK_LAYER_FEATURE.get()
                : SERIALIZATION_FALLBACK_FEATURE;
    }

    static void emitWorldGenContent(OreVeinWorldGenRegistries.WorldGenContentSink sink) {
        ResourceLocation rockLayer = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "rock_layer");

        sink.configuredFeature(
                new ConfiguredFeatureContent(CoreRef.MOD_ID, "rock_layer")
                        .feature(
                                featureTypeForContent(),
                                NoneFeatureConfiguration.INSTANCE
                        )
        );
        sink.placedFeature(
                new PlacedFeatureContent(CoreRef.MOD_ID, "rock_layer", rockLayer)
                        .count(1)
                        .inSquare()
                        .biomeFilter()
        );
        sink.biomeModifier(
                new BiomeModifierContent(CoreRef.MOD_ID, "add_rock_layer")
                        .addFeatures("#minecraft:is_overworld", List.of(CoreRef.MOD_ID + ":rock_layer"), "local_modifications")
        );
        emitOverworldOreRemoval(sink);
        emitVanillaNoiseSettingsOverrides(sink);

        // Optional: enable after TechCore/TechAscension has replacement Nether veins.
        // emitNetherOreRemoval(sink);
    }

    static void emitOverworldOreRemoval(OreVeinWorldGenRegistries.WorldGenContentSink sink) {
        sink.biomeModifier(
                new BiomeModifierContent(CoreRef.MOD_ID, "remove_vanilla_ores")
                        .removeFeatures(
                                "#minecraft:is_overworld",
                                List.of(
                                        "minecraft:ore_coal_upper",
                                        "minecraft:ore_coal_lower",
                                        "minecraft:ore_iron_upper",
                                        "minecraft:ore_iron_middle",
                                        "minecraft:ore_iron_small",
                                        "minecraft:ore_gold",
                                        "minecraft:ore_gold_lower",
                                        "minecraft:ore_gold_extra",
                                        "minecraft:ore_redstone",
                                        "minecraft:ore_redstone_lower",
                                        "minecraft:ore_diamond",
                                        "minecraft:ore_diamond_large",
                                        "minecraft:ore_diamond_buried",
                                        "minecraft:ore_lapis",
                                        "minecraft:ore_lapis_buried",
                                        "minecraft:ore_copper",
                                        "minecraft:ore_copper_large",
                                        "minecraft:ore_emerald"
                                ),
                                List.of("underground_ores")
                        )
        );
    }

    static void emitVanillaNoiseSettingsOverrides(OreVeinWorldGenRegistries.WorldGenContentSink sink) {
        sink.noiseSettings(new NoiseSettingsContent("minecraft", "overworld")
                .setJson(VanillaNoiseSettingsOverrides.overworld()));
        sink.noiseSettings(new NoiseSettingsContent("minecraft", "large_biomes")
                .setJson(VanillaNoiseSettingsOverrides.largeBiomes()));
        sink.noiseSettings(new NoiseSettingsContent("minecraft", "amplified")
                .setJson(VanillaNoiseSettingsOverrides.amplified()));
    }

    static void emitNetherOreRemoval(OreVeinWorldGenRegistries.WorldGenContentSink sink) {
        sink.biomeModifier(
                new BiomeModifierContent(CoreRef.MOD_ID, "remove_vanilla_nether_ores")
                        .removeFeatures(
                                "#minecraft:is_nether",
                                List.of(
                                        "minecraft:ore_gold_nether",
                                        "minecraft:ore_gold_deltas",
                                        "minecraft:ore_quartz_nether",
                                        "minecraft:ore_quartz_deltas",
                                        "minecraft:ore_ancient_debris_large",
                                        "minecraft:ore_debris_small"
                                ),
                                List.of("underground_decoration")
                        )
        );
    }

    @Override
    public RockLayerWorldGenRegistries initClient() {
        if (initialized) {
            return null;
        }

        initialized = true;

        emitWorldGenContent(new OreVeinWorldGenRegistries.WorldGenContentSink() {
            @Override
            public void configuredFeature(ConfiguredFeatureContent content) {
                DataPackRegistries.saveConfiguredFeature(content, false);
            }

            @Override
            public void placedFeature(PlacedFeatureContent content) {
                DataPackRegistries.savePlacedFeature(content, false);
            }

            @Override
            public void noiseSettings(NoiseSettingsContent content) {
                DataPackRegistries.saveNoiseSettings(content, true);
            }

            @Override
            public void biomeModifier(BiomeModifierContent content) {
                DataPackRegistries.saveBiomeModifier(content, false);
            }
        });

        return this;
    }
}
