package com.mightydanp.techcore.materials.Item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.mightydanp.techcore.materials.properties.ExtendedProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by MightyDanp on 3/12/2021.
 */
public class OreProductsItem extends Item {
    public final String symbol;
    public final int color;
    public final double meltingPoint;
    public final double boilingPoint;


    public OreProductsItem(ExtendedProperties properties) {
        super(properties);
        symbol = properties.getSymbol();
        color = properties.getColor();
        meltingPoint = properties.getMeltingPoint();
        boilingPoint = properties.getBoilingPoint();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        if (symbol != null) {
            tooltip.add(Component.nullToEmpty(symbol));
        }

        if (meltingPoint != 0) {
            tooltip.add(Component.nullToEmpty("Melting Point of" + " §5" + meltingPoint));
        }
        if (boilingPoint != 0) {
            tooltip.add(Component.nullToEmpty("Boiling Point of" + " §5" + boilingPoint));
        }
    }
}
