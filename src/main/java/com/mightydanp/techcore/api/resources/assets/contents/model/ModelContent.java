package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ModelContent<T extends ModelContent<T>> {
    public static final String ITEM_FOLDER = "item";

    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");
    protected TCModelBuilder model;
    protected final String modid;
    protected final String modelFolder;
    protected final String parentFolder;
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

    public TCModelBuilder overrideModel(TCModelBuilder model) {
        this.model = model;
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

    public ResourceLocation extendWithFolder(ResourceLocation rl) {
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

    public TCModelBuilder singleTexture(String parent, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), texture);
    }

    public TCModelBuilder singleTexture(ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(parent, "texture", texture);
    }

    public TCModelBuilder singleTexture(String parent, String textureKey, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), textureKey, texture);
    }

    public TCModelBuilder singleTexture(ResourceLocation parent, String textureKey, ResourceLocation texture) {
        return withExistingParent(parent)
                .texture(textureKey, texture);
    }

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

}
