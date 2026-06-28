package com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.VeinFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.ConfiguredVeinFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinDefinition;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinDefinitions;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SparseHaloVeinFeature implements VeinFeature {
    @Contract(value = " -> new", pure = true)
    public static @NotNull Config defaultConfig() {
        return new Config(24);
    }

    @Override
    public void validateDefinition(@NotNull OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, List<String> problems) {
        for (ResourceKey<Level> dimension : definition.dimensions())
            for (Material hostRockMaterial : RockLayerFeature.getAllowedMaterials(dimension)) {
                if (!isConfiguredHostRock(hostRockMaterial)) continue;

                for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
                    Material oreMaterial = entry.oreMaterial().get();
                    if (oreMaterial == null || oreMaterial.ore == null || !OreVeinDefinitions.isOreCompatibleWithHost(oreMaterial, hostRockMaterial)) continue;
                    if (!oreMaterial.ore.getSparseOreBlocks().containsKey(hostRockMaterial.name))
                        problems.add(problem(definition, "missing sparseOreBlocks mapping for " + oreMaterial.name + " in " + hostRockMaterial.name));
                }
            }
    }

    @Override
    public int exteriorReachBlocks(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull ConfiguredVeinFeature configuredFeature) {
        return configuredFeature.configuration(Config.class).reachBlocks();
    }

    @Override
    public OreVeinOreCellEvaluator.OreCellResult applyExterior(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.@NotNull OreCellResult currentResult) {
        if (currentResult.terminalDecision() || currentResult.finalDensity() > 0) return currentResult;
        return OreVeinSparseHaloEvaluator.evaluateHaloCell(descriptor, configuredFeature.configuration(Config.class), position, contribution, selectedOreEntry);
    }

    @Contract(pure = true)
    private static @NotNull String problem(@NotNull OreVeinDefinition definition, String reason) {
        return definition.id() + "\n  feature: sparse_halo\n  reason: " + reason;
    }

    private static boolean isConfiguredHostRock(Material material) {
        return material != null && material.rockLayer != null && material.rockLayer.isRockLayer && material.rockLayer.rockType != null;
    }

    public record Config(int reachBlocks) {
        public Config {
            if (reachBlocks < 0) throw new IllegalArgumentException("reachBlocks must be non-negative");
        }
    }
}
