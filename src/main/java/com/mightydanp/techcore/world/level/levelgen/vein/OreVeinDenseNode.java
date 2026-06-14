package com.mightydanp.techcore.world.level.levelgen.vein;

public record OreVeinDenseNode(
        long nodeId,
        double localCenterX,
        double localCenterY,
        double localCenterZ,
        double radiusX,
        double radiusY,
        double radiusZ,
        int configuredPeakDensity
) {
    public OreVeinDenseNode {
        if (!Double.isFinite(localCenterX)
                || !Double.isFinite(localCenterY)
                || !Double.isFinite(localCenterZ)
                || !Double.isFinite(radiusX)
                || !Double.isFinite(radiusY)
                || !Double.isFinite(radiusZ)) {
            throw new IllegalArgumentException("node coordinates and radii must be finite");
        }

        if (radiusX <= 0.0D || radiusY <= 0.0D || radiusZ <= 0.0D) {
            throw new IllegalArgumentException("node radii must be positive");
        }

        if (configuredPeakDensity < 1) {
            throw new IllegalArgumentException("configuredPeakDensity must be positive");
        }
    }
}
