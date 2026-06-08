package com.mightydanp.techcore.api.resources.assets.content.model.block.component;

import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class OreBlockModelContent extends MaterialBlockModelContent{
    public OreBlockModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public OreBlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public OreBlockModelContent saveOreBlockModel(Icons.Icon icon, String modelName, ResourceLocation stoneTexture, String variant) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "block"))
                .texture("stone", stoneTexture)
                .texture("ore", oreBlockTexture(icon, variant, false))
                .texture("ore_overlay", oreBlockTexture(icon, variant, true));


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
                        .texture("#ore")
                        .tintindex(0)
                        .cullface(direction));

        model.element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((direction, face) -> face
                        .texture("#ore_overlay")
                        .cullface(direction));

        save(false);
        return this;
    }



    @Contract("_, _, _ -> new")
    private @NotNull ResourceLocation oreBlockTexture(Icons.@NotNull Icon icon, String variant, boolean overlay) {
        return ResourceLocation.fromNamespaceAndPath(
                modid(),
                "block/material_icons/" + icon.label() + "/" + variant + (overlay ? "_overlay" : "")
        );
    }



}
