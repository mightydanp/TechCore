package com.mightydanp.techcore.api.resources.assets.contents.blockstate;

import com.google.common.collect.ImmutableMap;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import com.mightydanp.techcore.api.resources.assets.contents.model.TCBlockModelContent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TCBlockStateContent extends BlockStateContent<TCBlockStateContent> {
    public TCBlockStateContent(String modid, String name) {
        super(modid, name);
    }

    public TCBlockStateContent(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void axisBlock(RotatedPillarBlock block, String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.ColumnAssetRecord> columnTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent columnModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent columnHorizontalModel = new TCBlockModelContent(modid(), name(), "");

        if(renderType != null){
            columnModel.model().renderType(renderType);
            columnHorizontalModel.model().renderType(renderType);
        }

        TCModelBuilder<?> column = columnModel.tcColumnModelDefine(baseName, particle, columnTextures);
        TCModelBuilder<?> columnHorizontal = columnModel.tcColumnHorizontalModelDefine(baseName, particle, columnTextures);

        axisBlock(block, column, columnHorizontal);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void buttonBlock(ButtonBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent buttonModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent buttonPressedModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_pressed"), "");

        if(renderType != null){
            buttonModel.model().renderType(renderType);
            buttonPressedModel.model().renderType(renderType);
        }

        TCModelBuilder<?> button = buttonModel.tcButtonModel(baseName, particle, textures);
        TCModelBuilder<?> buttonPressed = buttonPressedModel.tcButtonPressedModel(baseName, particle, textures);

        buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(ButtonBlock block, ModelFile button, ModelFile buttonPressed) throws Exception {
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

    //------------------------------------------------------------------------------------------------------------------

    private void doorBlockInternal(DoorBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> bottomTextures, Map<Integer, ResourceLocation> topTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent bottomLeftBlockModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom_left"), "");
        TCBlockModelContent bottomLeftOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom_left_open"), "");
        TCBlockModelContent bottomRightModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom_right"), "");
        TCBlockModelContent bottomRightOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom_right_open"), "");
        TCBlockModelContent topLeftModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top_left"), "");
        TCBlockModelContent topLeftOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top_left_open"), "");
        TCBlockModelContent topRightModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top_right"), "");
        TCBlockModelContent topRightOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top_right_open"), "");

        if(renderType != null){
            bottomLeftBlockModel.model().renderType(renderType);
            bottomLeftOpenModel.model().renderType(renderType);
            bottomRightModel.model().renderType(renderType);
            bottomRightOpenModel.model().renderType(renderType);
            topLeftModel.model().renderType(renderType);
            topLeftOpenModel.model().renderType(renderType);
            topRightModel.model().renderType(renderType);
            topRightOpenModel.model().renderType(renderType);
        }

        TCModelBuilder<?> bottomLeft = bottomLeftBlockModel.tcDoorBottomLeftModel(baseName, particle, bottomTextures);
        TCModelBuilder<?> bottomLeftOpen =  bottomLeftOpenModel.tcDoorBottomLeftOpenModel(baseName, particle, bottomTextures);
        TCModelBuilder<?> bottomRight =  bottomRightModel.tcDoorBottomRightModel(baseName, particle, bottomTextures);
        TCModelBuilder<?> bottomRightOpen =  bottomRightOpenModel.tcDoorBottomRightOpenModel(baseName, particle, bottomTextures);
        TCModelBuilder<?> topLeft =  topLeftModel.tcDoorTopLeftModel(baseName, particle, topTextures);
        TCModelBuilder<?> topLeftOpen =  topLeftOpenModel.tcDoorTopLeftOpenModel(baseName, particle, topTextures);
        TCModelBuilder<?> topRight =  topRightModel.tcDoorTopRightModel(baseName, particle, topTextures);
        TCModelBuilder<?> topRightOpen =  topRightOpenModel.tcDoorTopRightOpenModel(baseName, particle, topTextures);

        doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    public void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) throws Exception {
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
    public void fenceBlockInternal(FenceBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> postTextures, Map<Integer, ResourceLocation> sideTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent fencePostModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_post"), "");
        TCBlockModelContent fenceSideModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_side"), "");

        if(renderType != null){
            fencePostModel.model().renderType(renderType);
            fenceSideModel.model().renderType(renderType);
        }
        
        TCModelBuilder<?> fencePost = fencePostModel.tcFencePostModel(baseName, particle, postTextures);
        TCModelBuilder<?> fenceSide = fenceSideModel.tcFenceSideModel(baseName, particle, sideTextures);

        fourWayBlock(block, fencePost, fenceSide);
    }

    public void fenceBlock(FenceBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> fencePostTextures, Map<Integer, ResourceLocation> fenceSideTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent fencePostModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_fence_post"), "");
        TCBlockModelContent fenceSideModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_fence_side"), "");

        if(renderType != null){
            fencePostModel.model().renderType(renderType);
            fenceSideModel.model().renderType(renderType);
        }
        
        TCModelBuilder<?> fencePost = fencePostModel.tcFencePostModel(baseName, particle, fencePostTextures);
        TCModelBuilder<?> fenceSide = fenceSideModel.tcFenceSideModel(baseName, particle, fenceSideTextures);

        fourWayBlock(block, fencePost, fenceSide);
    }

//----------------------------------------------------------------------------------------------------------------------

    private void fenceGateBlockInternal(FenceGateBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, Map<Integer, ResourceLocation> openTextures, Map<Integer, ResourceLocation> wallTextures, Map<Integer, ResourceLocation> wallOpenTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent fenceGateModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent fenceGateOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_open"), "");
        TCBlockModelContent fenceGateWallModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_wall"), "");
        TCBlockModelContent fenceGateWallOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_wall_open"), "");

        if(renderType != null){
            fenceGateModel.model().renderType(renderType);
            fenceGateOpenModel.model().renderType(renderType);
            fenceGateWallModel.model().renderType(renderType);
            fenceGateWallOpenModel.model().renderType(renderType);
        }

        TCModelBuilder<?> fenceGate = fenceGateModel.tcFenceGateModel(baseName, particle, textures);
        TCModelBuilder<?> fenceGateOpen = fenceGateOpenModel.tcFenceGateOpenModel(baseName, particle, openTextures);
        TCModelBuilder<?> fenceGateWall = fenceGateWallModel.tcFenceGateWallModel(baseName, particle, wallTextures);
        TCModelBuilder<?> fenceGateWallOpen = fenceGateWallOpenModel.tcFenceGateWallOpenModel(baseName, particle, wallOpenTextures);

        fenceGateBlock(block, fenceGate, fenceGateOpen, fenceGateWall, fenceGateWallOpen);
    }

    public void fenceGateBlock(FenceGateBlock block, ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen) throws Exception {
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
    public void hangingSignBlockInternal(Block block, String baseName, ResourceLocation particle, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent hangingSignModel = new TCBlockModelContent(modid(), name(), "");

        if(renderType != null){
            hangingSignModel.model().renderType(renderType);
        }

        TCModelBuilder<?> hangingSign = hangingSignModel.tcHangingSignModel(baseName, particle);

        hangingSignBlock(block, hangingSign);
    }

    public void hangingSignBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void leavesInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent leavesModel = new TCBlockModelContent(modid(), name(), "");

        if(renderType != null){
            leavesModel.model().renderType(renderType);
        }

        TCModelBuilder<?> leaves = leavesModel.tcLeavesModel(baseName, particle, textures);

        leavesBlock(block, leaves);
    }

    public void leavesBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void logBlockInternal(RotatedPillarBlock block, String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.LogAssetRecord> logTextures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent logsModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent logsHorizontalModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_horizontal"), "");

        if(renderType != null){
            logsModel.model().renderType(renderType);
            logsHorizontalModel.model().renderType(renderType);
        }


        TCModelBuilder<?> logs = logsModel.tcLogModel(baseName, particle, logTextures);
        TCModelBuilder<?> logsHorizontal = logsHorizontalModel.tcLogHorizontalModel(baseName, particle, logTextures);

        logBlock(block, logs, logsHorizontal);
    }

    public void logBlock(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) throws Exception {
        this.axisBlock(block, vertical, horizontal);
    }
    //------------------------------------------------------------------------------------------------------------------
    private void paneBlockInternal(IronBarsBlock block, String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.PaneSideAltRecord> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent panePostModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_post"), "");
        TCBlockModelContent paneSideModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_side"), "");
        TCBlockModelContent paneSideAltModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_side_alt"), "");
        TCBlockModelContent paneNoSideModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_noside"), "");
        TCBlockModelContent paneNoSideAltModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_noside_alt"), "");

        if(renderType != null){
            panePostModel.model().renderType(renderType);
            paneSideModel.model().renderType(renderType);
            paneSideAltModel.model().renderType(renderType);
            paneNoSideModel.model().renderType(renderType);
            paneNoSideAltModel.model().renderType(renderType);
        }


        TCModelBuilder<?> panePostBuilder = panePostModel.tcPanePostModel(baseName, particle, textures);
        TCModelBuilder<?> paneSideBuilder = paneSideModel.tcPaneSideModel(baseName, particle, textures);
        TCModelBuilder<?> paneSideAltBuilder = paneSideAltModel.tcPaneSideAltModel(baseName, particle, textures);
        TCModelBuilder<?> paneNoSideBuilder = paneNoSideModel.tcPaneNoSideModel(baseName, particle, textures);
        TCModelBuilder<?> paneNoSideAltBuilder = paneNoSideAltModel.tcPaneNoSideAltModel(baseName, particle, textures);



        paneBlock(block, panePostBuilder, paneSideBuilder, paneSideAltBuilder, paneNoSideBuilder, paneNoSideAltBuilder);
    }

    public void paneBlock(IronBarsBlock block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) throws Exception {
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
    public void planksBlockSeparateInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.CubeSeparateRecord> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent planksModel = new TCBlockModelContent(modid(), name(), "");

        if(renderType != null){
            planksModel.model().renderType(renderType);
        }

        TCModelBuilder<?> leaves = planksModel.tcPlanksSeparateModel(baseName, particle, textures);

        planksBlock(block, leaves);
    }

    public void planksBlockTogetherInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent planksModel = new TCBlockModelContent(modid(), name(), "");

        if(renderType != null){
            planksModel.model().renderType(renderType);
        }

        TCModelBuilder<?> leaves = planksModel.tcPlanksTogetherModel(baseName, particle, textures);

        planksBlock(block, leaves);
    }
    
    public void planksBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void pressurePlateBlock(PressurePlateBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent pressurePlateModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent pressurePlateDownModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_down"), "");

        if(renderType != null){
            pressurePlateModel.model().renderType(renderType);
            pressurePlateDownModel.model().renderType(renderType);
        }
        
        TCModelBuilder<?> pressurePlate = pressurePlateModel.tcPressurePlateUpModel(baseName, particle, textures);
        TCModelBuilder<?> pressurePlateDown = pressurePlateDownModel.tcPressurePlateDownModel(baseName, particle, textures);

        pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) throws Exception {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder
                .partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
                .partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));

        this.setBlockState(builder).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void saplingBlockInternal(Block block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        ResourceLocation base = key(block);

        TCBlockModelContent saplingModel = new TCBlockModelContent(base, "");

        if(renderType != null){
            saplingModel.model().renderType(renderType);
        }

        TCModelBuilder<?> leaves = saplingModel.tcSaplingModel(baseName, particle, textures);

        saplingBlock(block, leaves);
    }

    public void saplingBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void signBlockInternal(StandingSignBlock signBlock, WallSignBlock wallSignBlock, String baseName, ResourceLocation particle, @Nullable ResourceLocation renderType) throws Exception {
        ResourceLocation base = key(signBlock);

        TCBlockModelContent signModel = new TCBlockModelContent(base, "");

        if(renderType != null){
            signModel.model().renderType(renderType);
        }

        TCModelBuilder<?> sign = signModel.tcSignModel(baseName, particle);

        signBlock(signBlock, wallSignBlock, sign);
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, TCModelBuilder<?> sign) throws Exception {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void slabBlockInternal(SlabBlock block, String baseName, ResourceLocation particle, ResourceLocation doubleSlab, Map<Integer, TCBlockModelContent.SlabRecord> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent slabModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent slabTopModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top"), "");

        if(renderType != null){
            slabModel.model().renderType(renderType);
            slabTopModel.model().renderType(renderType);
        }

        TCModelBuilder<?> slab = slabModel.tcSlabModel(baseName, particle, textures);
        TCModelBuilder<?> slabTop = slabTopModel.tcTopSlabModel(baseName, particle, textures);

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

        this.setBlockState(builder).save(false);
    }
    //------------------------------------------------------------------------------------------------------------------
    private void stairsBlockInternal(StairBlock block, String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent stairsModel = new TCBlockModelContent(modid(), name(), "");
        TCBlockModelContent stairsInnerModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_inner"), "");
        TCBlockModelContent stairsOuterModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_outer"), "");

        if(renderType != null){
            stairsModel.model().renderType(renderType);
            stairsInnerModel.model().renderType(renderType);
            stairsOuterModel.model().renderType(renderType);
        }

        ModelFile stairs = stairsModel.tcStairsModel(baseName, particle, textures);
        ModelFile stairsInner = stairsInnerModel.tcInnerStairsModel(baseName, particle, textures);
        ModelFile stairsOuter = stairsOuterModel.tcOuterStairsModel(baseName, particle, textures);

        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    public void stairsBlock(StairBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) throws Exception {
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

    private static int getyRot(Direction facing, StairsShape shape, Half half) {
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

    private void trapdoorBlockInternal(TrapDoorBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType, boolean orientable) throws Exception {
        if(orientable){
            TCBlockModelContent trapdoorOrientableBottomModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom"), "");
            TCBlockModelContent trapdoorOrientableTopModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top"), "");
            TCBlockModelContent trapdoorOrientableOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_open"), "");

            if(renderType != null){
                trapdoorOrientableBottomModel.model().renderType(renderType);
                trapdoorOrientableTopModel.model().renderType(renderType);
                trapdoorOrientableOpenModel.model().renderType(renderType);
            }

            TCModelBuilder<?> trapdoorOrientableBottom = trapdoorOrientableBottomModel.tcOrientableTrapDoorBottomModel(baseName, particle, textures);
            TCModelBuilder<?> trapdoorOrientableTop = trapdoorOrientableTopModel.tcOrientableTrapDoorTopModel(baseName, particle, textures);
            TCModelBuilder<?> trapdoorOrientableOpen = trapdoorOrientableOpenModel.tcOrientableTrapDoorOpenModel(baseName, particle, textures);

            trapdoorBlock(block, trapdoorOrientableBottom, trapdoorOrientableTop, trapdoorOrientableOpen, true);
        }else{
            TCBlockModelContent trapdoorBottomModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_bottom"), "");
            TCBlockModelContent trapdoorTopModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_top"), "");
            TCBlockModelContent trapdoorOpenModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_open"), "");

            if(renderType != null){
                trapdoorBottomModel.model().renderType(renderType);
                trapdoorTopModel.model().renderType(renderType);
                trapdoorOpenModel.model().renderType(renderType);
            }

            TCModelBuilder<?> trapdoorBottom = trapdoorBottomModel.tcTrapDoorBottomModel(baseName, particle, textures);
            TCModelBuilder<?> trapdoorTop = trapdoorTopModel.tcTrapDoorTopModel(baseName, particle, textures);
            TCModelBuilder<?> trapdoorOpen = trapdoorOpenModel.tcTrapDoorOpenModel(baseName, particle, textures);

            trapdoorBlock(block, trapdoorBottom, trapdoorTop, trapdoorOpen, false);
        }
    }

    public void trapdoorBlock(TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open, boolean orientable) throws Exception {
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
    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textures, @Nullable ResourceLocation renderType) throws Exception {
        TCBlockModelContent wallPostModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_post"), "");
        TCBlockModelContent wallSideModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_side"), "");
        TCBlockModelContent wallSideTallModel = new TCBlockModelContent(new ResourceLocation(modid(), name() + "_side_tall"), "");

        if(renderType != null){
            wallPostModel.model().renderType(renderType);
            wallSideModel.model().renderType(renderType);
            wallSideTallModel.model().renderType(renderType);
        }

        TCModelBuilder<?> wallPostBuilder = wallPostModel.tcWallPostModel(baseName, particle, textures);
        TCModelBuilder<?> wallSideBuilder = wallSideModel.tcWallSideModel(baseName, particle, textures);
        TCModelBuilder<?> wallSideTallBuilder = wallSideTallModel.tcWallSideTallModel(baseName, particle, textures);

        wallBlock(block, wallPostBuilder, wallSideBuilder, wallSideTallBuilder);
    }

    public static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.<Direction, Property<WallSide>>builder()
            .put(Direction.EAST, BlockStateProperties.EAST_WALL)
            .put(Direction.NORTH, BlockStateProperties.NORTH_WALL)
            .put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL)
            .put(Direction.WEST, BlockStateProperties.WEST_WALL)
            .build();

    public void wallBlock(WallBlock block, ModelFile post, ModelFile side, ModelFile sideTall) throws Exception {
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

    private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Map.Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part()
                .modelFile(model)
                .rotationY((((int) entry.getKey().toYRot()) + 180) % 360)
                .uvLock(true)
                .addModel()
                .condition(entry.getValue(), height);
    }

}
