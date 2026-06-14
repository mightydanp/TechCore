package com.mightydanp.techcore.world.level.levelgen.vein;

public record OreVeinHaloSettings(
        double transitionWidthBlocks,
        double haloRadiusBlocks,
        int outerHaloFillNumerator
) {
    public OreVeinHaloSettings {
        if (!Double.isFinite(transitionWidthBlocks) || transitionWidthBlocks <= 0.0D) {
            throw new IllegalArgumentException("transitionWidthBlocks must be finite and positive");
        }

        if (!Double.isFinite(haloRadiusBlocks) || haloRadiusBlocks < transitionWidthBlocks) {
            throw new IllegalArgumentException("haloRadiusBlocks must be finite and at least transitionWidthBlocks");
        }

        if (outerHaloFillNumerator < 0) {
            throw new IllegalArgumentException("outerHaloFillNumerator must be non-negative");
        }
    }
}
