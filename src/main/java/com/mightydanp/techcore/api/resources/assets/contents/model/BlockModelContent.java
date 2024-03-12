package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.mightydanp.techcore.api.resources.assets.contents.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;

public class BlockModelContent<A extends BlockModelContent<A>> extends ModelContent<BlockModelContent<A>>{
    public static final String BLOCK_FOLDER = "block";

    public BlockModelContent(String modelName, String parentFolder) {
        super(CoreRef.MOD_ID, modelName, BLOCK_FOLDER, parentFolder);
    }

    public BlockModelContent(String modid, String modelName, String parentFolder) {
        super(modelName, modid, BLOCK_FOLDER, parentFolder);
    }

    public BlockModelContent(ResourceLocation resourceLocation, String parentFolder) {
        super(resourceLocation, BLOCK_FOLDER, parentFolder);
    }

    @SuppressWarnings("ALL")
    public A save(boolean override){
        AssetPackRegistries.saveBlockModel((A)this, override);
        return (A)this;
    }

    public TCModelBuilder<BlockModelContent<A>> cube(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        return withExistingParent("cube")
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west);
    }

    public TCModelBuilder<BlockModelContent<A>> cubeAll(ResourceLocation texture) {
        return singleTexture(BLOCK_FOLDER + "/cube_all", "all", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> cubeTop(ResourceLocation side, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/cube_top")
                .texture("side", side)
                .texture("top", top);
    }

    private TCModelBuilder<BlockModelContent<A>> sideBottomTop(String parent, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(parent)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder<BlockModelContent<A>> cubeBottomTop(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/cube_bottom_top", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> cubeColumn(ResourceLocation side, ResourceLocation end) {
        return withExistingParent(BLOCK_FOLDER + "/cube_column")
                .texture("side", side)
                .texture("end", end);
    }

    public TCModelBuilder<BlockModelContent<A>> cubeColumnHorizontal(ResourceLocation side, ResourceLocation end) {
        return withExistingParent(BLOCK_FOLDER + "/cube_column_horizontal")
                .texture("side", side)
                .texture("end", end);
    }

    public TCModelBuilder<BlockModelContent<A>> orientableVertical(ResourceLocation side, ResourceLocation front) {
        return withExistingParent(BLOCK_FOLDER + "/orientable_vertical")
                .texture("side", side)
                .texture("front", front);
    }

    public TCModelBuilder<BlockModelContent<A>> orientableWithBottom(ResourceLocation side, ResourceLocation front, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/orientable_with_bottom")
                .texture("side", side)
                .texture("front", front)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder<BlockModelContent<A>> orientable(ResourceLocation side, ResourceLocation front, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/orientable")
                .texture("side", side)
                .texture("front", front)
                .texture("top", top);
    }

    public TCModelBuilder<BlockModelContent<A>> crop(ResourceLocation crop) {
        return this.singleTexture(BLOCK_FOLDER + "/crop", "crop", crop);
    }

    public TCModelBuilder<BlockModelContent<A>> cross(ResourceLocation cross) {
        return singleTexture(BLOCK_FOLDER + "/cross", "cross", cross);
    }

    public TCModelBuilder<BlockModelContent<A>> stairs(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/stairs", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> stairsOuter(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/outer_stairs", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> stairsInner(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/inner_stairs", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> slab(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/slab", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> slabTop(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(BLOCK_FOLDER + "/slab_top", side, bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> button(ResourceLocation texture) {
        return singleTexture(BLOCK_FOLDER + "/button", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> buttonPressed(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_pressed", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> buttonInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_inventory", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> pressurePlate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_up", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> pressurePlateDown(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_down", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> sign(String name, ResourceLocation texture) {
        return model().texture("particle", texture);
        //return getBuilder(name).texture("particle", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fencePost(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_post", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceSide(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_side", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_inventory", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceGate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceGateOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_open", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceGateWall(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> fenceGateWallOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall_open", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> wallPost(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_post", "wall", wall);
    }

    public TCModelBuilder<BlockModelContent<A>> wallSide(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_side", "wall", wall);
    }

    public TCModelBuilder<BlockModelContent<A>> wallSideTall(ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/template_wall_side_tall", "wall", wall);
    }

    public TCModelBuilder<BlockModelContent<A>> wallInventory(String name, ResourceLocation wall) {
        return singleTexture(BLOCK_FOLDER + "/wall_inventory", "wall", wall);
    }

    private TCModelBuilder<BlockModelContent<A>> door(String model, ResourceLocation bottom, ResourceLocation top) {
        return withExistingParent(BLOCK_FOLDER + "/" + model)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorBottomLeft(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_left", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorBottomLeftOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_left_open", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorBottomRight(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_right", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorBottomRightOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_bottom_right_open", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorTopLeft(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_left", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorTopLeftOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_left_open", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorTopRight(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_right", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> doorTopRightOpen(ResourceLocation bottom, ResourceLocation top) {
        return door("door_top_right_open", bottom, top);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_bottom", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_top", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_open", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorOrientableBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorOrientableTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> trapdoorOrientableOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture);
    }

    public TCModelBuilder<BlockModelContent<A>> torch(ResourceLocation torch) {
        return singleTexture(BLOCK_FOLDER + "/template_torch", "torch", torch);
    }

    public TCModelBuilder<BlockModelContent<A>> torchWall(ResourceLocation torch) {
        return singleTexture(BLOCK_FOLDER + "/template_torch_wall", "torch", torch);
    }

    public TCModelBuilder<BlockModelContent<A>> carpet(ResourceLocation wool) {
        return singleTexture(BLOCK_FOLDER + "/carpet", "wool", wool);
    }
}
