package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.world.item.ItemStack;

public class OreItem extends MaterialItem {
    public OreItem(MaterialItemProperties properties) {
        super(properties.stacksTo(1));
    }

    public float getQuantityLevel(ItemStack itemStack) {
        Quantity quantity = Quantity.stack(itemStack).get();

        if (quantity == null) return 1.0f;

        int stackQuantity = quantity.quantity();
        int maxQuantity = quantity.maxQuantity();

        if (stackQuantity <= (maxQuantity / 72)) return 0.0f;//div72

        if (stackQuantity <= (maxQuantity / 9)) return 0.25f;//tiny

        if (stackQuantity <= (maxQuantity / 4)) return 0.50f;//small

        return 1.0f;//full
    }
}