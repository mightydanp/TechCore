package com.mightydanp.techcore.world.level.levelgen.vein;

public record OreVeinOverlapSettings(int mainBodyGapNumerator, int denominator) {
    public OreVeinOverlapSettings {
        if (denominator <= 0) throw new IllegalArgumentException("denominator must be positive");
        if (mainBodyGapNumerator < 0 || mainBodyGapNumerator > denominator) throw new IllegalArgumentException("mainBodyGapNumerator must be in [0, denominator]");
    }
}
