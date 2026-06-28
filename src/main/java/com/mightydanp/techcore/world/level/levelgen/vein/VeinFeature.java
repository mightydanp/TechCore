package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface VeinFeature {
    default int loadOrder() {
        return Integer.MAX_VALUE;
    }

    default void validateDefinition(OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, List<String> problems) {}

    default OreVeinInstanceDescriptor attachToDescriptor(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature) {
        return descriptor;
    }

    default int exteriorReachBlocks(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature) {
        return 0;
    }

    default OreVeinOreCellEvaluator.OreCellResult applyMainBody(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.OreCellResult currentResult) {
        return currentResult;
    }

    default OreVeinOreCellEvaluator.OreCellResult applyExterior(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, ConfiguredVeinFeature configuredFeature, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.OreCellResult currentResult) {
        return currentResult;
    }
}
