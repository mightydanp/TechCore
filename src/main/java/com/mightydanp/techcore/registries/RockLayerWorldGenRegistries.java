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
    private static final RockLayerFeature SERIALIZATION_FALLBACK_FEATURE = new RockLayerFeature(NoneFeatureConfiguration.CODEC);
    private static boolean initialized;

    private static RockLayerFeature featureTypeForContent() {
        return RegistriesHandler.ROCK_LAYER_FEATURE.isPresent() ?
                RegistriesHandler.ROCK_LAYER_FEATURE.get() : SERIALIZATION_FALLBACK_FEATURE;
    }

    static void emitWorldGenContent(OreVeinWorldGenRegistries.WorldGenContentSink sink) {
        ResourceLocation rockLayer = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "rock_layer");

        sink.configuredFeature(new ConfiguredFeatureContent(CoreRef.MOD_ID, "rock_layer")
                        .feature(
                                featureTypeForContent(),
                                NoneFeatureConfiguration.INSTANCE
                        )
        );
        sink.placedFeature(new PlacedFeatureContent(CoreRef.MOD_ID, "rock_layer", rockLayer)
                        .count(1)
                        .inSquare()
                        .biomeFilter()
        );
        sink.biomeModifier(new BiomeModifierContent(CoreRef.MOD_ID, "add_rock_layer")
                        .addFeatures("#minecraft:is_overworld", List.of(CoreRef.MOD_ID + ":rock_layer"), "local_modifications")
        );
    }

    @Override
    public RockLayerWorldGenRegistries initClient() {
        if (initialized) return null;

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
