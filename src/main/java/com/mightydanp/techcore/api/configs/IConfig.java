package com.mightydanp.techcore.api.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfig {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    default ForgeConfigSpec build() {
        return builder.build();
    }

    default void load() {
    }
}
