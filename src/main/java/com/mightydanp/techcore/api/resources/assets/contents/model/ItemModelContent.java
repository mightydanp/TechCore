package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Objects;

public class ItemModelContent<A extends ItemModelContent<A>> extends ModelContent<ItemModelContent<A>> {
    public static final String ITEM_FOLDER = "item";

    public ItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, ITEM_FOLDER, parentFolder);
    }

    public ItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, ITEM_FOLDER, parentFolder);
    }

    public ItemModelContent<A> save(boolean override) {
        AssetPackRegistries.saveItemModel(this, override);
        return this;
    }

    public TCModelBuilder<A> basicItem(Item item) {
        return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    @SuppressWarnings("unchecked")
    public TCModelBuilder<A> basicItem(ResourceLocation item) {
        TCModelBuilder<A> model = new TCModelBuilder<A>(item)
                .parent(new ModelFile.UncheckedModelFile("item/generated"));

        model.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));

        return model;
    }

    @SuppressWarnings("unchecked")
    public A end() {
        return (A) this;
    }
}
