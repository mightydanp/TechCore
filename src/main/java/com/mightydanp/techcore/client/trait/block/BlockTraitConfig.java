package com.mightydanp.techcore.client.trait.block;

import com.mightydanp.techcore.api.configs.IConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class BlockTraitConfig implements IConfig {
    private final ForgeConfigSpec.ConfigValue<String> registryConfig;
    private final ForgeConfigSpec.ConfigValue<Integer> colorConfig;
    private final ForgeConfigSpec.ConfigValue<Double> kilogramConfig;
    private final ForgeConfigSpec.ConfigValue<Boolean> canPickUpConfig;

    public String registry;
    public Integer color;
    public Double kilogram;
    public Boolean canPickUp;

    public BlockTraitConfig(String registry, Integer color, Double kilogram, Boolean canPickUp) {
        registryConfig = builder
                .comment("registry of the Block.")
                .define("registry", registry);
        colorConfig = builder
                .comment("color for the block.")
                .define("color", color);
        kilogramConfig = builder
                .comment("how much does the block weigh in kilograms")
                .define("kilogram", kilogram);
        canPickUpConfig = builder
                .comment("are you allowed to pick this up and put it on your back?")
                .define("can pick up", canPickUp);
    }

    @Override
    public void load() {
        this.registry = registryConfig.get();
        this.color = colorConfig.get();
        this.kilogram = kilogramConfig.get();
        this.canPickUp = canPickUpConfig.get();
    }
}
