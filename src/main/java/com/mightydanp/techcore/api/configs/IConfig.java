package com.mightydanp.techcore.api.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public interface IConfig {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    default ModConfigSpec build() {
        return builder.build();
    }

    default void load() {
    }
}
