package com.mightydanp.techcore.client.render;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperatureClientRender {
    private TemperatureClientRender() {}

    public static boolean shouldRender(ItemStack stack, ItemDisplayContext context) {
        return !stack.isEmpty();
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
            default -> false;
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
        return 1.0F;
    }

    public static int getColor(ItemStack stack) {
        return 0xFF5500;
    }
}