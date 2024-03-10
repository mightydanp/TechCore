package com.mightydanp.techcore.api.resources.assets.contents.model;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.assets.contents.TCModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ModelContent {
    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");
    private final TCModelBuilder model;

    private final String modid;

    private final String name;
    private final String modelType;
    private final String organizationPath;

    public ModelContent(String modid, String name, String modelType, String organizationPath) {
        this.modid = modid;
        this.name = name;
        this.modelType = modelType;
        this.organizationPath = organizationPath;
        this.model = new TCModelBuilder(new ResourceLocation(modid, "models/" + modelType + "/" + (organizationPath == null ? "" : organizationPath + "/") + name + ".json"));
    }

    public ModelContent(ResourceLocation resourceLocation, String modelType, String organizationPath) {
        this.name = resourceLocation.getPath();
        this.modid = resourceLocation.getNamespace();
        this.modelType = modelType;
        this.organizationPath = organizationPath;
        this.model = new TCModelBuilder(new ResourceLocation(modid, "models/" + modelType + "/" + (organizationPath == null ? "" : organizationPath + "/") + name + ".json"));
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public TCModelBuilder model() {
        return model;
    }

    public String getModelType() {
        return modelType;
    }

    public String getOrganizationPath() {
        return organizationPath;
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
        return new ResourceLocation(rl.getNamespace(), modelType + "/" + rl.getPath());
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
