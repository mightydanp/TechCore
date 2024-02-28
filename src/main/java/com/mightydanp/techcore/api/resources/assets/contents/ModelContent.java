package com.mightydanp.techcore.api.resources.assets.contents;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class ModelContent {
    public static final String BLOCK_FOLDER = "block";
    public static final String ITEM_FOLDER = "item";

    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");
    private final TCModelBuilder model;
    private final String modid;
    private final String modelFolder;
    private final String parentFolder;
    public String modelName;

    public ModelContent(String modelName, String modelFolder, String parentFolder) {
        this.modelName = modelName;
        this.modid = CoreRef.MOD_ID;
        this.modelFolder = modelFolder;
        this.parentFolder = parentFolder;
        this.model = new TCModelBuilder(new ResourceLocation(CoreRef.MOD_ID, "models/" + modelFolder + "/" + (parentFolder == null ? "" : parentFolder + "/") + modelName + ".json"));
    }

    public ModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        this.modelName = modelName;
        this.modid = modid;
        this.modelFolder = modelFolder;
        this.parentFolder = parentFolder;
        this.model = new TCModelBuilder(new ResourceLocation(modid, "models/" + modelFolder + "/" + (parentFolder == null ? "" : parentFolder + "/") + modelName + ".json"));
    }

    public ModelContent(ResourceLocation resourceLocation, String modelFolder, String parentFolder) {
        this.modelName = resourceLocation.getPath();
        this.modid = resourceLocation.getNamespace();
        this.modelFolder = modelFolder;
        this.parentFolder = parentFolder;
        this.model = new TCModelBuilder(new ResourceLocation(modid, "models/" + modelFolder + "/" + (parentFolder == null ? "" : parentFolder + "/") + modelName + ".json"));
    }

    public TCModelBuilder getModel() {
        return model;
    }

    public String getModelFolder() {
        return modelFolder;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public JsonObject createJson() {
        return model.toJson();
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), modelFolder + "/" + rl.getPath());
    }

    public ModelFile.UncheckedModelFile getFile(ResourceLocation path) {
        ModelFile.UncheckedModelFile ret = new ModelFile.UncheckedModelFile(extendWithFolder(path));
        ret.assertExistence();
        return ret;
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(modid, name);
    }

    public TCModelBuilder withExistingParent(String parent) {
        return withExistingParent(mcLoc(parent));
    }

    public TCModelBuilder withExistingParent(ResourceLocation parent) {
        return model.parent(getFile(parent));
    }

    public TCModelBuilder cube(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        return withExistingParent("cube")
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west);
    }

    private TCModelBuilder singleTexture(String parent, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), texture);
    }

    public TCModelBuilder singleTexture(ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(parent, "texture", texture);
    }

    private TCModelBuilder singleTexture(String parent, String textureKey, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), textureKey, texture);
    }

    public TCModelBuilder singleTexture(ResourceLocation parent, String textureKey, ResourceLocation texture) {
        return withExistingParent(parent)
                .texture(textureKey, texture);
    }

    public TCModelBuilder cubeAll(ResourceLocation texture) {
        return singleTexture(BLOCK_FOLDER + "/cube_all", "all", texture);
    }

    public TCModelBuilder cubeTop(ResourceLocation side, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/cube_top")
                .texture("side", side)
                .texture("top", top);
    }

    private TCModelBuilder sideBottomTop(String parent, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(parent)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder cubeBottomTop(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/cube_bottom_top", side, bottom, top);
    }

    public TCModelBuilder cubeColumn(ResourceLocation side, ResourceLocation end) {
        return withExistingParent(BLOCK_FOLDER + "/cube_column")
                .texture("side", side)
                .texture("end", end);
    }

    public TCModelBuilder cubeColumnHorizontal(ResourceLocation side, ResourceLocation end) {
        return withExistingParent(BLOCK_FOLDER + "/cube_column_horizontal")
                .texture("side", side)
                .texture("end", end);
    }

    public TCModelBuilder orientableVertical(ResourceLocation side, ResourceLocation front) {
        return withExistingParent(BLOCK_FOLDER + "/orientable_vertical")
                .texture("side", side)
                .texture("front", front);
    }

    public TCModelBuilder orientableWithBottom(ResourceLocation side, ResourceLocation front, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/orientable_with_bottom")
                .texture("side", side)
                .texture("front", front)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder orientable(ResourceLocation side, ResourceLocation front, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/orientable")
                .texture("side", side)
                .texture("front", front)
                .texture("top", top);
    }

    public TCModelBuilder crop(ResourceLocation crop) {
        return singleTexture(BLOCK_FOLDER + "/crop", "crop", crop);
    }

    public TCModelBuilder cross(ResourceLocation cross) {
        return singleTexture(BLOCK_FOLDER + "/cross", "cross", cross);
    }

    public TCModelBuilder stairs(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/stairs", side, bottom, top);
    }

    public TCModelBuilder stairsOuter(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/outer_stairs", side, bottom, top);
    }

    public TCModelBuilder stairsInner(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/inner_stairs", side, bottom, top);
    }

    public TCModelBuilder slab(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/slab", side, bottom, top);
    }

    public TCModelBuilder slabTop(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/slab_top", side, bottom, top);
    }

    public TCModelBuilder button(ResourceLocation texture) {
        return singleTexture(BLOCK_FOLDER + "/button", texture);
    }

    public TCModelBuilder buttonPressed(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_pressed", texture);
    }

    public TCModelBuilder buttonInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_inventory", texture);
    }

    public TCModelBuilder pressurePlate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_up", texture);
    }

    public TCModelBuilder pressurePlateDown(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_down", texture);
    }

    public TCModelBuilder sign(String name, ResourceLocation texture) {
        return model.texture("particle", texture);
        //return getBuilder(name).texture("particle", texture);
    }

    public TCModelBuilder fencePost(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_post", texture);
    }

    public TCModelBuilder fenceSide(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_side", texture);
    }

    public TCModelBuilder fenceInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_inventory", texture);
    }

    public TCModelBuilder fenceGate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate", texture);
    }

    public TCModelBuilder fenceGateOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_open", texture);
    }

    public TCModelBuilder fenceGateWall(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall", texture);
    }

    public TCModelBuilder fenceGateWallOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall_open", texture);
    }

    public TCModelBuilder wallPost(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_post", "wall", wall);
    }

    public TCModelBuilder wallSide(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_side", "wall", wall);
    }

    public TCModelBuilder wallSideTall(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_side_tall", "wall", wall);
    }

    public TCModelBuilder wallInventory(String name, ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/wall_inventory", "wall", wall);
    }

    private TCModelBuilder pane(String parent, ResourceLocation pane, ResourceLocation edge) {
        return withExistingParent(BLOCK_FOLDER + "/" + parent)
                .texture("pane", pane)
                .texture("edge", edge);
    }

    public TCModelBuilder panePost(ResourceLocation pane, ResourceLocation edge) {
        return pane("template_glass_pane_post", pane, edge);
    }

    public TCModelBuilder paneSide(ResourceLocation pane, ResourceLocation edge) {
        return pane("template_glass_pane_side", pane, edge);
    }

    public TCModelBuilder paneSideAlt(ResourceLocation pane, ResourceLocation edge) {
        return pane("template_glass_pane_side_alt", pane, edge);
    }

    public TCModelBuilder paneNoSide(ResourceLocation pane) {
        return singleTexture(BLOCK_FOLDER + "/template_glass_pane_noside", "pane", pane);
    }

    public TCModelBuilder paneNoSideAlt(ResourceLocation pane) {
        return singleTexture(BLOCK_FOLDER + "/template_glass_pane_noside_alt", "pane", pane);
    }

    private TCModelBuilder door(String model, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/" + model)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder doorBottomLeft(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_left", bottom, top);
    }

    public TCModelBuilder doorBottomLeftOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_left_open", bottom, top);
    }

    public TCModelBuilder doorBottomRight(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_right", bottom, top);
    }

    public TCModelBuilder doorBottomRightOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_right_open", bottom, top);
    }

    public TCModelBuilder doorTopLeft(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_left", bottom, top);
    }

    public TCModelBuilder doorTopLeftOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_left_open", bottom, top);
    }

    public TCModelBuilder doorTopRight(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_right", bottom, top);
    }

    public TCModelBuilder doorTopRightOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_right_open", bottom, top);
    }

    public TCModelBuilder trapdoorBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_bottom", texture);
    }

    public TCModelBuilder trapdoorTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_top", texture);
    }

    public TCModelBuilder trapdoorOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_open", texture);
    }

    public TCModelBuilder trapdoorOrientableBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture);
    }

    public TCModelBuilder trapdoorOrientableTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture);
    }

    public TCModelBuilder trapdoorOrientableOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture);
    }

    public TCModelBuilder torch(ResourceLocation torch) {
        return singleTexture(BLOCK_FOLDER + "/template_torch", "torch", torch);
    }

    public TCModelBuilder torchWall(ResourceLocation torch) {
        return singleTexture(BLOCK_FOLDER + "/template_torch_wall", "torch", torch);
    }

    public TCModelBuilder carpet(ResourceLocation wool) {
        return singleTexture(BLOCK_FOLDER + "/carpet", "wool", wool);
    }

    //to-do also check to make sure everything has a tinted base model

    public TCModelBuilder singleTextureMap(ResourceLocation parent, Map<String, String> map) {
        TCModelBuilder model = withExistingParent(parent);

        map.forEach(model::texture);

        return model;
    }

    public void resourceTextureMap(ResourceLocation parent, Map<String, ResourceLocation> map) {
        model.parent(parent);
        map.forEach(model::texture);
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

    public void tcButton(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintButton(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
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

    public void tcButtonInventory(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintButtonInventory(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
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

    public void tcButtonPressed(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintButtonPressed(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
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

    public void tcDoorBottomLeft(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorBottomLeft(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
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

    public void tcDoorBottomLeftOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorBottomLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
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

    public void tcDoorBottomRight(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorBottomRight(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
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

    public void tcDoorBottomRightOpen(ResourceLocation particle, Map<Integer, ResourceLocation> textureMap) {
        String name = "tint_" + modelName;

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorBottomRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record);
        });
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

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorTopLeft(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

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

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorTopLeftOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

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

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorTopRight(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

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

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintDoorTopRightOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, record) -> {
            model.texture("top_" + integer, record);
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    public void tintFenceGate(int numberOfTints) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        model.transforms.transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .translation(0, -1, 0)
                .scale(0.8F, 0.8F, 0.8F).end();

        model.transforms.transform(ItemDisplayContext.HEAD)
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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceGate(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceGateOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceGateWall(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceGateWallOpen(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", particle);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintPressurePlateUp(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintPressurePlateDown(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

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
        model.texture("particle", "#texture_0");

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(6, 0, 0)
                    .to(10, 16, 4)
                    .face(Direction.DOWN).uvs(6, 0, 10, 4).texture("#texture_" + i).tintindex(i).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(6, 0, 10, 4).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 0, 4, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 0, 4, 16).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(6, 0, 12)
                    .to(10, 16, 16)
                    .face(Direction.DOWN).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(6, 12, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(6, 0, 10, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(12, 0, 16, 16).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 13, -2)
                    .to(9, 15, 18)
                    .face(Direction.DOWN).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 1, 9, 3).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(7, 1, 9, 3).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 1, 16, 3).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 1, 16, 3).texture("#texture_" + i).tintindex(i).end();

            model.element()
                    .from(7, 5, -2)
                    .to(9, 7, 18)
                    .face(Direction.DOWN).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.UP).uvs(7, 0, 9, 16).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.NORTH).uvs(7, 9, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.SOUTH).uvs(7, 9, 9, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.WEST).uvs(0, 9, 16, 11).texture("#texture_" + i).tintindex(i).end()
                    .face(Direction.EAST).uvs(0, 9, 16, 11).texture("#texture_" + i).tintindex(i).end();
        }
    }

    public void tcFenceInventory(Map<Integer, ResourceLocation> textureMap) {
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceInventory(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

        textureMap.forEach((integer, resourceLocation) -> {
            model.texture("texture_" + integer, resourceLocation);
        });
    }

    public void tintFencePost(int numberOfTints) {
        model.texture("particle", "#texture_0");

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

    public void tcFencePost(Map<Integer, ResourceLocation> textureMap) {
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFencePost(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);
        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
    }

    public void tintFenceSide(int numberOfTints) {
        model.texture("particle", "#texture_0");

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

    public void tcFenceSide(Map<Integer, ResourceLocation> textureMap) {
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintFenceSide(textureMap.size());

        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

        textureMap.forEach((integer, resourceLocation) -> model.texture("texture_" + integer, resourceLocation));
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

    public void tcStairs(Map<Integer, StairsRecord> assetMap) {
        //todo separate resources for tinting
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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

    public void tcInnerStairs(Map<Integer, StairsRecord> assetMap) {
        //todo separate resources for tinting
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintInnerStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

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

    public void tcOuterStairs(Map<Integer, StairsRecord> assetMap) {
        ModelContent modelContent = new ModelContent("tint_" + modelName, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintOuterStairs(assetMap.size());
        AssetPackRegistries.saveBlockModelContent("tint_" + modelName, modelContent, true);

        model.parent(modelContent);

        assetMap.forEach((integer, record) -> {
            model.texture("bottom_" + integer, record.bottom);
            model.texture("top_" + integer, record.top);
            model.texture("side_" + integer, record.side);
        });
    }

    public void tintCube(int numberOfTints) {
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


    public void tcLogTintCube(int numberOfTints, boolean Horizontal) {
        model.parent(TCModelBuilder.ExistingBlockModels.block.model);

        for (int i = 0; i < numberOfTints; i++) {
            model.element()
                    .from(0F, 0F, 0F)
                    .to(16F, 16F, 16F)
                    .face(Direction.DOWN).texture("#down_" + i).cullface(Direction.DOWN).tintindex(i).end()
                    .face(Direction.UP).texture("#up_" + i).cullface(Direction.UP).rotation(Horizontal ? TCModelBuilder.FaceRotation.UPSIDE_DOWN : TCModelBuilder.FaceRotation.ZERO).tintindex(i).end()
                    .face(Direction.NORTH).texture("#north_" + i).cullface(Direction.NORTH).tintindex(0).end()
                    .face(Direction.SOUTH).texture("#south_" + i).cullface(Direction.SOUTH).tintindex(0).end()
                    .face(Direction.WEST).texture("#west_" + i).cullface(Direction.WEST).tintindex(0).end()
                    .face(Direction.EAST).texture("#east_" + i).cullface(Direction.EAST).tintindex(0).end();
        }
    }

    public void tcLogCubeColumn(Map<Integer, logAssetRecord> assetMap, boolean Horizontal) {
        String name = modelName + "_cube";

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tcLogTintCube(assetMap.size(), Horizontal);
        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        model.parent(modelContent);

        model.texture("particle", "#side_0");

        assetMap.forEach((integer, resourceLocation) -> {
            model.texture("down_" + integer, "#end_" + integer);
            model.texture("up_" + integer, "#end_" + integer);
            model.texture("north_" + integer, "#side_" + integer);
            model.texture("east_" + integer, "#side_" + integer);
            model.texture("south_" + integer, "#side_" + integer);
            model.texture("west_" + integer, "#side_" + integer);
        });
    }

    public void tcLog(boolean Horizontal, Map<Integer, logAssetRecord> assetMap) {
        String name = modelName + "_cube_column";

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tcLogCubeColumn(assetMap, Horizontal);
        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        Map<String, ResourceLocation> resourceLocationMap = new HashMap<>();
        assetMap.forEach((integer, logAssets) -> {
            resourceLocationMap.put("#side_" + integer, logAssets.side);
            resourceLocationMap.put("#end_" + integer, logAssets.end);
        });

        this.resourceTextureMap(modelContent.model.getUncheckedLocation(), resourceLocationMap);
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
        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tintSlab();

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        this.resourceTextureMap(modelContent.getModel().getUncheckedLocation(), Map.of("bottom", bottom, "top", top, "side", side));
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

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tcTopTintSlab();
        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        this.resourceTextureMap(modelContent.getModel().getUncheckedLocation(), Map.of("bottom", bottom, "top", top, "side", side));
    }

    public void tcSaplingCross(ResourceLocation overlay_0, ResourceLocation overlay_1) {
        String name = "ta_" + modelName + "_cross";

        ModelContent modelContent = new ModelContent(name, BLOCK_FOLDER, "tree_icons/");
        modelContent.tcSaplingCross();

        AssetPackRegistries.saveBlockModelContent(name, modelContent, true);

        this.resourceTextureMap(modelContent.getModel().getUncheckedLocation(), Map.of("overlay_0", overlay_0, "overlay_1", overlay_1));
    }

    public record StairsRecord(ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
    }

    public record logAssetRecord(ResourceLocation side, ResourceLocation end) {
    }
}
    
}
