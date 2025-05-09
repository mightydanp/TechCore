package com.mightydanp.techcore.data.data.traits.item;

import com.mightydanp.techcore.api.traits.item.ItemTrait;
import com.mightydanp.techcore.data.data.traits.TraitProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTraitProvider extends TraitProvider<ItemTrait> {
    public ItemTraitProvider(PackOutput output, String modId, ResourceKey<? extends Registry<ItemTrait>> resourceKey, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modId, resourceKey, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {

    }
}