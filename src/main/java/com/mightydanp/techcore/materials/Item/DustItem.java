package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.properties.MaterialProperties;
import net.minecraft.world.item.ItemStack;


public class DustItem extends MaterialItem{
    public DustItem(MaterialProperties properties) {
        super(properties.stacksTo(1));
    }

    public float getFillLevel(ItemStack itemStack) {
        Integer quantity = ((DustItem)itemStack.getItem()).getQuantity(itemStack);
        Integer maxQuantity = ((DustItem)itemStack.getItem()).getMaxQuantity();

        if(maxQuantity == null || quantity == null) return -1;

        if(quantity <= (maxQuantity / 9)) return 0.0f;

        if(quantity <= (maxQuantity / 4)) return 0.5f;

        return 1.0f;
    }
}
