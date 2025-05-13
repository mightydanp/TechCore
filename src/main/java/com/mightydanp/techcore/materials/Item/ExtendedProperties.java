package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.component.TCDataComponents;
import net.minecraft.world.item.Item;

public class ExtendedProperties extends Item.Properties {
    public ExtendedProperties temperature(int meltingPoint, int boilingPoint) {
        this.component(TCDataComponents.MELTING_POINT, meltingPoint);
        this.component(TCDataComponents.BOILING_POINT, boilingPoint);
        return this;
    }


}