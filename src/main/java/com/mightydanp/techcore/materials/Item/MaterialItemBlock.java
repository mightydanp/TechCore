package com.mightydanp.techcore.materials.Item;

import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MaterialItemBlock extends BlockItem {
    private final String symbol;
    private final int color;
    private final Double boilingPoint;
    private final Double meltingPoint;
    private final Integer defaultQuantity;
    private final Integer defaultQuality;
    private final Double defaultPurity;
    private final Integer maxQuantity;
    private final Integer maxQuality;
    private final Double maxPurity;

    public MaterialItemBlock(Block block, MaterialItemProperties properties) {
        super(block, properties);
        this.symbol = properties.getSymbol();
        this.color = properties.getColor();
        this.boilingPoint = properties.getBoilingPoint();
        this.meltingPoint = properties.getMeltingPoint();
        this.defaultQuantity = properties.getDefaultQuantity();
        this.defaultQuality = properties.getDefaultQuality();
        this.defaultPurity = properties.getDefaultPurity();
        this.maxQuantity = properties.getMaxQuantity();
        this.maxQuality = properties.getMaxQuality();
        this.maxPurity = properties.getMaxPurity();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        Integer maxQuantity = this.getMaxQuantity();

        if(maxQuantity != null) {
            Integer fallbackQuantity = defaultQuantity == null ? maxQuantity : defaultQuantity;
            int quantity = java.util.Objects.requireNonNull(Quantity.getQuantityOrDefault(itemStack, fallbackQuantity));

            return Quantity.hasQuantity(itemStack) && quantity < maxQuantity;
        }

        return super.isBarVisible(itemStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        Integer maxQuantity = this.getMaxQuantity();

        if(maxQuantity != null) {
            Integer fallbackQuantity = defaultQuantity == null ? maxQuantity : defaultQuantity;
            int quantity = java.util.Objects.requireNonNull(
                    Quantity.getQuantityOrDefault(itemStack, fallbackQuantity)
            );

            return Math.round(13.0f * quantity / maxQuantity);
        }

        return super.getBarWidth(itemStack);
    }


    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        Integer maxQuantity = this.getMaxQuantity();

        if(maxQuantity != null) {
            Integer fallbackQuantity = defaultQuantity == null ? maxQuantity : defaultQuantity;
            int quantity = java.util.Objects.requireNonNull(
                    Quantity.getQuantityOrDefault(itemStack, fallbackQuantity)
            );

            int barColor = 0xFFFFFFFF;

            if (quantity < maxQuantity / 2 && quantity > maxQuantity / 4)
                barColor = 0xFFFFFF00;

            if (quantity <= maxQuantity / 4)
                barColor = 0xFFFF0000;

            if (quantity <= 1)
                barColor = 0xFF000000;

            return barColor;
        }

        return super.getBarColor(itemStack);
    }



    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        Temperature temperature = Temperature.fromStack(itemStack);

        if(ProcessedStage.hasProcessedStage(itemStack)) {
            ProcessedStage processedStage = ProcessedStage.fromStack(itemStack);
            if(processedStage.processedStage() != ProcessedStage.ProcessedStages.fromStage("none")) {
                tooltip.add(Component.translatable(MaterialRef.processed_stage_translatable).append(" : " + processedStage.processedStage().getStage()));
            }
        }

        if (symbol != null) {
            tooltip.add(Component.nullToEmpty(symbol));
        }

        if(temperature != null) {
            tooltip.add(Component.translatable(MaterialRef.temperature_translatable).append(" : " + temperature.getTemperature(temperature.scale()) + " " + temperature.scale().label()));
        }

        Integer quantity = Quantity.getQuantityOrDefault(itemStack, defaultQuantity == null ? maxQuantity : defaultQuantity);
        if(quantity != null) {
            tooltip.add(Component.translatable(MaterialRef.quantity_translatable).append(" : " + quantity + "/" + this.getMaxQuantity()));
        }

        Integer quality = Quality.getQualityOrDefault(itemStack, defaultQuality);
        if(quality != null) {
            tooltip.add(Component.translatable(MaterialRef.quality_translatable).append(" : " + quality));
        }

        Double purity = Purity.getPurityOrDefault(itemStack, defaultPurity);
        if(purity != null) {
            tooltip.add(Component.translatable(MaterialRef.purity_translatable).append(" : "+ Purity.getPurityOrDefault(itemStack, purity)));
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
}
