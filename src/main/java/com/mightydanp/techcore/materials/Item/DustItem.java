package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.properties.MaterialProperties;
import net.minecraft.world.item.ItemStack;


public class DustItem extends MaterialItem{
    public DustItem(MaterialProperties properties) {
        super(properties.stacksTo(1));
    }

    public float getQuantityLevel(ItemStack itemStack) {
        Integer quantity = ((DustItem)itemStack.getItem()).getQuantity(itemStack);
        Integer maxQuantity = ((DustItem)itemStack.getItem()).getMaxQuantity();

        if(maxQuantity == null || quantity == null) return -1;

        if(quantity <= (maxQuantity / 72)) return 0.0f;//div72

        if(quantity <= (maxQuantity / 9)) return 0.25f;//tiny

        if(quantity <= (maxQuantity / 4)) return 0.50f;//small

        return 1.0f;//full
    }

    public float getPurityLevel(ItemStack itemStack) {
        Double purity = ((DustItem)itemStack.getItem()).getPurity(itemStack);
        Double maxPurity = ((DustItem)itemStack.getItem()).getMaxPurity();

        if(maxPurity == null || purity == null) return -1;

        if(purity < 75) return 0.0f;//impure

        if(purity < 100) return 0.75f;//normal

        return 1.0f;//pure
    }
}