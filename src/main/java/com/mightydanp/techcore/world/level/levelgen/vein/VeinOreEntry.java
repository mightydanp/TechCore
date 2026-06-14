package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;

import java.util.Objects;
import java.util.function.Supplier;

public record VeinOreEntry(
        Supplier<Material> oreMaterial,
        int distributionWeight
) {
    public VeinOreEntry {
        Objects.requireNonNull(oreMaterial, "oreMaterial");

        if (distributionWeight <= 0) {
            throw new IllegalArgumentException(
                    "distributionWeight must be positive"
            );
        }
    }
}
