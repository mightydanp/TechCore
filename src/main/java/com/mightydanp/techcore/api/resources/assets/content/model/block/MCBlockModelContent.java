package com.mightydanp.techcore.api.resources.assets.content.model.block;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.model.ModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MCBlockModelContent extends BlockModelContent<MCBlockModelContent> {
    public MCBlockModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public MCBlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public MCBlockModelContent end() {
        return MCBlockModelContent.this;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButton(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(5F, 0F, 6F)
                    .to(11F, 2F, 10F)
                    .face(Direction.DOWN).uvs(5, 6, 11, 10).texture("#texture_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).uvs(5, 6, 11, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(5, 14, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(5, 14, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(6, 14, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(6, 14, 10, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintButtonModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");

        tintModel.tintButton(numberOfTints);

        return tintModel.save(false).model();
    }

    public void mcButton(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButton(textureMap.size());

        tintModel.save(false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcButtonModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintButtonModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(modelContent, false);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonInventory(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(5F, 6F, 6F)
                    .to(11F, 10F, 10F)
                    .face(Direction.DOWN).uvs(5, 6, 11, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(5, 10, 11, 6).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(5, 12, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(5, 12, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintButtonInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcButtonInventory(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcButtonInventoryModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintButtonInventoryModel(baseName, textureMap.size());

        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), baseName, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonPressed(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(5F, 0F, 6F)
                    .to(11F, 1F, 10F)
                    .face(Direction.DOWN).uvs(5, 6, 11, 10).texture("#texture_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).uvs(5, 10, 11, 6).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(5, 14, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(5, 14, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(6, 14, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(6, 14, 10, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintButtonPressedModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcButtonPressed(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcButtonPressedModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintButtonPressedModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeft(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(16, 13, 0, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorBottomLeftModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcDoorBottomLeft(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorBottomLeftModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorBottomLeftModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), baseName, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeftOpen(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(0, 16, 16, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorBottomLeftOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcDoorBottomLeftOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorBottomLeftOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorBottomLeftOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRight(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(0, 13, 16, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorBottomRightModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcDoorBottomRight(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorBottomRightModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorBottomRightModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRightOpen(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(16, 16, 0, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorBottomRightOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcDoorBottomRightOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorBottomRightOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorBottomRightOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeft(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorTopLeftModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeft(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcDoorTopLeft(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorTopLeftModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorTopLeftModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeftOpen(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorTopLeftOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeftOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcDoorTopLeftOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorTopLeftOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorTopLeftOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRight(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorTopRightModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcDoorTopRight(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorTopRightModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorTopRightModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRightOpen(int numberOfTints) {
        model().ao(false);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintDoorTopRightOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");

        tintModel.tintDoorTopRightOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcDoorTopRightOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcDoorTopRightOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintDoorTopRightOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGate(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        model().transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .translation(0, -1, 0)
                .scale(0.8F, 0.8F, 0.8F).end();

        model().transforms().transform(ItemDisplayContext.HEAD)
                .rotation(0, 0, 0)
                .translation(0, -3, -6)
                .scale(1, 1, 1).end();

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 5, 7)
                    .to(2, 16, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 5, 7)
                    .to(16, 16, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(6, 6, 7)
                    .to(8, 15, 9)
                    .face(Direction.DOWN).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(8, 6, 7)
                    .to(10, 15, 9)
                    .face(Direction.DOWN).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(2, 6, 7)
                    .to(6, 9, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(2, 12, 7)
                    .to(6, 15, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(10, 6, 7)
                    .to(14, 9, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(10, 12, 7)
                    .to(14, 15, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceGateModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGate(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFenceGate(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGate(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceGateModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceGateModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateOpen(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 5, 7)
                    .to(2, 16, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 5, 7)
                    .to(16, 16, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(0, 6, 13)
                    .to(2, 15, 15)
                    .face(Direction.DOWN).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 6, 13)
                    .to(16, 15, 15)
                    .face(Direction.DOWN).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(0, 6, 9)
                    .to(2, 9, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(0, 12, 9)
                    .to(2, 15, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 6, 9)
                    .to(16, 9, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 12, 9)
                    .to(16, 15, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceGateOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFenceGateOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceGateOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceGateOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWall(int numberOfTints) {
        model().ao(true);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 2, 7)
                    .to(2, 13, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 2, 7)
                    .to(16, 13, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(6, 3, 7)
                    .to(8, 12, 9)
                    .face(Direction.DOWN).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(8, 3, 7)
                    .to(10, 12, 9)
                    .face(Direction.DOWN).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(2, 3, 7)
                    .to(6, 6, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(2, 9, 7)
                    .to(6, 12, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(10, 3, 7)
                    .to(14, 6, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(10, 9, 7)
                    .to(14, 12, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceGateWallModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWall(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFenceGateWall(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWall(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceGateWallModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceGateWallModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWallOpen(int numberOfTints) {
        model().ao(true);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 2, 7)
                    .to(2, 13, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 2, 7)
                    .to(16, 13, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(0, 3, 13)
                    .to(2, 12, 15)
                    .face(Direction.DOWN).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 3, 13)
                    .to(16, 12, 15)
                    .face(Direction.DOWN).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(0, 3, 9)
                    .to(2, 6, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(0, 9, 9)
                    .to(2, 12, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 3, 9)
                    .to(16, 6, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(14, 9, 9)
                    .to(16, 12, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceGateWallOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWallOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFenceGateWallOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWallOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceGateWallOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceGateWallOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceInventory(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        model().transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0, 0, 0)
                .scale(0.625F, 0.625F, 0.625F).end();
        model().transforms().transform(ItemDisplayContext.FIXED)
                .rotation(0, 90, 0)
                .translation(0, 0, 0)
                .scale(0.5F, 0.5F, 0.5F).end();

        model().ao(false);

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(6, 0, 0)
                    .to(10, 16, 4)
                    .face(Direction.DOWN).uvs(6, 0, 10, 4).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(6, 0, 10, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 4, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 4, 16).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(6, 0, 12)
                    .to(10, 16, 16)
                    .face(Direction.DOWN).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 12, 0)
                    .to(9, 15, 16)
                    .face(Direction.DOWN).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 1, 16, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 1, 16, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 12, 16)
                    .to(9, 15, 18)
                    .face(Direction.DOWN).uvs(7, 14, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 1, 9, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 1, 2, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(14, 1, 16, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 6, 0)
                    .to(9, 9, 16)
                    .face(Direction.DOWN).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 7, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 16, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 6, -2)
                    .to(9, 9, 0)
                    .face(Direction.DOWN).uvs(7, 0, 9, 2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 14, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(14, 7, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 2, 10).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 6, 16)
                    .to(9, 9, 18)
                    .face(Direction.DOWN).uvs(7, 14, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 7, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(14, 7, 16, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFenceInventory(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceInventoryModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceInventoryModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFencePost(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(6, 0, 6)
                    .to(10, 16, 10)
                    .face(Direction.DOWN).uvs(6, 6, 10, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(6, 6, 10, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFencePostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFencePost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcFencePost(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFencePost(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFencePostModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFencePostModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintFenceSide(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 12, 0)
                    .to(9, 15, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 1, 9, 4).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 1, 9, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 1, 9, 4).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(7, 6, 0)
                    .to(9, 9, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintFenceSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcFenceSide(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcFenceSideModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintFenceSideModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void mcHangingSign(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model().texture("particle", particle);
    }

    /// /
    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcHangingSignModel(String baseName, ResourceLocation particle) {
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintLeaves(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(7, 6, 0)
                    .to(9, 9, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintLeavesModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintLeaves(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcLeaves(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintLeaves(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcLeavesModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintLeavesModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tinmcubeSeparate(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 0F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).texture("#down_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).texture("#up_" + i).cullface(Direction.UP).tintindex(i).end()
                    .face(Direction.NORTH).texture("#north_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).texture("#south_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).texture("#west_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).texture("#east_" + i).cullface(Direction.EAST).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tinmcubeSeparateModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeSeparate(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcCubeSeparate(String baseName, int numberOfTints) {
        String name = name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeSeparate(numberOfTints);

        model().parent(tintModel);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().texture("down_" + i, "#down_" + i);
            model().texture("up_" + i, "#up_" + i);
            model().texture("north_" + i, "#north_" + i);
            model().texture("south_" + i, "#south_" + i);
            model().texture("west_" + i, "#west_" + i);
            model().texture("east_" + i, "#east_" + i);
        }
    }

    public void mcCubeSeparateDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, CubeSeparateRecord> textureMap) {
        String name = name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeSeparate(textureMap.size());

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model().texture("down_" + integer, resourceLocation.down);
            model().texture("up_" + integer, resourceLocation.up);
            model().texture("north_" + integer, resourceLocation.north);
            model().texture("south_" + integer, resourceLocation.south);
            model().texture("west_" + integer, resourceLocation.west);
            model().texture("east_" + integer, resourceLocation.east);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcCubeSeparateModel(String baseName, int numberOfTints) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcubeSeparateModel(baseName, numberOfTints);
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", "#partical");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("down_" + i, "#down_" + i);
            modelContent.model().texture("up_" + i, "#up_" + i);
            modelContent.model().texture("north_" + i, "#north_" + i);
            modelContent.model().texture("south_" + i, "#south_" + i);
            modelContent.model().texture("west_" + i, "#west_" + i);
            modelContent.model().texture("east_" + i, "#east_" + i);
        }

        return modelContent.save(false).model();
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcCubeSeparateModelDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, CubeSeparateRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcubeSeparateModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);


        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.model().texture("down_" + integer, resourceLocation.down);
            modelContent.model().texture("up_" + integer, resourceLocation.up);
            modelContent.model().texture("north_" + integer, resourceLocation.north);
            modelContent.model().texture("south_" + integer, resourceLocation.south);
            modelContent.model().texture("west_" + integer, resourceLocation.west);
            modelContent.model().texture("east_" + integer, resourceLocation.east);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tinmcubeTogether(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 0F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).texture("#texture_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).texture("#texture_" + i).cullface(Direction.UP).tintindex(i).end()
                    .face(Direction.NORTH).texture("#texture_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).texture("#texture_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).texture("#texture_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).texture("#texture_" + i).cullface(Direction.EAST).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tinmcubeTogetherModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeTogether(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcCubeTogether(String baseName, int numberOfTints) {
        String name = name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeTogether(numberOfTints);

        model().parent(tintModel);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().texture("texture_" + i, "#texture_" + i);
        }
    }

    public void mcCubeTogetherDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = name() + "_cube";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeTogether(textureMap.size());

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcCubeTogetherModel(String baseName, int numberOfTints) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcubeTogetherModel(baseName, numberOfTints);
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("texture_" + i, "#texture_" + i);
        }

        return modelContent.save(false).model();
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcCubeTogetherModelDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcubeTogetherModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void mcColumn(String baseName, int numberOfTints) {
        String name = name() + "_cube_column";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeSeparate(numberOfTints);

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().texture("down_" + i, "#end_" + i);
            model().texture("up_" + i, "#end_" + i);
            model().texture("north_" + i, "#side_" + i);
            model().texture("south_" + i, "#side_" + i);
            model().texture("west_" + i, "#side_" + i);
            model().texture("east_" + i, "#side_" + i);
        }
    }

    public void mcColumnDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcubeSeparate(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model().texture("down_" + integer, resourceLocation.end);
            model().texture("up_" + integer, resourceLocation.end);
            model().texture("north_" + integer, resourceLocation.side);
            model().texture("south_" + integer, resourceLocation.side);
            model().texture("west_" + integer, resourceLocation.side);
            model().texture("east_" + integer, resourceLocation.side);
        });
    }

    /// /

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcColumnModel(String baseName, int numberOfTints) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = mcCubeSeparateModel(baseName, numberOfTints);
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("down_" + i, "#end_" + i);
            modelContent.model().texture("up_" + i, "#end_" + i);
            modelContent.model().texture("north_" + i, "#side_" + i);
            modelContent.model().texture("south_" + i, "#side_" + i);
            modelContent.model().texture("west_" + i, "#side_" + i);
            modelContent.model().texture("east_" + i, "#side_" + i);
        }

        return modelContent.save(false).model();
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcColumnModelDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, ColumnAssetRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = mcCubeSeparateModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.model().texture("down_" + integer, resourceLocation.end);
            modelContent.model().texture("up_" + integer, resourceLocation.end);
            modelContent.model().texture("north_" + integer, resourceLocation.side);
            modelContent.model().texture("south_" + integer, resourceLocation.side);
            modelContent.model().texture("west_" + integer, resourceLocation.side);
            modelContent.model().texture("east_" + integer, resourceLocation.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tinmcolumnHorizontal(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");
        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 0F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).texture("#down_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).texture("#up_" + i).cullface(Direction.UP).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).tintindex(i).end()
                    .face(Direction.NORTH).texture("#north_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).texture("#south_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).texture("#west_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).texture("#east_" + i).cullface(Direction.EAST).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tinmcolumnHorizontalModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube_column_horizontal";

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tinmcolumnHorizontal(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcColumnHorizontal(String baseName, int numberOfTints) {
        String name = name() + "_cube_column_horizontal";
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcolumnHorizontal(numberOfTints);

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().texture("down_" + i, "#end_" + i);
            model().texture("up_" + i, "#end_" + i);
            model().texture("north_" + i, "#side_" + i);
            model().texture("south_" + i, "#side_" + i);
            model().texture("west_" + i, "#side_" + i);
            model().texture("east_" + i, "#side_" + i);
        }
    }

    public void mcColumnHorizontalDefined(String baseName, ResourceLocation particle, @NotNull Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column_horizontal";
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcolumnHorizontal(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model().texture("down_" + integer, resourceLocation.end);
            model().texture("up_" + integer, resourceLocation.end);
            model().texture("north_" + integer, resourceLocation.side);
            model().texture("south_" + integer, resourceLocation.side);
            model().texture("west_" + integer, resourceLocation.side);
            model().texture("east_" + integer, resourceLocation.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcColumnHorizontalModel(String baseName, int numberOfTints) {
        String name = name() + "_cube_column_horizontal";

        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcolumnHorizontalModel(baseName, numberOfTints);
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");


        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("down_" + i, "#end_" + i);
            modelContent.model().texture("up_" + i, "#end_" + i);
            modelContent.model().texture("north_" + i, "#side_" + i);
            modelContent.model().texture("south_" + i, "#side_" + i);
            modelContent.model().texture("west_" + i, "#side_" + i);
            modelContent.model().texture("east_" + i, "#side_" + i);
        }

        return modelContent.save(false).model();
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcColumnHorizontalModelDefine(String baseName, ResourceLocation particle, @NotNull Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column_horizontal";

        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcolumnHorizontalModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");

        textureMap.forEach((integer, resourceLocation) -> {
            model().texture("down_" + integer, resourceLocation.end);
            model().texture("up_" + integer, resourceLocation.end);
            model().texture("north_" + integer, resourceLocation.side);
            model().texture("south_" + integer, resourceLocation.side);
            model().texture("west_" + integer, resourceLocation.side);
            model().texture("east_" + integer, resourceLocation.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void mcLog(String baseName, ResourceLocation particle, @NotNull Map<Integer, LogAssetRecord> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.mcColumn(baseName, textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) -> {
            model().texture("#side_" + integer, logAssetRecord.side);
            model().texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model().getUncheckedLocation(), resourceLocationMap);
    }

    /// /
    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcLogModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, LogAssetRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = mcColumnModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) -> {
            modelContent.model().texture("#side_" + integer, logAssetRecord.side);
            modelContent.model().texture("#end_" + integer, logAssetRecord.end);
        });

        return modelContent.save(false).model();
        //this.resourceTextureMap(tintModel.model().getUncheckedLocation(), resourceLocationMap);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void mcLogHorizontal(String baseName, ResourceLocation particle, @NotNull Map<Integer, LogAssetRecord> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcolumnHorizontal(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) -> {
            model().texture("#side_" + integer, logAssetRecord.side);
            model().texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model().getUncheckedLocation(), resourceLocationMap);
    }

    /// /
    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcLogHorizontalModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, LogAssetRecord> textureMap) {
        String name = name() + "_horizontal";

        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = mcColumnHorizontalModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) -> {
            modelContent.model().texture("#side_" + integer, logAssetRecord.side);
            modelContent.model().texture("#end_" + integer, logAssetRecord.end);
        });

        AssetPackRegistries.saveBlockModel(modelContent, true);

        return modelContent.model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public ModelBuilder<BlockModelContent<MCBlockModelContent>> pane(String parent, ResourceLocation pane, ResourceLocation edge) {
        return withExistingParent(BLOCK_FOLDER + "/" + parent)
                .texture("pane", pane)
                .texture("edge", edge);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPaneNoSide(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 0, 7)
                    .to(9, 16, 9)
                    .face(Direction.NORTH).uvs(9, 0, 7, 16).texture("#pane_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPaneNoSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPaneNoSide(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPaneNoSideModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, PaneSideAltRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPaneNoSideModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("pane_" + integer, resourceLocation.pane));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPaneNoSideAlt(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 0, 7)
                    .to(9, 16, 9)
                    .face(Direction.EAST).uvs(7, 0, 9, 16).texture("#pane_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPaneNoSideAltModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSideAlt(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPaneNoSideAlt(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSideAlt(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPaneNoSideAltModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, PaneSideAltRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPaneNoSideAltModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("pane_" + integer, resourceLocation.pane));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPanePost(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 0, 7)
                    .to(9, 16, 9)
                    .face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#edge_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 7, 9, 9).texture("#edge_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPanePostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPanePost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPanePost(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPanePost(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("edge_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPanePostModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, PaneSideAltRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPanePostModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("edge_" + integer, resourceLocation.edge));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPaneSide(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 0, 0)
                    .to(9, 16, 7)
                    .face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#pane_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 7).texture("#pane_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 0, 9, 16).texture("#pane_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(16, 0, 9, 16).texture("#pane_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(9, 0, 16, 16).texture("#pane_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPaneSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPaneSide(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPaneSideModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, PaneSideAltRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPaneSideModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("pane_" + integer, resourceLocation.pane));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPaneSideAlt(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(7, 0, 9)
                    .to(9, 16, 16)
                    .face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#edge_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 7).texture("#edge_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(7, 0, 0, 16).texture("#pane_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 7, 16).texture("#pane_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPaneSideAltModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSideAlt(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPaneSideAlt(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSideAlt(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPaneSideAltModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, PaneSideAltRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPaneSideAltModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.model().texture("edge_" + integer, resourceLocation.edge);
            modelContent.model().texture("pane_" + integer, resourceLocation.pane);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    public void mcPlanksTogether(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.mcCubeTogetherDefine(baseName, particle, textureMap);
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPlanksTogetherModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        return mcCubeTogetherModelDefine(baseName, particle, textureMap);
    }

    public void mcPlanksSeparate(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        this.mcCubeSeparateDefine(baseName, particle, textureMap);
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPlanksSeparateModel(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        return mcCubeSeparateModelDefine(baseName, particle, textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateUp(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(1, 0, 1)
                    .to(15, 1, 15)
                    .face(Direction.DOWN).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPressurePlateUpModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateUp(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcPressurePlateUp(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateUp(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPressurePlateUpModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPressurePlateUpModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateDown(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(1, 0, 1)
                    .to(15, 0.5F, 15)
                    .face(Direction.DOWN).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintPressurePlateDownModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateDown(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcPressurePlateDown(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateDown(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcPressurePlateDownModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintPressurePlateDownModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tinmcross(int numberOfTints) {
        model().ao(false);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0.8F, 0F, 8F)
                    .to(15.2F, 16F, 8F)
                    .shade(false)
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .rotation().origin(8, 8, 8)
                    .axis(Direction.Axis.Y).angle(45F).rescale(true);
            model().element()
                    .from(8F, 0F, 0.8F)
                    .to(8F, 16F, 15.2F)
                    .shade(false)
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .rotation().origin(8, 8, 8)
                    .axis(Direction.Axis.Y).angle(45F).rescale(true);
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tinmcrossModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tinmcross(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcCross(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tinmcross(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcCrossModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tinmcrossModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void mcSapling(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        mcCross(baseName, particle, textureMap);
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcSaplingModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        return mcCrossModel(baseName, particle, textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void mcSign(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model().texture("particle", particle);
    }

    /// /
    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcSignModel(String baseName, ResourceLocation particle) {
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintSlab(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 0F, 0F)
                    .to(16F, 8F, 16F)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).cullface(Direction.EAST).tintindex(i).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintSlabModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintSlab(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcSlab(String baseName, ResourceLocation particle, @NotNull Map<Integer, SlabRecord> textureMap) {
        String name = "tint_" + name();
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintSlab(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model().texture("bottom_" + integer, record.bottom);
            model().texture("top_" + integer, record.top);
            model().texture("side_" + integer, record.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcSlabModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, SlabRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintSlabModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTopSlab(int numberOfTints) {
        model().texture("particle", "#side");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 8F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.UP).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.EAST).tintindex(i).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintTopSlabModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTopSlab(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void topSlab(String baseName, ResourceLocation particle, @NotNull Map<Integer, SlabRecord> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTopSlab(textureMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model().texture("bottom_" + integer, record.bottom);
            model().texture("top_" + integer, record.top);
            model().texture("side_" + integer, record.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcTopSlabModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, SlabRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintTopSlabModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintStairs(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0F, 0F, 0F)
                .scale(0.625F, 0.625F, 0.625F).end();
        model().transforms().transform(ItemDisplayContext.HEAD)
                .rotation(0, -90, 0)
                .translation(0, 0, 0)
                .scale(1, 1, 1).end();
        model().transforms().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(75, -135, 0)
                .translation(0, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F).end();

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(8, 8, 0)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcStairs(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> assetMap) {
        String name = "tint_" + name();
        //todo separate resources for tinting
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintStairs(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> {
            model().texture("bottom_" + integer, record.bottom);
            model().texture("top_" + integer, record.top);
            model().texture("side_" + integer, record.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcStairsModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintStairsModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintInnerStairs(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(8, 8, 0)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(0, 8, 8)
                    .to(8, 16, 16)
                    .face(Direction.UP).uvs(0, 8, 8, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintInnerStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintInnerStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcInnerStairs(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> assetMap) {
        String name = "tint_" + name();
        //todo separate resources for tinting
        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintInnerStairs(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> {
            model().texture("bottom_" + integer, record.bottom);
            model().texture("top_" + integer, record.top);
            model().texture("side_" + integer, record.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcInnerStairsModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintInnerStairsModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOuterStairs(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model().element()
                    .from(8, 8, 8)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(8, 8, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintOuterStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOuterStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void mcOuterStairs(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> textureMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOuterStairs(textureMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model().texture("bottom_" + integer, record.bottom);
            model().texture("top_" + integer, record.top);
            model().texture("side_" + integer, record.side);
        });
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcOuterStairsModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, StairsRecord> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintOuterStairsModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        return modelContent.save(false).model();
    }
    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorBottom(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 3, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintTrapDoorBottomModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorBottom(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcTrapDoorBottom(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorBottom(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcTrapDoorBottomModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintTrapDoorBottomModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorOpen(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 13)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 13, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(16, 0, 13, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(13, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintTrapDoorOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcTrapDoorOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorOpen(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcTrapDoorOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintTrapDoorOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorTop(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 13, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintTrapDoorTopModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorTop(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcTrapDoorTop(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorTop(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcTrapDoorTopModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintTrapDoorTopModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorBottom(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 0)
                    .to(16, 3, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintOrientableTrapDoorBottomModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorBottom(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcOrientableTrapDoorBottom(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorBottom(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcOrientableTrapDoorBottomModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintOrientableTrapDoorBottomModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorOpen(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 13)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 3, 16, 0).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintOrientableTrapDoorOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcOrientableTrapDoorOpen(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorOpen(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcOrientableTrapDoorOpenModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintOrientableTrapDoorOpenModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorTop(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 13, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintOrientableTrapDoorTopModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorTop(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcOrientableTrapDoorTop(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorTop(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcOrientableTrapDoorTopModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintOrientableTrapDoorTopModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintWallInventory(int numberOfTints) {
        model().parent(ModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");

        model().transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0, 0, 0)
                .scale(0.625F, 0.625F, 0.625F).end();

        model().transforms().transform(ItemDisplayContext.HEAD)
                .rotation(0, 90, 0)
                .translation(0, 0, -6)
                .scale(0.5F, 0.5F, 0.5F).end();

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(4, 0, 4)
                    .to(12, 16, 12)
                    .face(Direction.DOWN).uvs(4, 4, 12, 12).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(4, 4, 12, 12).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(4, 0, 12, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(4, 0, 12, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(4, 0, 12, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(4, 0, 12, 16).texture("#texture_" + i).tintindex(i).end();

            model().element()
                    .from(5, 0, 0)
                    .to(11, 13, 16)
                    .face(Direction.DOWN).uvs(5, 0, 11, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(5, 0, 11, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(5, 3, 11, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(5, 3, 11, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 3, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 3, 16, 16).texture("#texture_" + i).tintindex(i).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintWallInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcWallInventory(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallInventory(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcWallInventoryModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintWallInventoryModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintWallPost(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(4, 0, 4)
                    .to(12, 16, 12)
                    .face(Direction.DOWN).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).texture("#texture_" + i).tintindex(i).end();
        }

    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintWallPostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallPost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcWallPost(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallPost(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcWallPostModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintWallPostModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintWallSide(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(5, 0, 0)
                    .to(11, 14, 8)
                    .face(Direction.DOWN).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintWallSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcWallSide(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallSide(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcWallSideModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintWallSideModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintWallSideTall(int numberOfTints) {
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(5, 0, 0)
                    .to(11, 16, 8)
                    .face(Direction.DOWN).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> tintWallSideTallModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallSideTall(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void mcWallSideTall(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        MCBlockModelContent tintModel = new MCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallSideTall(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public ModelBuilder<?> mcWallSideTallModel(String baseName, ResourceLocation particle, @NotNull Map<Integer, ResourceLocation> textureMap) {
        ModelBuilder<BlockModelContent<MCBlockModelContent>> tintModel = tintWallSideTallModel(baseName, textureMap.size());
        MCBlockModelContent modelContent = new MCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void mcWood(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        mcColumnDefine(baseName, particle, textureMap);
    }

    public ModelBuilder<BlockModelContent<MCBlockModelContent>> mcWoodModel(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        return mcColumnModelDefine(baseName, particle, textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public record CubeSeparateRecord(ResourceLocation down, ResourceLocation up, ResourceLocation north,
                                     ResourceLocation south, ResourceLocation west, ResourceLocation east) {
    }

    public record SlabRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
    }

    public record StairsRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
    }

    public record LogAssetRecord(ResourceLocation side, ResourceLocation end) {
    }

    public record ColumnAssetRecord(ResourceLocation side, ResourceLocation end) {
    }

    public record PaneSideAltRecord(ResourceLocation edge, ResourceLocation pane) {
    }
}