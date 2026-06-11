package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

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

        DataPackRegistries.saveAddFeaturesBiomeModifier(CoreRef.MOD_ID,"add_rock_layer",
                "#minecraft:is_overworld",
                List.of(CoreRef.MOD_ID + ":rock_layer"),
                "underground_ores",
                false);

        DataPackRegistries.saveRemoveFeaturesBiomeModifier(
                CoreRef.MOD_ID,
                "remove_vanilla_ores",
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
                List.of("underground_ores"),
                false
        );

        return this;
        }
}