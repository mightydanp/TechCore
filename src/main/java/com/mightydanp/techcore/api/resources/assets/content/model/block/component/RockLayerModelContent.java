package com.mightydanp.techcore.api.resources.assets.content.model.block.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;

public class RockLayerModelContent extends MaterialBlockModelContent {
    public RockLayerModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public RockLayerModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public RockLayerModelContent saveRockLayerBlockModel(ResourceLocation texture) {
        cubeAll(texture);
        save(false);
        return this;
    }

    public RockLayerModelContent saveRockLayerBlockModel(ResourceLocation texture, ResourceLocation overlayTexture) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "block"))
                .texture("particle", texture)
                .texture("stone", texture)
                .texture("overlay", overlayTexture);

        model.element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((direction, face) -> face
                        .texture("#stone")
                        .cullface(direction));

        model.element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((direction, face) -> face
                        .texture("#overlay")
                        .cullface(direction));

        save(false);
        return this;
    }
}
