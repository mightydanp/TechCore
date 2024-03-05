package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Objects;

public class ItemModelContent extends ModelContent{
    public static final String ITEM_FOLDER = "item";

    public ItemModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public ItemModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }

    public ItemModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }

    public TCModelBuilder basicItem(Item item) {
        return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public TCModelBuilder basicItem(ResourceLocation item) {
        return new TCModelBuilder(item)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }




}
