package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.components.StoneLayerComponent;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


public class DustItem extends MaterialItem {
    public DustItem(MaterialItemProperties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        Component component = super.getName(itemStack);
        Purity purity = Purity.stack(itemStack).get();

        if (purity == null || purity.purity() < Purity.MAX) return component;

        var itemKey = itemStack.getItemHolder()
                .unwrapKey()
                .orElseThrow()
                .location();
        String path = itemKey.getPath();

        return StoneLayerComponent.getStoneLayerMaterials().stream()
                .map(material -> material.name + "_")
                .filter(path::startsWith)
                .max(Comparator.comparingInt(String::length))
                .<Component>map(prefix -> {
                    String strippedPath = path.substring(prefix.length());
                    if ("dust".equals(strippedPath)) return component;

                    return Component.translatable("item." + itemKey.getNamespace() + "." + strippedPath);
                })
                .orElse(component);
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

        if (purity.purity() < 75) return 0.0f;//impure

        if (purity.purity() < 100) return 0.75f;//normal

        return 1.0f;//pure
    }


}