package com.mightydanp.techcore.materials.config;

import com.mightydanp.techcore.materials.properties.Temperature;
import net.minecraftforge.common.ForgeConfigSpec;

public class MaterialConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.EnumValue<Temperature.Scales> TEMPERATURE_SCALE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("display");
        TEMPERATURE_SCALE = builder
                .comment("Temperature scale used for display. Options: CELSIUS, FAHRENHEIT, KELVIN")
                .defineEnum("temperatureScale", Temperature.Scales.CELSIUS);
        builder.pop();

        SPEC = builder.build();
    }
}
