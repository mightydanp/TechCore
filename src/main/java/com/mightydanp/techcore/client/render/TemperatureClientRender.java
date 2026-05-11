package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.common.tag.TechCoreItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperatureClientRender {
    private TemperatureClientRender() {}

    public static boolean shouldRender(ItemStack stack, ItemDisplayContext context) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.is(TechCoreItemTags.DISABLE_TEMPERATURE_RENDER)) {
            return false;
        }

        if (!hasTemperature(stack)) {
            return false;
        }

        return shouldRenderInContext(context);
    }

    private static boolean shouldRenderInContext(ItemDisplayContext context) {
        return switch (context) {
            case GUI -> true;
            case GROUND -> true;
            case FIXED -> true;
            case FIRST_PERSON_LEFT_HAND,
                 FIRST_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND,
                 THIRD_PERSON_RIGHT_HAND -> true;
            default -> true;
        };
    }

    public static boolean hasTemperature(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Temperature");
    }

    public static float getTemperature(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0.0F;
        }

        return stack.getTag().getFloat("Temperature");
    }

    public static float getStrength(ItemStack stack) {
        float temp = Math.abs(getTemperature(stack));

        // Change this to your real max temperature.
        float max = 100.0F;

        return Mth.clamp(temp / max, 0.0F, 1.0F);
    }

    public static int getColor(ItemStack stack) {
        float temp = Mth.clamp(getTemperature(stack), -100.0F, 100.0F);

        if (temp < 0.0F) {
            float amount = -temp / 100.0F;

            int r = (int) Mth.lerp(amount, 180, 80);
            int g = (int) Mth.lerp(amount, 220, 160);
            int b = 255;

            return (r << 16) | (g << 8) | b;
        }

        float amount = temp / 100.0F;

        int r = 255;
        int g = (int) Mth.lerp(amount, 100, 235);
        int b = (int) Mth.lerp(amount, 40, 160);

        return (r << 16) | (g << 8) | b;
    }
}