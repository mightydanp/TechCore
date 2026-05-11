package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.common.tag.TechCoreItemTags;
import com.mightydanp.techcore.materials.properties.Temperature;
import net.minecraft.nbt.CompoundTag;
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
            default -> false;
        };
    }

    public static boolean hasTemperature(ItemStack stack) {
        return Temperature.hasTemperature(stack);
    }

    public static double getTemperature(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag == null) return Temperature.celsiusDefaultRoomTemperature;
        if (!Temperature.hasTemperature(stack)) return Temperature.celsiusDefaultRoomTemperature;

        return getTemperature(stack);
    }

    public static float getStrength(ItemStack stack) {
        double temp = Math.abs(getTemperature(stack));

        // Use your real max visual temperature here.
        double max = 100.0D;

        return (float) Mth.clamp(temp / max, 0.0D, 1.0D);
    }

    public static int getColor(ItemStack stack) {
        double temp = Mth.clamp(getTemperature(stack), -100.0D, 100.0D);

        if (temp < 0.0D) {
            double amount = -temp / 100.0D;

            int r = (int) Mth.lerp(amount, 180.0D, 80.0D);
            int g = (int) Mth.lerp(amount, 220.0D, 160.0D);
            int b = 255;

            return (r << 16) | (g << 8) | b;
        }

        double amount = temp / 100.0D;

        int r = 255;
        int g = (int) Mth.lerp(amount, 100.0D, 235.0D);
        int b = (int) Mth.lerp(amount, 40.0D, 160.0D);

        return (r << 16) | (g << 8) | b;
    }
}