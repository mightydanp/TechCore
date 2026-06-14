package com.mightydanp.techcore.command;

import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.api.resources.data.DataPackRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class WorldGenDiagnosticsCommand {
    private static final List<String> REMOVED_VANILLA_FEATURE_IDS = List.of(
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
    );
    private static final List<String> OVERWORLD_NOISE_SETTINGS_IDS = List.of(
            "minecraft:overworld",
            "minecraft:large_biomes",
            "minecraft:amplified"
    );

    private WorldGenDiagnosticsCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        register(dispatcher, !FMLEnvironment.production);
    }

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean developmentMode) {
        if (!developmentMode) {
            return;
        }

        dispatcher.register(
                Commands.literal(CoreRef.MOD_ID)
                        .then(
                                Commands.literal("worldgendiagnostics")
                                        .requires(source -> source.hasPermission(4))
                                        .executes(context -> execute(context.getSource()))
                        )
        );
    }

    private static int execute(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        BlockPos position = BlockPos.containing(source.getPosition());
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        String generatorClass = generator.getClass().getName();
        String noiseSettingsKey = "unavailable";
        String oreVeinsEnabled = "unavailable";

        if (generator instanceof NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            Holder<NoiseGeneratorSettings> holder = noiseBasedChunkGenerator.generatorSettings();
            noiseSettingsKey = holder.unwrapKey().map(ResourceKey::location).map(ResourceLocation::toString).orElse("unregistered");
            oreVeinsEnabled = Boolean.toString(holder.value().oreVeinsEnabled());
        }
        final String finalNoiseSettingsKey = noiseSettingsKey;
        final String finalOreVeinsEnabled = oreVeinsEnabled;

        Holder<net.minecraft.world.level.biome.Biome> biomeHolder = level.getBiome(position);
        List<String> undergroundOres = undergroundOresFeatureIds(biomeHolder.value().getGenerationSettings());
        Set<String> removedStillPresent = new LinkedHashSet<>(undergroundOres);
        removedStillPresent.retainAll(REMOVED_VANILLA_FEATURE_IDS);
        boolean modifierExists = DataPackRegistries.getBiomeModifier(CoreRef.MOD_ID, "remove_vanilla_ores").json().size() > 0;
        boolean netherModifierExists = DataPackRegistries.getBiomeModifier(CoreRef.MOD_ID, "remove_vanilla_nether_ores").json().size() > 0;
        Set<String> activeNoiseOverrides = new LinkedHashSet<>();
        for (String id : OVERWORLD_NOISE_SETTINGS_IDS) {
            String[] parts = id.split(":", 2);
            if (DataPackRegistries.getNoiseSettings(parts[0], parts[1]).json().size() > 0) {
                activeNoiseOverrides.add(id);
            }
        }
        boolean giantNoiseVeinOverrideActive = activeNoiseOverrides.contains(finalNoiseSettingsKey)
                && "false".equals(finalOreVeinsEnabled);

        source.sendSuccess(() -> Component.literal("dimension=" + level.dimension().location()), false);
        source.sendSuccess(() -> Component.literal("generatorClass=" + generatorClass), false);
        source.sendSuccess(() -> Component.literal("noiseSettingsKey=" + finalNoiseSettingsKey), false);
        source.sendSuccess(() -> Component.literal("oreVeinsEnabled=" + finalOreVeinsEnabled), false);
        source.sendSuccess(() -> Component.literal("giant_noise_vein_datapack_override_active=" + giantNoiseVeinOverrideActive), false);
        source.sendSuccess(() -> Component.literal("dynamicPackId=" + ResourcePackRegistry.PACK.packId()), false);
        source.sendSuccess(() -> Component.literal("remove_vanilla_ores_exists=" + modifierExists), false);
        source.sendSuccess(() -> Component.literal("overworld_noise_settings_overrides=" + activeNoiseOverrides), false);
        source.sendSuccess(() -> Component.literal("remove_vanilla_nether_ores_exists=" + netherModifierExists), false);
        source.sendSuccess(() -> Component.literal("underground_ores=" + undergroundOres), false);
        source.sendSuccess(() -> Component.literal("removed_features_still_present=" + removedStillPresent), false);
        return 1;
    }

    static List<String> undergroundOresFeatureIds(BiomeGenerationSettings generationSettings) {
        int stepIndex = GenerationStep.Decoration.UNDERGROUND_ORES.ordinal();

        if (generationSettings.features().size() <= stepIndex) {
            return List.of();
        }

        List<String> ids = new ArrayList<>();
        HolderSet<PlacedFeature> features = generationSettings.features().get(stepIndex);

        for (Holder<PlacedFeature> feature : features) {
            ids.add(feature.unwrapKey().map(ResourceKey::location).map(ResourceLocation::toString).orElse("unregistered"));
        }

        return List.copyOf(ids);
    }
}
