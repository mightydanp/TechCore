package com.mightydanp.techcore.world.level.levelgen.vein;

public record OreVeinHaloSettings(double transitionWidthBlocks) {
    public OreVeinHaloSettings {
        if (!Double.isFinite(transitionWidthBlocks) || transitionWidthBlocks < 0.0D) throw new IllegalArgumentException("transitionWidthBlocks must be finite and non-negative");
    }
}
