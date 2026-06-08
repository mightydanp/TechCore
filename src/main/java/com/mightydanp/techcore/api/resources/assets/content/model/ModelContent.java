package com.mightydanp.techcore.api.resources.assets.content.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ModelContent<A> {
    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");
    private ModelBuilder<A> model;

    private final String modid;

    private final String name;
    private final String modelType;
    private final String organizationPath;

    @SuppressWarnings("unchecked")
    public ModelContent(String modid, String name, String modelType, String organizationPath) {
        this.modid = modid;
        this.name = name;
        this.modelType = modelType;
        this.organizationPath = organizationPath;
        this.model = new ModelBuilder<>(ResourceLocation.fromNamespaceAndPath(modid, "models/" + modelType + "/" + (organizationPath == null ? "" : organizationPath + "/") + name + ".json"), (A) this);
    }

    @SuppressWarnings("unchecked")
    public ModelContent(@NotNull ResourceLocation resourceLocation, String modelType, String organizationPath) {
        this.name = resourceLocation.getPath();
        this.modid = resourceLocation.getNamespace();
        this.modelType = modelType;
        this.organizationPath = organizationPath;
        this.model = new ModelBuilder<>(ResourceLocation.fromNamespaceAndPath(modid, "models/" + modelType + "/" + (organizationPath == null ? "" : organizationPath + "/") + name + ".json"), (A) this);
    }

    protected ModelContent(String modid, String name, String modelType, String organizationPath, ModelBuilder<A> builder) {
        this.modid = modid;
        this.name = name;
        this.modelType = modelType;
        this.organizationPath = organizationPath;
        this.model = builder;
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public ModelBuilder<A> model() {
        return model;
    }

    protected void model(ModelBuilder<A> builder) {
        this.model = builder;
    }

    public String getModelType() {
        return modelType;
    }

    public String getOrganizationPath() {
        return organizationPath;
    }

    public JsonObject json() {
        return model.toJson();
    }

    public ResourceLocation mcLoc(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    public ResourceLocation extendWithFolder(@NotNull ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), modelType + "/" + rl.getPath());
    }

    public ModelFile.UncheckedModelFile getFile(ResourceLocation path) {
        ModelFile.UncheckedModelFile ret = new ModelFile.UncheckedModelFile(extendWithFolder(path));
        ret.assertExistence();
        return ret;
    }

    public ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    public ModelBuilder<A> withExistingParent(String parent) {
        return withExistingParent(mcLoc(parent));
    }

    public ModelBuilder<A> withExistingParent(ResourceLocation parent) {
        return model.parent(getFile(parent));
    }

    public ModelBuilder<A> singleTexture(String parent, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), texture);
    }

    public ModelBuilder<A> singleTexture(ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(parent, "texture", texture);
    }

    public ModelBuilder<A> singleTexture(String parent, String textureKey, ResourceLocation texture) {
        return singleTexture(mcLoc(parent), textureKey, texture);
    }

    public ModelBuilder<A> singleTexture(ResourceLocation parent, String textureKey, ResourceLocation texture) {
        return withExistingParent(parent)
                .texture(textureKey, texture);
    }

    public ModelBuilder<A> singleTextureMap(ResourceLocation parent, @NotNull Map<String, String> map) {
        ModelBuilder<A> model = withExistingParent(parent);

        map.forEach(model::texture);

        return model;
    }

    public void resourceTextureMap(ResourceLocation parent, @NotNull Map<String, ResourceLocation> map) {
        model.parent(parent);
        map.forEach(model::texture);
    }

    public ModelFile.UncheckedModelFile uncheckedMCModelFile(String folder, String model) {
        return new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace(folder + "/" + model));
    }

    public ModelFile.UncheckedModelFile uncheckedModelFile(String folder, String model) {
        return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid(), folder + "/" + model));
    }

    public ModelFile.UncheckedModelFile uncheckedModelFile(String modId, String folder, String model) {
        return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modId, folder + "/" + model));
    }
    //------------------------------------------------------------------------------------------------------------------

}
