package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.properties.MaterialProperties;
import net.minecraft.world.item.ItemStack;


public class DustItem extends MaterialItem{

    public DustItem(MaterialProperties properties) {
        super(properties.stacksTo(1));
    }

    public float getFillLevel(ItemStack itemStack) {
        int quantity = getQuantity(itemStack);

        if(quantity <= (this.getMaxQuantity() / 9)) return 0.0f;

        if(quantity <= (this.getMaxQuantity() / 4)) return 0.5f;

        return 1.0f;
    }
}
