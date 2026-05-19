package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.properties.MaterialProperties;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.world.item.ItemStack;

public class OreItem extends MaterialItem{
    public OreItem(MaterialProperties properties) {
        super(properties.stacksTo(1));
    }

    public float getQuantityLevel(ItemStack itemStack) {
        Integer quantity = Quantity.getQuantity(itemStack);
        Integer maxQuantity = ((OreItem)itemStack.getItem()).getMaxQuantity();

        if (maxQuantity == null) return -1;
        if (quantity == null) return 1.0f;

        if(quantity <= (maxQuantity / 72)) return 0.0f;//div72

        if(quantity <= (maxQuantity / 9)) return 0.25f;//tiny

        if(quantity <= (maxQuantity / 4)) return 0.50f;//small

        return 1.0f;//full
    }
}