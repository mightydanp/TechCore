package com.mightydanp.techcore.api.resources.assets.contents.blockstate;

import com.google.common.collect.ImmutableMap;
import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import com.mightydanp.techcore.api.resources.assets.contents.model.BlockModelContent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;

import java.util.Map;

public class TCBlockStateContent extends BlockStateContent {
    public TCBlockStateContent(String modid, String blockStateName) {
        super(modid, blockStateName);
    }

    public TCBlockStateContent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void buttonBlock(ButtonBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);

        BlockModelContent buttonModel = new BlockModelContent(name, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder button = buttonModel.button(texture);

        simpleBlockWithItem(block, buttonModel.model());

        BlockModelContent buttonPressedModel = new BlockModelContent(name + "_pressed", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder buttonPressed = buttonPressedModel.buttonPressed(name, texture);

        simpleBlockWithItem(block, buttonPressedModel.model());
        buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(ButtonBlock block, ModelFile button, ModelFile buttonPressed) throws Exception {
        String name = key(block).getPath();
        String modId = key(block).getNamespace();

        VariantBlockStateBuilder builder = getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(ButtonBlock.FACING);
            AttachFace face = state.getValue(ButtonBlock.FACE);
            boolean powered = state.getValue(ButtonBlock.POWERED);

            return ConfiguredModel.builder()
                    .modelFile(powered ? buttonPressed : button)
                    .rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
                    .rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot())
                    .uvLock(face == AttachFace.WALL)
                    .build();
        });

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modId, name).setBlockState(builder), true);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void doorBlock(DoorBlock block, ResourceLocation bottom, ResourceLocation top) throws Exception {
        doorBlockInternal(block, key(block).toString(), bottom, top);
    }

    public void doorBlock(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top) throws Exception {
        doorBlockInternal(block, name + "_door", bottom, top);
    }

    public void doorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top, String renderType) throws Exception {
        doorBlockInternalWithRenderType(block, key(block).toString(), bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void doorBlockWithRenderType(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top, String renderType) throws Exception {
        doorBlockInternalWithRenderType(block, name + "_door", bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void doorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        doorBlockInternalWithRenderType(block, key(block).toString(), bottom, top, renderType);
    }

    public void doorBlockWithRenderType(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        doorBlockInternalWithRenderType(block, name + "_door", bottom, top, renderType);
    }

    private void doorBlockInternal(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top) throws Exception {
        String name = name(block);

        BlockModelContent bottomLeftModel = new BlockModelContent(name + "_bottom_left", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomLeftBuilder = bottomLeftModel.doorBottomLeft(bottom, top);

        simpleBlockWithItem(block, bottomLeftModel.model());

        BlockModelContent bottomLeftOpenModel = new BlockModelContent(name + "_bottom_left_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomLeftOpenBuilder = bottomLeftOpenModel.doorBottomLeftOpen(bottom, top);

        simpleBlockWithItem(block, bottomLeftOpenModel.model());

        BlockModelContent bottomRightModel = new BlockModelContent(name + "_bottom_right", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomRightBuilder = bottomRightModel.doorBottomRight(bottom, top);

        simpleBlockWithItem(block, bottomRightModel.model());

        BlockModelContent bottomRightOpenModel = new BlockModelContent(name + "_bottom_right_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomRightOpenBuilder = bottomRightOpenModel.doorBottomRightOpen(bottom, top);

        simpleBlockWithItem(block, bottomRightOpenModel.model());

        BlockModelContent topLeftModel = new BlockModelContent(name + "_top_left", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topLeftBuilder = topLeftModel.doorTopLeft(bottom, top);

        simpleBlockWithItem(block, topLeftModel.model());

        BlockModelContent topLeftOpenModel = new BlockModelContent(name + "_top_left_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topLeftOpenBuilder = topLeftOpenModel.doorTopLeftOpen(bottom, top);

        simpleBlockWithItem(block, topLeftOpenModel.model());

        BlockModelContent topRightModel = new BlockModelContent(name + "_top_right", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topRightBuilder = topRightModel.doorTopRight(bottom, top);

        simpleBlockWithItem(block, topRightModel.model());

        BlockModelContent topRightOpenModel = new BlockModelContent(name + "_top_right_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topRightOpenBuilder = topRightOpenModel.doorTopRightOpen(bottom, top);

        simpleBlockWithItem(block, topRightOpenModel.model());

        doorBlock(block, bottomLeftBuilder, bottomLeftOpenBuilder, bottomRightBuilder, bottomRightOpenBuilder, topLeftBuilder, topLeftOpenBuilder, topRightBuilder, topRightOpenBuilder);
    }

    private void doorBlockInternalWithRenderType(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        String name = name(block);

        BlockModelContent bottomLeftModel = new BlockModelContent(name + "_bottom_left", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomLeftBuilder = bottomLeftModel.doorBottomLeft(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, bottomLeftModel.model());

        BlockModelContent bottomLeftOpenModel = new BlockModelContent(name + "_bottom_left_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomLeftOpenBuilder = bottomLeftOpenModel.doorBottomLeftOpen(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, bottomLeftOpenModel.model());

        BlockModelContent bottomRightModel = new BlockModelContent(name + "_bottom_right", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomRightBuilder = bottomRightModel.doorBottomRight(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, bottomRightModel.model());

        BlockModelContent bottomRightOpenModel = new BlockModelContent(name + "_bottom_right_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder bottomRightOpenBuilder = bottomRightOpenModel.doorBottomRightOpen(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, bottomRightOpenModel.model());

        BlockModelContent topLeftModel = new BlockModelContent(name + "_top_left", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topLeftBuilder = topLeftModel.doorTopLeft(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, topLeftModel.model());

        BlockModelContent topLeftOpenModel = new BlockModelContent(name + "_top_left_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topLeftOpenBuilder = topLeftOpenModel.doorTopLeftOpen(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, topLeftOpenModel.model());

        BlockModelContent topRightModel = new BlockModelContent(name + "_top_right", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topRightBuilder = topRightModel.doorTopRight(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, topRightModel.model());

        BlockModelContent topRightOpenModel = new BlockModelContent(name + "_top_right_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder topRightOpenBuilder = topRightOpenModel.doorTopRightOpen(bottom, top).renderType(renderType);

        simpleBlockWithItem(block, topRightOpenModel.model());

        doorBlock(block, bottomLeftBuilder, bottomLeftOpenBuilder, bottomRightBuilder, bottomRightOpenBuilder, topLeftBuilder, topLeftOpenBuilder, topRightBuilder, topRightOpenBuilder);
    }

    public void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block).forAllStatesExcept(state -> {
            int yRot = ((int) state.getValue(DoorBlock.FACING).toYRot()) + 90;
            boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            boolean open = state.getValue(DoorBlock.OPEN);
            boolean lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
            if (open) {
                yRot += 90;
            }
            if (right && open) {
                yRot += 180;
            }
            yRot %= 360;

            ModelFile model = null;
            if (lower && right && open) {
                model = bottomRightOpen;
            } else if (lower && !right && open) {
                model = bottomLeftOpen;
            }
            if (lower && right && !open) {
                model = bottomRight;
            } else if (lower && !right && !open) {
                model = bottomLeft;
            }
            if (!lower && right && open) {
                model = topRightOpen;
            } else if (!lower && !right && open) {
                model = topLeftOpen;
            }
            if (!lower && right && !open) {
                model = topRight;
            } else if (!lower && !right && !open) {
                model = topLeft;
            }

            return ConfiguredModel.builder().modelFile(model)
                    .rotationY(yRot)
                    .build();
        }, DoorBlock.POWERED);

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void fenceBlock(FenceBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);
        String baseName = key(block).toString();

        BlockModelContent fencePostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_post", texture);

        simpleBlockWithItem(block, fencePostModel.model());

        BlockModelContent fenceSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_side", texture);

        simpleBlockWithItem(block, fenceSideModel.model());
        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlock(FenceBlock block, String name, ResourceLocation texture) throws Exception {
        String baseName = key(block).toString();

        BlockModelContent fencePostModel = new BlockModelContent(name + "_fence_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_fence_post", texture);

        simpleBlockWithItem(block, fencePostModel.model());

        BlockModelContent fenceSideModel = new BlockModelContent(name + "_fence_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_fence_side", texture);

        simpleBlockWithItem(block, fenceSideModel.model());

        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlockWithRenderType(FenceBlock block, ResourceLocation texture, String renderType) throws Exception {
        String name = name(block);
        String baseName = key(block).toString();

        BlockModelContent fencePostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_post", texture).renderType(renderType);

        simpleBlockWithItem(block, fencePostModel.model());

        BlockModelContent fenceSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_side", texture).renderType(renderType);

        simpleBlockWithItem(block, fenceSideModel.model());
        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlockWithRenderType(FenceBlock block, String name, ResourceLocation texture, String renderType) throws Exception {
        String baseName = key(block).toString();

        BlockModelContent fencePostModel = new BlockModelContent(name + "_fence_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_fence_post", texture).renderType(renderType);

        simpleBlockWithItem(block, fencePostModel.model());

        BlockModelContent fenceSideModel = new BlockModelContent(name + "_fence_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_fence_side", texture).renderType(renderType);

        simpleBlockWithItem(block, fenceSideModel.model());

        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

//----------------------------------------------------------------------------------------------------------------------

    public void fenceGateBlock(FenceGateBlock block, ResourceLocation texture) throws Exception {
        fenceGateBlockInternal(block, key(block).toString(), texture);
    }

    public void fenceGateBlock(FenceGateBlock block, String name, ResourceLocation texture) throws Exception {
        fenceGateBlockInternal(block, name + "_fence_gate", texture);
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, ResourceLocation texture, String renderType) throws Exception {
        fenceGateBlockInternalWithRenderType(block, key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, String name, ResourceLocation texture, String renderType) throws Exception {
        fenceGateBlockInternalWithRenderType(block, name + "_fence_gate", texture, ResourceLocation.tryParse(renderType));
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        fenceGateBlockInternalWithRenderType(block, key(block).toString(), texture, renderType);
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, String name, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        fenceGateBlockInternalWithRenderType(block, name + "_fence_gate", texture, renderType);
    }

    private void fenceGateBlockInternal(FenceGateBlock block, String name, ResourceLocation texture) throws Exception {
        String baseName = key(block).toString();

        BlockModelContent fenceGateModel = new BlockModelContent(name, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateBuilder = fenceGateModel.fenceGate(baseName, texture);

        simpleBlockWithItem(block, fenceGateModel.model());

        BlockModelContent fenceGateOpenModel = new BlockModelContent(name + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateOpenBuilder = fenceGateOpenModel.fenceGateOpen(baseName + "_open", texture);

        simpleBlockWithItem(block, fenceGateOpenModel.model());

        BlockModelContent fenceGateWallModel = new BlockModelContent(name + "_wall", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallBuilder = fenceGateWallModel.fenceGateWall(baseName + "_wall", texture);

        simpleBlockWithItem(block, fenceGateWallModel.model());

        BlockModelContent fenceGateWallOpenModel = new BlockModelContent(name + "_wall_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallOpenBuilder = fenceGateWallOpenModel.fenceGateWallOpen(baseName + "_wall_open", texture);

        simpleBlockWithItem(block, fenceGateWallOpenModel.model());

        fenceGateBlock(block, fenceGateBuilder, fenceGateOpenBuilder, fenceGateWallBuilder, fenceGateWallOpenBuilder);
    }

    private void fenceGateBlockInternalWithRenderType(FenceGateBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        BlockModelContent fenceGateModel = new BlockModelContent(baseName, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateBuilder = fenceGateModel.fenceGate(baseName, texture);

        simpleBlockWithItem(block, fenceGateModel.model());

        BlockModelContent fenceGateOpenModel = new BlockModelContent(baseName + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateOpenBuilder = fenceGateOpenModel.fenceGateOpen(baseName + "_open", texture);

        simpleBlockWithItem(block, fenceGateOpenModel.model());

        BlockModelContent fenceGateWallModel = new BlockModelContent(baseName + "_wall", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallBuilder = fenceGateWallModel.fenceGateWall(baseName + "_wall", texture);

        simpleBlockWithItem(block, fenceGateWallModel.model());

        BlockModelContent fenceGateWallOpenModel = new BlockModelContent(baseName + "_wall_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallOpenBuilder = fenceGateWallOpenModel.fenceGateWallOpen(baseName + "_wall_open", texture);

        simpleBlockWithItem(block, fenceGateWallOpenModel.model());

        fenceGateBlock(block, fenceGateBuilder, fenceGateOpenBuilder, fenceGateWallBuilder, fenceGateWallOpenBuilder);
    }

    public void fenceGateBlock(FenceGateBlock block, ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block).forAllStatesExcept(state -> {
            ModelFile model = gate;
            if (state.getValue(FenceGateBlock.IN_WALL)) {
                model = gateWall;
            }
            if (state.getValue(FenceGateBlock.OPEN)) {
                model = model == gateWall ? gateWallOpen : gateOpen;
            }
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) state.getValue(FenceGateBlock.FACING).toYRot())
                    .uvLock(true)
                    .build();
        }, FenceGateBlock.POWERED);

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void hangingSignBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void leavesBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void logBlock(RotatedPillarBlock block) throws Exception {
        axisBlock(block, blockTexture(block), extend(blockTexture(block), "_top"));
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), extend(blockTexture(block), "_top"), renderType);
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, String renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), extend(blockTexture(block), "_top"), renderType);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void paneBlock(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge) throws Exception {
        paneBlockInternal(block, key(block).toString(), pane, edge);
    }

    public void paneBlock(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge) throws Exception {
        paneBlockInternal(block, name + "_pane", pane, edge);
    }

    public void paneBlockWithRenderType(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, String renderType) throws Exception {
        paneBlockInternalWithRenderType(block, key(block).toString(), pane, edge, ResourceLocation.tryParse(renderType));
    }

    public void paneBlockWithRenderType(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge, String renderType) throws Exception {
        paneBlockInternalWithRenderType(block, name + "_pane", pane, edge, ResourceLocation.tryParse(renderType));
    }

    public void paneBlockWithRenderType(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) throws Exception {
        paneBlockInternalWithRenderType(block, key(block).toString(), pane, edge, renderType);
    }

    public void paneBlockWithRenderType(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) throws Exception {
        paneBlockInternalWithRenderType(block, name + "_pane", pane, edge, renderType);
    }

    private void paneBlockInternal(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge) throws Exception {
        String name = name(block);

        BlockModelContent panePostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder panePostBuilder = panePostModel.panePost(pane, edge);

        simpleBlockWithItem(block, panePostModel.model());

        BlockModelContent paneSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneSideBuilder = paneSideModel.paneSide(pane, edge);

        simpleBlockWithItem(block, paneSideModel.model());

        BlockModelContent paneSideAltModel = new BlockModelContent(name + "_side_alt", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneSideAltBuilder = paneSideAltModel.paneSideAlt(pane, edge);

        simpleBlockWithItem(block, paneSideAltModel.model());

        BlockModelContent paneNoSideModel = new BlockModelContent(name + "_noside", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneNoSideBuilder = paneNoSideModel.paneNoSide(pane);

        simpleBlockWithItem(block, paneNoSideModel.model());

        BlockModelContent paneNoSideAltModel = new BlockModelContent(name + "_noside_alt", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneNoSideAltBuilder = paneNoSideAltModel.paneNoSideAlt(pane);

        simpleBlockWithItem(block, paneNoSideAltModel.model());


        paneBlock(block, panePostBuilder, paneSideBuilder, paneSideAltBuilder, paneNoSideBuilder, paneNoSideAltBuilder);
    }

    private void paneBlockInternalWithRenderType(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) throws Exception {
        String name = name(block);

        BlockModelContent panePostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder panePostBuilder = panePostModel.panePost(pane, edge).renderType(renderType);

        simpleBlockWithItem(block, panePostModel.model());

        BlockModelContent paneSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneSideBuilder = paneSideModel.paneSide(pane, edge).renderType(renderType);

        simpleBlockWithItem(block, paneSideModel.model());

        BlockModelContent paneSideAltModel = new BlockModelContent(name + "_side_alt", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneSideAltBuilder = paneSideAltModel.paneSideAlt(pane, edge).renderType(renderType);

        simpleBlockWithItem(block, paneSideAltModel.model());

        BlockModelContent paneNoSideModel = new BlockModelContent(name + "_noside", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneNoSideBuilder = paneNoSideModel.paneNoSide(pane).renderType(renderType);

        simpleBlockWithItem(block, paneNoSideModel.model());

        BlockModelContent paneNoSideAltModel = new BlockModelContent(name + "_noside_alt", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder paneNoSideAltBuilder = paneNoSideAltModel.paneNoSideAlt(pane).renderType(renderType);

        simpleBlockWithItem(block, paneNoSideAltModel.model());


        paneBlock(block, panePostBuilder, paneSideBuilder, paneSideAltBuilder, paneNoSideBuilder, paneNoSideAltBuilder);
    }

    public void paneBlock(IronBarsBlock block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) throws Exception {
        String name = name(block);
        String modid = modid(block);

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
            if (dir.getAxis().isHorizontal()) {
                boolean alt = dir == Direction.SOUTH;
                builder.part().modelFile(alt || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel()
                        .condition(value, true).end()
                        .part().modelFile(alt || dir == Direction.EAST ? noSideAlt : noSide).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel()
                        .condition(value, false);
            }
        });

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void planksBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);

        BlockModelContent pressurePlateModel = new BlockModelContent(name, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder pressurePlate = pressurePlateModel.button(texture);

        simpleBlockWithItem(block, pressurePlateModel.model());

        BlockModelContent pressurePlateDownModel = new BlockModelContent(name + "_down", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder pressurePlateDown = pressurePlateDownModel.button(texture);

        simpleBlockWithItem(block, pressurePlateDownModel.model());

        pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder
                .partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
                .partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void saplingBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, ResourceLocation texture) throws Exception {
        String name = name(signBlock);

        BlockModelContent model = new BlockModelContent(name, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder sign = model.sign(name, texture);

        simpleBlockWithItem(signBlock, model.model());
        signBlock(signBlock, wallSignBlock, sign);
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, TCModelBuilder sign) throws Exception {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void slabBlock(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation texture) throws Exception {
        slabBlock(block, doubleSlab, texture, texture, texture);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) throws Exception {
        String name = name(block);
        BlockModelContent slabModel = new BlockModelContent(name, BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder slab = slabModel.slab(side, bottom, top);

        simpleBlockWithItem(block, slabModel.model());

        BlockModelContent slabTopModel = new BlockModelContent(name + "_top", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder slabTop = slabTopModel.slab(side, bottom, top);

        simpleBlockWithItem(block, slabTopModel.model());

        slabBlock(block, slab, slabTop, new ModelFile.UncheckedModelFile(doubleSlab));
    }

    public void slabBlock(SlabBlock block, ModelFile bottom, ModelFile top, ModelFile doubleslab) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleslab));

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void stairsBlock(StairBlock block, ResourceLocation texture) throws Exception {
        stairsBlock(block, texture, texture, texture);
    }

    public void stairsBlock(StairBlock block, String name, ResourceLocation texture) throws Exception {
        stairsBlock(block, name, texture, texture, texture);
    }

    public void stairsBlock(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) throws Exception {
        stairsBlockInternal(block, key(block).toString(), side, bottom, top);
    }

    public void stairsBlock(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) throws Exception {
        stairsBlockInternal(block, name + "_stairs", side, bottom, top);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation texture, String renderType) throws Exception {
        stairsBlockWithRenderType(block, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation texture, String renderType) throws Exception {
        stairsBlockWithRenderType(block, name, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, String renderType) throws Exception {
        stairsBlockInternalWithRenderType(block, key(block).toString(), side, bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, String renderType) throws Exception {
        stairsBlockInternalWithRenderType(block, name + "_stairs", side, bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        stairsBlockWithRenderType(block, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        stairsBlockWithRenderType(block, name, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        stairsBlockInternalWithRenderType(block, key(block).toString(), side, bottom, top, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        stairsBlockInternalWithRenderType(block, name + "_stairs", side, bottom, top, renderType);
    }

    private void stairsBlockInternal(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) throws Exception {
        BlockModelContent stairsModel = new BlockModelContent(baseName, BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairs = stairsModel.stairs(side, bottom, top);

        simpleBlockWithItem(block, stairsModel.model());

        BlockModelContent stairsInnerModel = new BlockModelContent(baseName + "_inner", BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairsInner = stairsInnerModel.stairsInner(side, bottom, top);

        simpleBlockWithItem(block, stairsInnerModel.model());

        BlockModelContent stairsOuterModel = new BlockModelContent(baseName + "_outer", BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairsOuter = stairsOuterModel.stairsOuter(side, bottom, top);

        simpleBlockWithItem(block, stairsOuterModel.model());
        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    private void stairsBlockInternalWithRenderType(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        BlockModelContent stairsModel = new BlockModelContent(baseName, BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairs = stairsModel.stairs(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsModel.model());

        BlockModelContent stairsInnerModel = new BlockModelContent(baseName + "_inner", BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairsInner = stairsInnerModel.stairsInner(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsInnerModel.model());

        BlockModelContent stairsOuterModel = new BlockModelContent(baseName + "_outer", BlockModelContent.BLOCK_FOLDER, "");
        ModelFile stairsOuter = stairsOuterModel.stairsOuter(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsOuterModel.model());
        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    public void stairsBlock(StairBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(StairBlock.FACING);
                    Half half = state.getValue(StairBlock.HALF);
                    StairsShape shape = state.getValue(StairBlock.SHAPE);
                    int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
                    if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                        yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
                    }
                    if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                        yRot += 90; // Top stairs are rotated 90 degrees clockwise
                    }
                    yRot %= 360;
                    boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
                    return ConfiguredModel.builder()
                            .modelFile(shape == StairsShape.STRAIGHT ? stairs : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? stairsInner : stairsOuter)
                            .rotationX(half == Half.BOTTOM ? 0 : 180)
                            .rotationY(yRot)
                            .uvLock(uvlock)
                            .build();
                }, StairBlock.WATERLOGGED);

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void trapdoorBlock(TrapDoorBlock block, ResourceLocation texture, boolean orientable) throws Exception {
        trapdoorBlockInternal(block, key(block).toString(), texture, orientable);
    }

    public void trapdoorBlock(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable) throws Exception {
        trapdoorBlockInternal(block, name + "_trapdoor", texture, orientable);
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, ResourceLocation texture, boolean orientable, String renderType) throws Exception {
        trapdoorBlockInternalWithRenderType(block, key(block).toString(), texture, orientable, ResourceLocation.tryParse(renderType));
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable, String renderType) throws Exception {
        trapdoorBlockInternalWithRenderType(block, name + "_trapdoor", texture, orientable, ResourceLocation.tryParse(renderType));
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, ResourceLocation texture, boolean orientable, ResourceLocation renderType) throws Exception {
        trapdoorBlockInternalWithRenderType(block, key(block).toString(), texture, orientable, renderType);
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable, ResourceLocation renderType) throws Exception {
        trapdoorBlockInternalWithRenderType(block, name + "_trapdoor", texture, orientable, renderType);
    }

    private void trapdoorBlockInternal(TrapDoorBlock block, String baseName, ResourceLocation texture, boolean orientable) throws Exception {
        BlockModelContent trapdoorOrientableBottomModel = new BlockModelContent(baseName + "_bottom", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableBottomBuilder = trapdoorOrientableBottomModel.trapdoorOrientableBottom(baseName + "_bottom", texture);

        simpleBlockWithItem(block, trapdoorOrientableBottomModel.model());

        BlockModelContent trapdoorBottomModel = new BlockModelContent(baseName + "_bottom", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorBottomBuilder = trapdoorBottomModel.trapdoorBottom(baseName + "_bottom", texture);

        simpleBlockWithItem(block, trapdoorBottomModel.model());

        BlockModelContent trapdoorOrientableTopModel = new BlockModelContent(baseName + "_top", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableTopBuilder = trapdoorOrientableTopModel.trapdoorOrientableTop(baseName + "_top", texture);

        simpleBlockWithItem(block, trapdoorOrientableTopModel.model());

        BlockModelContent trapdoorTopModel = new BlockModelContent(baseName + "_top", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorTopBuilder = trapdoorTopModel.trapdoorTop(baseName + "_top", texture);

        simpleBlockWithItem(block, trapdoorTopModel.model());

        BlockModelContent trapdoorOrientableOpenModel = new BlockModelContent(baseName + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableOpenBuilder = trapdoorOrientableOpenModel.trapdoorOrientableOpen(baseName + "_open", texture);

        simpleBlockWithItem(block, trapdoorOrientableOpenModel.model());

        BlockModelContent trapdoorOpenModel = new BlockModelContent(baseName + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOpenBuilder = trapdoorOpenModel.trapdoorOpen(baseName + "_open", texture);

        simpleBlockWithItem(block, trapdoorOpenModel.model());

        ModelFile bottom = orientable ? trapdoorOrientableBottomBuilder : trapdoorBottomBuilder;
        ModelFile top = orientable ? trapdoorOrientableTopBuilder : trapdoorTopBuilder;
        ModelFile open = orientable ? trapdoorOrientableOpenBuilder : trapdoorOpenBuilder;

        trapdoorBlock(block, bottom, top, open, orientable);
    }

    private void trapdoorBlockInternalWithRenderType(TrapDoorBlock block, String baseName, ResourceLocation texture, boolean orientable, ResourceLocation renderType) throws Exception {
        BlockModelContent trapdoorOrientableBottomModel = new BlockModelContent(baseName + "_bottom", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableBottomBuilder = trapdoorOrientableBottomModel.trapdoorOrientableBottom(baseName + "_bottom", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorOrientableBottomModel.model());

        BlockModelContent trapdoorBottomModel = new BlockModelContent(baseName + "_bottom", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorBottomBuilder = trapdoorBottomModel.trapdoorBottom(baseName + "_bottom", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorBottomModel.model());

        BlockModelContent trapdoorOrientableTopModel = new BlockModelContent(baseName + "_top", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableTopBuilder = trapdoorOrientableTopModel.trapdoorOrientableTop(baseName + "_top", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorOrientableTopModel.model());

        BlockModelContent trapdoorTopModel = new BlockModelContent(baseName + "_top", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorTopBuilder = trapdoorTopModel.trapdoorTop(baseName + "_top", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorTopModel.model());

        BlockModelContent trapdoorOrientableOpenModel = new BlockModelContent(baseName + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOrientableOpenBuilder = trapdoorOrientableOpenModel.trapdoorOrientableOpen(baseName + "_open", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorOrientableOpenModel.model());

        BlockModelContent trapdoorOpenModel = new BlockModelContent(baseName + "_open", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder trapdoorOpenBuilder = trapdoorOpenModel.trapdoorOpen(baseName + "_open", texture).renderType(renderType);

        simpleBlockWithItem(block, trapdoorOpenModel.model());

        ModelFile bottom = orientable ? trapdoorOrientableBottomBuilder : trapdoorBottomBuilder;
        ModelFile top = orientable ? trapdoorOrientableTopBuilder : trapdoorTopBuilder;
        ModelFile open = orientable ? trapdoorOrientableOpenBuilder : trapdoorOpenBuilder;

        trapdoorBlock(block, bottom, top, open, orientable);
    }

    public void trapdoorBlock(TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open, boolean orientable) throws Exception {
        String name = name(block);
        String modid = modid(block);

        VariantBlockStateBuilder builder = getVariantBuilder(block).forAllStatesExcept(state -> {
            int xRot = 0;
            int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
            boolean isOpen = state.getValue(TrapDoorBlock.OPEN);
            if (orientable && isOpen && state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                xRot += 180;
                yRot += 180;
            }
            if (!orientable && !isOpen) {
                yRot = 0;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(isOpen ? open : state.getValue(TrapDoorBlock.HALF) == Half.TOP ? top : bottom)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void wallBlock(WallBlock block, ResourceLocation texture) throws Exception {
        wallBlockInternal(block, key(block).toString(), texture);
    }

    public void wallBlock(WallBlock block, String name, ResourceLocation texture) throws Exception {
        wallBlockInternal(block, name + "_wall", texture);
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, String renderType) throws Exception {
        wallBlockInternalWithRenderType(block, key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, String renderType) throws Exception {
        wallBlockInternalWithRenderType(block, name + "_wall", texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        wallBlockInternalWithRenderType(block, key(block).toString(), texture, renderType);
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        wallBlockInternalWithRenderType(block, name + "_wall", texture, renderType);
    }

    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) throws Exception {
        String name = name(block);

        BlockModelContent wallPostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallPostBuilder = wallPostModel.wallPost(texture);

        simpleBlockWithItem(block, wallPostModel.model());

        BlockModelContent wallSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallSideBuilder = wallSideModel.wallSide(texture);

        simpleBlockWithItem(block, wallSideModel.model());

        BlockModelContent wallSideTallModel = new BlockModelContent(name + "_side_tall", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallSideTallBuilder = wallSideTallModel.wallSideTall(texture);

        simpleBlockWithItem(block, wallSideTallModel.model());

        wallBlock(block, wallPostBuilder, wallSideBuilder, wallSideTallBuilder);
    }

    private void wallBlockInternalWithRenderType(WallBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        String name = name(block);

        BlockModelContent wallPostModel = new BlockModelContent(name + "_post", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallPostBuilder = wallPostModel.wallPost(texture).renderType(renderType);

        simpleBlockWithItem(block, wallPostModel.model());

        BlockModelContent wallSideModel = new BlockModelContent(name + "_side", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallSideBuilder = wallSideModel.wallSide(texture).renderType(renderType);

        simpleBlockWithItem(block, wallSideModel.model());

        BlockModelContent wallSideTallModel = new BlockModelContent(name + "_side_tall", BlockModelContent.BLOCK_FOLDER, "");
        TCModelBuilder wallSideTallBuilder = wallSideTallModel.wallSideTall(texture).renderType(renderType);

        simpleBlockWithItem(block, wallSideTallModel.model());

        wallBlock(block, wallPostBuilder, wallSideBuilder, wallSideTallBuilder);
    }

    public static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.<Direction, Property<WallSide>>builder()
            .put(Direction.EAST, BlockStateProperties.EAST_WALL)
            .put(Direction.NORTH, BlockStateProperties.NORTH_WALL)
            .put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL)
            .put(Direction.WEST, BlockStateProperties.WEST_WALL)
            .build();

    public void wallBlock(WallBlock block, ModelFile post, ModelFile side, ModelFile sideTall) throws Exception {
        String name = name(block);
        String modid = modid(block);

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel()
                .condition(WallBlock.UP, true).end();
        WALL_PROPS.entrySet().stream()
                .filter(e -> e.getKey().getAxis().isHorizontal())
                .forEach(e -> {
                    wallSidePart(builder, side, e, WallSide.LOW);
                    wallSidePart(builder, sideTall, e, WallSide.TALL);
                });

        AssetPackRegistries.saveBlockState(name, new BlockStateContent(modid, name).setBlockState(builder), true);
    }

    private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Map.Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part()
                .modelFile(model)
                .rotationY((((int) entry.getKey().toYRot()) + 180) % 360)
                .uvLock(true)
                .addModel()
                .condition(entry.getValue(), height);
    }

}
