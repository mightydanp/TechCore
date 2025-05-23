package com.mightydanp.techcore.materials.Item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Temperature(double temperature, Scales.Scale scale) {
    public static final Codec<Temperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("temperature").forGetter(Temperature::temperature),
            Scales.Scale.CODEC.fieldOf("scale").forGetter(Temperature::scale)
    ).apply(instance, Temperature::new));

    public static final StreamCodec<FriendlyByteBuf, Temperature> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, Temperature::temperature,
            Scales.Scale.STREAM_CODEC, Temperature::scale,
            Temperature::new
    );

    public double toKelvin() {
        return scale == Scales.CELSIUS.scale
                ? temperature + 273.15
                : (temperature - 32) * 5 / 9 + 273.15;
    }

    public double getTemperature(Scales.Scale targetScale) {
        if (this.scale == targetScale) {
            return this.temperature;
        }

        return targetScale == Scales.CELSIUS.scale
                ? (temperature - 32) * 5 / 9
                : (temperature * 9 / 5) + 32;
    }

    public int[] getRGBColor() {
        double kelvin = toKelvin();
        kelvin = Math.max(1000, Math.min(kelvin, 40000)); // clamp for realism

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

    public static void main(String[] args) {
        Temperature t = new Temperature(100, Scales.CELSIUS.scale);
        System.out.printf("Original: %.2f %s%n", t.temperature(), t.scale());
        System.out.printf("In fahrenheit: %.2f %s%n", t.getTemperature(Scales.FAHRENHEIT.scale), Scales.FAHRENHEIT);
        int[] rgb = t.getRGBColor();
        System.out.printf("RGB color: (%d, %d, %d)%n", rgb[0], rgb[1], rgb[2]);
    }

    public enum Scales {
        CELSIUS(new Scale("celsius")),
        FAHRENHEIT(new Scale("fahrenheit"));

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

            public static final StreamCodec<FriendlyByteBuf, Scale> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    Scale::label,
                    Scale::new
            );
        }
    }
}
