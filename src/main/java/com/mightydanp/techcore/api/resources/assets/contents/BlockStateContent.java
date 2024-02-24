package com.mightydanp.techcore.api.resources.assets.contents;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class BlockStateContent {
    public final String blockStateName;

    private final String modid;
    private IGeneratedBlockState blockState;

    public BlockStateContent(String blockStateName, String modid) {
        this.blockStateName = blockStateName;
        this.modid = modid;
    }

    public VariantBlockStateBuilder getVariantBuilder(Block b) throws Exception {
        Constructor<VariantBlockStateBuilder> constructor = VariantBlockStateBuilder.class.getDeclaredConstructor(Block.class);
        constructor.setAccessible(true);

        return constructor.newInstance(b);
    }

    public MultiPartBlockStateBuilder getMultipartBuilder(Block b) throws Exception {
        Constructor<MultiPartBlockStateBuilder> constructor = MultiPartBlockStateBuilder.class.getDeclaredConstructor(Block.class);
        constructor.setAccessible(true);

        return constructor.newInstance(b);
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public BlockStateContent setBlockState(IGeneratedBlockState generatedBlockState){
        blockState = generatedBlockState;
        return this;
    }

    public JsonObject createJson(){
        return blockState.toJson();
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation resourceLocation(Block block) {
        return key(block);
    }

    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = key(block);
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public TCModelBuilder cubeAll(Block block) {
        String blockName = name(block);

        ModelContent modelContent = new ModelContent(blockName, ModelContent.BLOCK_FOLDER, "");
        modelContent.cubeAll(blockTexture(block));
        return modelContent.getModel();
    }

    public void simpleBlock(Block block) throws Exception {
        simpleBlock(block, cubeAll(block));
    }

    public void simpleBlock(Block block, Function<TCModelBuilder, ConfiguredModel[]> expander) throws Exception {
        simpleBlock(block, expander.apply(cubeAll(block)));
    }

    public void simpleBlock(Block block, TCModelBuilder model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }

    public void simpleBlockItem(Block block, TCModelBuilder model) throws Exception {
        simpleBlockWithItem(block, model);
        simpleBlock(block, new ConfiguredModel(model));
        //itemModels().getBuilder(key(block).getPath()).parent(model);
    }

    public void simpleBlockWithItem(Block block, TCModelBuilder model) throws Exception {
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    public void simpleBlock(Block block, ConfiguredModel... models) throws Exception {
        String blockName = resourceLocation(block).getPath();
        String modid = resourceLocation(block).getNamespace();



        VariantBlockStateBuilder variantBlockStateBuilder = getVariantBuilder(block).partialState().setModels(models);

        AssetPackRegistries.blockStateContent.put(blockName, new BlockStateContent(blockName, modid).setBlockState(variantBlockStateBuilder));

        getVariantBuilder(block)
                .partialState().setModels(models);
    }

    public void axisBlock(RotatedPillarBlock block) throws Exception {
        axisBlock(block, blockTexture(block));
    }

    public void logBlock(RotatedPillarBlock block) throws Exception {
        axisBlock(block, blockTexture(block), extend(blockTexture(block), "_top"));
    }

    public void axisBlock(RotatedPillarBlock block, ResourceLocation baseName) throws Exception {
        axisBlock(block, extend(baseName, "_side"), extend(baseName, "_end"));
    }

    public void axisBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) throws Exception {
        String blockName = name(block);
        ModelContent modeContent = new ModelContent(blockName + "_horizontal", ModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, String renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), renderType);
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, String renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), extend(blockTexture(block), "_top"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, String renderType) throws Exception {
        axisBlockWithRenderType(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, String renderType) throws Exception {
        String blockName = name(block);
        ModelContent modeContent = new ModelContent(blockName, ModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), renderType);
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), extend(blockTexture(block), "_top"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, ResourceLocation renderType) throws Exception {
        String blockName = name(block);
        ModelContent modeContent = new ModelContent(blockName, ModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType));
    }

    public void axisBlock(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) throws Exception {
        getVariantBuilder(block)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public void horizontalBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top) throws Exception {
        String name = name(block);

        ModelContent modelContent = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        
        horizontalBlock(block, modelContent.orientable(side, front, top));
    }

    public void horizontalBlock(Block block, ModelFile model) throws Exception {
        horizontalBlock(block, model, DEFAULT_ANGLE_OFFSET);
    }

    public void horizontalBlock(Block block, ModelFile model, int angleOffset) throws Exception {
        horizontalBlock(block, $ -> model, angleOffset);
    }

    public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc) throws Exception {
        horizontalBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
    }

    public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) throws Exception {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
                        .build());
    }

    public void horizontalFaceBlock(Block block, ModelFile model) throws Exception {
        horizontalFaceBlock(block, model, DEFAULT_ANGLE_OFFSET);
    }

    public void horizontalFaceBlock(Block block, ModelFile model, int angleOffset) throws Exception {
        horizontalFaceBlock(block, $ -> model, angleOffset);
    }

    public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc) throws Exception {
        horizontalFaceBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
    }

    public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) throws Exception {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationX(state.getValue(BlockStateProperties.ATTACH_FACE).ordinal() * 90)
                        .rotationY((((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) + (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)) % 360)
                        .build());
    }

    public void directionalBlock(Block block, ModelFile model) throws Exception {
        directionalBlock(block, model, DEFAULT_ANGLE_OFFSET);
    }

    public void directionalBlock(Block block, ModelFile model, int angleOffset) throws Exception {
        directionalBlock(block, $ -> model, angleOffset);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) throws Exception {
        directionalBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) throws Exception {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });
    }

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
        ModelContent stairsModel = new ModelContent(baseName, ModelContent.BLOCK_FOLDER, "");
        ModelFile stairs = stairsModel.stairs(side, bottom, top);

        simpleBlockWithItem(block, stairsModel.getModel());

        ModelContent stairsInnerModel = new ModelContent(baseName + "_inner", ModelContent.BLOCK_FOLDER, "");
        ModelFile stairsInner = stairsInnerModel.stairsInner(side, bottom, top);

        simpleBlockWithItem(block, stairsInnerModel.getModel());

        ModelContent stairsOuterModel = new ModelContent(baseName + "_outer", ModelContent.BLOCK_FOLDER, "");
        ModelFile stairsOuter = stairsOuterModel.stairsOuter(side, bottom, top);

        simpleBlockWithItem(block, stairsOuterModel.getModel());
        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    private void stairsBlockInternalWithRenderType(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        ModelContent stairsModel = new ModelContent(baseName, ModelContent.BLOCK_FOLDER, "");
        ModelFile stairs = stairsModel.stairs(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsModel.getModel());

        ModelContent stairsInnerModel = new ModelContent(baseName + "_inner", ModelContent.BLOCK_FOLDER, "");
        ModelFile stairsInner = stairsInnerModel.stairsInner(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsInnerModel.getModel());

        ModelContent stairsOuterModel = new ModelContent(baseName + "_outer", ModelContent.BLOCK_FOLDER, "");
        ModelFile stairsOuter = stairsOuterModel.stairsOuter(side, bottom, top).renderType(renderType);

        simpleBlockWithItem(block, stairsOuterModel.getModel());
        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    public void stairsBlock(StairBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) throws Exception {
        getVariantBuilder(block)
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
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation texture) throws Exception {
        slabBlock(block, doubleSlab, texture, texture, texture);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) throws Exception {
        String name = name(block);
        ModelContent slabModel = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder slab = slabModel.slab(side, bottom, top);

        simpleBlockWithItem(block, slabModel.getModel());

        ModelContent slabTopModel = new ModelContent(name + "_top", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder slabTop = slabTopModel.slab(side, bottom, top);

        simpleBlockWithItem(block, slabTopModel.getModel());
        
        slabBlock(block, slab, slabTop, new ModelFile.UncheckedModelFile(doubleSlab));
    }

    public void slabBlock(SlabBlock block, ModelFile bottom, ModelFile top, ModelFile doubleslab) throws Exception {
        getVariantBuilder(block)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleslab));
    }

    public void buttonBlock(ButtonBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);

        ModelContent buttonModel = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder button = buttonModel.button(texture);

        simpleBlockWithItem(block, buttonModel.getModel());

        ModelContent buttonPressedModel = new ModelContent(name + "_pressed", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder buttonPressed = buttonPressedModel.buttonPressed(name, texture);

        simpleBlockWithItem(block, buttonPressedModel.getModel());
        buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(ButtonBlock block, ModelFile button, ModelFile buttonPressed) throws Exception {
        String name = resourceLocation(block).getPath();
        String modId = resourceLocation(block).getNamespace();

        VariantBlockStateBuilder variantBlockStateBuilder = getVariantBuilder(block).forAllStates(state -> {
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

        AssetPackRegistries.blockStateContent.put(name, new BlockStateContent(name, modId).setBlockState(variantBlockStateBuilder));
    }

    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);

        ModelContent pressurePlateModel = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder pressurePlate = pressurePlateModel.button(texture);

        simpleBlockWithItem(block, pressurePlateModel.getModel());

        ModelContent pressurePlateDownModel = new ModelContent(name + "_down", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder pressurePlateDown = pressurePlateDownModel.button(texture);

        simpleBlockWithItem(block, pressurePlateDownModel.getModel());
        pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) throws Exception {
        getVariantBuilder(block)
                .partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
                .partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, ResourceLocation texture) throws Exception {
        String name = name(signBlock);

        ModelContent model = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder sign = model.sign(name, texture);

        simpleBlockWithItem(signBlock, model.getModel());
        signBlock(signBlock, wallSignBlock, sign);
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, TCModelBuilder sign) throws Exception {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    public void fourWayBlock(CrossCollisionBlock block, ModelFile post, ModelFile side) throws Exception {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel().end();
        fourWayMultipart(builder, side);
    }

    public void fourWayMultipart(MultiPartBlockStateBuilder builder, ModelFile side) {
        PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
            if (dir.getAxis().isHorizontal()) {
                builder.part().modelFile(side).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).addModel()
                        .condition(value, true);
            }
        });
    }

    public void fenceBlock(FenceBlock block, ResourceLocation texture) throws Exception {
        String name = name(block);
        String baseName = key(block).toString();

        ModelContent fencePostModel = new ModelContent(name + "_post", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_post", texture);

        simpleBlockWithItem(block, fencePostModel.getModel());

        ModelContent fenceSideModel = new ModelContent(name + "_side", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_side", texture);

        simpleBlockWithItem(block, fenceSideModel.getModel());
        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlock(FenceBlock block, String name, ResourceLocation texture) throws Exception {
        String baseName = key(block).toString();

        ModelContent fencePostModel = new ModelContent(name + "_fence_post", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_fence_post", texture);

        simpleBlockWithItem(block, fencePostModel.getModel());

        ModelContent fenceSideModel = new ModelContent(name + "_fence_side", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_fence_side", texture);

        simpleBlockWithItem(block, fenceSideModel.getModel());

        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlockWithRenderType(FenceBlock block, ResourceLocation texture, String renderType) throws Exception {
        String name = name(block);
        String baseName = key(block).toString();

        ModelContent fencePostModel = new ModelContent(name + "_post", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_post", texture).renderType(renderType);

        simpleBlockWithItem(block, fencePostModel.getModel());

        ModelContent fenceSideModel = new ModelContent(name + "_side", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_side", texture).renderType(renderType);

        simpleBlockWithItem(block, fenceSideModel.getModel());
        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

    public void fenceBlockWithRenderType(FenceBlock block, String name, ResourceLocation texture, String renderType) throws Exception {
        String baseName = key(block).toString();

        ModelContent fencePostModel = new ModelContent(name + "_fence_post", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fencePostBuilder = fencePostModel.fencePost(baseName + "_fence_post", texture).renderType(renderType);

        simpleBlockWithItem(block, fencePostModel.getModel());

        ModelContent fenceSideModel = new ModelContent(name + "_fence_side", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceSideBuilder = fenceSideModel.fenceSide(baseName + "_fence_side", texture).renderType(renderType);

        simpleBlockWithItem(block, fenceSideModel.getModel());

        fourWayBlock(block, fencePostBuilder, fenceSideBuilder);
    }

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

        ModelContent fenceGateModel = new ModelContent(name, ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateBuilder = fenceGateModel.fenceGate(baseName, texture);

        simpleBlockWithItem(block, fenceGateModel.getModel());

        ModelContent fenceGateOpenModel = new ModelContent(name + "_open", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateOpenBuilder = fenceGateOpenModel.fenceGateOpen(baseName + "_open", texture);

        simpleBlockWithItem(block, fenceGateOpenModel.getModel());

        ModelContent fenceGateWallModel = new ModelContent(name + "_wall", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallBuilder = fenceGateWallModel.fenceGateWall(baseName + "_wall", texture);

        simpleBlockWithItem(block, fenceGateWallModel.getModel());

        ModelContent fenceGateWallOpenModel = new ModelContent(name + "_wall_open", ModelContent.BLOCK_FOLDER, "");
        TCModelBuilder fenceGateWallOpenBuilder = fenceGateWallOpenModel.fenceGateWallOpen(baseName + "_wall_open", texture);

        simpleBlockWithItem(block, fenceGateWallOpenModel.getModel());

        fenceGateBlock(block, fenceGateBuilder, fenceGateOpenBuilder, fenceGateWallBuilder, fenceGateWallOpenBuilder);
    }

    private void fenceGateBlockInternalWithRenderType(FenceGateBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) throws Exception {
        ModelFile gate = models().fenceGate(baseName, texture).renderType(renderType);
        ModelFile gateOpen = models().fenceGateOpen(baseName + "_open", texture).renderType(renderType);
        ModelFile gateWall = models().fenceGateWall(baseName + "_wall", texture).renderType(renderType);
        ModelFile gateWallOpen = models().fenceGateWallOpen(baseName + "_wall_open", texture).renderType(renderType);
        fenceGateBlock(block, gate, gateOpen, gateWall, gateWallOpen);
    }

    public void fenceGateBlock(FenceGateBlock block, ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen) throws Exception {
        getVariantBuilder(block).forAllStatesExcept(state -> {
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
    }

    public void wallBlock(WallBlock block, ResourceLocation texture) {
        wallBlockInternal(block, key(block).toString(), texture);
    }

    public void wallBlock(WallBlock block, String name, ResourceLocation texture) {
        wallBlockInternal(block, name + "_wall", texture);
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, String renderType) {
        wallBlockInternalWithRenderType(block, key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, String renderType) {
        wallBlockInternalWithRenderType(block, name + "_wall", texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, ResourceLocation renderType) {
        wallBlockInternalWithRenderType(block, key(block).toString(), texture, renderType);
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        wallBlockInternalWithRenderType(block, name + "_wall", texture, renderType);
    }

    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
        wallBlock(block, models().wallPost(baseName + "_post", texture),
                models().wallSide(baseName + "_side", texture),
                models().wallSideTall(baseName + "_side_tall", texture));
    }

    private void wallBlockInternalWithRenderType(WallBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) {
        wallBlock(block, models().wallPost(baseName + "_post", texture).renderType(renderType),
                models().wallSide(baseName + "_side", texture).renderType(renderType),
                models().wallSideTall(baseName + "_side_tall", texture).renderType(renderType));
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
    }

    private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Map.Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part()
                .modelFile(model)
                .rotationY((((int) entry.getKey().toYRot()) + 180) % 360)
                .uvLock(true)
                .addModel()
                .condition(entry.getValue(), height);
    }

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
        ModelFile post = models().panePost(baseName + "_post", pane, edge);
        ModelFile side = models().paneSide(baseName + "_side", pane, edge);
        ModelFile sideAlt = models().paneSideAlt(baseName + "_side_alt", pane, edge);
        ModelFile noSide = models().paneNoSide(baseName + "_noside", pane);
        ModelFile noSideAlt = models().paneNoSideAlt(baseName + "_noside_alt", pane);
        paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
    }

    private void paneBlockInternalWithRenderType(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) throws Exception {
        ModelFile post = models().panePost(baseName + "_post", pane, edge).renderType(renderType);
        ModelFile side = models().paneSide(baseName + "_side", pane, edge).renderType(renderType);
        ModelFile sideAlt = models().paneSideAlt(baseName + "_side_alt", pane, edge).renderType(renderType);
        ModelFile noSide = models().paneNoSide(baseName + "_noside", pane).renderType(renderType);
        ModelFile noSideAlt = models().paneNoSideAlt(baseName + "_noside_alt", pane).renderType(renderType);
        paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
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
    }

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
        ModelFile bottomLeft = models().doorBottomLeft(baseName + "_bottom_left", bottom, top);
        ModelFile bottomLeftOpen = models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top);
        ModelFile bottomRight = models().doorBottomRight(baseName + "_bottom_right", bottom, top);
        ModelFile bottomRightOpen = models().doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top);
        ModelFile topLeft = models().doorTopLeft(baseName + "_top_left", bottom, top);
        ModelFile topLeftOpen = models().doorTopLeftOpen(baseName + "_top_left_open", bottom, top);
        ModelFile topRight = models().doorTopRight(baseName + "_top_right", bottom, top);
        ModelFile topRightOpen = models().doorTopRightOpen(baseName + "_top_right_open", bottom, top);
        doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    private void doorBlockInternalWithRenderType(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) throws Exception {
        ModelFile bottomLeft = models().doorBottomLeft(baseName + "_bottom_left", bottom, top).renderType(renderType);
        ModelFile bottomLeftOpen = models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top).renderType(renderType);
        ModelFile bottomRight = models().doorBottomRight(baseName + "_bottom_right", bottom, top).renderType(renderType);
        ModelFile bottomRightOpen = models().doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top).renderType(renderType);
        ModelFile topLeft = models().doorTopLeft(baseName + "_top_left", bottom, top).renderType(renderType);
        ModelFile topLeftOpen = models().doorTopLeftOpen(baseName + "_top_left_open", bottom, top).renderType(renderType);
        ModelFile topRight = models().doorTopRight(baseName + "_top_right", bottom, top).renderType(renderType);
        ModelFile topRightOpen = models().doorTopRightOpen(baseName + "_top_right_open", bottom, top).renderType(renderType);
        doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    public void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) throws Exception {
        getVariantBuilder(block).forAllStatesExcept(state -> {
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
    }

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
        ModelFile bottom = orientable ? models().trapdoorOrientableBottom(baseName + "_bottom", texture) : models().trapdoorBottom(baseName + "_bottom", texture);
        ModelFile top = orientable ? models().trapdoorOrientableTop(baseName + "_top", texture) : models().trapdoorTop(baseName + "_top", texture);
        ModelFile open = orientable ? models().trapdoorOrientableOpen(baseName + "_open", texture) : models().trapdoorOpen(baseName + "_open", texture);
        trapdoorBlock(block, bottom, top, open, orientable);
    }

    private void trapdoorBlockInternalWithRenderType(TrapDoorBlock block, String baseName, ResourceLocation texture, boolean orientable, ResourceLocation renderType) throws Exception {
        ModelFile bottom = orientable ? models().trapdoorOrientableBottom(baseName + "_bottom", texture).renderType(renderType) : models().trapdoorBottom(baseName + "_bottom", texture).renderType(renderType);
        ModelFile top = orientable ? models().trapdoorOrientableTop(baseName + "_top", texture).renderType(renderType) : models().trapdoorTop(baseName + "_top", texture).renderType(renderType);
        ModelFile open = orientable ? models().trapdoorOrientableOpen(baseName + "_open", texture).renderType(renderType) : models().trapdoorOpen(baseName + "_open", texture).renderType(renderType);
        trapdoorBlock(block, bottom, top, open, orientable);
    }

    public void trapdoorBlock(TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open, boolean orientable) throws Exception {
        getVariantBuilder(block).forAllStatesExcept(state -> {
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
    }

    private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, Block owner) {
        ResourceLocation blockName = Preconditions.checkNotNull(key(owner));
        Path outputPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
        return DataProvider.saveStable(cache, stateJson, outputPath);
    }

    @NotNull
    @Override
    public String getName() {
        return "Block States: " + modid;
    }

    public static class ConfiguredModelList {
        private final List<ConfiguredModel> models;

        private ConfiguredModelList(List<ConfiguredModel> models) {
            Preconditions.checkArgument(!models.isEmpty());
            this.models = models;
        }

        public ConfiguredModelList(ConfiguredModel model) {
            this(ImmutableList.of(model));
        }

        public ConfiguredModelList(ConfiguredModel... models) {
            this(Arrays.asList(models));
        }

        public JsonElement toJSON() {
            if (models.size() == 1) {
                return models.get(0).toJSON(false);
            } else {
                JsonArray ret = new JsonArray();
                for (ConfiguredModel m : models) {
                    ret.add(m.toJSON(true));
                }
                return ret;
            }
        }

        public BlockStateProvider.ConfiguredModelList append(ConfiguredModel... models) {
            return new BlockStateProvider.ConfiguredModelList(ImmutableList.<ConfiguredModel>builder().addAll(this.models).add(models).build());
        }
    }
}