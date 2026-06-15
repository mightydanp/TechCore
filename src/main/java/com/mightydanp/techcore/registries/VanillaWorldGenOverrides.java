package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.api.resources.data.biomemodifier.BiomeModifierContent;
import com.mightydanp.techcore.api.resources.data.worldgen.NoiseSettingsContent;
import com.mightydanp.techcore.client.ref.CoreRef;

import java.util.List;

public final class VanillaWorldGenOverrides
        implements BaseRegistries<VanillaWorldGenOverrides> {

    private static boolean initialized;

    @Override
    public VanillaWorldGenOverrides initClient() {
        if (initialized) {
            return this;
        }

        initialized = true;

        emitOverworldOreRemoval();
        emitVanillaNoiseSettingsOverrides();

        // Optional: enable after TechCore/TechAscension has replacement Nether veins.
        // emitNetherOreRemoval();

        return this;
    }

    private static void emitOverworldOreRemoval() {
        DataPackRegistries.saveBiomeModifier(
                new BiomeModifierContent(
                        CoreRef.MOD_ID,
                        "remove_vanilla_ores"
                ).removeFeatures(
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
                ),
                false
        );
    }


    private static void emitVanillaNoiseSettingsOverrides() {
        DataPackRegistries.saveNoiseSettings(new NoiseSettingsContent(
                        "minecraft",
                        "overworld"
                ).vanillaOverworld(
                        false,
                        false,
                        false
                ),
                true
        );

        DataPackRegistries.saveNoiseSettings(
                new NoiseSettingsContent(
                        "minecraft",
                        "large_biomes"
                ).vanillaOverworld(
                        true,
                        false,
                        false
                ),
                true
        );

        DataPackRegistries.saveNoiseSettings(
                new NoiseSettingsContent(
                        "minecraft",
                        "amplified"
                ).vanillaOverworld(
                        false,
                        true,
                        false
                ),
                true
        );
    }



    private static void emitNetherOreRemoval() {
        DataPackRegistries.saveBiomeModifier(
                new BiomeModifierContent(
                        CoreRef.MOD_ID,
                        "remove_vanilla_nether_ores"
                ).removeFeatures(
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
                ),
                false
        );
    }
}
