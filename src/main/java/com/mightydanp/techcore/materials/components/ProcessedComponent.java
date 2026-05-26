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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ProcessedComponent<A extends Material> extends Component<ProcessedComponent<A>> {
    private final A material;

    public Supplier<Item> dust;

    public Map<String, Supplier<Item>> impureDustItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> dustItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> pureDustItems = new LinkedHashMap<>();

    public ProcessedComponent(A material) {
        super("processed", "component");
        this.material = material;
    }

    @Override
    public ProcessedComponent<A> init() {
        if(material.ore.getOreType() == null) {
            dust = registerDustItem(material.name + "_dust");
        }

        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null){
            for (Material stoneLayer : material.ore.sameRockMaterials) {
                String stoneName = stoneLayer.name;

                impureDustItems.put(stoneName, registerDustItem("impure_" + stoneName + "_" + material.name + "_dust"));
                dustItems.put(stoneName, registerDustItem(stoneName + "_" + material.name + "_dust"));
                pureDustItems.put(stoneName, registerDustItem("pure_" + stoneName + "_" + material.name + "_dust"));
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
        registerDustProperties(dust);

        registerDustProperties(impureDustItems);
        registerDustProperties(dustItems);
        registerDustProperties(pureDustItems);

        return this;
    }

    private void registerDustProperties(Supplier<Item> dustItem) {
        registerItemProperty(dustItem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                (stack, level, entity, seed) ->
                        ((DustItem) dustItem.get()).getQuantityLevel(stack));
    }

    private void registerDustProperties(Map<String, Supplier<Item>> dustItems) {
        for (Supplier<Item> dustItem : dustItems.values()) {
            registerDustProperties(dustItem);
        }
    }

    @Override
    public ProcessedComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        registerBasicItemColor(event, dust, material.physical.getColor());

        registerDustColors(event, impureDustItems);
        registerDustColors(event, dustItems);
        registerDustColors(event, pureDustItems);

        return this;
    }

    private void registerDustColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event, Map<String, Supplier<Item>> dustItems) {
        for(int i=0; i < material.ore.sameRockMaterials.size(); i++) {
            List<Supplier<Item>> dusts = dustItems.values().stream().toList();

            registerMultiItemColor(event, dusts.get(i), material.physical.getColor(), material.ore.sameRockMaterials.get(i).physical.getColor());
        }
    }

    @Override
    public ProcessedComponent<A> initLanguages() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;


        AssetPackRegistries.safetyMSLT(false, dust,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_dust", LanguageContent.translateUpperCase(name) + " Dust")
        );


        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null) {
            for (Material stoneLayer : material.ore.sameRockMaterials) {
                String rockName = stoneLayer.name;

                AssetPackRegistries.safetyMSLT(false, impureDustItems.get(rockName),
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".impure_" + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase("Impure " + rockName + "_" + name) + " Dust")
                );

                AssetPackRegistries.safetyMSLT(false, dustItems.get(rockName),
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase(rockName + "_" + name) + " Dust")
                );

                AssetPackRegistries.safetyMSLT(false, pureDustItems.get(rockName),
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".pure_" + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase("Pure " + rockName + "_" + name) + " Dust")
                );
            }
        }

        return this;
    }

    private void initDustModels(String modid, String name) {
        saveDustModels(modid, "impure_");
        saveDustModels(modid, "");
        saveDustModels(modid, "pure_");

        if (dust != null) {
            saveStageDustItemModel(modid, "impure_" + name + "_dust", "impure_");
            saveStageDustItemModel(modid, name + "_dust", "");
            saveStageDustItemModel(modid, "pure_" + name + "_dust", "pure_");
        }

        for (Material stoneLayer : material.ore.sameRockMaterials) {
            String rockName = stoneLayer.name;
            saveStageDustItemModel(modid, "impure_" + rockName + "_" + name + "_dust", "impure_");
            saveStageDustItemModel(modid, rockName + "_" + name + "_dust", "");
            saveStageDustItemModel(modid, "pure_" + rockName + "_" + name + "_dust", "pure_");
        }
    }

    private void saveStageDustItemModel(String modid, String itemName, String prefix) {
        TCItemModelBuilder builder = itemModelBuilder(modid, itemName);
        addDustOverrides(builder, modid, prefix);
        new ItemModelContent<>(modid, itemName, null, builder).save(false);
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

    private void addDustOverrides(TCItemModelBuilder builder, String modid, String prefix) {
        String[] models = {prefix + "div72_dust", prefix + "tiny_dust", prefix + "small_dust", prefix + "full_dust"};
        float[] quantities = {0f, 0.25f, 0.50f, 1.0f};

        for (int i = 0; i < models.length; i++) {
            builder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "quantity"), quantities[i])
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

    private Supplier<Item> registerDustItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new DustItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
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
