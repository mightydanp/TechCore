package com.mightydanp.techcore.api.client.registries;

import com.mightydanp.techcore.materials.components.Component;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ClientRegistrar {

    public static void register(Supplier<Item> item, ResourceLocation key, Component.ItemPropertyValue property) {
        ClampedItemPropertyFunction clampedProperty =
                property::call;

        ItemProperties.register(
                item.get(),
                key,
                clampedProperty
        );
    }
}