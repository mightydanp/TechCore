package com.mightydanp.techcore.client.render.entity;

import com.mightydanp.techcore.client.config.ClientConfig;
import com.mightydanp.techcore.common.tag.TechCoreItemTags;
import com.mightydanp.techcore.world.item.properties.Temperature;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperatureClientRender {
    private TemperatureClientRender() {}

    public static boolean shouldRender(ItemStack stack, ItemDisplayContext context) {
        if (stack.isEmpty()) return false;

        if (stack.is(TechCoreItemTags.DISABLE_TEMPERATURE_RENDER)) return false;

        if (ClientConfig.isBlacklisted(stack.getItem())) return false;

        if (!Temperature.hasTemperature(stack)) return false;

        return shouldRenderInContext(context);
    }

    private static boolean shouldRenderInContext(ItemDisplayContext context) {
        return switch (context) {
            case GUI,
                 GROUND,
                 FIXED,
                 FIRST_PERSON_LEFT_HAND,
                 FIRST_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND,
                 THIRD_PERSON_RIGHT_HAND -> true;
            default -> false;
        };
    }

    public static float getStrength(ItemStack stack) {
        Double itemStackTemperature = Temperature.getTemperature(stack);

        if(itemStackTemperature == null) return 0.0F;

        return getStrength(itemStackTemperature);
    }

    private static float getStrength(double itemStackTemperature) {
        if (itemStackTemperature < 0.0D) {
            double coldness = Math.abs(itemStackTemperature);

            double amount = 1.0D - Math.exp(-coldness / 300.0D);
            amount = Math.pow(amount, 1.25D);

            return (float) Mth.clamp(amount, 0.0D, 0.72D);
        }

        if (itemStackTemperature <= 425.0D) {
            return 0.0F;
        }

        double x = itemStackTemperature - 425.0D;

        double amount = 1.0D - Math.exp(-x / 1450.0D);
        amount = Math.pow(amount, 1.05D);

        double strength = Mth.clamp(amount, 0.0D, 0.72D);

        double yellowRange = smoothstep(1250.0D, 2200.0D, itemStackTemperature);
        strength *= Mth.lerp(yellowRange, 1.0D, 0.84D);

        double whiteRange = smoothstep(2200.0D, 5500.0D, itemStackTemperature);
        strength *= Mth.lerp(whiteRange, 1.0D, 0.96D);

        return (float) strength;
    }

    private static double smoothstep(double edge0, double edge1, double value) {
        double t = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0D, 1.0D);
        return t * t * (3.0D - 2.0D * t);
    }
}