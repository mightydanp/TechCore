package com.mightydanp.techcore.api.resources.assets.content.blockstate.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;

public class OreBlockStateComponent extends MaterialBlockStateContent {
    public OreBlockStateComponent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public OreBlockStateComponent(String modid, String name) {
        super(modid, name);
    }

    public OreBlockStateComponent saveOreBlockState(Block block) {
        try {
            VariantBlockStateBuilder builder = getVariantBuilder(block)
                    .forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(new ModelFile.UncheckedModelFile(
                                    ResourceLocation.fromNamespaceAndPath(
                                            modid(),
                                            ModelProvider.BLOCK_FOLDER + "/ore/" + name()
                                    ))).build());
            setBlockState(builder).save(false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ore blockstate for " + name(), e);
        }

        return this;
    }
}
