package com.mightydanp.techcore.world.level.levelgen.vein;

public record OreVeinDensitySettings(
        int regularFillNumerator,
        int maximumFillNumerator,
        int fillDenominator,
        long blocksPerDenseNode,
        int minNodeCount,
        int maxNodeCount,
        double minNodeRadiusX,
        double maxNodeRadiusX,
        double minNodeRadiusY,
        double maxNodeRadiusY,
        double minNodeRadiusZ,
        double maxNodeRadiusZ,
        int minPeakDensity,
        int maxPeakDensity
) {
    public OreVeinDensitySettings {
        if (fillDenominator <= 0) {
            throw new IllegalArgumentException("fillDenominator must be positive");
        }

        if (regularFillNumerator < 0 || regularFillNumerator > fillDenominator) {
            throw new IllegalArgumentException("regularFillNumerator must be in [0, fillDenominator]");
        }

        if (maximumFillNumerator < regularFillNumerator || maximumFillNumerator > fillDenominator) {
            throw new IllegalArgumentException("maximumFillNumerator must be in [regularFillNumerator, fillDenominator]");
        }

        if (blocksPerDenseNode <= 0L) {
            throw new IllegalArgumentException("blocksPerDenseNode must be positive");
        }

        if (minNodeCount < 0 || maxNodeCount < minNodeCount) {
            throw new IllegalArgumentException("node-count range is invalid");
        }

        validateRadiusRange(minNodeRadiusX, maxNodeRadiusX, "X");
        validateRadiusRange(minNodeRadiusY, maxNodeRadiusY, "Y");
        validateRadiusRange(minNodeRadiusZ, maxNodeRadiusZ, "Z");

        if (minPeakDensity < 1 || maxPeakDensity < minPeakDensity) {
            throw new IllegalArgumentException("peak-density range is invalid");
        }
    }

    private static void validateRadiusRange(double minRadius, double maxRadius, String axis) {
        if (!Double.isFinite(minRadius) || !Double.isFinite(maxRadius) || minRadius <= 0.0D || maxRadius < minRadius) {
            throw new IllegalArgumentException("node-radius " + axis + " range is invalid");
        }
    }
}
