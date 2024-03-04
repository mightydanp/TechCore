package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public class TCBlockModelContent extends ModelContent<TCBlockModelContent>{
    public static final String BLOCK_FOLDER = "block";

    public TCBlockModelContent(String modelName, String modelFolder, String parentFolder) {
        super(modelName, modelFolder, parentFolder);
    }

    public TCBlockModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        super(modelName, modid, modelFolder, parentFolder);
    }

    public TCBlockModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        super(resourceLocation, modelFolder, parentFolder);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintButton(int numberOfTints) {
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

    public TCBlockModelContent tintButtonModel(String baseName, String category, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintButton(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }
    ////
    public void tcButton(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButton(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public TCBlockModelContent tcButtonModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintButtonModel(baseName, category, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.getModel().texture("texture_" + integer, resourceLocation);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintButtonInventory(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public TCBlockModelContent tintButtonInventoryModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintButtonInventory(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }
    ////

    public void tcButtonInventory(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButtonInventory(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public TCBlockModelContent tcButtonInventoryModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintButtonInventoryModel(baseName, category, particle, textureMap.size());

        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.getModel().texture("texture_" + integer, resourceLocation);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

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

    public TCBlockModelContent tintButtonPressedModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintButtonPressed(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcButtonPressed(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintButtonPressed(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public void tcButtonPressedModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintButtonPressedModel(baseName, category, particle, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            modelContent.getModel().texture("texture_" + integer, resourceLocation);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeft(int numberOfTints) {
        model.ao(false);

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

    public TCBlockModelContent tintDoorBottomLeftModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeft(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomLeft(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomLeft(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
    }

    public TCBlockModelContent tcDoorBottomLeftModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintDoorBottomLeftModel(baseName, category, particle, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.getModel().texture("bottom_" + integer, record);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomLeftOpen(int numberOfTints) {
        model.ao(false);

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

    public TCBlockModelContent tintDoorBottomLeftOpenModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomLeftOpen(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomLeftOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
    }

    public TCBlockModelContent tcDoorBottomLeftOpenModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintDoorBottomLeftOpenModel(baseName, category, particle, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.getModel().texture("bottom_" + integer, record);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        return modelContent;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRight(int numberOfTints) {
        model.ao(false);

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
    public TCBlockModelContent tintDoorBottomRightModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRight(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomRight(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomRight(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
    }

    public TCBlockModelContent tcDoorBottomRightModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintDoorBottomRightModel(baseName, category, particle, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.getModel().texture("bottom_" + integer, record);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorBottomRightOpen(int numberOfTints) {
        model.ao(false);

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

    public TCBlockModelContent tintDoorBottomRightOpenModel(String baseName, String category, ResourceLocation particle, int numberOfTints) {
        String name = "tint_" + baseName + "_" + category;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");
        tintModel.tintDoorBottomRightOpen(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        return tintModel;
    }

    ////
    public void tcDoorBottomRightOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorBottomRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
    }

    public TCBlockModelContent tcDoorBottomRightOpenModel(String baseName, String category, ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = baseName + "_" + category;

        TCBlockModelContent tintModel = tintDoorBottomRightOpenModel(baseName, category, particle, textureMap.size());
        TCBlockModelContent modelContent = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/" + baseName + "/");

        modelContent.getModel().parent(tintModel);

        modelContent.getModel().texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            modelContent.getModel().texture("bottom_" + integer, record);
        });

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        return modelContent;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeft(int numberOfTints) {
        model.ao(false);

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

    public void tcDoorTopLeft(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopLeft(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("top_" + integer, record);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopLeftOpen(int numberOfTints) {
        model.ao(false);

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

    public void tcDoorTopLeftOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("top_" + integer, record);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRight(int numberOfTints) {
        model.ao(false);

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

    public void tcDoorTopRight(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopRight(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("top_" + integer, record);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintDoorTopRightOpen(int numberOfTints) {
        model.ao(false);

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

    public void tcDoorTopRightOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintDoorTopRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("top_" + integer, record);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGate(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public void tcFenceGate(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGate(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateOpen(int numberOfTints) {

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

    public void tcFenceGateOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWall(int numberOfTints) {
        model.ao(true);

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

    public void tcFenceGateWall(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateWall(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGateWallOpen(int numberOfTints) {
        model.ao(true);

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

    public void tcFenceGateWallOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceGateWallOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceInventory(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);
        model.transforms.transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .translation(0, 0, 0)
                .scale(0.625F, 0.625F, 0.625F).end();
        model.transforms.transform(ItemDisplayContext.FIXED)
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

    public void tcFenceInventory(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceInventory(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFencePost(int numberOfTints) {

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

    public void tcFencePost(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFencePost(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tintFenceSide(int numberOfTints) {

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

    public void tcFenceSide(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintFenceSide(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    //------------------------------------------------------------------------------------------------------------------

    public void tcHangingSign(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        this.model.texture("particle", particle);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintLeaves(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public void tcLeaves(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintLeaves(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintCubeSeparate(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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

    public void tcCubeSeparate(ResourceLocation particle,  Map<Integer, TCBlockModelContent.CubeSeparate> textureMap){
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(textureMap.size());

    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintCubeTogether(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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


    //------------------------------------------------------------------------------------------------------------------
    public void tcColumn(int numberOfTints) {
        String name = modelName + "_cube";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintCubeSeparate(numberOfTints);

        AssetPackRegistries.saveBlockModelContent(name, tintModel, false);

        model.parent(tintModel);

        for (int i = 0; i < numberOfTints; i++){
            model.texture("down_" + i, "#end_" + i);
            model.texture("up_" + i, "#end_" + i);
            model.texture("north_" + i, "#side_" + i);
            model.texture("south_" + i, "#side_" + i);
            model.texture("west_" + i, "#side_" + i);
            model.texture("east_" + i, "#side_" + i);
        };
    }

    public void tintColumnHorizontal(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

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
    //------------------------------------------------------------------------------------------------------------------
    public void tcLog(ResourceLocation particle,  Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        String name = modelName + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tcColumn(textureMap.size());
        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            model.texture("#side_" + integer, logAssetRecord.side);
            model.texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model.getUncheckedLocation(), resourceLocationMap);
    }

    public void tcLogHorizontal(ResourceLocation particle,  Map<Integer, TCBlockModelContent.LogAssetRecord> textureMap) {
        String name = modelName + "_cube_column";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintColumnHorizontal(textureMap.size());

        for (int i = 0; i < textureMap.size(); i++){
            tintModel.model.texture("down_" + i, "#end_" + i);
            tintModel.model.texture("up_" + i, "#end_" + i);
            tintModel.model.texture("north_" + i, "#side_" + i);
            tintModel.model.texture("south_" + i, "#side_" + i);
            tintModel.model.texture("west_" + i, "#side_" + i);
            tintModel.model.texture("east_" + i, "#side_" + i);
        };

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        model.parent(tintModel);
        model.texture("particle", particle);

        textureMap.forEach((integer, logAssetRecord) ->{
            model.texture("#side_" + integer, logAssetRecord.side);
            model.texture("#end_" + integer, logAssetRecord.end);
        });

        //this.resourceTextureMap(tintModel.model.getUncheckedLocation(), resourceLocationMap);
    }
    //------------------------------------------------------------------------------------------------------------------
    public void tintPressurePlateUp(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);
        model.texture("particle", "#texture_0");

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

    public void tcPressurePlateUp(Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintPressurePlateUp(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public void tintPressurePlateDown(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.thin_block.model);
        model.texture("particle", "#texture_0");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(1, 0, 1)
                    .to(15, 0.5F, 15)
                    .face(Direction.DOWN).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(1, 1, 15, 15).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(1, 15, 15, 16).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public void tcPressurePlateDown(Map<Integer, ResourceLocation> textureMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintPressurePlateDown(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public void tintStairs(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.transforms().transform(ItemDisplayContext.GUI).rotation(30, 135, 0).translation(0F, 0F, 0F).scale(0.625F, 0.625F, 0.625F).build();
        model.transforms().transform(ItemDisplayContext.HEAD).rotation(0, -90, 0).translation(0F, 0F, 0F).scale(1F, 1F, 1F).build();
        model.transforms().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, -135, 0).translation(0F, 2.5F, 0F).scale(0.375F, 0.375F, 0.375F).build();

        model.texture("particle", "#side_0");

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
                    .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side_" + i).tintindex(i).cullface(Direction.EAST).end();
        }

    }

    public void tcStairs(Map<Integer, TCBlockModelContent.StairsRecord> assetMap) {
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public void tintInnerStairs(int numberOfTints) {
        TCModelBuilder model = getModel();
        model.texture("particle", "#side_0");

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

    public void tcInnerStairs(Map<Integer, TCBlockModelContent.StairsRecord> assetMap) {
        //todo separate resources for tinting
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintInnerStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public void tintOuterStairs(int numberOfTints) {
        TCModelBuilder model = getModel();
        model.texture("particle", "#side_0");

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

    public void tcOuterStairs(Map<Integer, TCBlockModelContent.StairsRecord> assetMap) {
        TCBlockModelContent tintModel = new TCBlockModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintOuterStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, tintModel, true);

        model.parent(tintModel);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public void tcSaplingCross() {
        model.ao(false);
        //model.texture("particle", )
        model.element()
                .from(0.8F, 0F, 8F)
                .to(15.2F, 16F, 8F)
                .shade(false)
                .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#overlay_0").tintindex(0).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#overlay_0").tintindex(0).end()
                .rotation().origin(8, 8, 8)
                .axis(Direction.Axis.Y).angle(45F).rescale(true);
        model.element()
                .from(0.8F, 0F, 8F)
                .to(15.2F, 16F, 8F)
                .shade(false)
                .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#overlay_1").tintindex(1).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#overlay_1").tintindex(1).end()
                .rotation().origin(8, 8, 8)
                .axis(Direction.Axis.Y).angle(45F).rescale(true);
        model.element()
                .from(8F, 0F, 0.8F)
                .to(8F, 16F, 15.2F)
                .shade(false)
                .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#overlay_0").tintindex(0).end()
                .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#overlay_0").tintindex(0).end()
                .rotation().origin(8, 8, 8)
                .axis(Direction.Axis.Y).angle(45F).rescale(true);
        model.element()
                .from(8F, 0F, 0.8F)
                .to(8F, 16F, 15.2F)
                .shade(false)
                .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#overlay_1").tintindex(1).end()
                .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#overlay_1").tintindex(1).end()
                .rotation().origin(8, 8, 8)
                .axis(Direction.Axis.Y).angle(45F).rescale(true);
    }

    public void tintSlab() {
        model.texture("particle", "#side");
        model.element()
                .from(0F, 0F, 0F)
                .to(16F, 8F, 16F)
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").tintindex(0).end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#toptop").cullface(Direction.UP).tintindex(0).end()
                .face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
                .face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
                .face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side").cullface(Direction.WEST).tintindex(0).end()
                .face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side").cullface(Direction.EAST).tintindex(0).end();

    }

    public void tcTintSlab(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
        String name = "tint_" + modelName;
        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tintSlab();

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        this.resourceTextureMap(tintModel.getModel().getUncheckedLocation(), Map.of("bottom", bottom, "top", top, "side", side));
    }

    public void tcTopTintSlab() {
        model.texture("particle", "#side");

        model.element()
                .from(0F, 8F, 0F)
                .to(16F, 16F, 16F)
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").tintindex(0).end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#toptop").cullface(Direction.UP).tintindex(1).end()
                .face(Direction.NORTH).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.NORTH).tintindex(2).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.SOUTH).tintindex(2).end()
                .face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.WEST).tintindex(2).end()
                .face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side").cullface(Direction.EAST).tintindex(2).end();

    }

    public void tcTopTintSlab(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
        String name = "top_tint" + modelName;

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tcTopTintSlab();
        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        this.resourceTextureMap(tintModel.getModel().getUncheckedLocation(), Map.of("bottom", bottom, "top", top, "side", side));
    }

    public void tcSaplingCross(ResourceLocation overlay_0, ResourceLocation overlay_1) {
        String name = "ta_" + modelName + "_cross";

        TCBlockModelContent tintModel = new TCBlockModelContent(name, BLOCK_FOLDER, "tree_icons/");
        tintModel.tcSaplingCross();

        AssetPackRegistries.saveBlockModelContent(name, tintModel, true);

        this.resourceTextureMap(tintModel.getModel().getUncheckedLocation(), Map.of("overlay_0", overlay_0, "overlay_1", overlay_1));
    }

    public record CubeSeparate(ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation west, ResourceLocation east){}
    public record StairsRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {}

    public record LogAssetRecord(ResourceLocation side, ResourceLocation end) {}


}