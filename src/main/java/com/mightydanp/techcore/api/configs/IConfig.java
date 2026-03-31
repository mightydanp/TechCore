package com.mightydanp.techcore.api.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfig {
    ForgeConfigSpec build();

    default void load() {
    }
}
