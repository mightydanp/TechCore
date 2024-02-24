package com.mightydanp.techcore.api.resources.assets.contents;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModelContent {
    public static final String BLOCK_FOLDER = "block";
    public static final String ITEM_FOLDER = "item";

    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");
    private final TCModelBuilder model;

    public String modelName;

    private final String modid;
    private final String modelFolder;
    private final String parentFolder;

    public ModelContent(String modelName, String modelFolder, String parentFolder) {
        this.modelName = modelName;
        this.modid = CoreRef.MOD_ID;
        this.modelFolder = modelFolder;
        this.parentFolder = parentFolder;
        this.model = new TCModelBuilder(new ResourceLocation(CoreRef.MOD_ID, "models/" + modelFolder + "/" + (parentFolder == null ? "" : parentFolder + "/")  + modelName + ".json"));
    }

    public ModelContent(String modelName, String modid, String modelFolder, String parentFolder) {
        this.modelName = modelName;
        this.modid = modid;
        this.modelFolder = modelFolder;
        this.parentFolder = parentFolder;
        this.model = new TCModelBuilder(new ResourceLocation(modid, "models/" + modelFolder + "/" + (parentFolder == null ? "" : parentFolder + "/")  + modelName + ".json"));
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
}
