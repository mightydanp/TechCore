package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record OreVeinContribution(long instanceId, ResourceLocation definitionId, double localX, double localY, double localZ, double normalizedRadius, double distortionBlocks, double signedBoundaryDistanceBlocks, ContributionState state) {
    public OreVeinContribution {
        Objects.requireNonNull(definitionId, "definitionId");
        Objects.requireNonNull(state, "state");

        if (!Double.isFinite(localX) || !Double.isFinite(localY) || !Double.isFinite(localZ) || !Double.isFinite(normalizedRadius) || !Double.isFinite(distortionBlocks) || !Double.isFinite(signedBoundaryDistanceBlocks))
            throw new IllegalArgumentException("contribution values must be finite");
    }

    public enum ContributionState {
        OUTSIDE,
        INSIDE_MAIN_BODY
    }
}
