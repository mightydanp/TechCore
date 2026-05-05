package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.MaterialProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MaterialItem extends Item {
    private final String symbol;
    private final int color;
    private final Double boilingPoint;
    private final Double meltingPoint;
    private final Integer maxQuantity;
    private final Integer maxQuality;
    private final Double maxPurity;

    public MaterialItem(MaterialProperties properties) {
        super(properties);
        this.symbol = properties.getSymbol();
        this.color = properties.getColor();
        this.boilingPoint = properties.getBoilingPoint();
        this.meltingPoint = properties.getMeltingPoint();
        this.maxQuantity = properties.getMaxQuantity();
        this.maxQuality = properties.getMaxQuality();
        this.maxPurity = properties.getMaxPurity();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        Integer maxQuantity = getMaxQuantity();
        if(maxQuantity != null){
            return hasQuantity(itemStack) && getQuantity(itemStack) < maxQuantity;
        }

        return super.isBarVisible(itemStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        Integer maxQuantity = getMaxQuantity();

        if (maxQuantity == null) return super.getBarWidth(itemStack);

        return Math.round(13.0f * getQuantity(itemStack) / this.getMaxQuantity());
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        Integer maxQuantity = getMaxQuantity();

        if (getMaxQuantity() == null) return super.getBarColor(itemStack);

        if(hasQuantity(itemStack)) {
            int barColor = 0xFFFFFFFF;
            if (getQuantity(itemStack) < this.getMaxQuantity() / 2 && getQuantity(itemStack) > this.getMaxQuantity() / 4)
                barColor = 0xFFFFFF00;
            if (getQuantity(itemStack) <= this.getMaxQuantity() / 4) barColor = 0xFFFF0000;
            if (getQuantity(itemStack) <= 1) barColor = 0xFF000000;

            return barColor;
        }

        return super.getBarColor(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        if (symbol != null) {
            tooltip.add(Component.nullToEmpty(symbol));
        }

        if(hasQuantity(itemStack)) {
            tooltip.add(Component.translatable(MaterialRef.quantity_left_translatable).append(" : " + getQuantity(itemStack) + "/" + this.getMaxQuantity()));
        }

        if(hasQuality(itemStack)) {
            tooltip.add(Component.translatable(MaterialRef.quality_translatable).append(" : " + getQuality(itemStack)));
        }

        if(hasPurity(itemStack)) {
            tooltip.add(Component.translatable(MaterialRef.purity_translatable).append(" : "+ getPurity(itemStack)));
        }

        if (getMeltingPoint() != null) {
            tooltip.add(Component.translatable(MaterialRef.melting_point_translatable).append(" : §5" + meltingPoint));
        }

        if (getBoilingPoint() != null) {
            tooltip.add(Component.translatable(MaterialRef.boiling_point_translatable).append("  : §5" + boilingPoint));
        }

        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    public String getSymbol() {
        return symbol;
    }

    public int getColor() {
        return color;
    }

    public Double getBoilingPoint() {
        return boilingPoint;
    }

    public Double getMeltingPoint() {
        return meltingPoint;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public Integer getMaxQuality() {
        return maxQuality;
    }

    public Double getMaxPurity() {
        return maxPurity;

    }

    public Integer getQuantity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("quantity")) {
            return tag.getInt("quantity");
        } else {
            setQuantity(itemStack, this.getMaxQuantity());
            return this.getMaxQuantity();
        }
    }

    public Integer getQuality(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains("quality")) {
            return tag.getInt("quality");
        } else {
            setQuality(itemStack, this.getMaxQuality());
            return this.getMaxQuality();
        }
    }

    public Double getPurity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains("purity")) {
            return tag.getDouble("purity");
        } else {
            setPurity(itemStack, this.getMaxPurity());
            return this.getMaxPurity();
        }

    }

    public void setQuantity(ItemStack itemStack, int value) {
        itemStack.getOrCreateTag().putInt("quantity", this.getMaxQuantity() != null ? Mth.clamp(value, 0, this.getMaxQuantity()) : value);
    }

    public void setQuality(ItemStack itemStack, int value) {
        Integer maxQuality = getMaxQuality();
        itemStack.getOrCreateTag().putInt("quality", maxQuality != null ? Mth.clamp(value, 0, maxQuality) : value);
    }

    public void setPurity(ItemStack itemStack, double value) {
        itemStack.getOrCreateTag().putDouble("purity", this.getMaxPurity() != null ? Mth.clamp(value, 0, this.getMaxPurity()) : value);
    }

    public boolean hasQuantity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("quantity");
    }

    public boolean hasQuality(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("quality");
    }

    public boolean hasPurity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("purity");
    }



}
