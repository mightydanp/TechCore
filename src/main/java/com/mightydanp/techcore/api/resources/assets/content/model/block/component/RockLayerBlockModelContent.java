package com.mightydanp.techcore.api.resources.assets.content.model.block.component;

import com.mightydanp.techcore.api.resources.assets.content.blockstate.MCBlockStateContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.MCItemModelContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;

public class RockLayerBlockModelContent extends MaterialBlockModelContent {
    public RockLayerBlockModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public RockLayerBlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public RockLayerBlockModelContent saveRockLayerBlockModel(ResourceLocation texture) {
        cubeAll(texture);
        save(false);
        return this;
    }

    public RockLayerBlockModelContent saveRockLayerSlabBlockModel(SlabBlock block, ResourceLocation texture, ResourceLocation doubleSlab) {
        RockLayerBlockModelContent topModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top"), null);

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

    public RockLayerBlockModelContent saveRockLayerStairsBlockModel(StairBlock block, ResourceLocation texture) {
        RockLayerBlockModelContent innerModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inner"), null);
        RockLayerBlockModelContent outerModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_outer"), null);

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

    public RockLayerBlockModelContent saveRockLayerWallBlockModel(WallBlock block, ResourceLocation texture) {
        RockLayerBlockModelContent postModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), null);
        RockLayerBlockModelContent sideModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), null);
        RockLayerBlockModelContent sideTallModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side_tall"), null);
        RockLayerBlockModelContent inventoryModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), null);

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

    public RockLayerBlockModelContent saveRockLayerButtonBlockModel(ButtonBlock block, ResourceLocation texture) {
        RockLayerBlockModelContent pressedModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_pressed"), null);
        RockLayerBlockModelContent inventoryModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), null);

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

    public RockLayerBlockModelContent saveRockLayerWallBlockModel(WallBlock block, ResourceLocation texture, ResourceLocation overlayTexture) {
        RockLayerBlockModelContent postModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), null);
        RockLayerBlockModelContent sideModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), null);
        RockLayerBlockModelContent sideTallModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side_tall"), null);
        RockLayerBlockModelContent inventoryModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), null);

        postModel.wallOverlayPost(texture, overlayTexture);
        postModel.save(false);

        sideModel.wallOverlaySide(texture, overlayTexture);
        sideModel.save(false);

        sideTallModel.wallOverlaySideTall(texture, overlayTexture);
        sideTallModel.save(false);

        inventoryModel.wallOverlayInventory(texture, overlayTexture);
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

    public RockLayerBlockModelContent saveRockLayerPressurePlateBlockModel(PressurePlateBlock block, ResourceLocation texture) {
        RockLayerBlockModelContent downModel = new RockLayerBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_down"), null);

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

    public RockLayerBlockModelContent saveRockLayerBlockModel(ResourceLocation texture, ResourceLocation overlayTexture) {
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

    private void wallOverlayPost(ResourceLocation texture, ResourceLocation overlayTexture) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "block"))
                .texture("particle", texture)
                .texture("wall", texture)
                .texture("overlay", overlayTexture);

        model.element()
                .from(4, 0, 4)
                .to(12, 16, 12)
                .allFaces((direction, face) -> face.texture("#wall"));

        model.element()
                .from(4, 0, 4)
                .to(12, 16, 12)
                .allFaces((direction, face) -> face.texture("#overlay"));
    }

    private void wallOverlaySide(ResourceLocation texture, ResourceLocation overlayTexture) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "block"))
                .texture("particle", texture)
                .texture("wall", texture)
                .texture("overlay", overlayTexture);

        model.element()
                .from(5, 0, 0)
                .to(11, 14, 8)
                .allFaces((direction, face) -> face.texture("#wall"));

        model.element()
                .from(5, 0, 0)
                .to(11, 14, 8)
                .allFaces((direction, face) -> face.texture("#overlay"));
    }

    private void wallOverlaySideTall(ResourceLocation texture, ResourceLocation overlayTexture) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "block"))
                .texture("particle", texture)
                .texture("wall", texture)
                .texture("overlay", overlayTexture);

        model.element()
                .from(5, 0, 0)
                .to(11, 16, 8)
                .allFaces((direction, face) -> face.texture("#wall"));
        model.element()
                .from(5, 0, 0)
                .to(11, 16, 8)
                .allFaces((direction, face) -> face.texture("#overlay"));
    }

    private void wallOverlayInventory(ResourceLocation texture, ResourceLocation overlayTexture) {
        var model = model()
                .renderType("cutout")
                .parent(uncheckedMCModelFile(ModelProvider.BLOCK_FOLDER, "wall_inventory"))
                .texture("particle", texture)
                .texture("wall", texture)
                .texture("overlay", overlayTexture);

        // center post overlay
        model.element()
                .from(4, 0, 4)
                .to(12, 16, 12)
                .allFaces((direction, face) -> face.texture("#wall"));
        model.element()
                .from(4, 0, 4)
                .to(12, 16, 12)
                .allFaces((direction, face) -> face.texture("#overlay"));

        // north-south bar overlay
        model.element()
                .from(5, 0, 0)
                .to(11, 13, 16)
                .allFaces((direction, face) -> face.texture("#wall"));
        model.element()
                .from(5, 0, 0)
                .to(11, 13, 16)
                .allFaces((direction, face) -> face.texture("#overlay"));
    }
}