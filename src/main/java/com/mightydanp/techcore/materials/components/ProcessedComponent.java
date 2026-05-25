package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.TCItemModelBuilder;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.ItemModelContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.TCItemModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ProcessedComponent<A extends Material> extends Component<ProcessedComponent<A>> {
    private final A material;

    public Supplier<Item> dust;
    public List<Supplier<Item>> dustItems = new ArrayList<>();

    public ProcessedComponent(A material) {
        super("processed", "component");
        this.material = material;
    }

    @Override
    public ProcessedComponent<A> init() {
        if(material.ore.getOreType() == null) {
            dust = RegistriesHandler.ITEMS.register(material.name + "_dust", () -> new DustItem(new MaterialItemProperties()
                    .color(material.physical.getColor())
                    .symbol(material.chemical.getSymbol())
                    .boilingPoint(material.thermal.getBoilingPoint())
                    .meltingPoint(material.thermal.getMeltingPoint())
            ));
        }

        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null){
            for (Material stoneLayer : material.ore.sameRockMaterials) {
                String stoneName = stoneLayer.name;

                dustItems.add(RegistriesHandler.ITEMS.register(stoneName + "_" + material.name + "_dust", () -> new DustItem(new MaterialItemProperties()
                        .color(material.physical.getColor())
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
                )));
            }
        }

        return this;
    }

    @Override
    public ProcessedComponent<A> initClient() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        if (dust != null || !dustItems.isEmpty()) initDustModels(modid, name);

        return this;
    }

    @Override
    public ProcessedComponent<A> initItemProperties() {
        if(material.ore.getOreType() == null) {
            registerItemProperty(dust,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                    (stack, level, entity, seed) ->
                            ((DustItem) dust.get()).getQuantityLevel(stack));

            registerItemProperty(dust,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "purity"),
                    (stack, level, entity, seed) ->
                            ((DustItem) dust.get()).getPurityLevel(stack));
        }

        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null) {
            for (Supplier<Item> dustItem : dustItems) {
                registerItemProperty(dustItem,
                        ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                        (stack, level, entity, seed) ->
                                ((DustItem) dustItem.get()).getQuantityLevel(stack));

                registerItemProperty(dustItem,
                        ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "purity"),
                        (stack, level, entity, seed) ->
                                ((DustItem) dustItem.get()).getPurityLevel(stack));
            }
        }

        return this;
    }

    @Override
    public ProcessedComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        if(material.ore.getOreType() == null) {
            registerBasicItemColor(event, dust, material.physical.getColor());
        }


        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null) {
            for (Supplier<Item> dustItem : dustItems) {
                registerBasicItemColor(event, dustItem, material.physical.getColor());
            }
        }

        return this;
    }

    @Override
    public ProcessedComponent<A> initLanguages() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_dust", LanguageContent.translateUpperCase(name) + " Dust")
        );

        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null) {
            for (int i = 0; i < dustItems.size(); i++) {
                String rockName = material.ore.sameRockMaterials.get(i).name;

                AssetPackRegistries.safetyMSLT(false, dustItems.get(i),
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase(rockName + "_" + name) + " Dust")
                );
            }
        }

        return this;
    }

    private void initDustModels(String modid, String name) {
        saveDustModels(modid, "impure_");
        saveDustModels(modid, "");
        saveDustModels(modid, "pure_");

        TCItemModelBuilder mainBuilder = itemModelBuilder(modid, "dust");
        addDustOverrides(mainBuilder, modid, "impure_", 0f);
        addDustOverrides(mainBuilder, modid, "", 0.75f);
        addDustOverrides(mainBuilder, modid, "pure_", 1.0f);

        new ItemModelContent<>(modid, name + "_dust", null, mainBuilder).save(false);

        for (int i = 0; i < dustItems.size(); i++) {
            String rockName = material.ore.sameRockMaterials.get(i).name;
            new ItemModelContent<>(modid, rockName + "_" + name + "_dust", null, mainBuilder).save(false);
        }
    }

    private void saveDustModels(String modid, String prefix) {
        String[] models = {prefix + "div72_dust", prefix + "tiny_dust", prefix + "small_dust", prefix + "full_dust"};
        String[] layer0 = {"div72_dust", "tiny_dust", "small_dust", prefix.isEmpty() ? "dust" : prefix + "dust"};
        String[] overlayBase = {"div72_dust", "tiny_dust", "small_dust", "dust"};

        for (int i = 0; i < models.length; i++) {
            String layer1 = prefix.isEmpty() ? null : prefix + overlayBase[i] + "_overlay";
            saveItemModel(modid, "dust", models[i], layer0[i], layer1);
        }
    }

    private void addDustOverrides(TCItemModelBuilder builder, String modid, String prefix, float purity) {
        String[] models = {prefix + "div72_dust", prefix + "tiny_dust", prefix + "small_dust", prefix + "full_dust"};
        float[] quantities = {0f, 0.25f, 0.50f, 1.0f};

        for (int i = 0; i < models.length; i++) {
            builder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "quantity"), quantities[i])
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "purity"), purity)
                    .model(uncheckedItemModel(modid, "dust", models[i]))
                    .end();
        }
    }

    private TCItemModelBuilder itemModelBuilder(String modid, String modelName) {
        TCItemModelBuilder builder = new TCItemModelBuilder(
                ResourceLocation.fromNamespaceAndPath(modid, "models/item/" + modelName + ".json"));
        builder.parent(new ModelFile.UncheckedModelFile("item/generated"));
        return builder;
    }

    private void saveItemModel(String modid, String folder, String modelName, String layer0, String layer1) {
        var model = new TCItemModelContent(modid, modelName, folder)
                .model()
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", iconTexture(modid, layer0));

        if (layer1 != null) {
            model.texture("layer1", iconTexture(modid, layer1));
        }

        model.end().save(false);
    }

    private ResourceLocation iconTexture(String modid, String texture) {
        return ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/" + texture);
    }

    private ModelFile.UncheckedModelFile uncheckedItemModel(String modid, String folder, String model) {
        return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/" + folder + "/" + model));
    }

    public A end() {
        return material;
    }
}
