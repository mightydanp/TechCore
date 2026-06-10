package com.mightydanp.techcore.api.resources.assets.content.model.block.component;

import com.mightydanp.techcore.api.resources.assets.content.blockstate.MCBlockStateContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.MCItemModelContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.ModelFile;
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

    public RockLayerModelContent saveRockLayerSlabBlockModel(SlabBlock block, ResourceLocation texture, ResourceLocation doubleSlab) {
        RockLayerModelContent topModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top"), null);

        slab(texture, texture, texture);
        save(false);

        topModel.slabTop(texture, texture, texture);
        topModel.save(false);

        new MCBlockStateContent(modid(), name()).slabBlock(
                block,
                model(),
                topModel.model(),
                new ModelFile.UncheckedModelFile(doubleSlab)
        );
        new MCItemModelContent(modid(), name(), null).saveBlockItem(this);

        return this;
    }

    public RockLayerModelContent saveRockLayerStairsBlockModel(StairBlock block, ResourceLocation texture) {
        RockLayerModelContent innerModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inner"), null);
        RockLayerModelContent outerModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_outer"), null);

        stairs(texture, texture, texture);
        save(false);

        innerModel.stairsInner(texture, texture, texture);
        innerModel.save(false);

        outerModel.stairsOuter(texture, texture, texture);
        outerModel.save(false);

        new MCBlockStateContent(modid(), name()).stairsBlock(
                block,
                model(),
                innerModel.model(),
                outerModel.model()
        );
        new MCItemModelContent(modid(), name(), null).saveBlockItem(this);

        return this;
    }

    public RockLayerModelContent saveRockLayerWallBlockModel(WallBlock block, ResourceLocation texture) {
        RockLayerModelContent postModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), null);
        RockLayerModelContent sideModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), null);
        RockLayerModelContent sideTallModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side_tall"), null);
        RockLayerModelContent inventoryModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), null);

        postModel.wallPost(texture);
        postModel.save(false);

        sideModel.wallSide(texture);
        sideModel.save(false);

        sideTallModel.wallSideTall(texture);
        sideTallModel.save(false);

        inventoryModel.wallInventory(name() + "_inventory", texture);
        inventoryModel.save(false);

        new MCBlockStateContent(modid(), name()).wallBlock(
                block,
                postModel.model(),
                sideModel.model(),
                sideTallModel.model()
        );
        new MCItemModelContent(modid(), name(), null).saveBlockItem(inventoryModel);

        return this;
    }

    public RockLayerModelContent saveRockLayerButtonBlockModel(ButtonBlock block, ResourceLocation texture) {
        RockLayerModelContent pressedModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_pressed"), null);
        RockLayerModelContent inventoryModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), null);

        button(texture);
        save(false);

        pressedModel.buttonPressed(name() + "_pressed", texture);
        pressedModel.save(false);

        inventoryModel.buttonInventory(name() + "_inventory", texture);
        inventoryModel.save(false);

        new MCBlockStateContent(modid(), name()).buttonBlock(
                block,
                model(),
                pressedModel.model()
        );
        new MCItemModelContent(modid(), name(), null).saveBlockItem(inventoryModel);

        return this;
    }

    public RockLayerModelContent saveRockLayerPressurePlateBlockModel(PressurePlateBlock block, ResourceLocation texture) {
        RockLayerModelContent downModel = new RockLayerModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_down"), null);

        pressurePlate(name(), texture);
        save(false);

        downModel.pressurePlateDown(name() + "_down", texture);
        downModel.save(false);

        new MCBlockStateContent(modid(), name()).pressurePlateBlock(
                block,
                model(),
                downModel.model()
        );
        new MCItemModelContent(modid(), name(), null).saveBlockItem(this);

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
