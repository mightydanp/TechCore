package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record OreVeinOreCellResult(
        long instanceId,
        ResourceLocation definitionId,
        Material selectedMaterial,
        int selectedDistributionWeight,
        OreVeinContribution shapeContribution,
        int candidateDensity,
        int finalDensity,
        OreVariant variant,
        long winningNodeId,
        double winningNodeInfluence
) {
    public OreVeinOreCellResult {
        Objects.requireNonNull(definitionId, "definitionId");
        Objects.requireNonNull(selectedMaterial, "selectedMaterial");
        Objects.requireNonNull(shapeContribution, "shapeContribution");
        Objects.requireNonNull(variant, "variant");

        if (selectedDistributionWeight <= 0) {
            throw new IllegalArgumentException("selectedDistributionWeight must be positive");
        }

        if (candidateDensity < 1) {
            throw new IllegalArgumentException("candidateDensity must be at least 1");
        }

        if (finalDensity < 0) {
            throw new IllegalArgumentException("finalDensity must be non-negative");
        }

        if (!Double.isFinite(winningNodeInfluence)) {
            throw new IllegalArgumentException("winningNodeInfluence must be finite");
        }
    }

    public enum OreVariant {
        HOST_ROCK,
        REGULAR_ORE,
        DENSE_ORE,
        SPARSE_ORE
    }
}
