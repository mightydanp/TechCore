package com.mightydanp.techcore.api.resources.assets.contents.model.material.item.component;

import com.mightydanp.techcore.api.resources.assets.contents.model.MCItemModelContent;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraft.resources.ResourceLocation;

public class MaterialItemModelContent extends MCItemModelContent {
    public MaterialItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public MaterialItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public MCItemModelContent materialItem(Icons.Icon icon, String... layers) {
        return materialModel(icon, name(), layers);
    }

    public MCItemModelContent materialModel(Icons.Icon icon, String modelName, String... layers) {
        if (layers == null || layers.length == 0) {
            throw new IllegalArgumentException("materialModel requires at least one layer");
        }

        ResourceLocation[] layerTextures = new ResourceLocation[layers.length];

        for (int i = 0; i < layers.length; i++) {
            if (layers[i] == null || layers[i].isBlank()) {
                throw new IllegalArgumentException("materialModel layer " + i + " is null or blank");
            }

            layerTextures[i] = materialIconTexture(modid(), icon.label(), layers[i]);
        }

        return new MCItemModelContent(modid(), modelName, getOrganizationPath())
                .basicLayeredItem(layerTextures)
                .end()
                .save(false);
    }

    private ResourceLocation materialIconTexture(String modid, String icon, String texture) {
        return ResourceLocation.fromNamespaceAndPath(
                modid,
                "item/material_icons/" + icon + "/" + texture
        );
    }
}
