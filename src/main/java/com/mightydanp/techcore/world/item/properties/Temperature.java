package com.mightydanp.techcore.world.item.properties;

import com.mightydanp.techcore.materials.config.MaterialConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Temperature(double temperature, Scales.Scale scale) {
    public static final String TAG = "temperature";

    public static final double celsiusDefaultRoomTemperature = 20.0;

    public static final Codec<Temperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf(TAG).forGetter(Temperature::temperature),
            Scales.Scale.CODEC.fieldOf("scale").forGetter(Temperature::scale)
    ).apply(instance, Temperature::new));

    /// item helpers
    public static boolean hasTemperature(@NotNull ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static @Nullable Double getTemperature(@NotNull ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains(TAG)) {
            return tag.getDouble(TAG);
        }
        return null;
    }

    public static void setTemperature(@NotNull ItemStack itemStack, double temperature) {
        itemStack.getOrCreateTag().putDouble(TAG, temperature);
    }

    public static @Nullable Temperature fromStack(ItemStack itemStack) {
        Double temperature = getTemperature(itemStack);

        if (temperature == null) return null;

        return new Temperature(temperature, getScale());
    }

    public static Scales.Scale getScale() {
        return MaterialConfig.TEMPERATURE_SCALE.get().scale;
    }

    public static int getColor(ItemStack itemStack) {
        Temperature temperature = fromStack(itemStack);
        return temperature == null ? 0xFFFFFF : temperature.getColor();
    }

    private static double smoothstep(double edge0, double edge1, double value) {
        double t = clamp01((value - edge0) / (edge1 - edge0));
        return t * t * (3.0D - 2.0D * t);
    }

    private static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    private static double clamp01(double value) {
        return Math.max(0.0D, Math.min(1.0D, value));
    }

    private static int clamp(double value) {
        return (int) Math.round(Math.max(0, Math.min(255, value)));
    }

    public double toKelvin() {
        return temperature + 273.15;
    }

    public double toFahrenheit() {
        return (temperature * 9.0 / 5.0) + 32;
    }

    ///

    @Contract(pure = true)
    public @NotNull Double toKelvin(int temperature) {
        return temperature + 273.15;
    }

    @Contract(pure = true)
    public @NotNull Double toFahrenheit(int temperature) {
        return (temperature * 9.0 / 5.0) + 32;
    }

    public Double getTemperature(Scales.@NotNull Scale scale) {
        if (scale.equals(Scales.KELVIN.scale)) return toKelvin();
        if (scale.equals(Scales.FAHRENHEIT.scale)) return toFahrenheit();
        return temperature;
    }

    private int[] getRGBColor() {
        if (temperature < 0.0D) {
            return coldColor();
        }

        return heatColor();
    }

    public int getColor() {
        int[] rgb = getRGBColor();
        return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    @Contract(" -> new")
    private int @NotNull [] heatColor() {
        if (temperature <= 425.0D) {
            return new int[]{0, 0, 0};
        }

        double t = 1.0D - Math.exp(-(temperature - 425.0D) / 1350.0D);
        t = clamp01(t);

        int r;
        int g;
        int b;

        if (t < 0.20D) {
            double u = smoothstep(0.0D, 0.20D, t);
            r = (int) lerp(u, 120.0D, 180.0D);
            g = (int) lerp(u, 0.0D, 8.0D);
            b = 0;
        } else if (t < 0.42D) {
            double u = smoothstep(0.20D, 0.42D, t);
            r = (int) lerp(u, 180.0D, 255.0D);
            g = (int) lerp(u, 8.0D, 35.0D);
            b = 0;
        } else if (t < 0.63D) {
            double u = smoothstep(0.42D, 0.63D, t);
            r = 255;
            g = (int) lerp(u, 35.0D, 115.0D);
            b = 0;
        } else if (t < 0.82D) {
            double u = smoothstep(0.63D, 0.82D, t);
            r = 255;
            g = (int) lerp(u, 115.0D, 220.0D);
            b = (int) lerp(u, 0.0D, 35.0D);
        } else {
            double u = smoothstep(0.86D, 1.0D, t);

            r = 255;
            g = (int) lerp(u, 235.0D, 255.0D);
            b = (int) lerp(u, 70.0D, 255.0D);
        }

        return new int[]{clamp(r), clamp(g), clamp(b)};
    }

    @Contract(" -> new")
    private int @NotNull [] coldColor() {
        double coldness = Math.abs(Math.min(0.0D, temperature));

        double amount = 1.0D - Math.exp(-coldness / 300.0D);
        amount = clamp01(amount);

        int r = (int) lerp(amount, 170.0D, 45.0D);
        int g = (int) lerp(amount, 220.0D, 170.0D);
        int b = 255;

        return new int[]{clamp(r), clamp(g), clamp(b)};
    }

    public enum Scales {
        CELSIUS(new Scale("Celsius")),
        FAHRENHEIT(new Scale("Fahrenheit")),
        KELVIN(new Scale("Kelvin"));

        public final Scale scale;

        Scales(Scale scale) {
            this.scale = scale;
        }

        @Override
        public String toString() {
            return scale.label;
        }

        public record Scale(String label) {
            public static final Codec<Scale> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("label").forGetter(Scale::label)
                    ).apply(instance, Scale::new)
            );
        }
    }
}
