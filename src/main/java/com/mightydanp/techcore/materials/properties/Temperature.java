package com.mightydanp.techcore.materials.properties;

import com.mightydanp.techcore.materials.config.MaterialConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record Temperature(double temperature, Scales.Scale scale) {
    public static final double celsiusDefaultRoomTemperature = 20.0;

    public static final Codec<Temperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("temperature").forGetter(Temperature::temperature),
            Scales.Scale.CODEC.fieldOf("scale").forGetter(Temperature::scale)
    ).apply(instance, Temperature::new));

    public double toKelvin() {
        return temperature + 273.15;
    }

    public double toFahrenheit() {
        return (temperature * 9.0 / 5.0) + 32;
    }

    public double getTemperature(Scales.Scale targetScale) {
        if (targetScale.equals(Scales.KELVIN.scale)) return toKelvin();
        if (targetScale.equals(Scales.FAHRENHEIT.scale)) return toFahrenheit();
        return temperature;
    }

    public static Double getTemperature(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("temperature")) {
            return tag.getDouble("temperature");
        }
        return null;
    }

    public static boolean hasTemperature(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains("temperature");
    }

    public static Scales.Scale getScale() {
        return MaterialConfig.TEMPERATURE_SCALE.get().scale;
    }

    public int[] getRGBColor() {
        double kelvin = toKelvin();
        double roomKelvin = celsiusDefaultRoomTemperature + 273.15;

        if (kelvin < roomKelvin) {
            double t = kelvin / roomKelvin;
            return new int[]{
                (int) Math.round(t * 255),
                (int) Math.round(t * 255),
                255
            };
        }

        kelvin = Math.max(1000, Math.min(kelvin, 40000));

        double temp = kelvin / 100;
        double red, green, blue;

        red = temp <= 66 ? 255 : 329.698727446 * Math.pow(temp - 60, -0.1332047592);
        red = clamp(red);

        green = temp <= 66
                ? 99.4708025861 * Math.log(temp) - 161.1195681661
                : 288.1221695283 * Math.pow(temp - 60, -0.0755148492);
        green = clamp(green);

        blue = temp >= 66 ? 255 :
                temp <= 19 ? 0 :
                        138.5177312231 * Math.log(temp - 10) - 305.0447927307;
        blue = clamp(blue);

        return new int[]{
                (int) Math.round(red),
                (int) Math.round(green),
                (int) Math.round(blue)
        };
    }

    private int clamp(double value) {
        return (int) Math.round(Math.max(0, Math.min(255, value)));
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
