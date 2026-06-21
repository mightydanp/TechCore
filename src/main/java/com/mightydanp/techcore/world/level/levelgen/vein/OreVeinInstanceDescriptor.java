package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record OreVeinInstanceDescriptor(long instanceId, long instanceSeed, long shapeSeed,
                                        ResourceLocation definitionId, BlockPos center, int sizeX, int sizeY, int sizeZ,
                                        double yaw, double pitch, double roll, int originRegionX, int originRegionZ,
                                        int originIndex, OreVeinBounds bounds, List<DenseNode> denseNodes) {
    public OreVeinInstanceDescriptor {
        Objects.requireNonNull(definitionId, "definitionId");
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(bounds, "bounds");
        denseNodes = List.copyOf(Objects.requireNonNull(denseNodes, "denseNodes"));

        if (sizeX < 6 || sizeY < 6 || sizeZ < 6) throw new IllegalArgumentException("sizes must be at least 6");

        if (!Double.isFinite(yaw) || !Double.isFinite(pitch) || !Double.isFinite(roll))
            throw new IllegalArgumentException("rotation must be finite");
    }

    @Contract("_ -> new")
    public @NotNull OreVeinInstanceDescriptor withDenseNodes(List<DenseNode> denseNodes) {
        return new OreVeinInstanceDescriptor(
                instanceId,
                instanceSeed,
                shapeSeed,
                definitionId,
                center,
                sizeX,
                sizeY,
                sizeZ,
                yaw,
                pitch,
                roll,
                originRegionX,
                originRegionZ,
                originIndex,
                bounds,
                denseNodes
        );
    }

    public record DenseNode(long nodeId, double localCenterX, double localCenterY, double localCenterZ, double radiusX,
                            double radiusY, double radiusZ, int configuredPeakDensity) {
        public DenseNode {
            if (!Double.isFinite(localCenterX) || !Double.isFinite(localCenterY) || !Double.isFinite(localCenterZ) || !Double.isFinite(radiusX) || !Double.isFinite(radiusY) || !Double.isFinite(radiusZ))
                throw new IllegalArgumentException("node coordinates and radii must be finite");

            if (radiusX <= 0.0D || radiusY <= 0.0D || radiusZ <= 0.0D)
                throw new IllegalArgumentException("node radii must be positive");

            if (configuredPeakDensity < 1) throw new IllegalArgumentException("configuredPeakDensity must be positive");
        }
    }
}
