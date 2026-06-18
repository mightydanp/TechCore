package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Objects;

public record OreVeinDimensionGenerationSettings(ResourceKey<Level> dimension, int originWeightBudget) {
    public OreVeinDimensionGenerationSettings {
        Objects.requireNonNull(dimension, "dimension");

        if (originWeightBudget <= 0) throw new IllegalArgumentException("originWeightBudget must be positive");
    }
}
