package com.mightydanp.techcore.api.resources.assets.contents.blockstate;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import com.mightydanp.techcore.api.resources.assets.contents.model.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.TCItemModelContent;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;

import java.lang.reflect.Constructor;
import java.util.function.Function;

public class BlockStateContent<A extends BlockStateContent<A>> {
    private final String modid;
    private final String name;
    private IGeneratedBlockState blockState;

    public BlockStateContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public BlockStateContent(ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    @SuppressWarnings("ALL")
    public A save(boolean override) {
        AssetPackRegistries.saveBlockState((A) this, override);
        return (A) this;
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

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public IGeneratedBlockState blockState() {
        return blockState;
    }

    public ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public BlockStateContent<A> setBlockState(IGeneratedBlockState generatedBlockState) {
        blockState = generatedBlockState;
        return this;
    }

    public JsonObject json() {
        return blockState.toJson();
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public String modid(Block block) {
        return key(block).getNamespace();
    }

    public ResourceLocation blockTexture(Block block) {
        String modid = modid(block);
        String name = name(block);

        return ResourceLocation.fromNamespaceAndPath(modid, ModelProvider.BLOCK_FOLDER + "/" + name);
    }

    public ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    public TCModelBuilder<? extends BlockModelContent<?>> cubeAll(Block block) {
        BlockModelContent<?> modelContent = new BlockModelContent<>(modid(), name(), "");
        modelContent.cubeAll(blockTexture(block));

        return modelContent.model();
    }

    public void simpleBlock(Block block) throws Exception {
        simpleBlock(block, cubeAll(block));
    }

    public void simpleBlock(Block block, Function<TCModelBuilder<?>, ConfiguredModel[]> expander) throws Exception {
        simpleBlock(block, expander.apply(cubeAll(block)));
    }

    public void simpleBlock(Block block, TCModelBuilder<?> model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }

    public void simpleBlock(Block block, ModelFile model) throws Exception {
        simpleBlock(block, new ConfiguredModel(model));
    }

    public void simpleBlockItem(Block block, TCModelBuilder<?> model) {
        TCItemModelContent itemModel = new TCItemModelContent(modid(), name(), "");
        itemModel.model().parent(model);
    }

    public void simpleBlockWithItem(Block block, TCModelBuilder<?> model) throws Exception {
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    public void simpleBlock(Block block, ConfiguredModel... models) throws Exception {
        VariantBlockStateBuilder builder = getVariantBuilder(block).partialState().setModels(models);

        getVariantBuilder(block)
                .partialState().setModels(models);

        this.setBlockState(builder).save(false);
    }

    public void axisBlock(RotatedPillarBlock block) throws Exception {
        axisBlock(block, blockTexture(block));
    }


    public void axisBlock(RotatedPillarBlock block, ResourceLocation baseName) throws Exception {
        axisBlock(block, extend(baseName, "_side"), extend(baseName, "_end"));
    }

    public void axisBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) throws Exception {
        BlockModelContent<?> modeContent = new BlockModelContent<>(name() + "_horizontal", BlockModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, String renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), renderType);
    }


    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, String renderType) throws Exception {
        axisBlockWithRenderType(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, String renderType) throws Exception {
        BlockModelContent<?> modeContent = new BlockModelContent<>(name() + "_horizontal", BlockModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, blockTexture(block), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, ResourceLocation renderType) throws Exception {
        axisBlockWithRenderType(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, ResourceLocation renderType) throws Exception {
        BlockModelContent<?> modeContent = new BlockModelContent<>(name(), BlockModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType));
    }

    public void axisBlockWithRenderTypeAndFast(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, String renderType, String renderTypeFast) throws Exception {
        BlockModelContent<?> modeContent = new BlockModelContent<>(name() + "_horizontal", BlockModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType, renderTypeFast));
    }

    public void axisBlockWithRenderTypeAndFast(RotatedPillarBlock block, ResourceLocation renderType, ResourceLocation renderTypeFast) throws Exception {
        axisBlockWithRenderTypeAndFast(block, blockTexture(block), renderType, renderTypeFast);
    }

    public void axisBlockWithRenderTypeAndFast(RotatedPillarBlock block, ResourceLocation baseName, String renderType, String renderTypeFast) throws Exception {
        axisBlockWithRenderTypeAndFast(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType, renderTypeFast);
    }

    public void axisBlockWithRenderTypeAndFast(RotatedPillarBlock block, ResourceLocation baseName, ResourceLocation renderType, ResourceLocation renderTypeFast) throws Exception {
        axisBlockWithRenderTypeAndFast(block, extend(baseName, "_side"), extend(baseName, "_end"), renderType, renderTypeFast);
    }

    public void axisBlockWithRenderTypeAndFast(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, ResourceLocation renderType, ResourceLocation renderTypeFast) throws Exception {
        BlockModelContent<?> modeContent = new BlockModelContent<>(name() + "_horizontal", BlockModelContent.BLOCK_FOLDER, "");

        axisBlock(block, modeContent.cubeColumn(side, end), modeContent.cubeColumnHorizontal(side, end).renderType(renderType, renderTypeFast));
    }


    public void axisBlock(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) throws Exception {
        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();

        this.setBlockState(builder).save(false);
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public void horizontalBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top) throws Exception {
        BlockModelContent<?> modelContent = new BlockModelContent<>(modid(), name(), "");

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
        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
                        .build());

        this.setBlockState(builder).save(false);
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
        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationX(state.getValue(BlockStateProperties.ATTACH_FACE).ordinal() * 90)
                        .rotationY((((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) + (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)) % 360)
                        .build());

        this.setBlockState(builder).save(false);
    }

    public void directionalBlock(Block block, ModelFile model, int angleOffset) throws Exception {
        directionalBlock(block, $ -> model, angleOffset);
    }


    public void directionalBlock(Block block, ModelFile model) throws Exception {
        directionalBlock(block, model, DEFAULT_ANGLE_OFFSET);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) throws Exception {
        directionalBlock(block, modelFunc, DEFAULT_ANGLE_OFFSET);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) throws Exception {
        VariantBlockStateBuilder builder = getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });

        this.setBlockState(builder).save(false);
    }

    public void fourWayBlock(CrossCollisionBlock block, ModelFile post, ModelFile side) throws Exception {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel().end();
        fourWayMultipart(builder, side);

        this.setBlockState(builder).save(false);
    }

    public void fourWayMultipart(MultiPartBlockStateBuilder builder, ModelFile side) {
        PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
            if (dir.getAxis().isHorizontal()) {
                builder.part().modelFile(side).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).addModel()
                        .condition(value, true);
            }
        });
    }
}