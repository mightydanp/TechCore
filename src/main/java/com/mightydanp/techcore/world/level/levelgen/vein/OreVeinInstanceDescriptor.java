package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;

public record OreVeinInstanceDescriptor(
        long instanceId,
        long instanceSeed,
        long shapeSeed,
        ResourceLocation definitionId,
        BlockPos center,
        int sizeX,
        int sizeY,
        int sizeZ,
        double yaw,
        double pitch,
        double roll,
        int originRegionX,
        int originRegionZ,
        int originIndex,
        OreVeinBounds bounds,
        List<OreVeinDenseNode> denseNodes
) {
    public OreVeinInstanceDescriptor {
        Objects.requireNonNull(definitionId, "definitionId");
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(bounds, "bounds");
        denseNodes = List.copyOf(Objects.requireNonNull(denseNodes, "denseNodes"));

        if (sizeX < 6 || sizeY < 6 || sizeZ < 6) {
            throw new IllegalArgumentException("sizes must be at least 6");
        }

        if (!Double.isFinite(yaw) || !Double.isFinite(pitch) || !Double.isFinite(roll)) {
            throw new IllegalArgumentException("rotation must be finite");
        }
    }
}
