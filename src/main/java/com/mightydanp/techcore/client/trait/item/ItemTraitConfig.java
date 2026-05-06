package com.mightydanp.techcore.client.trait.item;

import com.mightydanp.techcore.client.config.IConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ItemTraitConfig implements IConfig {
    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private final ForgeConfigSpec.ConfigValue<String> registryConfig;
    private final ForgeConfigSpec.ConfigValue<Integer> colorConfig;
    private final ForgeConfigSpec.ConfigValue<Integer> maxDamageConfig;
    private final ForgeConfigSpec.ConfigValue<String> textureIconConfig;
    private final ForgeConfigSpec.ConfigValue<Double> kilogramsConfig;
    private final ForgeConfigSpec.ConfigValue<Double> metersConfig;

    public String registry;
    public Integer color;
    public Integer maxDamage;
    public String textureIcon;
    public Double kilograms;
    public Double meters;

    public ItemTraitConfig(String registry, Integer color, Integer maxDamage, String textureIcon, Double kilograms, Double meters) {
        registryConfig = builder
                .comment("registry of the Item.")
                .define("registry", registry);
        colorConfig = builder
                .comment("color for the item.")
                .define("color", color);
        maxDamageConfig = builder
                .comment("maximum damage the item can take.")
                .define("maxDamage", maxDamage);
        textureIconConfig = builder
                .comment("texture icon for the item.")
                .define("textureIcon", textureIcon);
        kilogramsConfig = builder
                .comment("how much does the item weigh in kilograms.")
                .define("kilograms", kilograms);
        metersConfig = builder
                .comment("size of the item in meters.")
                .define("meters", meters);
    }

    @Override
    public ForgeConfigSpec build() {
        return builder.build();
    }

    @Override
    public void load() {
        this.registry = registryConfig.get();
        this.color = colorConfig.get();
        this.maxDamage = maxDamageConfig.get();
        this.textureIcon = textureIconConfig.get();
        this.kilograms = kilogramsConfig.get();
        this.meters = metersConfig.get();
    }
}
