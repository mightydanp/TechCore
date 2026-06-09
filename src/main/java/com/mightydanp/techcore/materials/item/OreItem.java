package com.mightydanp.techcore.materials.item;

import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OreItem extends MaterialItem {
    public OreItem(@NotNull MaterialItemProperties properties) {
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

    public float getPurityLevel(ItemStack itemStack) {
        Purity purity = Purity.stack(itemStack).get();

        if (purity == null) return 0.75f;

        if (purity.purity() < 100) return 0.75f;//normal

        return 1.0f;//pure
    }
}