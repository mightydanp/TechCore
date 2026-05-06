package com.mightydanp.techcore.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfig {
    ForgeConfigSpec build();

    default void load() {
    }
}
