package com.mightydanp.techcore.api.resources.assets.content.model.item.component;

import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;

public class ProcessedItemModelContent extends MaterialItemModelContent {
    public ProcessedItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public ProcessedItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public ProcessedItemModelContent saveDustItemModel(Icons.Icon icon, String prefix) {
        String[] models = {prefix + "div72_dust", prefix + "tiny_dust", prefix + "small_dust", prefix + "full_dust"};

        String[] layer0 = {"div72_dust", "tiny_dust", "small_dust", "dust"};

        String[] overlayBase = {"div72_dust", "tiny_dust", "small_dust", "dust"};

        for (int i = 0; i < models.length; i++) {
            if (prefix.isEmpty()) {
                materialModel(icon, models[i], layer0[i]);
            } else {
                materialModel(icon, models[i], layer0[i], prefix + overlayBase[i] + "_overlay"
                );
            }
        }
        return this;
    }

    public ProcessedItemModelContent saveStageDustItemModel(String prefix) {
        ProcessedItemModelContent content = new ProcessedItemModelContent(modid(), name(), null);

        String[] models = {prefix + "div72_dust", prefix + "tiny_dust", prefix + "small_dust", prefix + "full_dust"};
        float[] quantities = {0f, 0.25f, 0.50f, 1.0f};

        for (int i = 0; i < models.length; i++) {
            content.model().override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid(), "quantity"), quantities[i])
                    .model(uncheckedModelFile(ModelProvider.ITEM_FOLDER, "dust" + "/" + models[i]))
                    .end();
        }

        content.save(false);
        return this;
    }


}
