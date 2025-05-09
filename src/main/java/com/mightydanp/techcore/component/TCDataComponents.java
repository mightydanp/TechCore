package com.mightydanp.techcore.component;

import com.mightydanp.techcore.api.materials.properties.Temperature;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TCDataComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Temperature>> TEMPERATURE =
            RegistriesHandler.DATA_COMPONENT_TYPES.register("temperature", () ->
                    DataComponentType.<Temperature>builder()
                            .persistent(Temperature.CODEC)
                            .networkSynchronized(Temperature.STREAM_CODEC)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MELTING_POINT =
            RegistriesHandler.DATA_COMPONENT_TYPES.register("melting_point", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BOILING_POINT =
            RegistriesHandler.DATA_COMPONENT_TYPES.register("boiling_point", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
                            .build()
            );

    public static void init() {
    }
}
