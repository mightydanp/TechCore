package com.mightydanp.techcore.materials.Item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by MightyDanp on 3/12/2021.
 */
public class OreProductsItem extends Item {
    public String symbol;
    public Integer meltingPoint;
    public Integer boilingPoint;

    public OreProductsItem(Properties properties, Integer boilingPointIn, Integer meltingPointIn, String symbol) {
        super(properties);
        meltingPoint = meltingPointIn;
        boilingPoint = boilingPointIn;
        this.symbol = symbol;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (symbol != null) {
            tooltip.add(Component.nullToEmpty(symbol));
        }

        if (meltingPoint != null) {
            tooltip.add(Component.nullToEmpty("Melting Point of" + " §5" + meltingPoint));
        }
        if (boilingPoint != null) {
            tooltip.add(Component.nullToEmpty("Boiling Point of" + " §5" + boilingPoint));
        }
    }
}
