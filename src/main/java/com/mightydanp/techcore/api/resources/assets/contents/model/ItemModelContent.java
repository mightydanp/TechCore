package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.ItemModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemModelContent<A extends ItemModelContent<A>> extends ModelContent<A> {
    public static final String ITEM_FOLDER = "item";

    public ItemModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, ITEM_FOLDER, parentFolder);
        this.initItemModelBuilder();
    }

    public ItemModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, ITEM_FOLDER, parentFolder);
        this.initItemModelBuilder();
    }

    @SuppressWarnings("unchecked")
    private void initItemModelBuilder() {
        this.model(new ItemModelBuilder<>(super.model().getLocation(), (A) this));
    }

    @Override
    public ItemModelBuilder<A> model() {
        return (ItemModelBuilder<A>) super.model();
    }

    public A save(boolean override) {
        AssetPackRegistries.saveItemModel(this, override);
        return end();
    }

    public ItemModelBuilder<A> basicItem(Item item) {
        return basicItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder<A> basicItem(ResourceLocation item) {
        ItemModelBuilder<A> model = this.model();
        model.parent(new ModelFile.UncheckedModelFile("item/generated"));

        model.texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));

        return model;
    }

    public ItemModelBuilder<A> basicLayeredItem(ResourceLocation... layerTextures) {
        if (layerTextures == null || layerTextures.length == 0) {
            throw new IllegalArgumentException("basicLayeredItem requires at least one layer texture");
        }

        ItemModelBuilder<A> model = model();

        if (layerTextures.length == 1) {
            model.parent(new ModelFile.UncheckedModelFile("item/generated"));
            model.texture("layer0", layerTextures[0]);
            return model;
        }

        model.ao(false);
        model.texture("particle", layerTextures[0]);

        for (int i = 0; i < layerTextures.length; i++) {
            model.texture("layer" + i, layerTextures[i]);

            float offset = i == 0 ? 0.0F : 0.025F * i;
            addFlatLayer(model, "layer" + i, i, 7.50F, offset);
        }

        addGeneratedItemDisplay(model);

        return model;
    }

    private ItemModelBuilder<A> addFlatLayer(ItemModelBuilder<A> model, String textureKey, int tintIndex, float baseZ, float offset) {
        float halfThickness = 0.005F;

        addFlatFace(model, Direction.NORTH, "#" + textureKey, tintIndex, baseZ - halfThickness - offset);
        addFlatFace(model, Direction.SOUTH, "#" + textureKey, tintIndex, baseZ + halfThickness + offset);

        return model;
    }

    private ItemModelBuilder<A> addFlatFace(ItemModelBuilder<A> model, Direction direction, String texture, int tintIndex, float z) {
        model.element()
                .from(0.0F, 0.0F, z)
                .to(16.0F, 16.0F, z)
                .shade(false)
                .face(direction)
                .texture(texture)
                .tintindex(tintIndex)
                .uvs(0.0F, 0.0F, 16.0F, 16.0F)
                .end();

        return model;
    }

    private ItemModelBuilder<A> addGeneratedItemDisplay(ItemModelBuilder<A> model) {
        model.transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(0.0F, 0.0F, 0.0F)
                .translation(0.0F, 0.0F, 0.0F)
                .scale(1.0F)
                .end()

                .transform(ItemDisplayContext.GROUND)
                .rotation(0.0F, 0.0F, 0.0F)
                .translation(0.0F, 2.0F, 0.0F)
                .scale(0.5F)
                .end()

                .transform(ItemDisplayContext.FIXED)
                .rotation(0.0F, 180.0F, 0.0F)
                .translation(0.0F, 0.0F, 0.0F)
                .scale(1.0F)
                .end()

                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75.0F, 45.0F, 0.0F)
                .translation(0.0F, 2.5F, 0.0F)
                .scale(0.55F)
                .end()

                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(75.0F, -45.0F, 0.0F)
                .translation(0.0F, 2.5F, 0.0F)
                .scale(0.55F)
                .end()

                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0.0F, -90.0F, 25.0F)
                .translation(1.13F, 3.2F, 1.13F)
                .scale(0.68F)
                .end()

                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0.0F, 90.0F, -25.0F)
                .translation(1.13F, 3.2F, 1.13F)
                .scale(0.68F)
                .end();

        return model;
    }

    @SuppressWarnings("unchecked")
    public A end() {
        return (A) this;
    }
}
