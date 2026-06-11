package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class RockLayerWorldGenRegistries  implements BaseRegistries<RockLayerWorldGenRegistries> {
        private static boolean initialized;


    @Override
    public RockLayerWorldGenRegistries initClient() {
        if (initialized) {
            return null;
        }

        initialized = true;

        ResourceLocation rockLayer = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "rock_layer");

        DataPackRegistries.saveConfiguredFeature(
                new ConfiguredFeatureContent(CoreRef.MOD_ID, "rock_layer")
                        .feature(
                                RegistriesHandler.ROCK_LAYER_FEATURE.get(),
                                NoneFeatureConfiguration.INSTANCE
                        ),
                false
        );

        DataPackRegistries.savePlacedFeature(
                new PlacedFeatureContent(CoreRef.MOD_ID, "rock_layer", rockLayer)
                        .count(1)
                        .inSquare()
                        .biomeFilter(),
                false
        );
            return this;
        }
}