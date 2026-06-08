package com.mightydanp.techcore.api.resources.assets.content.model.item.component;

import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class OreItemModelContent extends MaterialItemModelContent{
    public OreItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public OreItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public OreItemModelContent saveGemModel(Icons.Icon icon, String modelName) {
        materialModel(icon, modelName, modelName);
        return this;
    }

    public OreItemModelContent saveGemItemModel(Icons.@NotNull Icon icon, String texture) {
        basicItem(ResourceLocation.fromNamespaceAndPath(modid(), "material_icons/" + icon.label() + "/" + texture));
        save(false);
        return this;
    }

    public OreItemModelContent saveOreItemModel(Icons.Icon icon, String stage) {
        for (String model : oreModels(stage)) {
            materialModel(icon, model, model, model + "_overlay");
        }

        return this;
    }

    public OreItemModelContent saveStageOreItemModel(String stage) {
        OreItemModelContent content = new OreItemModelContent(modid(), name(), null);
        String[] models = oreModels(stage);
        float[] quantities = {0f, 0.25f, 0.50f, 1.0f};

        for (int i = 0; i < models.length; i++) {
            content.model().override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid(), "quantity"), quantities[i])
                    .model(uncheckedModelFile(ModelProvider.ITEM_FOLDER, "ore" + "/" + models[i]))
                    .end();
        }

        content.save(false);
        return this;
    }

    public OreItemModelContent saveOreBlockItemModel(String modelName) {
        new OreItemModelContent(modid(), name(), null)
                .model()
                .parent(this.uncheckedModelFile(ModelProvider.BLOCK_FOLDER, modelName))
                .end()
                .save(false);

        return this;
    }



    @Contract(value = "_ -> new", pure = true)
    private String @NotNull [] oreModels(String stage) {
        return new String[] {
                stage + "_div72_ore",
                stage + "_tiny_ore",
                stage + "_small_ore",
                stage + "_ore"
        };
    }


}
