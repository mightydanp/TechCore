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
import com.mightydanp.techcore.world.item.properties.ProcessedStage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import java.util.function.Supplier;

public class OreComponent<A extends Material> extends Component<OreComponent<A>> {
    private final A material;

    private OreTypes.OreType oreType;
    private int maxDensity = 1;

    public Supplier<Item> ore, dust, gem;
    public Supplier<Item> ingot;

    public OreComponent(A material) {
        super("ore", "component");
        this.material = material;
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType, int maxDensity) {
        this.oreType = oreType;
        this.maxDensity = maxDensity;
        return this;
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType) {
        this.oreType = oreType;
        return this;
    }

    @Override
    public OreComponent<A> init() {
        if (oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()) {
            //List<RegistryObject<Material>> stoneLayerList = RegistriesHandler.getMaterials().stream().filter(material -> {
            //    return material.get().stoneLayer.hasStoneLayer;
            //}).toList();

            //for (RegistryObject<Material> stoneLayer : stoneLayerList) {}

            if (oreType == OreTypes.GEM.oreType()) {
                gem = RegistriesHandler.ITEMS.register(material.name + "_gem", () -> new GemItem(new MaterialItemProperties()
                        .color(material.physical.getColor())
                        .symbol(material.chemical.getSymbol())
                        .defaultQuality(material.physical.getDefaultQuality())
                        .maxQuality(material.physical.getMaxQuality())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
                ));
            }

            ore = RegistriesHandler.ITEMS.register(material.name + "_ore", () -> new OreItem(new MaterialItemProperties()
                    .color(material.physical.getColor())
                    .symbol(material.chemical.getSymbol())
                    .defaultQuantity(material.physical.getDefaultQuantity())
                    .maxQuantity(material.physical.getMaxQuantity())
                    .defaultPurity(material.physical.getDefaultPurity())
                    .maxPurity(material.physical.getMaxPurity())
                    .boilingPoint(material.thermal.getBoilingPoint())
                    .meltingPoint(material.thermal.getMeltingPoint())
            ));
        }

        dust = RegistriesHandler.ITEMS.register(material.name + "_dust", () -> new DustItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .defaultQuantity(material.physical.getDefaultQuantity())
                .maxQuantity(material.physical.getMaxQuantity())
                .defaultPurity(material.physical.getDefaultPurity())
                .maxPurity(material.physical.getMaxPurity())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));

        return this;
    }

    @Override
    public OreComponent<A> initClient() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        if (gem != null) initGemModels(modid, name);
        if (ore != null) initOreModels(modid, name);
        if (dust != null) initDustModels(modid, name);

        return this;
    }

    public OreComponent<A> initItemProperties(){
        registerItemProperty(gem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (gem.get())).getGemQuality(stack));

        registerItemProperty(ore,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                (stack, level, entity, seed) ->
                        ((OreItem) (ore.get())).getQuantityLevel(stack));

        registerItemProperty(ore,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "processed_stage"),
                (stack, level, entity, seed) -> ProcessedStage.hasProcessedStage(stack)
                        ? ProcessedStage.ProcessedStages.getProcessedStageLevel(stack)
                        : 1f);

        registerItemProperty(dust,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                (stack, level, entity, seed) ->
                        ((DustItem) (dust.get())).getQuantityLevel(stack));

        registerItemProperty(dust,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "purity"),
                (stack, level, entity, seed) ->
                        ((DustItem) (dust.get())).getPurityLevel(stack));
        return this;
    }

    public OreComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        registerBasicItemColor(event, gem, material.physical.getColor());
        registerBasicItemColor(event, ore, material.physical.getColor());
        registerBasicItemColor(event, dust, material.physical.getColor());

        return this;
    }

    @Override
    public OreComponent<A> initLanguages(){
        String modid = CoreRef.MOD_ID;
        String name = material.name;


        AssetPackRegistries.safetyMSLT(false, gem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_gem", LanguageContent.translateUpperCase(name) + " Gem")
        );

        AssetPackRegistries.safetyMSLT(false, ore,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_ore", LanguageContent.translateUpperCase(name) + " Ore")
        );

        AssetPackRegistries.safetyMSLT(false, dust,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_dust", LanguageContent.translateUpperCase(name) + " Dust")
        );

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

        new ItemModelContent<>(modid, name + "_ore", null, mainBuilder).save(false);
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

        new ItemModelContent<>(modid, name + "_dust", null, mainBuilder).save(false);
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

    public OreComponent<A> setOreType(OreTypes.OreType oreType) {
        this.oreType = oreType;
        return this;
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
