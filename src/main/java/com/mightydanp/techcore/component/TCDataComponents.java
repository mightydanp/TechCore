package com.mightydanp.techcore.component;

import com.mightydanp.techcore.api.materials.properties.Temperature;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.Tool;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.UnaryOperator;

public class TCDataComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Temperature>> TEMPERATURE =
            RegistriesHandler.DATA_COMPONENT_TYPES.register("temperature", () ->
                    DataComponentType.<Temperature>builder()
                            .persistent(Temperature.CODEC)
                            .networkSynchronized(Temperature.STREAM_CODEC)
                            .build()
            );

    public static void init() {}
}
