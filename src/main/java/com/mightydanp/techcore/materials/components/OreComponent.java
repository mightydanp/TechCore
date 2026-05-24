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
import com.mightydanp.techcore.materials.Item.GemItem;
import com.mightydanp.techcore.materials.Item.OreItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import com.mightydanp.techcore.world.item.properties.ProcessedStage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class OreComponent<A extends Material> extends Component<OreComponent<A>> {
    private final A material;

    public List<Material> sameRockMaterials = new ArrayList<>();

    private OreTypes.OreType oreType;
    private List<RockTypes.RockType> rockTypes = new ArrayList<>();
    private int maxDensity = 1;

    public Supplier<Item> gem;
    public List<Supplier<Item>> oreItems =  new ArrayList<>();
    public List<Supplier<Item>> dustItems = new ArrayList<>();
    //public Supplier<Item> ingot;

    public OreComponent(A material) {
        super("ore", "component");
        this.material = material;
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType , int maxDensity, RockTypes.RockType... rockTypes) {
        this.oreType = oreType;
        this.maxDensity = maxDensity;
        this.rockTypes = Arrays.asList(rockTypes);
        this.sameRockMaterials = StoneLayerComponent.getStoneLayerMaterials().stream().filter(material1 -> this.rockTypes.contains(material1.stoneLayer.rockType)).toList();
        return this;
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType, RockTypes.RockType... rockTypes) {
        this.oreType = oreType;
        this.rockTypes = Arrays.asList(rockTypes);
        this.sameRockMaterials = StoneLayerComponent.getStoneLayerMaterials().stream().filter(material1 -> this.rockTypes.contains(material1.stoneLayer.rockType)).toList();
        return this;
    }

    @Override
    public OreComponent<A> init() {
        //grade of the ore
        if (oreType == OreTypes.GEM.oreType()) {
            gem = RegistriesHandler.ITEMS.register(material.name + "_gem", () -> new GemItem(new MaterialItemProperties()
                    .color(material.physical.getColor())
                    .symbol(material.chemical.getSymbol())
                    .boilingPoint(material.thermal.getBoilingPoint())
                    .meltingPoint(material.thermal.getMeltingPoint())
            ));
        }

        for (Material stoneLayer : sameRockMaterials) {
            String stoneName = stoneLayer.name;

            if (oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()) {

                oreItems.add(RegistriesHandler.ITEMS.register(stoneName + "_" + material.name + "_ore", () -> new OreItem(new MaterialItemProperties()
                        .color(material.physical.getColor())
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
                )));
            }

            dustItems.add(RegistriesHandler.ITEMS.register(stoneName + "_" + material.name + "_dust", () -> new DustItem(new MaterialItemProperties()
                    .color(material.physical.getColor())
                    .symbol(material.chemical.getSymbol())
                    .symbol(material.chemical.getSymbol())
                    .boilingPoint(material.thermal.getBoilingPoint())
                    .meltingPoint(material.thermal.getMeltingPoint())
            )));
        }

        return this;
    }

    @Override
    public OreComponent<A> initClient() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        if (gem != null) initGemModels(modid, name);

        for (Supplier<Item> oreItem: oreItems) {
            if (oreItem != null) initOreModels(modid, name);
        }

        for (Supplier<Item> dustItem: dustItems) {
            if (dustItem != null) initDustModels(modid, name);
        }

        return this;
    }

    public OreComponent<A> initItemProperties(){
        registerItemProperty(gem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (gem.get())).getGemQuality(stack));

        for (Supplier<Item> oreItem: oreItems) {
            registerItemProperty(oreItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                    (stack, level, entity, seed) ->
                            ((OreItem) (oreItem.get())).getQuantityLevel(stack));

            registerItemProperty(oreItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "processed_stage"),
                    (stack, level, entity, seed) -> ProcessedStage.hasProcessedStage(stack)
                            ? ProcessedStage.ProcessedStages.getProcessedStageLevel(stack)
                            : 1f);
        }

        for (Supplier<Item> dustItem: dustItems) {
            registerItemProperty(dustItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                    (stack, level, entity, seed) ->
                            ((DustItem) (dustItem.get())).getQuantityLevel(stack));

            registerItemProperty(dustItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "purity"),
                    (stack, level, entity, seed) ->
                            ((DustItem) (dustItem.get())).getPurityLevel(stack));
        }
        return this;
    }

    public OreComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        registerBasicItemColor(event, gem, material.physical.getColor());

        for(int i = 0; i < oreItems.size(); i++) {
            int rockColor = sameRockMaterials.get(i).physical.getColor();

            registerMultiItemColor(event, oreItems.get(i), material.physical.getColor(), rockColor);
        }

        for(int i = 0; i < dustItems.size(); i++) {
            int rockColor = sameRockMaterials.get(i).physical.getColor();
            registerBasicItemColor(event, dustItems.get(i), material.physical.getColor());
        }

        return this;
    }

    @Override
    public OreComponent<A> initLanguages(){
        String modid = CoreRef.MOD_ID;
        String name = material.name;


        AssetPackRegistries.safetyMSLT(false, gem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_gem", LanguageContent.translateUpperCase(name) + " Gem")
        );

        for(int i = 0; i < oreItems.size(); i++) {
            String rockName = sameRockMaterials.get(i).name;

            AssetPackRegistries.safetyMSLT(false, oreItems.get(i),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + rockName + "_" + name + "_ore", LanguageContent.translateUpperCase(rockName + "_" + name) + " Ore")
            );
        }

        for(int i = 0; i < dustItems.size(); i++) {
            String rockName = sameRockMaterials.get(i).name;

            AssetPackRegistries.safetyMSLT(false, dustItems.get(i),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase(rockName + "_" + name) + " Dust")
            );

        }

        return this;
    }

    private void initGemModels(String modid, String name) {
        String[] models = {"chipped_gem", "flawed_gem", "gem", "flawless_gem", "legendary_gem"};
        float[] qualities = {0.2f, 0.4f, 0.6f, 0.8f, 1.0f};

        for (String model : models) {
            saveItemModel(modid, "gem", model, model, null);
        }

        TCItemModelBuilder mainBuilder = itemModelBuilder(modid, "gem");
        mainBuilder.texture("layer0", iconTexture(modid, "chipped_gem"));

        for (int i = 0; i < models.length; i++) {
            addOverride(mainBuilder, modid, "gem_quality", qualities[i], "gem", models[i]);
        }

        new ItemModelContent<>(modid, name + "_gem", null, mainBuilder).save(false);
    }

    private void initOreModels(String modid, String name) {
        String[] stages = {"raw", "centrifuged", "crushed", "purified"};

        for (String stage : stages) {
            saveOreModels(modid, stage);
        }

        TCItemModelBuilder mainBuilder = itemModelBuilder(modid, "ore");


        addOreOverrides(mainBuilder, modid, "centrifuged", ProcessedStage.ProcessedStages.CENTRIFUGED.getValue());
        addOreOverrides(mainBuilder, modid, "crushed", ProcessedStage.ProcessedStages.CRUSHED.getValue());
        addOreOverrides(mainBuilder, modid, "purified", ProcessedStage.ProcessedStages.PURIFIED.getValue());
        addOreOverrides(mainBuilder, modid, "raw", 1F);

        for(int i = 0; i < oreItems.size(); i++) {
            String rockName = sameRockMaterials.get(i).name;
            new ItemModelContent<>(modid, rockName + "_" + name + "_ore", null, mainBuilder).save(false);
        }
    }

    private void saveOreModels(String modid, String stage) {
        for (String model : oreModels(stage)) {
            saveItemModel(modid, "ore", model, model, model + "_overlay");
        }
    }

    private void addOreOverrides(TCItemModelBuilder builder, String modid, String stage, Float processedStage) {
        String[] models = oreModels(stage);
        float[] quantities = {0f, 0.25f, 0.50f, 1f};

        for (int i = 0; i < models.length; i++) {
            var override = builder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "quantity"), quantities[i]);

            if (processedStage != null) {
                override.predicate(ResourceLocation.fromNamespaceAndPath(modid, "processed_stage"), processedStage);
            }

            override.model(uncheckedItemModel(modid, "ore", models[i])).end();
        }
    }

    private String[] oreModels(String stage) {
        return new String[] {
                stage + "_div72_ore",
                stage + "_tiny_ore",
                stage + "_small_ore",
                stage + "_ore"
        };
    }

    private void initDustModels(String modid, String name) {
        saveDustModels(modid, "impure_");
        saveDustModels(modid, "");
        saveDustModels(modid, "pure_");

        TCItemModelBuilder mainBuilder = itemModelBuilder(modid, "dust");
        addDustOverrides(mainBuilder, modid, "impure_", 0f);
        addDustOverrides(mainBuilder, modid, "", 0.75f);
        addDustOverrides(mainBuilder, modid, "pure_", 1.0f);


        for(int i = 0; i < dustItems.size(); i++) {
            String rockName = sameRockMaterials.get(i).name;
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

    private void addOverride(TCItemModelBuilder builder, String modid, String property, float value, String folder, String model) {
        builder.override()
                .predicate(ResourceLocation.fromNamespaceAndPath(modid, property), value)
                .model(uncheckedItemModel(modid, folder, model))
                .end();
    }

    private ResourceLocation iconTexture(String modid, String texture) {
        return ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/" + texture);
    }

    private ModelFile.UncheckedModelFile uncheckedItemModel(String modid, String folder, String model) {
        return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/" + folder + "/" + model));
    }

    public OreComponent<A> setMaxDensity(int maxDensity) {
        this.maxDensity = maxDensity;
        return this;
    }

    public OreTypes.OreType getOreType() {
        return oreType;
    }

    public int getMaxDensity() {
        return maxDensity;
    }

    public A end(){
        return material;
    }
}
