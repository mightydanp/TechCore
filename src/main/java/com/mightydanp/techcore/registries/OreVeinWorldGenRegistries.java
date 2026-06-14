package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.api.resources.data.biomemodifier.BiomeModifierContent;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.NoiseSettingsContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import com.mightydanp.techcore.world.level.levelgen.feature.OreVeinFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public final class OreVeinWorldGenRegistries implements BaseRegistries<OreVeinWorldGenRegistries> {
    private static boolean initialized;
    private static final OreVeinFeature SERIALIZATION_FALLBACK_FEATURE = new OreVeinFeature(NoneFeatureConfiguration.CODEC);

    interface WorldGenContentSink {
        void configuredFeature(ConfiguredFeatureContent content);

        void placedFeature(PlacedFeatureContent content);

        void noiseSettings(NoiseSettingsContent content);

        void biomeModifier(BiomeModifierContent content);
    }

    static void emitWorldGenContent(WorldGenContentSink sink) {
        ResourceLocation configuredFeature = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "ore_veins");

        sink.configuredFeature(
                new ConfiguredFeatureContent(CoreRef.MOD_ID, "ore_veins")
                        .feature(featureTypeForContent(), NoneFeatureConfiguration.INSTANCE)
        );
        sink.placedFeature(
                new PlacedFeatureContent(CoreRef.MOD_ID, "ore_veins", configuredFeature)
        );
        sink.biomeModifier(
                new BiomeModifierContent(CoreRef.MOD_ID, "add_overworld_ore_veins")
                        .addFeatures("#minecraft:is_overworld", List.of(CoreRef.MOD_ID + ":ore_veins"), "underground_ores")
        );
    }

    static OreVeinFeature featureTypeForContent() {
        return RegistriesHandler.ORE_VEIN_FEATURE.isPresent()
                ? RegistriesHandler.ORE_VEIN_FEATURE.get()
                : SERIALIZATION_FALLBACK_FEATURE;
    }

    @Override
    public OreVeinWorldGenRegistries initClient() {
        if (initialized) {
            return this;
        }

        initialized = true;

        emitWorldGenContent(new WorldGenContentSink() {
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
                DataPackRegistries.saveNoiseSettings(content, false);
            }

            @Override
            public void biomeModifier(BiomeModifierContent content) {
                DataPackRegistries.saveBiomeModifier(content, false);
            }
        });

        return this;
    }
}
