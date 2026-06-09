package com.mightydanp.techcore.materials.item;

import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.world.item.properties.Quality;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GemItem extends MaterialItem {
    public GemItem(MaterialItemProperties properties) {
        super(properties);
    }

    public float getGemQuality(ItemStack itemStack) {
        Quality quality = Quality.stack(itemStack).get();
        int maxQuality = Quality.MAX;

        if (quality == null) return 0.6f;

        if (quality.quality() <= (maxQuality / 5)) return 0.2f; //chipped
        if (quality.quality() <= (maxQuality * 2 / 5)) return 0.4f; //flawed
        if (quality.quality() <= (maxQuality * 3 / 5)) return 0.6f; //gem
        if (quality.quality() <= (maxQuality * 4 / 5)) return 0.8f; //flawless

        return 1.0f; //legendaryGem
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        Component gemState = Component.empty();

        if (getGemQuality(itemStack) >= 0.2f) gemState = Component.translatable(MaterialRef.chipped_translatable);
        if (getGemQuality(itemStack) >= 0.4f) gemState = Component.translatable(MaterialRef.flawed_translatable);
        if (getGemQuality(itemStack) >= 0.6f) gemState = Component.translatable(MaterialRef.gem_translatable);
        if (getGemQuality(itemStack) >= 0.8f) gemState = Component.translatable(MaterialRef.flawless_translatable);
        if (getGemQuality(itemStack) >= 1.0f) gemState = Component.translatable(MaterialRef.legendary_translatable);


        tooltip.add(Component.translatable(MaterialRef.gem_state_translatable).append(" : ").append(gemState));

        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }
}
