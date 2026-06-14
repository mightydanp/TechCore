package com.mightydanp.techcore.registries;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.data.biomemodifier.BiomeModifierContent;
import com.mightydanp.techcore.api.resources.data.worldgen.ConfiguredFeatureContent;
import com.mightydanp.techcore.api.resources.data.worldgen.NoiseSettingsContent;
import com.mightydanp.techcore.api.resources.data.worldgen.PlacedFeatureContent;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinWorldGenResourcesTest {
    static {
        SharedConstants.tryDetectVersion();
        try {
            Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
            bootstrapped.setAccessible(true);
            bootstrapped.setBoolean(null, true);
            try {
                BuiltInRegistries.bootStrap();
            } catch (RuntimeException ignored) {
            }
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void emitsExactSerializedWorldGenResources() {
        ensureFeatureRegisteredForSerialization();

        Map<String, JsonObject> resources = new LinkedHashMap<>();

        OreVeinWorldGenRegistries.emitWorldGenContent(new OreVeinWorldGenRegistries.WorldGenContentSink() {
            @Override
            public void configuredFeature(ConfiguredFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/configured_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void placedFeature(PlacedFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/placed_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void noiseSettings(NoiseSettingsContent content) {
                resources.put("data/" + content.modid() + "/worldgen/noise_settings/" + content.name() + ".json", content.json());
            }

            @Override
            public void biomeModifier(BiomeModifierContent content) {
                resources.put("data/" + content.modid() + "/forge/biome_modifier/" + content.name() + ".json", content.json());
            }
        });

        JsonObject configured = resources.get("data/techcore/worldgen/configured_feature/ore_veins.json");
        JsonObject placed = resources.get("data/techcore/worldgen/placed_feature/ore_veins.json");
        JsonObject modifier = resources.get("data/techcore/forge/biome_modifier/add_overworld_ore_veins.json");

        assertEquals("techcore:ore_veins", configured.get("type").getAsString());
        assertTrue(configured.getAsJsonObject("config").entrySet().isEmpty());
        assertEquals("techcore:ore_veins", placed.get("feature").getAsString());
        assertEquals(0, placed.getAsJsonArray("placement").size());
        assertEquals("forge:add_features", modifier.get("type").getAsString());
        assertEquals("#minecraft:is_overworld", modifier.get("biomes").getAsString());
        assertEquals(1, modifier.getAsJsonArray("features").size());
        assertEquals("techcore:ore_veins", modifier.getAsJsonArray("features").get(0).getAsString());
        assertEquals("underground_ores", modifier.get("step").getAsString());
    }

    @Test
    void emitsExactBroadVanillaRemovalResource() {
        ensureFeatureRegisteredForSerialization();
        ensureRockLayerFeatureRegisteredForSerialization();

        Map<String, JsonObject> resources = new LinkedHashMap<>();
        OreVeinWorldGenRegistries.emitWorldGenContent(new OreVeinWorldGenRegistries.WorldGenContentSink() {
            @Override
            public void configuredFeature(ConfiguredFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/configured_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void placedFeature(PlacedFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/placed_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void noiseSettings(NoiseSettingsContent content) {
                resources.put("data/" + content.modid() + "/worldgen/noise_settings/" + content.name() + ".json", content.json());
            }

            @Override
            public void biomeModifier(BiomeModifierContent content) {
                resources.put("data/" + content.modid() + "/forge/biome_modifier/" + content.name() + ".json", content.json());
            }
        });

        RockLayerWorldGenRegistries.emitWorldGenContent(new OreVeinWorldGenRegistries.WorldGenContentSink() {
            @Override
            public void configuredFeature(ConfiguredFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/configured_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void placedFeature(PlacedFeatureContent content) {
                resources.put("data/" + content.modid() + "/worldgen/placed_feature/" + content.name() + ".json", content.json());
            }

            @Override
            public void noiseSettings(NoiseSettingsContent content) {
                resources.put("data/" + content.modid() + "/worldgen/noise_settings/" + content.name() + ".json", content.json());
            }

            @Override
            public void biomeModifier(BiomeModifierContent content) {
                resources.put("data/" + content.modid() + "/forge/biome_modifier/" + content.name() + ".json", content.json());
            }
        });

        assertFalse(resources.containsKey("data/techcore/forge/biome_modifier/remove_vanilla_iron_and_copper.json"));

        JsonObject modifier = resources.get("data/techcore/forge/biome_modifier/remove_vanilla_ores.json");
        assertEquals("forge:remove_features", modifier.get("type").getAsString());
        assertEquals("#minecraft:is_overworld", modifier.get("biomes").getAsString());
        assertEquals(18, modifier.getAsJsonArray("features").size());
        assertEquals("minecraft:ore_coal_upper", modifier.getAsJsonArray("features").get(0).getAsString());
        assertEquals("minecraft:ore_coal_lower", modifier.getAsJsonArray("features").get(1).getAsString());
        assertEquals("minecraft:ore_iron_upper", modifier.getAsJsonArray("features").get(2).getAsString());
        assertEquals("minecraft:ore_iron_middle", modifier.getAsJsonArray("features").get(3).getAsString());
        assertEquals("minecraft:ore_iron_small", modifier.getAsJsonArray("features").get(4).getAsString());
        assertEquals("minecraft:ore_gold", modifier.getAsJsonArray("features").get(5).getAsString());
        assertEquals("minecraft:ore_gold_lower", modifier.getAsJsonArray("features").get(6).getAsString());
        assertEquals("minecraft:ore_gold_extra", modifier.getAsJsonArray("features").get(7).getAsString());
        assertEquals("minecraft:ore_redstone", modifier.getAsJsonArray("features").get(8).getAsString());
        assertEquals("minecraft:ore_redstone_lower", modifier.getAsJsonArray("features").get(9).getAsString());
        assertEquals("minecraft:ore_diamond", modifier.getAsJsonArray("features").get(10).getAsString());
        assertEquals("minecraft:ore_diamond_large", modifier.getAsJsonArray("features").get(11).getAsString());
        assertEquals("minecraft:ore_diamond_buried", modifier.getAsJsonArray("features").get(12).getAsString());
        assertEquals("minecraft:ore_lapis", modifier.getAsJsonArray("features").get(13).getAsString());
        assertEquals("minecraft:ore_lapis_buried", modifier.getAsJsonArray("features").get(14).getAsString());
        assertEquals("minecraft:ore_copper", modifier.getAsJsonArray("features").get(15).getAsString());
        assertEquals("minecraft:ore_copper_large", modifier.getAsJsonArray("features").get(16).getAsString());
        assertEquals("minecraft:ore_emerald", modifier.getAsJsonArray("features").get(17).getAsString());
        assertEquals(1, modifier.getAsJsonArray("steps").size());
        assertEquals("underground_ores", modifier.getAsJsonArray("steps").get(0).getAsString());
        assertTrue(resources.containsKey("data/techcore/forge/biome_modifier/add_overworld_ore_veins.json"));
        assertTrue(resources.containsKey("data/minecraft/worldgen/noise_settings/overworld.json"));
        assertTrue(resources.containsKey("data/minecraft/worldgen/noise_settings/large_biomes.json"));
        assertTrue(resources.containsKey("data/minecraft/worldgen/noise_settings/amplified.json"));
        assertFalse(resources.containsKey("data/minecraft/worldgen/noise_settings/nether.json"));
        assertFalse(resources.containsKey("data/techcore/forge/biome_modifier/remove_vanilla_nether_ores.json"));
    }

    @Test
    void productionUsesExistingDynamicDatapackPathWithoutStaticDataResources() {
        assertEquals(null, OreVeinWorldGenResourcesTest.class.getResource("/data/techcore/forge/biome_modifier/remove_vanilla_ores.json"));
        assertEquals(null, OreVeinWorldGenResourcesTest.class.getResource("/data/techcore/worldgen/noise_settings/overworld.json"));
    }

    @Test
    void exactVanillaNoiseSettingsIdsUseOverrideEnabledAndNetherInvocationStaysCommentedOut() throws Exception {
        Path source = resolveProjectPath(
                "src/main/java/com/mightydanp/techcore/registries/RockLayerWorldGenRegistries.java",
                "TechCore/src/main/java/com/mightydanp/techcore/registries/RockLayerWorldGenRegistries.java"
        );

        String code = Files.readString(source, StandardCharsets.UTF_8);
        assertTrue(code.contains("new NoiseSettingsContent(\"minecraft\", \"overworld\")"));
        assertTrue(code.contains("new NoiseSettingsContent(\"minecraft\", \"large_biomes\")"));
        assertTrue(code.contains("new NoiseSettingsContent(\"minecraft\", \"amplified\")"));
        assertTrue(code.contains("DataPackRegistries.saveNoiseSettings(content, true);"));
        assertTrue(code.contains("// emitNetherOreRemoval(sink);"));
    }

    private static void ensureFeatureRegisteredForSerialization() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("techcore", "ore_veins");

        if (BuiltInRegistries.FEATURE.containsKey(id)) {
            return;
        }

        Registry.register(BuiltInRegistries.FEATURE, id, OreVeinWorldGenRegistries.featureTypeForContent());
    }

    private static void ensureRockLayerFeatureRegisteredForSerialization() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("techcore", "rock_layer");

        if (BuiltInRegistries.FEATURE.containsKey(id)) {
            return;
        }

        Registry.register(BuiltInRegistries.FEATURE, id, RockLayerWorldGenRegistries.featureTypeForContent());
    }

    private static Path resolveProjectPath(String moduleRelative, String workspaceRelative) {
        Path modulePath = Path.of(moduleRelative);
        if (Files.exists(modulePath)) {
            return modulePath;
        }

        Path workspacePath = Path.of(workspaceRelative);
        if (Files.exists(workspacePath)) {
            return workspacePath;
        }

        throw new IllegalStateException("Missing expected source file: " + moduleRelative);
    }
}
