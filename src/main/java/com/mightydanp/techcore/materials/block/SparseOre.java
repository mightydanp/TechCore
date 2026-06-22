package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SparseOre extends OreBlock {
    public SparseOre(MaterialBlockProperties properties, Material oreMaterial, Material hostMaterial, Supplier<Item> rawOreItem) {
        super(properties, oreMaterial, hostMaterial, rawOreItem);
    }

    @Override
    protected double baseRockMultiplier() {
        return 1.5D;
    }
}
