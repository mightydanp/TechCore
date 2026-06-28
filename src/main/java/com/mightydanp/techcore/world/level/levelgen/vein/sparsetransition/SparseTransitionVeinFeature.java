package com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition;

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

public final class SparseTransitionVeinFeature implements VeinFeature {
    @Contract(" -> new")
    public static @NotNull Config defaultConfig() {
        return new Config(4.0D);
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
        return (int) Math.ceil(configuredFeature.configuration(Config.class).transitionWidthBlocks() * 0.5D);
    }

    @Override
    public OreVeinOreCellEvaluator.@NotNull OreCellResult applyMainBody(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.@NotNull OreCellResult currentResult) {
        if (currentResult.variant() != OreVeinOreCellEvaluator.OreCellResult.OreVariant.REGULAR_ORE) return currentResult;

        Config config = configuredFeature.configuration(Config.class);
        if (!OreVeinSparseTransitionEvaluator.isInsideTransitionHalf(contribution.signedBoundaryDistanceBlocks(), config.transitionWidthBlocks())) return currentResult;

        return OreVeinSparseTransitionEvaluator.insideTransitionSelectsSparseOre(descriptor, position, contribution.signedBoundaryDistanceBlocks(), config.transitionWidthBlocks())
                ? sparseOreResult(descriptor, selectedOreEntry, contribution)
                : currentResult;
    }

    @Override
    public OreVeinOreCellEvaluator.OreCellResult applyExterior(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.@NotNull ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.OreCellResult currentResult) {
        Config config = configuredFeature.configuration(Config.class);
        double halfTransitionWidth = config.transitionWidthBlocks() * 0.5D;

        if (contribution.signedBoundaryDistanceBlocks() > halfTransitionWidth) return currentResult;

        int threshold = OreVeinSparseTransitionEvaluator.transitionShellThreshold(contribution.signedBoundaryDistanceBlocks(), config.transitionWidthBlocks());
        int roll = OreVeinOreCellEvaluator.occupancyRoll(descriptor, position, OreVeinSparseTransitionEvaluator.transitionVariantSalt(), OreVeinSparseTransitionEvaluator.sparseOccupancyDenominator());

        return roll < threshold
                ? sparseExteriorResult(descriptor, selectedOreEntry, contribution)
                : OreVeinOreCellEvaluator.terminalHostRockResult(descriptor, selectedOreEntry, contribution);
    }

    @Contract("_, _, _ -> new")
    private static OreVeinOreCellEvaluator.@NotNull OreCellResult sparseOreResult(OreVeinInstanceDescriptor descriptor, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinShapeEvaluator.ShapeContribution contribution) {
        return OreVeinOreCellEvaluator.createResult(
                descriptor,
                selectedOreEntry,
                contribution,
                1,
                1,
                OreVeinOreCellEvaluator.OreCellResult.OreVariant.FEATURE_ORE,
                OreVeinSparseTransitionEvaluator.sparseTransitionOreReplacement()
        );
    }

    @Contract("_, _, _ -> new")
    private static OreVeinOreCellEvaluator.@NotNull OreCellResult sparseExteriorResult(OreVeinInstanceDescriptor descriptor, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinShapeEvaluator.ShapeContribution contribution) {
        return OreVeinOreCellEvaluator.createResult(
                descriptor,
                selectedOreEntry,
                contribution,
                1,
                1,
                OreVeinOreCellEvaluator.OreCellResult.OreVariant.FEATURE_ORE,
                OreVeinSparseTransitionEvaluator.sparseTransitionOreReplacement(),
                true
        );
    }

    @Contract(pure = true)
    private static @NotNull String problem(@NotNull OreVeinDefinition definition, String reason) {
        return definition.id() + "\n  feature: sparse_transition\n  reason: " + reason;
    }

    private static boolean isConfiguredHostRock(Material material) {
        return material != null && material.rockLayer != null && material.rockLayer.isRockLayer && material.rockLayer.rockType != null;
    }

    public record Config(double transitionWidthBlocks) {
        public Config {
            if (!Double.isFinite(transitionWidthBlocks) || transitionWidthBlocks < 0.0D) throw new IllegalArgumentException("transitionWidthBlocks must be finite and non-negative");
        }
    }
}
