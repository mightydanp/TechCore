package com.mightydanp.techcore.materials.item;

import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.Impurities;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quality;
import com.mightydanp.techcore.world.item.properties.Quantity;
import com.mightydanp.techcore.world.item.properties.Temperature;
import net.minecraft.network.chat.Component;
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

    public MaterialItem(MaterialItemProperties properties) {
        super(properties);
        this.symbol = properties.getSymbol();
        this.color = properties.getColor();
        this.boilingPoint = properties.getBoilingPoint();
        this.meltingPoint = properties.getMeltingPoint();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        Quantity quantity = Quantity.stack(itemStack).get();

        if (quantity != null) {
            return quantity.quantity() < quantity.maxQuantity();
        }

        return super.isBarVisible(itemStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        Quantity quantity = Quantity.stack(itemStack).get();

        if (quantity != null) {
            return Math.round(13 * quantity.level());
        }

        return super.getBarWidth(itemStack);
    }


    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        Quantity quantity = Quantity.stack(itemStack).get();

        if (quantity != null) {
            int stackQuantity = quantity.quantity();
            int maxQuantity = quantity.maxQuantity();
            int barColor = 0xFFFFFFFF;

            if (stackQuantity < maxQuantity / 2 && stackQuantity > maxQuantity / 4)
                barColor = 0xFFFFFF00;

            if (stackQuantity <= maxQuantity / 4)
                barColor = 0xFFFF0000;

            if (stackQuantity <= 1)
                barColor = 0xFF000000;

            return barColor;
        }

        return super.getBarColor(itemStack);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        Temperature temperature = Temperature.fromStack(itemStack);

        if (symbol != null) {
            tooltip.add(Component.nullToEmpty(symbol));
        }

        if (temperature != null) {
            tooltip.add(Component.translatable(MaterialRef.temperature_translatable).append(" : " + temperature.getTemperature(temperature.scale()) + " " + temperature.scale().label()));
        }

        Quantity quantity = Quantity.stack(itemStack).get();
        if (quantity != null) {
            tooltip.add(Component.translatable(MaterialRef.quantity_translatable).append(" : " + quantity.quantity() + "/" + quantity.maxQuantity()));
        }

        Quality quality = Quality.stack(itemStack).get();
        if (quality != null) {
            tooltip.add(Component.translatable(MaterialRef.quality_translatable).append(" : " + quality.quality()));
        }

        Purity purity = Purity.stack(itemStack).get();
        if (purity != null) {
            tooltip.add(Component.translatable(MaterialRef.purity_translatable).append(" : " + purity.purity()));
        }

        if (getMeltingPoint() != null) {
            tooltip.add(Component.translatable(MaterialRef.melting_point_translatable).append(" : §5" + meltingPoint));
        }

        if (getBoilingPoint() != null) {
            tooltip.add(Component.translatable(MaterialRef.boiling_point_translatable).append(" : §5" + boilingPoint));
        }

        Impurities impurities = Impurities.stack(itemStack).get();
        if(impurities != null){
            tooltip.add(Component.translatable(MaterialRef.impurities).append(" :"));
            impurities.impurities().forEach((resourceLocation, percentage) -> tooltip.add(Component.nullToEmpty(resourceLocation.toString() + " : " + percentage + " %")));
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


}
