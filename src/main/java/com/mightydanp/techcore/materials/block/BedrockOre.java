package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;

public class BedrockOre extends MaterialBlock {
    private final Material oreMaterial;

    public BedrockOre(MaterialBlockProperties properties, Material oreMaterial) {
        super(properties);
        this.oreMaterial = oreMaterial;
    }

    public Material getOreMaterial() {
        return oreMaterial;
    }
}
