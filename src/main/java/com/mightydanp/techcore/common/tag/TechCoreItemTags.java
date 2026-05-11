package com.mightydanp.techcore.common.tag;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TechCoreItemTags {
    public static final TagKey<Item> DISABLE_TEMPERATURE_RENDER =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "disable_temperature_render"));

    private TechCoreItemTags() {}
}