package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.common.tag.TechCoreItemTags;
import com.mightydanp.techcore.materials.properties.Temperature;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperatureClientRender {
    private TemperatureClientRender() {}

    public static boolean shouldRender(ItemStack stack, ItemDisplayContext context) {
        if (stack.isEmpty()) return false;

        if (stack.is(TechCoreItemTags.DISABLE_TEMPERATURE_RENDER)) return false;

        if(stack.getTag() == null) return false;

        if (!Temperature.hasTemperature(stack)) return false;

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

    public static double getTemperature(ItemStack stack) {
        if (!Temperature.hasTemperature(stack)) {
            return Temperature.celsiusDefaultRoomTemperature;
        }

        Double temperature = Temperature.getTemperature(stack);
        return temperature != null ? temperature : Temperature.celsiusDefaultRoomTemperature;
    }

    public static float getStrength(ItemStack stack) {
        double celsius = getTemperature(stack);

        if (celsius <= 425.0D) {
            return 0.0F;
        }

        double x = celsius - 425.0D;

        double amount = 1.0D - Math.exp(-x / 1450.0D);
        amount = Math.pow(amount, 1.05D);

        double strength = Mth.clamp(amount, 0.0D, 0.72D);

        double yellowRange = smoothstep(1250.0D, 2200.0D, celsius);
        strength *= Mth.lerp(yellowRange, 1.0D, 0.84D);

        double whiteRange = smoothstep(2200.0D, 5500.0D, celsius);
        strength *= Mth.lerp(whiteRange, 1.0D, 0.96D);

        return (float) strength;
    }

    public static int getColor(ItemStack stack) {
        double celsius = getTemperature(stack);

        if (celsius < 0.0D) {
            return coldColor(celsius);
        }

        return heatColor(celsius);
    }

    private static int heatColor(double celsius) {
        if (celsius <= 425.0D) {
            return 0x000000;
        }

        double t = 1.0D - Math.exp(-(celsius - 425.0D) / 1350.0D);
        t = Mth.clamp(t, 0.0D, 1.0D);

        int r;
        int g;
        int b;

        if (t < 0.20D) {
            // first visible glow: deep red, not gray/purple
            double u = smoothstep(0.0D, 0.20D, t);
            r = (int) Mth.lerp(u, 120.0D, 180.0D);
            g = (int) Mth.lerp(u, 0.0D, 8.0D);
            b = 0;
        } else if (t < 0.42D) {
            // deep red -> red
            double u = smoothstep(0.20D, 0.42D, t);
            r = (int) Mth.lerp(u, 180.0D, 255.0D);
            g = (int) Mth.lerp(u, 8.0D, 35.0D);
            b = 0;
        } else if (t < 0.63D) {
            // red -> orange
            double u = smoothstep(0.42D, 0.63D, t);
            r = 255;
            g = (int) Mth.lerp(u, 35.0D, 115.0D);
            b = 0;
        } else if (t < 0.82D) {
            // orange -> yellow
            double u = smoothstep(0.63D, 0.82D, t);
            r = 255;
            g = (int) Mth.lerp(u, 115.0D, 220.0D);
            b = (int) Mth.lerp(u, 0.0D, 35.0D);
        } else {
            // yellow -> warm white, but not flat white too early
            double u = smoothstep(0.86D, 1.0D, t);

            r = 255;
            g = (int) Mth.lerp(u, 235.0D, 255.0D);
            b = (int) Mth.lerp(u, 70.0D, 255.0D);
        }

        return (r << 16) | (g << 8) | b;
    }

    private static int coldColor(double celsius) {
        double coldness = Math.abs(celsius);

        double amount = 1.0D - Math.exp(-coldness / 120.0D);
        amount = Mth.clamp(amount, 0.0D, 1.0D);

        int r = (int) Mth.lerp(amount, 180.0D, 70.0D);
        int g = (int) Mth.lerp(amount, 220.0D, 170.0D);
        int b = 255;

        return (r << 16) | (g << 8) | b;
    }

    private static double smoothstep(double edge0, double edge1, double value) {
        double t = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0D, 1.0D);
        return t * t * (3.0D - 2.0D * t);
    }
}