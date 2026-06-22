package com.mightydanp.techcore.api.resources.data.worldgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class NoiseSettingsContent {
    private final String modid;
    private final String name;

    private @Nullable NoiseGeneratorSettings settings;

    public NoiseSettingsContent(String modid, String name) {
        this.modid = Objects.requireNonNull(modid, "modid");
        this.name = Objects.requireNonNull(name, "name");
    }

    public NoiseSettingsContent(@NotNull ResourceLocation resourceLocation) {
        this(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public ResourceLocation id() {
        return ResourceLocation.fromNamespaceAndPath(
                modid,
                name
        );
    }

    public NoiseSettingsContent settings(NoiseGeneratorSettings settings) {
        this.settings = Objects.requireNonNull(
                settings,
                "settings"
        );

        return this;
    }

    public JsonObject json() {
        if (settings == null) {
            throw new IllegalStateException(
                    "Noise generator settings have not been set: " + id()
            );
        }

        RegistryOps<JsonElement> registryOps = RegistryOps.create(
                JsonOps.INSTANCE,
                VanillaLookups.PROVIDER
        );

        return NoiseGeneratorSettings.DIRECT_CODEC
                .encodeStart(registryOps, settings)
                .getOrThrow(false, error -> {
                    throw new IllegalStateException(
                            "Failed to encode noise generator settings '" +
                                    id() + "': " +
                                    error
                    );
                })
                .getAsJsonObject();
    }

    private static final class VanillaLookups {
        private static final HolderLookup.Provider PROVIDER =
                VanillaRegistries.createLookup();
    }

    public NoiseSettingsContent vanillaOverworld(boolean largeBiomes, boolean amplified, boolean oreVeinsEnabled) {
        HolderLookup.Provider provider = VanillaLookups.PROVIDER;

        HolderGetter<DensityFunction> densityFunctions = provider.lookupOrThrow(
                        Registries.DENSITY_FUNCTION
        );

        HolderGetter<NormalNoise.NoiseParameters> noises = provider.lookupOrThrow(
                        Registries.NOISE
        );

        return settings(new NoiseGeneratorSettings(
                        NoiseSettings.create(
                                -64,
                                384,
                                1,
                                2
                        ),
                        Blocks.STONE.defaultBlockState(),
                        Blocks.WATER.defaultBlockState(),
                        NoiseRouterDataAccess.createOverworld(
                                densityFunctions,
                                noises,
                                largeBiomes,
                                amplified
                        ),
                        SurfaceRuleData.overworld(),
                        new OverworldBiomeBuilder()
                                .spawnTarget(),
                        63,
                        false,
                        true,
                        oreVeinsEnabled,
                        false
                )
        );
    }

    private static final class NoiseRouterDataAccess extends NoiseRouterData {
        private static NoiseRouter createOverworld(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noises, boolean largeBiomes, boolean amplified) {
            return overworld(
                    densityFunctions,
                    noises,
                    largeBiomes,
                    amplified
            );
        }
    }
}