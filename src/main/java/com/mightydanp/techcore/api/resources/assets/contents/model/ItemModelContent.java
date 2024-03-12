package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Objects;

public class ItemModelContent<A extends ItemModelContent<A>> extends ModelContent<ItemModelContent<A>>{
    public static final String ITEM_FOLDER = "item";

    public ItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, ITEM_FOLDER, parentFolder);
    }

    public ItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, ITEM_FOLDER, parentFolder);
    }

    @SuppressWarnings("ALL")
    public A save(boolean override){
        AssetPackRegistries.saveItemModel((A)this, override);
        return (A)this;
    }

    public TCModelBuilder<A> basicItem(Item item) {
        return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    @SuppressWarnings("ALL")
    public TCModelBuilder<A> basicItem(ResourceLocation item) {
        return (TCModelBuilder<A>) new TCModelBuilder(item)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
}
