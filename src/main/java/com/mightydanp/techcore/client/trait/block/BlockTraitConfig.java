package com.mightydanp.techcore.client.trait.block;

import com.mightydanp.techcore.api.configs.IConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BlockTraitConfig implements IConfig {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    private final ModConfigSpec.ConfigValue<String> registryConfig;
    private final ModConfigSpec.ConfigValue<Integer> colorConfig;
    private final ModConfigSpec.ConfigValue<Double> kilogramConfig;
    private final ModConfigSpec.ConfigValue<Boolean> canPickUpConfig;

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
    public ModConfigSpec build() {
        return builder.build();
    }

    @Override
    public void load() {
        this.registry = registryConfig.get();
        this.color = colorConfig.get();
        this.kilogram = kilogramConfig.get();
        this.canPickUp = canPickUpConfig.get();
    }
}
