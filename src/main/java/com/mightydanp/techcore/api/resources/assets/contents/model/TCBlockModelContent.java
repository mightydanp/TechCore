package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public class TCBlockModelContent extends BlockModelContent<TCBlockModelContent> {
    public TCBlockModelContent(String modid, String modelName, String parentFolder) {
        super(modid, modelName, parentFolder);
    }

    public TCBlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, parentFolder);
    }

    public TCBlockModelContent end() {
        return TCBlockModelContent.this;
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintButtonModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");

        tintModel.tintButton(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcButton(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButton(textureMap.size());

        tintModel.save(false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcButtonModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintButtonModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(modelContent, false);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonInventory(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintButtonInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcButtonInventory(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcButtonInventoryModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintButtonInventoryModel(baseName, textureMap.size());

        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), baseName, "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintButtonPressedModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcButtonPressed(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcButtonPressedModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintButtonPressedModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.DOWN).uvs(16, 13, 0, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorBottomLeftModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcDoorBottomLeft(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorBottomLeftModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorBottomLeftModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), baseName, "tree_icons/" + baseName + "/");

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
                    .face(Direction.DOWN).uvs(0, 16, 16, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorBottomLeftOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcDoorBottomLeftOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorBottomLeftOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorBottomLeftOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.DOWN).uvs(0, 13, 16, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorBottomRightModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcDoorBottomRight(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorBottomRightModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorBottomRightModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.DOWN).uvs(16, 16, 0, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorBottomRightOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcDoorBottomRightOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("bottom_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorBottomRightOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorBottomRightOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorTopLeftModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeft(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcDoorTopLeft(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorTopLeftModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorTopLeftModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorTopLeftOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeftOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcDoorTopLeftOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorTopLeftOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorTopLeftOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorTopRightModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcDoorTopRight(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorTopRightModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorTopRightModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintDoorTopRightOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");

        tintModel.tintDoorTopRightOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcDoorTopRightOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, record) -> model().texture("top_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcDoorTopRightOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintDoorTopRightOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGate(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceGateModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGate(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFenceGate(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGate(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceGateModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceGateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceGateOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFenceGateOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceGateOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceGateOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceGateWallModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWall(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFenceGateWall(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWall(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceGateWallModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceGateWallModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceGateWallOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWallOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFenceGateWallOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWallOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceGateWallOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceGateWallOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceInventory(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFenceInventory(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceInventoryModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceInventoryModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFencePostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFencePost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcFencePost(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFencePost(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFencePostModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFencePostModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintFenceSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintFenceSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcFenceSide(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintFenceSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcFenceSideModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintFenceSideModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tcHangingSign(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model().texture("particle", particle);
    }

    /// /
    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcHangingSignModel(String baseName, ResourceLocation particle) {
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintLeaves(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintLeavesModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintLeaves(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcLeaves(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintLeaves(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);

        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcLeavesModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintLeavesModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintCubeSeparate(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintCubeSeparateModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcCubeSeparate(String baseName, int numberOfTints) {
        String name = name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(numberOfTints);

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

    public void tcCubeSeparateDefine(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        String name = name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(textureMap.size());

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcCubeSeparateModel(String baseName, int numberOfTints) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintCubeSeparateModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcCubeSeparateModelDefine(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintCubeSeparateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
    public void tintCubeTogether(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintCubeTogetherModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintCubeTogether(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcCubeTogether(String baseName, int numberOfTints) {
        String name = name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeTogether(numberOfTints);

        model().parent(tintModel);
        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().texture("texture_" + i, "#texture_" + i);
        }
    }

    public void tcCubeTogetherDefine(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = name() + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeTogether(textureMap.size());

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcCubeTogetherModel(String baseName, int numberOfTints) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintCubeTogetherModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("texture_" + i, "#texture_" + i);
        }

        return modelContent.save(false).model();
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcCubeTogetherModelDefine(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintCubeTogetherModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcColumn(String baseName, int numberOfTints) {
        String name = name() + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(numberOfTints);

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

    public void tcColumnDefine(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(textureMap.size());

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcColumnModel(String baseName, int numberOfTints) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tcCubeSeparateModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcColumnModelDefine(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tcCubeSeparateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
    public void tintColumnHorizontal(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

        model().texture("particle", "#particle");
        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0F, 0F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).texture("#down_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).texture("#up_" + i).cullface(Direction.UP).rotation(TCModelBuilder.FaceRotation.UPSIDE_DOWN).tintindex(i).end()
                    .face(Direction.NORTH).texture("#north_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).texture("#south_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).texture("#west_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).texture("#east_" + i).cullface(Direction.EAST).tintindex(i).end();
        }
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintColumnHorizontalModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name() + "_cube_column_horizontal";

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintColumnHorizontal(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcColumnHorizontal(String baseName, int numberOfTints) {
        String name = name() + "_cube_column_horizontal";
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintColumnHorizontal(numberOfTints);

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

    public void tcColumnHorizontalDefined(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column_horizontal";
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintColumnHorizontal(textureMap.size());

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcColumnHorizontalModel(String baseName, int numberOfTints) {
        String name = name() + "_cube_column_horizontal";

        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintColumnHorizontalModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");


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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcColumnHorizontalModelDefine(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        String name = name() + "_cube_column_horizontal";

        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintColumnHorizontalModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");

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
    public void tcLog(String baseName, ResourceLocation particle, Map<Integer, LogAssetRecord> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tcColumn(baseName, textureMap.size());

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
    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcLogModel(String baseName, ResourceLocation particle, Map<Integer, LogAssetRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tcColumnModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public void tcLogHorizontal(String baseName, ResourceLocation particle, Map<Integer, LogAssetRecord> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintColumnHorizontal(textureMap.size());

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
    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcLogHorizontalModel(String baseName, ResourceLocation particle, Map<Integer, LogAssetRecord> textureMap) {
        String name = name() + "_horizontal";

        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tcColumnHorizontalModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");

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
    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> pane(String parent, ResourceLocation pane, ResourceLocation edge) {
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPaneNoSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPaneNoSide(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPaneNoSideModel(String baseName, ResourceLocation particle, Map<Integer, PaneSideAltRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPaneNoSideModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPaneNoSideAltModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSideAlt(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPaneNoSideAlt(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneNoSideAlt(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPaneNoSideAltModel(String baseName, ResourceLocation particle, Map<Integer, PaneSideAltRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPaneNoSideAltModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPanePostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPanePost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPanePost(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPanePost(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("edge_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPanePostModel(String baseName, ResourceLocation particle, Map<Integer, PaneSideAltRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPanePostModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPaneSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPaneSide(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPaneSideModel(String baseName, ResourceLocation particle, Map<Integer, PaneSideAltRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPaneSideModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPaneSideAltModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSideAlt(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPaneSideAlt(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPaneSideAlt(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("pane_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPaneSideAltModel(String baseName, ResourceLocation particle, Map<Integer, PaneSideAltRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPaneSideAltModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
    public void tcPlanksTogether(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.tcCubeTogetherDefine(baseName, particle, textureMap);
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPlanksTogetherModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        return tcCubeTogetherModelDefine(baseName, particle, textureMap);
    }

    public void tcPlanksSeparate(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        this.tcCubeSeparateDefine(baseName, particle, textureMap);
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPlanksSeparateModel(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap) {
        return tcCubeSeparateModelDefine(baseName, particle, textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateUp(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPressurePlateUpModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateUp(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcPressurePlateUp(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateUp(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPressurePlateUpModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPressurePlateUpModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintPressurePlateDownModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateDown(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcPressurePlateDown(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateDown(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcPressurePlateDownModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintPressurePlateDownModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintCross(int numberOfTints) {
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintCrossModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintCross(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcCross(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintCross(textureMap.size());

        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model().texture("texture_" + integer, resourceLocation));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcCrossModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintCrossModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcSapling(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        tcCross(baseName, particle, textureMap);
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcSaplingModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        return tcCrossModel(baseName, particle, textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void tcSign(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model().texture("particle", particle);
    }

    /// /
    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcSignModel(String baseName, ResourceLocation particle) {
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintSlab(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintSlabModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintSlab(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcSlab(String baseName, ResourceLocation particle, Map<Integer, SlabRecord> textureMap) {
        String name = "tint_" + name();
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcSlabModel(String baseName, ResourceLocation particle, Map<Integer, SlabRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintSlabModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintTopSlabModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTopSlab(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void topSlab(String baseName, ResourceLocation particle, Map<Integer, SlabRecord> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcTopSlabModel(String baseName, ResourceLocation particle, Map<Integer, SlabRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintTopSlabModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcStairs(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> assetMap) {
        String name = "tint_" + name();
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcStairsModel(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintInnerStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintInnerStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcInnerStairs(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> assetMap) {
        String name = "tint_" + name();
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcInnerStairsModel(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintInnerStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintOuterStairsModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOuterStairs(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /

    public void tcOuterStairs(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> textureMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcOuterStairsModel(String baseName, ResourceLocation particle, Map<Integer, StairsRecord> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintOuterStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintTrapDoorBottomModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorBottom(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcTrapDoorBottom(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorBottom(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcTrapDoorBottomModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintTrapDoorBottomModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorOpen(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintTrapDoorOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcTrapDoorOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorOpen(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcTrapDoorOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintTrapDoorOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorTop(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintTrapDoorTopModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorTop(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcTrapDoorTop(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorTop(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcTrapDoorTopModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintTrapDoorTopModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorBottom(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintOrientableTrapDoorBottomModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorBottom(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcOrientableTrapDoorBottom(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorBottom(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcOrientableTrapDoorBottomModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintOrientableTrapDoorBottomModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorOpen(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

        model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model().element()
                    .from(0, 0, 13)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 3).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 0).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 3).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 3, 16, 0).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintOrientableTrapDoorOpenModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorOpen(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcOrientableTrapDoorOpen(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorOpen(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcOrientableTrapDoorOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintOrientableTrapDoorOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOrientableTrapDoorTop(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintOrientableTrapDoorTopModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorTop(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcOrientableTrapDoorTop(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintOrientableTrapDoorTop(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcOrientableTrapDoorTopModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintOrientableTrapDoorTopModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintWallInventory(int numberOfTints) {
        model().parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintWallInventoryModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallInventory(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcWallInventory(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallInventory(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcWallInventoryModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintWallInventoryModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintWallPostModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallPost(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcWallPost(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallPost(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcWallPostModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintWallPostModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintWallSideModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallSide(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcWallSide(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallSide(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcWallSideModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintWallSideModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

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

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintWallSideTallModel(String baseName, int numberOfTints) {
        String tintName = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), tintName, "tree_icons/" + baseName + "/");
        tintModel.tintWallSideTall(numberOfTints);

        return tintModel.save(false).model();
    }

    /// /
    public void tcWallSideTall(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + name();

        TCBlockModelContent tintModel = new TCBlockModelContent(modid(), name, "tree_icons/" + baseName + "/");
        tintModel.tintWallSideTall(assetMap.size());
        AssetPackRegistries.saveBlockModel(tintModel, false);

        model().parent(tintModel);
        model().texture("particle", particle);

        assetMap.forEach((integer, record) -> model().texture("texture_" + integer, record));
    }

    public TCModelBuilder<?> tcWallSideTallModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCModelBuilder<BlockModelContent<TCBlockModelContent>> tintModel = tintWallSideTallModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(modid(), name(), "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        return modelContent.save(false).model();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcWood(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        tcColumnDefine(baseName, particle, textureMap);
    }

    public TCModelBuilder<BlockModelContent<TCBlockModelContent>> tcWoodModel(String baseName, ResourceLocation particle, Map<Integer, ColumnAssetRecord> textureMap) {
        return tcColumnModelDefine(baseName, particle, textureMap);
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