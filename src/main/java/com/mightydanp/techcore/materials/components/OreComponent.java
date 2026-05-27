package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.MCItemModelContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.material.item.component.OreItemModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.GemItem;
import com.mightydanp.techcore.materials.Item.OreItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OreComponent<A extends Material> extends Component<OreComponent<A>> {
    private final A material;

    public List<Material> sameRockMaterials = new ArrayList<>();

    private OreTypes.OreType oreType;
    private List<RockTypes.RockType> rockTypes = new ArrayList<>();
    private int maxDensity = 1;

    public Supplier<Item> chippedGem, flawedGem, gem, flawlessGem, legendaryGem;

    public Map<String, Supplier<Item>> rawOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> centrifugedOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> crushedOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> purifiedOreItems = new LinkedHashMap<>();
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
        if (oreType == OreTypes.GEM.oreType()) {
            chippedGem = registerGemItem("chipped_" + material.name + "_gem");
            flawedGem = registerGemItem("flawed_" + material.name + "_gem");
            gem = registerGemItem(material.name + "_gem");
            flawlessGem = registerGemItem("flawless_" + material.name + "_gem");
            legendaryGem = registerGemItem("legendary_" + material.name + "_gem");
        }

        for (Material stoneLayer : sameRockMaterials) {
            String stoneName = stoneLayer.name;

            if (oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()) {
                rawOreItems.put(stoneName, registerOreItem("raw_" + stoneName + "_" + material.name + "_ore"));
                centrifugedOreItems.put(stoneName, registerOreItem("centrifuged_" + stoneName + "_" + material.name + "_ore"));
                crushedOreItems.put(stoneName, registerOreItem("crushed_" + stoneName + "_" + material.name + "_ore"));
                purifiedOreItems.put(stoneName, registerOreItem("purified_" + stoneName + "_" + material.name + "_ore"));
            }
        }

        return this;
    }

    @Override
    public OreComponent<A> initClient() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        if (gem != null) initGemModels(modid, name);

        if (!rawOreItems.isEmpty()) {
            initOreModels(modid, name);
        }

        return this;
    }

    public OreComponent<A> initItemProperties(){
        registerItemProperty(chippedGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (chippedGem.get())).getGemQuality(stack));

        registerItemProperty(flawedGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (flawedGem.get())).getGemQuality(stack));

        registerItemProperty(gem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (gem.get())).getGemQuality(stack));

        registerItemProperty(flawlessGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (flawlessGem.get())).getGemQuality(stack));

        registerItemProperty(legendaryGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (legendaryGem.get())).getGemQuality(stack));

        registerOreQuantityProperty(rawOreItems);
        registerOreQuantityProperty(centrifugedOreItems);
        registerOreQuantityProperty(crushedOreItems);
        registerOreQuantityProperty(purifiedOreItems);

        return this;
    }

    private void registerOreQuantityProperty(Map<String, Supplier<Item>> oreItems) {
        for (Supplier<Item> oreItem: oreItems.values()) {
            registerItemProperty(oreItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                    (stack, level, entity, seed) ->
                            ((OreItem) (oreItem.get())).getQuantityLevel(stack));
        }
    }

    public OreComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        registerItemColor(event, chippedGem, material.physical.getColor());
        registerItemColor(event, flawedGem, material.physical.getColor());
        registerItemColor(event, gem, material.physical.getColor());
        registerItemColor(event, flawlessGem, material.physical.getColor());
        registerItemColor(event, legendaryGem, material.physical.getColor());

        registerOreColors(event, rawOreItems);
        registerOreColors(event, centrifugedOreItems);
        registerOreColors(event, crushedOreItems);
        registerOreColors(event, purifiedOreItems);

        return this;
    }

    private void registerOreColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event, Map<String, Supplier<Item>> oreItems) {
        for (Material stoneLayer : sameRockMaterials) {
            Supplier<Item> oreItem = oreItems.get(stoneLayer.name);
            if (oreItem != null) {
                registerItemColor(event, oreItem, material.physical.getColor(), stoneLayer.physical.getColor());
            }
        }
    }

    @Override
    public OreComponent<A> initLanguages(){
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        AssetPackRegistries.safetyMSLT(false, chippedGem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".chipped_" + name + "_gem", LanguageContent.translateUpperCase(name) + " Chipped Gem")
        );

        AssetPackRegistries.safetyMSLT(false, flawedGem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".flawed_" + name + "_gem", LanguageContent.translateUpperCase(name) + " Flawed Gem")
        );

        AssetPackRegistries.safetyMSLT(false, gem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_gem", LanguageContent.translateUpperCase(name) + " Gem")
        );

        AssetPackRegistries.safetyMSLT(false, flawlessGem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".flawless_" + name + "_gem", LanguageContent.translateUpperCase(name) + " Flawless Gem")
        );

        AssetPackRegistries.safetyMSLT(false, legendaryGem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".legendary_" + name + "_gem", LanguageContent.translateUpperCase(name) + " Legendary Gem")
        );

        for (Material stoneLayer : sameRockMaterials) {
            String rockName = stoneLayer.name;
            String translatedName = LanguageContent.translateUpperCase(rockName + "_" + name);

            AssetPackRegistries.safetyMSLT(false, rawOreItems.get(rockName),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".raw_" + rockName + "_" + name + "_ore", translatedName + " Raw Ore")
            );

            AssetPackRegistries.safetyMSLT(false, centrifugedOreItems.get(rockName),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".centrifuged_" + rockName + "_" + name + "_ore", translatedName + " Centrifuged Ore")
            );

            AssetPackRegistries.safetyMSLT(false, crushedOreItems.get(rockName),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".crushed_" + rockName + "_" + name + "_ore", translatedName + " Crushed Ore")
            );

            AssetPackRegistries.safetyMSLT(false, purifiedOreItems.get(rockName),
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".purified_" + rockName + "_" + name + "_ore", translatedName + " Purified Ore")
            );
        }

        return this;
    }

    private void initGemModels(String modid, String name) {
        String[] items = {"chipped_" + name + "_gem", "flawed_" + name + "_gem", name + "_gem", "flawless_" + name + "_gem", "legendary_" + name + "_gem"};
        String[] models = {"chipped_gem", "flawed_gem", "gem", "flawless_gem", "legendary_gem"};

        for (String model : models) {
            new OreItemModelContent(modid, model, "gem")
                    .saveGemModel(material.icon, model);
        }

        for (int i = 0; i < models.length; i++) {
            new OreItemModelContent(modid, items[i], null)
                    .saveGemItemModel(material.icon, models[i]);
        }
    }

    private void initOreModels(String modid, String name) {
        String[] stages = {"raw", "centrifuged", "crushed", "purified"};

        for (String stage : stages) {
            new OreItemModelContent(modid, stage + "_" + name + "_ore", "ore")
                    .saveOreItemModel(material.icon, stage);
        }

        for (Material stoneLayer : sameRockMaterials) {
            String rockName = stoneLayer.name;

            new OreItemModelContent(modid, "raw_" + rockName + "_" + name + "_ore", null)
                    .saveStageOreItemModel("raw");
            new OreItemModelContent(modid, "centrifuged_" + rockName + "_" + name + "_ore", null)
                    .saveStageOreItemModel("centrifuged");
            new OreItemModelContent(modid, "crushed_" + rockName + "_" + name + "_ore", null)
                    .saveStageOreItemModel("crushed");
            new OreItemModelContent(modid, "purified_" + rockName + "_" + name + "_ore", null)
                    .saveStageOreItemModel("purified");
        }
    }

    private void saveItemModel(String modid, String folder, String modelName, String layer0, String layer1) {
        var model = new MCItemModelContent(modid, modelName, folder)
                .model()
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", iconTexture(modid, layer0));

        if (layer1 != null) {
            model.texture("layer1", iconTexture(modid, layer1));
        }

        model.end().save(false);
    }

    private Supplier<Item> registerGemItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new GemItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private Supplier<Item> registerOreItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new OreItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private ResourceLocation iconTexture(String modid, String texture) {
        return ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/" + texture);
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