package com.mightydanp.techcore.world.level.levelgen.vein.densenode;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
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

public final class DenseNodeVeinFeature implements VeinFeature {
    @Contract(" -> new")
    public static @NotNull Config defaultConfig() {
        return new Config(8192L, 0, 5, 4.0D, 8.0D, 3.0D, 3.5D, 4.0D, 8.0D, 2, 4, 960);
    }

    @Override
    public void validateDefinition(@NotNull OreVeinDefinition definition, @NotNull ConfiguredVeinFeature configuredFeature, List<String> problems) {
        Config config = configuredFeature.configuration(Config.class);

        if (config.maxNodeRadiusX() > definition.minSizeX() / 2.0D) problems.add(problem(definition, "maxNodeRadiusX exceeds minSizeX / 2.0"));
        if (config.maxNodeRadiusY() > definition.minSizeY() / 2.0D) problems.add(problem(definition, "maxNodeRadiusY exceeds minSizeY / 2.0"));
        if (config.maxNodeRadiusZ() > definition.minSizeZ() / 2.0D) problems.add(problem(definition, "maxNodeRadiusZ exceeds minSizeZ / 2.0"));
        if (config.maxFillNumerator() > OreVeinOreCellEvaluator.baseMainBodyFillDenominator())
            problems.add(problem(definition, "maxFillNumerator exceeds base main-body fill denominator"));

        for (ResourceKey<Level> dimension : definition.dimensions())
            for (Material hostRockMaterial : RockLayerFeature.getAllowedMaterials(dimension)) {
                if (!isConfiguredHostRock(hostRockMaterial)) continue;

                for (OreVeinDefinition.OreEntry entry : definition.oreEntries()) {
                    Material oreMaterial = entry.oreMaterial().get();
                    if (oreMaterial == null || oreMaterial.ore == null || !OreVeinDefinitions.isOreCompatibleWithHost(oreMaterial, hostRockMaterial)) continue;
                    if (maxReachableDensity(config, oreMaterial) < 2) continue;
                    if (!oreMaterial.ore.getDenseOreBlocks().containsKey(hostRockMaterial.name))
                        problems.add(problem(definition, "missing denseOreBlocks mapping for " + oreMaterial.name + " in " + hostRockMaterial.name));
                }
            }
    }

    @Override
    public @NotNull OreVeinInstanceDescriptor attachToDescriptor(@NotNull OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull ConfiguredVeinFeature configuredFeature) {
        Config config = configuredFeature.configuration(Config.class);
        return descriptor.withFeatureState(configuredFeature.featureId(), new State(OreVeinDenseNodeLayout.generate(descriptor, config)));
    }

    @Override
    public OreVeinOreCellEvaluator.OreCellResult applyMainBody(@NotNull OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.OreCellResult currentResult) {
        State state = descriptor.featureState(configuredFeature.featureId(), State.class);
        if (state == null || state.nodes().isEmpty()) return currentResult;

        return OreVeinDenseNodeEvaluator.applyMainBodyCell(descriptor, position, configuredFeature.configuration(Config.class), state, contribution, selectedOreEntry, currentResult);
    }

    private static int maxReachableDensity(@NotNull Config config, @NotNull Material oreMaterial) {
        return Math.min(config.maxPeakDensity(), Math.min(DenseOre.MAX_DENSITY_PROPERTY, oreMaterial.ore.getMaxDensity()));
    }

    private static boolean isConfiguredHostRock(Material material) {
        return material != null && material.rockLayer != null && material.rockLayer.isRockLayer && material.rockLayer.rockType != null;
    }

    @Contract(pure = true)
    private static @NotNull String problem(@NotNull OreVeinDefinition definition, String reason) {
        return definition.id() + "\n  feature: dense_node\n  reason: " + reason;
    }

    public record Config(long blocksPerDenseNode, int minNodeCount, int maxNodeCount, double minNodeRadiusX, double maxNodeRadiusX, double minNodeRadiusY, double maxNodeRadiusY, double minNodeRadiusZ, double maxNodeRadiusZ, int minPeakDensity, int maxPeakDensity, int maxFillNumerator) {
        public Config {
            if (blocksPerDenseNode <= 0L) throw new IllegalArgumentException("blocksPerDenseNode must be positive");
            if (minNodeCount < 0 || maxNodeCount < minNodeCount) throw new IllegalArgumentException("node-count range is invalid");
            validateRadiusRange(minNodeRadiusX, maxNodeRadiusX, "X");
            validateRadiusRange(minNodeRadiusY, maxNodeRadiusY, "Y");
            validateRadiusRange(minNodeRadiusZ, maxNodeRadiusZ, "Z");
            if (minPeakDensity < 1 || maxPeakDensity < minPeakDensity) throw new IllegalArgumentException("peak-density range is invalid");
            if (maxFillNumerator < 0) throw new IllegalArgumentException("maxFillNumerator must be non-negative");
        }

        private static void validateRadiusRange(double minRadius, double maxRadius, String axis) {
            if (!Double.isFinite(minRadius) || !Double.isFinite(maxRadius) || minRadius <= 0.0D || maxRadius < minRadius) throw new IllegalArgumentException("node-radius " + axis + " range is invalid");
        }
    }

    public record State(List<Node> nodes) {
        public State {
            nodes = List.copyOf(nodes);
        }
    }

    public record Node(long nodeId, double localCenterX, double localCenterY, double localCenterZ, double radiusX, double radiusY, double radiusZ, int configuredPeakDensity) {
        public Node {
            if (!Double.isFinite(localCenterX) || !Double.isFinite(localCenterY) || !Double.isFinite(localCenterZ) || !Double.isFinite(radiusX) || !Double.isFinite(radiusY) || !Double.isFinite(radiusZ))
                throw new IllegalArgumentException("node coordinates and radii must be finite");
            if (radiusX <= 0.0D || radiusY <= 0.0D || radiusZ <= 0.0D) throw new IllegalArgumentException("node radii must be positive");
            if (configuredPeakDensity < 1) throw new IllegalArgumentException("configuredPeakDensity must be positive");
        }
    }
}
