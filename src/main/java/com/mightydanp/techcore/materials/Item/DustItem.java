package com.mightydanp.techcore.materials.Item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DustItem extends OreProductsItem{
    public final int maxQuantity;

    public DustItem(ExtendedProperties properties) {
        super(properties.stacksTo(1));
        maxQuantity = properties.getMaxQuantity();
    }

    public int getQuantity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains("quantity")) {
            return tag.getInt("quantity");
        }
        return maxQuantity;
    }

    public void setQuantity(ItemStack itemStack, int quantity) {
        itemStack.getOrCreateTag().putInt("quantity", Mth.clamp(quantity, 0, maxQuantity));
    }

    public float getFillLevel(ItemStack itemStack) {
        int quantity = getQuantity(itemStack);

        if(quantity <= (maxQuantity / 9)) return 0.0f;

        if(quantity <= (maxQuantity / 4)) return 0.5f;

        return 1.0f;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return getQuantity(itemStack) < maxQuantity;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return Math.round(13.0f * getQuantity(itemStack) / maxQuantity);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return color;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        tooltip.add(Component.nullToEmpty("Amount left:" + " " + getQuantity(stack) + "/" + maxQuantity));
    }
}
