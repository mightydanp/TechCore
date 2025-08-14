package com.mightydanp.techcore.data.traits.block;

import com.mightydanp.techcore.data.traits.TraitProvider;
import com.mightydanp.techcore.traits.block.BlockTrait;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTraitProvider extends TraitProvider<BlockTrait> {

    public BlockTraitProvider(PackOutput output, String modId, ResourceKey<? extends Registry<BlockTrait>> resourceKey, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modId, resourceKey, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {

    }


}
