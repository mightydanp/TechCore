package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public class TCBlockModelContent extends ModelContent{
    public static final String BLOCK_FOLDER = "block";

    public TCBlockModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public TCBlockModelContent(String modid, String modelName, String modelFolder, String parentFolder) {
        super(modid, modelName, modelFolder, parentFolder);
    }

    public TCBlockModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintButton(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintButtonModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        tintModel.tintButton(numberOfTints);


        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }
    ////
    public void tcButton(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButton(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcButtonModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintButtonModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonInventory(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintButtonInventoryModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }
    ////

    public void tcButtonInventory(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButtonInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcButtonInventoryModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = tintButtonInventoryModel(baseName, textureMap.size());

        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonPressed(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintButtonPressedModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcButtonPressed(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButtonPressed(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcButtonPressedModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {

        TCBlockModelContent tintModel = tintButtonPressedModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeft(int numberOfTints) {
        model.ao(false);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(16, 13, 0, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorBottomLeftModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomLeft(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("bottom_" + integer, record));
    }

    public TCBlockModelContent tcDoorBottomLeftModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorBottomLeftModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeftOpen(int numberOfTints) {
        model.ao(false);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(0, 16, 16, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorBottomLeftOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomLeftOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("bottom_" + integer, record));
    }

    public TCBlockModelContent tcDoorBottomLeftOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorBottomLeftOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRight(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(0, 13, 16, 16).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }
    public TCBlockModelContent tintDoorBottomRightModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomRight(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("bottom_" + integer, record));
    }

    public TCBlockModelContent tcDoorBottomRightModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorBottomRightModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRightOpen(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.DOWN).uvs(16, 16, 0, 13).texture("#bottom_" + i).cullface(Direction.DOWN).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#bottom_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#bottom_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#bottom_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorBottomRightOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }
    ////
    public void tcDoorBottomRightOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("bottom_" + integer, record));
    }

    public TCBlockModelContent tcDoorBottomRightOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorBottomRightOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("bottom_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeft(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorTopLeftModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeft(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcDoorTopLeft(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopLeft(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("top_" + integer, record));
    }

    public TCBlockModelContent tcDoorTopLeftModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorTopLeftModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeftOpen(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 3, 16, 0).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorTopLeftOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopLeftOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcDoorTopLeftOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("top_" + integer, record));
    }

    public TCBlockModelContent tcDoorTopLeftOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorTopLeftOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRight(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(16, 0, 0, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorTopRightModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcDoorTopRight(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopRight(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("top_" + integer, record));
    }

    public TCBlockModelContent tcDoorTopRightModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorTopRightModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorTopRight(textureMap.size());

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRightOpen(int numberOfTints) {
        model.ao(false);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(3, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 3).texture("#top_" + i).cullface(Direction.UP).tintindex(i).rotation(TCModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.NORTH).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(3, 0, 0, 16).texture("#top_" + i).cullface(Direction.SOUTH).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#top_" + i).cullface(Direction.WEST).tintindex(i).end()
                    .face(Direction.EAST).uvs(16, 0, 0, 16).texture("#top_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintDoorTopRightOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        tintModel.tintDoorTopRightOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcDoorTopRightOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> model.texture("top_" + integer, record));
    }

    public TCBlockModelContent tcDoorTopRightOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintDoorTopRightOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("top_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGate(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");

        model.transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .translation(0, -1, 0)
                .scale(0.8F, 0.8F, 0.8F).end();

        model.transforms().transform(ItemDisplayContext.HEAD)
                .rotation(0, 0, 0)
                .translation(0, -3, -6)
                .scale(1, 1, 1).end();

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 5, 7)
                    .to(2, 16, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 5, 7)
                    .to(16, 16, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(6, 6, 7)
                    .to(8, 15, 9)
                    .face(Direction.DOWN).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(8, 6, 7)
                    .to(10, 15, 9)
                    .face(Direction.DOWN).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(2, 6, 7)
                    .to(6, 9, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(2, 12, 7)
                    .to(6, 15, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(10, 6, 7)
                    .to(14, 9, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(10, 12, 7)
                    .to(14, 15, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintFenceGateModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGate(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFenceGate(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGate(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceGateModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceGateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateOpen(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 5, 7)
                    .to(2, 16, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 5, 7)
                    .to(16, 16, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(0, 6, 13)
                    .to(2, 15, 15)
                    .face(Direction.DOWN).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 6, 13)
                    .to(16, 15, 15)
                    .face(Direction.DOWN).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(0, 6, 9)
                    .to(2, 9, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(0, 12, 9)
                    .to(2, 15, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 6, 9)
                    .to(16, 9, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 12, 9)
                    .to(16, 15, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintFenceGateOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFenceGateOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceGateOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceGateOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWall(int numberOfTints) {
        model.ao(true);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 2, 7)
                    .to(2, 13, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 2, 7)
                    .to(16, 13, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(6, 3, 7)
                    .to(8, 12, 9)
                    .face(Direction.DOWN).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(6, 7, 8, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 1, 8, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(8, 3, 7)
                    .to(10, 12, 9)
                    .face(Direction.DOWN).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(8, 7, 10, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 1, 10, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 1, 9, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(2, 3, 7)
                    .to(6, 6, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 7, 6, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(2, 9, 7)
                    .to(6, 12, 9)
                    .face(Direction.DOWN).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(2, 7, 6, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(2, 1, 6, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(10, 3, 7)
                    .to(14, 6, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 7, 14, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(10, 9, 7)
                    .to(14, 12, 9)
                    .face(Direction.DOWN).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(10, 7, 14, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(10, 1, 14, 4).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintFenceGateWallModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWall(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFenceGateWall(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateWall(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceGateWallModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceGateWallModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWallOpen(int numberOfTints) {
        model.ao(true);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 2, 7)
                    .to(2, 13, 9)
                    .face(Direction.DOWN).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 7, 2, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 2, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 2, 7)
                    .to(16, 13, 9)
                    .face(Direction.DOWN).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 7, 16, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 0, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(7, 0, 9, 11).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(0, 3, 13)
                    .to(2, 12, 15)
                    .face(Direction.DOWN).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 13, 2, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 1, 2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 3, 13)
                    .to(16, 12, 15)
                    .face(Direction.DOWN).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 13, 16, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(14, 1, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(0, 3, 9)
                    .to(2, 6, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(0, 9, 9)
                    .to(2, 12, 13)
                    .face(Direction.DOWN).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(0, 9, 2, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 3, 9)
                    .to(16, 6, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 7, 15, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(14, 9, 9)
                    .to(16, 12, 13)
                    .face(Direction.DOWN).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(14, 9, 16, 13).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(13, 1, 15, 4).texture("#texture_" + i).tintindex(i).end();
            /*
            model.element()
                    .from()
                    .to()
                    .face(Direction.DOWN).uvs().texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs().texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs().texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs().texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs().texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs().texture("#texture_" + i).tintindex(i).end();
             */
        }
    }

    public TCBlockModelContent tintFenceGateWallOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceGateWallOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFenceGateWallOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateWallOpen(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceGateWallOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceGateWallOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceInventory(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");

        model.transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0, 0, 0)
                .scale(0.625F, 0.625F, 0.625F).end();
        model.transforms().transform(ItemDisplayContext.FIXED)
                .rotation(0, 90, 0)
                .translation(0, 0, 0)
                .scale(0.5F, 0.5F, 0.5F).end();

        model.ao(false);

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(6, 0, 0)
                    .to(10, 16, 4)
                    .face(Direction.DOWN) .uvs(6, 0, 10, 4) .texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs(6, 0, 10, 4) .texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST) .uvs(0, 0, 4, 16) .texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST) .uvs(0, 0, 4, 16) .texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(6, 0, 12)
                    .to(10, 16, 16)
                    .face(Direction.DOWN) .uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16) .texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16) .texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST) .uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST) .uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 12, 0)
                    .to(9, 15, 16)
                    .face(Direction.DOWN) .uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP)   .uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST) .uvs(0, 1, 16, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST) .uvs(0, 1, 16, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 12, 16)
                    .to(9, 15, 18)
                    .face(Direction.DOWN).uvs(7, 14,  9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7,  0,  9,  2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7,  1,  9,  4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0,  1,  2,  4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(14,  1, 16,  4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 6, 0)
                    .to(9, 9, 16)
                    .face(Direction.DOWN).uvs(7, 0,  9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0,  9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 7, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 16, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 6, -2)
                    .to(9, 9, 0)
                    .face(Direction.DOWN).uvs(7,  0,  9,  2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 14,  9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7,  7,  9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(14,  7, 16, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0,  7,  2, 10).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 6, 16)
                    .to(9, 9, 18)
                    .face(Direction.DOWN).uvs(7, 14,  9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7,  0,  9,  2).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(7,  7,  9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0,  7,  2, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(14,  7, 16, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintFenceInventoryModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceInventory(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFenceInventory(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceInventory(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceInventoryModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceInventoryModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFencePost(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintFencePostModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFencePost(numberOfTints);

        AssetPackRegistries.saveBlockModel("tint_" + modelName, tintModel, true);

        return tintModel;
    }

    ////

    public void tcFencePost(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFencePost(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFencePostModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFencePostModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintFenceSide(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(7, 12, 0)
                    .to(9, 15, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 1, 9, 4).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 1, 9, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 1, 9, 4).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 6, 0)
                    .to(9, 9, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintFenceSideModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintFenceSide(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcFenceSide(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceSide(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcFenceSideModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintFenceSideModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tcHangingSign(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model.texture("particle", particle);
    }

    ////
    public TCBlockModelContent tcHangingSignModel(String baseName, ResourceLocation particle) {
        

        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintLeaves(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN) .uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST) .uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST) .uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(7, 6, 0)
                    .to(9, 9, 9)
                    .face(Direction.DOWN).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 9).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.WEST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 7, 9, 10).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintLeavesModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintLeaves(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcLeaves(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintLeaves(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcLeavesModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintLeavesModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);

        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintCubeSeparate(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintCubeSeparateModel(String baseName, int numberOfTints){
        String name = "tint_" + baseName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintCubeSeparate(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcCubeSeparate(int numberOfTints){
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(numberOfTints);

        model.parent(tintModel);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.texture("down_" + i,"#down_" + i);
            model.texture("up_" + i, "#up_" + i);
            model.texture("north_" + i, "#north_" + i);
            model.texture("south_" + i, "#south_" + i);
            model.texture("west_" + i, "#west_" + i);
            model.texture("east_" + i, "#east_" + i);
        }
    }

    public void tcCubeSeparateDefine(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap){
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(textureMap.size());

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) ->{
            model.texture("down_" + integer, resourceLocation.down);
            model.texture("up_" + integer, resourceLocation.up);
            model.texture("north_" + integer, resourceLocation.north);
            model.texture("south_" + integer, resourceLocation.south);
            model.texture("west_" + integer, resourceLocation.west);
            model.texture("east_" + integer, resourceLocation.east);
        });
    }

    public TCBlockModelContent tcCubeSeparateModel(String baseName, int numberOfTints){
        

        TCBlockModelContent tintModel = tintCubeSeparateModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", "#partical");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("down_" + i,"#down_" + i);
            modelContent.model().texture("up_" + i, "#up_" + i);
            modelContent.model().texture("north_" + i, "#north_" + i);
            modelContent.model().texture("south_" + i, "#south_" + i);
            modelContent.model().texture("west_" + i, "#west_" + i);
            modelContent.model().texture("east_" + i, "#east_" + i);
        }

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    public TCBlockModelContent tcCubeSeparateModelDefine(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap){
        

        TCBlockModelContent tintModel = tintCubeSeparateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);


        textureMap.forEach((integer, resourceLocation) ->{
            modelContent.model().texture("down_" + integer,resourceLocation.down);
            modelContent.model().texture("up_" + integer, resourceLocation.up);
            modelContent.model().texture("north_" + integer, resourceLocation.north);
            modelContent.model().texture("south_" + integer, resourceLocation.south);
            modelContent.model().texture("west_" + integer, resourceLocation.west);
            modelContent.model().texture("east_" + integer, resourceLocation.east);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintCubeTogether(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintCubeTogetherModel(String baseName, int numberOfTints){
        String name = "tint_" + baseName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintCubeTogether(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcCubeTogether(int numberOfTints){
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeTogether(numberOfTints);

        model.parent(tintModel);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.texture("texture_" + i, "#texture_" + i);
        }
    }

    public void tcCubeTogetherDefine(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeTogether(textureMap.size());

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcCubeTogetherModel(String baseName, int numberOfTints){
        

        TCBlockModelContent tintModel = tintCubeTogetherModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            modelContent.model().texture("texture_" + i, "#texture_" + i);
        }

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    public TCBlockModelContent tcCubeTogetherModelDefine(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        

        TCBlockModelContent tintModel = tintCubeTogetherModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcColumn(int numberOfTints) {
        String name = modelName + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, false);

        model.parent(tintModel);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++){
            model.texture("down_" + i, "#end_" + i);
            model.texture("up_" + i, "#end_" + i);
            model.texture("north_" + i, "#side_" + i);
            model.texture("south_" + i, "#side_" + i);
            model.texture("west_" + i, "#side_" + i);
            model.texture("east_" + i, "#side_" + i);
        }
    }

    public void tcColumnDefine(ResourceLocation particle, Map<Integer, TCBlockModelContent.ColumnAssetRecord> textureMap) {
        String name = modelName + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, false);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) ->{
            model.texture("down_" + integer, resourceLocation.end);
            model.texture("up_" + integer, resourceLocation.end);
            model.texture("north_" + integer, resourceLocation.side);
            model.texture("south_" + integer, resourceLocation.side);
            model.texture("west_" + integer, resourceLocation.side);
            model.texture("east_" + integer, resourceLocation.side);
        });
    }

    ////

    public TCBlockModelContent tcColumnModel(String baseName, int numberOfTints) {
        

        TCBlockModelContent tintModel = tcCubeSeparateModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        for (int i = 0; i < numberOfTints; i++){
            modelContent.model().texture("down_" + i, "#end_" + i);
            modelContent.model().texture("up_" + i, "#end_" + i);
            modelContent.model().texture("north_" + i, "#side_" + i);
            modelContent.model().texture("south_" + i, "#side_" + i);
            modelContent.model().texture("west_" + i, "#side_" + i);
            modelContent.model().texture("east_" + i, "#side_" + i);
        }

        AssetPackRegistries.saveBlockModel(baseName, modelContent, false);

        return modelContent;
    }

    public TCBlockModelContent tcColumnModelDefine(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.ColumnAssetRecord> textureMap) {
        

        TCBlockModelContent tintModel = tcCubeSeparateModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        textureMap.forEach((integer, resourceLocation) ->{
            model.texture("down_" + integer, resourceLocation.end);
            model.texture("up_" + integer, resourceLocation.end);
            model.texture("north_" + integer, resourceLocation.side);
            model.texture("south_" + integer, resourceLocation.side);
            model.texture("west_" + integer, resourceLocation.side);
            model.texture("east_" + integer, resourceLocation.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, false);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintColumnHorizontal(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");
        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintColumnHorizontalModel(String baseName, int numberOfTints){
        String name = "tint_" + baseName + "_cube_column_horizontal";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintColumnHorizontal(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcColumnHorizontal(int numberOfTints) {
        String name = modelName + "_cube_column_horizontal";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintColumnHorizontal(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, false);

        model.parent(tintModel);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++){
            model.texture("down_" + i, "#end_" + i);
            model.texture("up_" + i, "#end_" + i);
            model.texture("north_" + i, "#side_" + i);
            model.texture("south_" + i, "#side_" + i);
            model.texture("west_" + i, "#side_" + i);
            model.texture("east_" + i, "#side_" + i);
        }
    }

    public TCBlockModelContent tcColumnHorizontalModel(String baseName, int numberOfTints) {
        String name = baseName + "_cube_column_horizontal";

        TCBlockModelContent tintModel = tintColumnHorizontalModel(baseName, numberOfTints);
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");


        for (int i = 0; i < numberOfTints; i++){
            modelContent.model().texture("down_" + i, "#end_" + i);
            modelContent.model().texture("up_" + i, "#end_" + i);
            modelContent.model().texture("north_" + i, "#side_" + i);
            modelContent.model().texture("south_" + i, "#side_" + i);
            modelContent.model().texture("west_" + i, "#side_" + i);
            modelContent.model().texture("east_" + i, "#side_" + i);
        }

        AssetPackRegistries.saveBlockModel(name, modelContent, false);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcLog(ResourceLocation particle,  Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tcColumn(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            model.texture("#side_" + integer, logAssetRecord.side);
            model.texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model.getUncheckedLocation(), resourceLocationMap);
    }

    ////
    public TCBlockModelContent tcLogModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        

        TCBlockModelContent tintModel = tcColumnModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            modelContent.model().texture("#side_" + integer, logAssetRecord.side);
            modelContent.model().texture("#end_" + integer, logAssetRecord.end);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
        //this.resourceTextureMap(tintModel.model.getUncheckedLocation(), resourceLocationMap);
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tcLogHorizontal(ResourceLocation particle,  Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintColumnHorizontal(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            model.texture("#side_" + integer, logAssetRecord.side);
            model.texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model.getUncheckedLocation(), resourceLocationMap);
    }

    ////
    public TCBlockModelContent tcLogHorizontal(String baseName, ResourceLocation particle,  Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        String name = baseName +  "_horizontal";

        TCBlockModelContent tintModel = tcColumnHorizontalModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            modelContent.model().texture("#side_" + integer, logAssetRecord.side);
            modelContent.model().texture("#end_" + integer, logAssetRecord.end);
        });

        AssetPackRegistries.saveBlockModel(name, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tcPlanksTogether(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        this.tcCubeTogetherDefine(baseName, particle, textureMap);
    }
    public TCBlockModelContent tcPlanksTogetherModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        return tcCubeTogetherModelDefine(baseName, particle, textureMap);
    }

    public void tcPlanksSeparate(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap){
        this.tcCubeSeparateDefine(baseName, particle, textureMap);
    }
    public TCBlockModelContent tcPlanksSeparateModel(String baseName, ResourceLocation particle, Map<Integer, CubeSeparateRecord> textureMap){
        return tcCubeSeparateModelDefine(baseName, particle, textureMap);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateUp(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintPressurePlateUpModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateUp(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcPressurePlateUp(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintPressurePlateUp(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcPressurePlateUpModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintPressurePlateUpModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateDown(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(1, 0, 1)
                    .to(15, 0.5F, 15)
                    .face(Direction.DOWN) .uvs(1,  1, 15, 15) .texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs(1,  1, 15, 15) .texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST) .uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST) .uvs(1, 15, 15, 15.5F).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public TCBlockModelContent tintPressurePlateDownModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintPressurePlateDown(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcPressurePlateDown(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintPressurePlateDown(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcPressurePlateDownModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintPressurePlateDownModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        AssetPackRegistries.saveBlockModel(baseName, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintCross(int numberOfTints) {
        model.ao(false);
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++){
            model.element()
                    .from(0.8F, 0F, 8F)
                    .to(15.2F, 16F, 8F)
                    .shade(false)
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .rotation().origin(8, 8, 8)
                    .axis(Direction.Axis.Y).angle(45F).rescale(true);
            model.element()
                    .from(8F, 0F, 0.8F)
                    .to(8F, 16F, 15.2F)
                    .shade(false)
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .rotation().origin(8, 8, 8)
                    .axis(Direction.Axis.Y).angle(45F).rescale(true);
        }
    }

    public TCBlockModelContent tintCrossModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintCross(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcCross(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCross(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public TCBlockModelContent tcCrossModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintCrossModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> modelContent.model().texture("texture_" + integer, resourceLocation));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcSapling(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        tcCross(particle, textureMap);
    }
    public TCBlockModelContent tcSaplingModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap){
        return tcCrossModel(baseName, particle, textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------

    public void tcSign(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model.texture("particle", particle);
    }

    ////
    public TCBlockModelContent tcSignModel(String baseName, ResourceLocation particle) {
        

        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().texture("particle", particle);

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintSlab(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintSlabModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintSlab(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcTintSlab(ResourceLocation particle, Map<Integer, TCBlockModelContent.SlabRecord> textureMap) {
        String name = "tint_" + modelName;
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintSlab(textureMap.size());

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public TCBlockModelContent tcTintSlabModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.SlabRecord> textureMap) {
        

        TCBlockModelContent tintModel = tintSlabModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model.parent(tintModel);
        modelContent.model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model.texture("bottom_" + integer, record.bottom);
            modelContent.model.texture("top_" + integer, record.top);
            modelContent.model.texture("side_" + integer, record.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTopTintSlab(int numberOfTints) {
        model.texture("particle", "#side");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
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

    public TCBlockModelContent tintTopTintSlabModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintTopTintSlab(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tintTopTintSlab(ResourceLocation particle, Map<Integer, TCBlockModelContent.SlabRecord> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintTopTintSlab(textureMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public TCBlockModelContent tcTopTintSlabModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.SlabRecord> textureMap) {
        

        TCBlockModelContent tintModel = tintTopTintSlabModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintStairs(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.transforms().transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0F, 0F, 0F)
                .scale(0.625F, 0.625F, 0.625F).end();
        model.transforms().transform(ItemDisplayContext.HEAD)
                .rotation(0, -90, 0)
                .translation(0, 0, 0)
                .scale(1, 1, 1).end();
        model.transforms().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(75, -135, 0)
                .translation(0, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F).end();

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(8, 8, 0)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCBlockModelContent tintStairsModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintStairs(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcStairs(ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> assetMap) {
        String name = "tint_" + modelName;
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintStairs(assetMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public TCBlockModelContent tcStairsModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> textureMap) {
        

        TCBlockModelContent tintModel = tintStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintInnerStairs(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(8, 8, 0)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(0, 8, 8)
                    .to(8, 16, 16)
                    .face(Direction.UP).uvs(0, 8, 8, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end();
        }

    }

    public TCBlockModelContent tintInnerStairsModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintInnerStairs(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcInnerStairs(ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> assetMap) {
        String name = "tint_" + modelName;
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintInnerStairs(assetMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public TCBlockModelContent tcInnerStairsModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> textureMap) {
        

        TCBlockModelContent tintModel = tintInnerStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintOuterStairs(int numberOfTints) {
        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(16, 8, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();

            model.element()
                    .from(8, 8, 8)
                    .to(16, 16, 16)
                    .face(Direction.UP).uvs(8, 8, 16, 16).texture("#top_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(8, 0, 16, 8).texture("#side_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 8, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCBlockModelContent tintOuterStairsModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintOuterStairs(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////

    public void tcOuterStairs(ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintOuterStairs(textureMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public TCBlockModelContent tcOuterStairsModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.StairsRecord> textureMap) {
        

        TCBlockModelContent tintModel = tintOuterStairsModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        AssetPackRegistries.saveBlockModel(baseName, tintModel, true);

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.model().texture("bottom_" + integer, record.bottom);
            modelContent.model().texture("top_" + integer, record.top);
            modelContent.model().texture("side_" + integer, record.side);
        });

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorBottom(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 0)
                    .to(16, 3, 16)
                    .face(Direction.DOWN).uvs( 0,  0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs(0,  0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST) .uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST) .uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCBlockModelContent tintTrapDoorBottomModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorBottom(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcTrapDoorBottom(ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintTrapDoorBottom(assetMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        assetMap.forEach((integer, record) -> model.texture("texture_" + integer, record));
    }

    public TCBlockModelContent tcTrapDoorBottomModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintTrapDoorBottomModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorOpen(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 0, 13)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs(  0, 13, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP)   .uvs( 0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs( 0,  0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs( 0,  0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST) .uvs(16,  0, 13, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST) .uvs(13,  0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCBlockModelContent tintTrapDoorOpenModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorOpen(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcTrapDoorOpen(ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintTrapDoorOpen(assetMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        assetMap.forEach((integer, record) -> model.texture("texture_" + integer, record));
    }

    public TCBlockModelContent tcTrapDoorOpenModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintTrapDoorOpenModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintTrapDoorTop(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);

        model.texture("particle", "#particle");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0, 13, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).uvs( 0,  0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP)   .uvs(0,  0, 16, 16).texture("#texture_" + i).tintindex(i).cullface(Direction.UP).end()
                    .face(Direction.NORTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.SOUTH).end()
                    .face(Direction.WEST) .uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST) .uvs(0, 16, 16, 13).texture("#texture_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public TCBlockModelContent tintTrapDoorTopModel(String baseName, int numberOfTints) {
        String name = "tint_" + baseName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintTrapDoorTop(numberOfTints);

        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcTrapDoorTop(ResourceLocation particle, Map<Integer, ResourceLocation> assetMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintTrapDoorTop(assetMap.size());
        AssetPackRegistries.saveBlockModel(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        assetMap.forEach((integer, record) -> model.texture("texture_" + integer, record));
    }

    public TCBlockModelContent tcTrapDoorTopModel(String baseName, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        

        TCBlockModelContent tintModel = tintTrapDoorTopModel(baseName, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(baseName, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.model().parent(tintModel);
        modelContent.model().texture("particle", particle);

        textureMap.forEach((integer, record) -> modelContent.model().texture("texture_" + integer, record));

        AssetPackRegistries.saveBlockModel(baseName, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tcWood(ResourceLocation particle, Map<Integer, TCBlockModelContent.ColumnAssetRecord> textureMap){
        tcColumnDefine(particle, textureMap);
    }

    public TCBlockModelContent tcWoodModel(String baseName, ResourceLocation particle, Map<Integer, TCBlockModelContent.ColumnAssetRecord> textureMap){
        return tcColumnModelDefine(baseName, particle, textureMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public record CubeSeparateRecord(ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation west, ResourceLocation east){}

    public record SlabRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side){}
    public record StairsRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {}

    public record LogAssetRecord(ResourceLocation side, ResourceLocation end) {}

    public record ColumnAssetRecord(ResourceLocation side, ResourceLocation end) {}

}