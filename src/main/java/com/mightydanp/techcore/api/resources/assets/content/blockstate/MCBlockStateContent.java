package com.mightydanp.techcore.api.resources.assets.content.blockstate;

import com.google.common.collect.ImmutableMap;
import com.mightydanp.techcore.api.resources.assets.content.model.ModelBuilder;
import com.mightydanp.techcore.api.resources.assets.content.model.block.MCBlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.MCItemModelContent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MCBlockStateContent extends BlockStateContent<MCBlockStateContent> {
    public static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.<Direction, Property<WallSide>>builder()
            .put(Direction.EAST, BlockStateProperties.EAST_WALL)
            .put(Direction.NORTH, BlockStateProperties.NORTH_WALL)
            .put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL)
            .put(Direction.WEST, BlockStateProperties.WEST_WALL)
            .build();

    public MCBlockStateContent(String modid, String name) {
        super(modid, name);
    }

    public MCBlockStateContent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }
    //------------------------------------------------------------------------------------------------------------------

    private static int getyRot(@NotNull Direction facing, StairsShape shape, Half half) {
        int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
        if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
            yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
        }
        if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
            yRot += 90; // Top stairs are rotated 90 degrees clockwise
        }
        yRot %= 360;
        return yRot;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void axisBlock(RotatedPillarBlock block, String baseName, ResourceLocation particle, Map<Integer, MCBlockModelContent.ColumnAssetRecord> columnTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) throws Exception {
        MCBlockModelContent columnModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent columnHorizontalModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            columnModel.model().renderType(renderType);
            columnHorizontalModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            columnModel.model().renderType(renderType, renderTypeFast);
            columnHorizontalModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> column = columnModel.mcColumnModelDefine(baseName, particle, columnTextures);
        ModelBuilder<?> columnHorizontal = columnModel.mcColumnHorizontalModelDefine(baseName, particle, columnTextures);
        itemModel.mcLog(columnModel);

        axisBlock(block, column, columnHorizontal);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void buttonBlock(ButtonBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent buttonModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent buttonPressedModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_pressed"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            buttonModel.model().renderType(renderType);
            buttonPressedModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            buttonModel.model().renderType(renderType, renderTypeFast);
            buttonPressedModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> button = buttonModel.mcButtonModel(baseName, particle, textures);
        ModelBuilder<?> buttonPressed = buttonPressedModel.mcButtonPressedModel(baseName, particle, textures);
        itemModel.mcButton(buttonModel);

        buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(ButtonBlock block, ModelFile button, ModelFile buttonPressed) {
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

        this.setBlockState(builder).save(false);
    }

    private void doorBlockInternal(DoorBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> itemTextures, Map<Integer, ResourceLocation> bottomTextures, Map<Integer, ResourceLocation> topTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent bottomLeftBlockModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom_left"), "");
        MCBlockModelContent bottomLeftOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom_left_open"), "");
        MCBlockModelContent bottomRightModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom_right"), "");
        MCBlockModelContent bottomRightOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom_right_open"), "");
        MCBlockModelContent topLeftModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top_left"), "");
        MCBlockModelContent topLeftOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top_left_open"), "");
        MCBlockModelContent topRightModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top_right"), "");
        MCBlockModelContent topRightOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top_right_open"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            bottomLeftBlockModel.model().renderType(renderType);
            bottomLeftOpenModel.model().renderType(renderType);
            bottomRightModel.model().renderType(renderType);
            bottomRightOpenModel.model().renderType(renderType);
            topLeftModel.model().renderType(renderType);
            topLeftOpenModel.model().renderType(renderType);
            topRightModel.model().renderType(renderType);
            topRightOpenModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            bottomLeftBlockModel.model().renderType(renderType, renderTypeFast);
            bottomLeftOpenModel.model().renderType(renderType, renderTypeFast);
            bottomRightModel.model().renderType(renderType, renderTypeFast);
            bottomRightOpenModel.model().renderType(renderType, renderTypeFast);
            topLeftModel.model().renderType(renderType, renderTypeFast);
            topLeftOpenModel.model().renderType(renderType, renderTypeFast);
            topRightModel.model().renderType(renderType, renderTypeFast);
            topRightOpenModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> bottomLeft = bottomLeftBlockModel.mcDoorBottomLeftModel(baseName, particle, bottomTextures);
        ModelBuilder<?> bottomLeftOpen = bottomLeftOpenModel.mcDoorBottomLeftOpenModel(baseName, particle, bottomTextures);
        ModelBuilder<?> bottomRight = bottomRightModel.mcDoorBottomRightModel(baseName, particle, bottomTextures);
        ModelBuilder<?> bottomRightOpen = bottomRightOpenModel.mcDoorBottomRightOpenModel(baseName, particle, bottomTextures);
        ModelBuilder<?> topLeft = topLeftModel.mcDoorTopLeftModel(baseName, particle, topTextures);
        ModelBuilder<?> topLeftOpen = topLeftOpenModel.mcDoorTopLeftOpenModel(baseName, particle, topTextures);
        ModelBuilder<?> topRight = topRightModel.mcDoorTopRightModel(baseName, particle, topTextures);
        ModelBuilder<?> topRightOpen = topRightOpenModel.mcDoorTopRightOpenModel(baseName, particle, topTextures);
        itemModel.mcDoor(itemTextures);

        doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

//----------------------------------------------------------------------------------------------------------------------

    public void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) {
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

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void fenceBlockInternal(FenceBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> inventoryTextures, Map<Integer, ResourceLocation> postTextures, Map<Integer, ResourceLocation> sideTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) throws Exception {
        MCBlockModelContent fenceInventoryModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inventory"), "");
        MCBlockModelContent fencePostModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), "");
        MCBlockModelContent fenceSideModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            fenceInventoryModel.model().renderType(renderType);
            fencePostModel.model().renderType(renderType);
            fenceSideModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            fenceInventoryModel.model().renderType(renderType, renderTypeFast);
            fencePostModel.model().renderType(renderType, renderTypeFast);
            fenceSideModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> fenceInventory = fencePostModel.mcFenceInventoryModel(baseName, particle, inventoryTextures);
        ModelBuilder<?> fencePost = fencePostModel.mcFencePostModel(baseName, particle, postTextures);
        ModelBuilder<?> fenceSide = fenceSideModel.mcFenceSideModel(baseName, particle, sideTextures);
        itemModel.mcFence(fenceInventoryModel);

        fourWayBlock(block, fencePost, fenceSide);
    }

    private void fenceGateBlockInternal(FenceGateBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, Map<Integer, ResourceLocation> openTextures, Map<Integer, ResourceLocation> wallTextures, Map<Integer, ResourceLocation> wallOpenTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent fenceGateModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent fenceGateOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_open"), "");
        MCBlockModelContent fenceGateWallModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_wall"), "");
        MCBlockModelContent fenceGateWallOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_wall_open"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            fenceGateModel.model().renderType(renderType);
            fenceGateOpenModel.model().renderType(renderType);
            fenceGateWallModel.model().renderType(renderType);
            fenceGateWallOpenModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            fenceGateModel.model().renderType(renderType, renderTypeFast);
            fenceGateOpenModel.model().renderType(renderType, renderTypeFast);
            fenceGateWallModel.model().renderType(renderType, renderTypeFast);
            fenceGateWallOpenModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> fenceGate = fenceGateModel.mcFenceGateModel(baseName, particle, textures);
        ModelBuilder<?> fenceGateOpen = fenceGateOpenModel.mcFenceGateOpenModel(baseName, particle, openTextures);
        ModelBuilder<?> fenceGateWall = fenceGateWallModel.mcFenceGateWallModel(baseName, particle, wallTextures);
        ModelBuilder<?> fenceGateWallOpen = fenceGateWallOpenModel.mcFenceGateWallOpenModel(baseName, particle, wallOpenTextures);
        itemModel.mcFenceGate(fenceGateModel);

        fenceGateBlock(block, fenceGate, fenceGateOpen, fenceGateWall, fenceGateWallOpen);
    }

    public void fenceGateBlock(FenceGateBlock block, ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen) {
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

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void hangingSignBlockInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> itemTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent hangingSignModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            hangingSignModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            hangingSignModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> hangingSign = hangingSignModel.mcHangingSignModel(baseName, particle);
        itemModel.mcSign(itemTextures);

        hangingSignBlock(block, hangingSign);
    }

    public void hangingSignBlock(Block block, ModelFile model) {
        simpleBlock(block, new ConfiguredModel(model));
    }

    //------------------------------------------------------------------------------------------------------------------
    public void leavesInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent leavesModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            leavesModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            leavesModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> leaves = leavesModel.mcLeavesModel(baseName, particle, textures);
        itemModel.mcLeaves(leavesModel);

        leavesBlock(block, leaves);
    }

    public void leavesBlock(Block block, ModelFile model) {
        simpleBlock(block, new ConfiguredModel(model));
    }

    //------------------------------------------------------------------------------------------------------------------
    public void logBlockInternal(RotatedPillarBlock block, String baseName, ResourceLocation particle, Map<Integer, MCBlockModelContent.LogAssetRecord> logTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) throws Exception {
        MCBlockModelContent logsModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent logsHorizontalModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_horizontal"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            logsModel.model().renderType(renderType);
            logsHorizontalModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            logsModel.model().renderType(renderType, renderTypeFast);
            logsHorizontalModel.model().renderType(renderType, renderTypeFast);
        }


        ModelBuilder<?> logs = logsModel.mcLogModel(baseName, particle, logTextures);
        ModelBuilder<?> logsHorizontal = logsHorizontalModel.mcLogHorizontalModel(baseName, particle, logTextures);
        itemModel.mcLog(logsModel);


        logBlock(block, logs, logsHorizontal);
    }

    public void logBlock(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) throws Exception {
        this.axisBlock(block, vertical, horizontal);
    }

    //------------------------------------------------------------------------------------------------------------------
    private void paneBlockInternal(IronBarsBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> itemTextures, Map<Integer, MCBlockModelContent.PaneSideAltRecord> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent panePostModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), "");
        MCBlockModelContent paneSideModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), "");
        MCBlockModelContent paneSideAltModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side_alt"), "");
        MCBlockModelContent paneNoSideModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_noside"), "");
        MCBlockModelContent paneNoSideAltModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_noside_alt"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            panePostModel.model().renderType(renderType);
            paneSideModel.model().renderType(renderType);
            paneSideAltModel.model().renderType(renderType);
            paneNoSideModel.model().renderType(renderType);
            paneNoSideAltModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            panePostModel.model().renderType(renderType, renderTypeFast);
            paneSideModel.model().renderType(renderType, renderTypeFast);
            paneSideAltModel.model().renderType(renderType, renderTypeFast);
            paneNoSideModel.model().renderType(renderType, renderTypeFast);
            paneNoSideAltModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> panePostBuilder = panePostModel.mcPanePostModel(baseName, particle, textures);
        ModelBuilder<?> paneSideBuilder = paneSideModel.mcPaneSideModel(baseName, particle, textures);
        ModelBuilder<?> paneSideAltBuilder = paneSideAltModel.mcPaneSideAltModel(baseName, particle, textures);
        ModelBuilder<?> paneNoSideBuilder = paneNoSideModel.mcPaneNoSideModel(baseName, particle, textures);
        ModelBuilder<?> paneNoSideAltBuilder = paneNoSideAltModel.mcPaneNoSideAltModel(baseName, particle, textures);
        itemModel.mcPane(itemTextures);

        paneBlock(block, panePostBuilder, paneSideBuilder, paneSideAltBuilder, paneNoSideBuilder, paneNoSideAltBuilder);
    }

    public void paneBlock(IronBarsBlock block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
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

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void planksBlockSeparateInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, MCBlockModelContent.CubeSeparateRecord> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent planksModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            planksModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            planksModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> planks = planksModel.mcPlanksSeparateModel(baseName, particle, textures);
        itemModel.mcPlanks(planksModel);

        planksBlock(block, planks);
    }

    public void planksBlockTogetherInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent planksModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            planksModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            planksModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> planks = planksModel.mcPlanksTogetherModel(baseName, particle, textures);

        planksBlock(block, planks);
        itemModel.mcPlanks(planksModel);
    }

    public void planksBlock(Block block, ModelFile model) {
        simpleBlock(block, new ConfiguredModel(model));
    }

    //------------------------------------------------------------------------------------------------------------------
    public void pressurePlateBlock(PressurePlateBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent pressurePlateModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent pressurePlateDownModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_down"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            pressurePlateModel.model().renderType(renderType);
            pressurePlateDownModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            pressurePlateModel.model().renderType(renderType, renderTypeFast);
            pressurePlateDownModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> pressurePlate = pressurePlateModel.mcPressurePlateUpModel(baseName, particle, textures);
        ModelBuilder<?> pressurePlateDown = pressurePlateDownModel.mcPressurePlateDownModel(baseName, particle, textures);
        itemModel.mcPressurePlate(pressurePlateModel);

        pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder
                .partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
                .partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void saplingBlockInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent saplingModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            saplingModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            saplingModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> leaves = saplingModel.mcSaplingModel(baseName, particle, textures);
        itemModel.mcSapling(textures);

        saplingBlock(block, leaves);
    }

    public void saplingBlock(Block block, ModelFile model) {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    public void signBlockInternal(StandingSignBlock signBlock, WallSignBlock wallSignBlock, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> itemTextures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent signModel = new MCBlockModelContent(modid(), name(), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            signModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            signModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> sign = signModel.mcSignModel(baseName, particle);
        itemModel.mcSign(itemTextures);

        signBlock(signBlock, wallSignBlock, sign);
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, ModelBuilder<?> sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    public void slabBlockInternal(SlabBlock block, String baseName, ResourceLocation particle, ResourceLocation doubleSlab, Map<Integer, MCBlockModelContent.SlabRecord> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent slabModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent slabTopModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            slabModel.model().renderType(renderType);
            slabTopModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            slabModel.model().renderType(renderType, renderTypeFast);
            slabTopModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> slab = slabModel.mcSlabModel(baseName, particle, textures);
        ModelBuilder<?> slabTop = slabTopModel.mcTopSlabModel(baseName, particle, textures);
        itemModel.mcSlab(slabModel);

        slabBlock(block, slab, slabTop, new ModelFile.UncheckedModelFile(doubleSlab));
    }

    public void slabBlock(SlabBlock block, ModelFile bottom, ModelFile top, ModelFile doubleslab) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleslab));

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    private void stairsBlockInternal(StairBlock block, String baseName, ResourceLocation particle, Map<Integer, MCBlockModelContent.StairsRecord> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent stairsModel = new MCBlockModelContent(modid(), name(), "");
        MCBlockModelContent stairsInnerModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_inner"), "");
        MCBlockModelContent stairsOuterModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_outer"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            stairsModel.model().renderType(renderType);
            stairsInnerModel.model().renderType(renderType);
            stairsOuterModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            stairsModel.model().renderType(renderType, renderTypeFast);
            stairsInnerModel.model().renderType(renderType, renderTypeFast);
            stairsOuterModel.model().renderType(renderType, renderTypeFast);
        }

        ModelBuilder<?> stairs = stairsModel.mcStairsModel(baseName, particle, textures);
        ModelBuilder<?> stairsInner = stairsInnerModel.mcInnerStairsModel(baseName, particle, textures);
        ModelBuilder<?> stairsOuter = stairsOuterModel.mcOuterStairsModel(baseName, particle, textures);
        itemModel.mcStairs(stairsModel);


        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void stairsBlock(StairBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) {
        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(StairBlock.FACING);
                    Half half = state.getValue(StairBlock.HALF);
                    StairsShape shape = state.getValue(StairBlock.SHAPE);
                    int yRot = getyRot(facing, shape, half);
                    boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
                    return ConfiguredModel.builder()
                            .modelFile(shape == StairsShape.STRAIGHT ? stairs : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? stairsInner : stairsOuter)
                            .rotationX(half == Half.BOTTOM ? 0 : 180)
                            .rotationY(yRot)
                            .uvLock(uvlock)
                            .build();
                }, StairBlock.WATERLOGGED);

        this.setBlockState(builder).save(false);
    }

    private void trapdoorBlockInternal(TrapDoorBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast, boolean orientable) {
        if (orientable) {
            MCBlockModelContent trapdoorOrientableBottomModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom"), "");
            MCBlockModelContent trapdoorOrientableTopModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top"), "");
            MCBlockModelContent trapdoorOrientableOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_open"), "");
            MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

            if (renderType != null) {
                trapdoorOrientableBottomModel.model().renderType(renderType);
                trapdoorOrientableTopModel.model().renderType(renderType);
                trapdoorOrientableOpenModel.model().renderType(renderType);
            }

            if (renderType != null && renderTypeFast != null) {
                trapdoorOrientableBottomModel.model().renderType(renderType, renderTypeFast);
                trapdoorOrientableTopModel.model().renderType(renderType, renderTypeFast);
                trapdoorOrientableOpenModel.model().renderType(renderType, renderTypeFast);
            }

            ModelBuilder<?> trapdoorOrientableBottom = trapdoorOrientableBottomModel.mcOrientableTrapDoorBottomModel(baseName, particle, textures);
            ModelBuilder<?> trapdoorOrientableTop = trapdoorOrientableTopModel.mcOrientableTrapDoorTopModel(baseName, particle, textures);
            ModelBuilder<?> trapdoorOrientableOpen = trapdoorOrientableOpenModel.mcOrientableTrapDoorOpenModel(baseName, particle, textures);
            itemModel.mcTrapDoor(trapdoorOrientableBottomModel);

            trapdoorBlock(block, trapdoorOrientableBottom, trapdoorOrientableTop, trapdoorOrientableOpen, true);
        } else {
            MCBlockModelContent trapdoorBottomModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_bottom"), "");
            MCBlockModelContent trapdoorTopModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_top"), "");
            MCBlockModelContent trapdoorOpenModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_open"), "");
            MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

            if (renderType != null) {
                trapdoorBottomModel.model().renderType(renderType);
                trapdoorTopModel.model().renderType(renderType);
                trapdoorOpenModel.model().renderType(renderType);
            }

            if (renderType != null && renderTypeFast != null) {
                trapdoorBottomModel.model().renderType(renderType, renderTypeFast);
                trapdoorTopModel.model().renderType(renderType, renderTypeFast);
                trapdoorOpenModel.model().renderType(renderType, renderTypeFast);
            }

            ModelBuilder<?> trapdoorBottom = trapdoorBottomModel.mcTrapDoorBottomModel(baseName, particle, textures);
            ModelBuilder<?> trapdoorTop = trapdoorTopModel.mcTrapDoorTopModel(baseName, particle, textures);
            ModelBuilder<?> trapdoorOpen = trapdoorOpenModel.mcTrapDoorOpenModel(baseName, particle, textures);
            itemModel.mcTrapDoor(trapdoorBottomModel);

            trapdoorBlock(block, trapdoorBottom, trapdoorTop, trapdoorOpen, false);
        }
    }

    public void trapdoorBlock(TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open, boolean orientable) {
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

        this.setBlockState(builder).save(false);
    }

    //------------------------------------------------------------------------------------------------------------------
    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, @Nullable ResourceLocation renderTypeFast) {
        MCBlockModelContent wallInventoryModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), "");
        MCBlockModelContent wallPostModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_post"), "");
        MCBlockModelContent wallSideModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side"), "");
        MCBlockModelContent wallSideTallModel = new MCBlockModelContent(ResourceLocation.fromNamespaceAndPath(modid(), name() + "_side_tall"), "");
        MCItemModelContent itemModel = new MCItemModelContent(modid(), name(), "");

        if (renderType != null) {
            wallInventoryModel.model().renderType(renderType);
            wallPostModel.model().renderType(renderType);
            wallSideModel.model().renderType(renderType);
            wallSideTallModel.model().renderType(renderType);
        }

        if (renderType != null && renderTypeFast != null) {
            wallInventoryModel.model().renderType(renderType, renderTypeFast);
            wallPostModel.model().renderType(renderType, renderTypeFast);
            wallSideModel.model().renderType(renderType, renderTypeFast);
            wallSideTallModel.model().renderType(renderType);
        }

        ModelBuilder<?> wallInventoryBuilder = wallPostModel.mcWallInventoryModel(baseName, particle, textures);
        ModelBuilder<?> wallPostBuilder = wallPostModel.mcWallPostModel(baseName, particle, textures);
        ModelBuilder<?> wallSideBuilder = wallSideModel.mcWallSideModel(baseName, particle, textures);
        ModelBuilder<?> wallSideTallBuilder = wallSideTallModel.mcWallSideTallModel(baseName, particle, textures);
        itemModel.mcWall(wallInventoryModel);

        wallBlock(block, wallPostBuilder, wallSideBuilder, wallSideTallBuilder);
    }

    public void wallBlock(WallBlock block, ModelFile post, ModelFile side, ModelFile sideTall) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel()
                .condition(WallBlock.UP, true).end();
        WALL_PROPS.entrySet().stream()
                .filter(e -> e.getKey().getAxis().isHorizontal())
                .forEach(e -> {
                    wallSidePart(builder, side, e, WallSide.LOW);
                    wallSidePart(builder, sideTall, e, WallSide.TALL);
                });

        this.setBlockState(builder).save(false);
    }

    private void wallSidePart(@NotNull MultiPartBlockStateBuilder builder, ModelFile model, Map.@NotNull Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part()
                .modelFile(model)
                .rotationY((((int) entry.getKey().toYRot()) + 180) % 360)
                .uvLock(true)
                .addModel()
                .condition(entry.getValue(), height);
    }

}
