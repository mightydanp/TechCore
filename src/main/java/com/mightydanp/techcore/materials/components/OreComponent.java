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
import com.mightydanp.techcore.materials.Item.MaterialItem;
import com.mightydanp.techcore.materials.properties.MaterialProperties;
import com.mightydanp.techcore.materials.Item.GemItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class OreComponent<A extends Material> extends Component<OreComponent<A>> {
    private final A material;

    private OreTypes.OreType oreType;
    private int maxDensity = 1;  // Default to 1 (non-dense ore)

    public Supplier<Item> crushedOre, purifiedOre, centrifugedOre;
    public Supplier<Item> dust;
    public Supplier<Item> dustBowl;
    //public Supplier<Item> impureDust, pureDust, dust, smallDust, tinyDust;
    public Supplier<Item> gem;
    //public Supplier<Item> chippedGem, flawedGem, gem, flawlessGem, legendaryGem;
    public Supplier<Item> ingot;

    public OreComponent(A material) {
        super("ore", "component");
        this.material = material;
    }

    @Override
    public OreComponent<A> init() {
        if(oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()){
            if (oreType == OreTypes.GEM.oreType()) {
                gem = RegistriesHandler.ITEMS.register(material.name + "_gem", () -> new GemItem(new MaterialProperties().symbol(material.chemical.getSymbol()).maxQuality(material.physical.getMaxQuality()).boilingPoint(material.thermal.getBoilingPoint()).meltingPoint(material.thermal.getMeltingPoint())));
            }
            crushedOre = RegistriesHandler.ITEMS.register("crushed_" + material.name + "_ore", () -> new MaterialItem(new MaterialProperties().symbol(material.chemical.getSymbol()).boilingPoint(material.thermal.getBoilingPoint()).meltingPoint(material.thermal.getMeltingPoint())));
            purifiedOre = RegistriesHandler.ITEMS.register("purified_" + material.name + "_ore", () -> new MaterialItem(new MaterialProperties().symbol(material.chemical.getSymbol()).boilingPoint(material.thermal.getBoilingPoint()).meltingPoint(material.thermal.getMeltingPoint())));
            centrifugedOre = RegistriesHandler.ITEMS.register("centrifuged_" + material.name + "_ore", () -> new MaterialItem(new MaterialProperties().symbol(material.chemical.getSymbol()).boilingPoint(material.thermal.getBoilingPoint()).meltingPoint(material.thermal.getMeltingPoint())));
        }

        dust = RegistriesHandler.ITEMS.register(material.name + "_dust", () -> new DustItem(new MaterialProperties().color(material.physical.getColor()).maxQuantity(material.physical.getMaxQuantity()).symbol(material.chemical.getSymbol()).boilingPoint(material.thermal.getBoilingPoint()).meltingPoint(material.thermal.getMeltingPoint())));

        return this;
    }

    @Override
    public OreComponent<A> initClient() {
        if (dust != null) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;

            // Full dust sub-model — referenced by the main model's 0 override
            new TCItemModelContent(modid, "tiny_dust", "dust")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/tiny_dust"))
                    .end()
                    .save(false);

            // Small dust sub-model — referenced by the main model's 0.5 override
            new TCItemModelContent(modid, "small_dust", "dust")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/small_dust"))
                    .end()
                    .save(false);

            // Full dust sub-model — referenced by the main model's 1.0 override
            new TCItemModelContent(modid, "full_dust", "dust")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/dust"))
                    .end()
                    .save(false);

            // Main dust model — tiny texture by default, overrides for small and full
            TCItemModelBuilder mainBuilder = new TCItemModelBuilder(
                    ResourceLocation.fromNamespaceAndPath(modid, "models/item/" + "dust.json"));
            mainBuilder.parent(new ModelFile.UncheckedModelFile("item/generated"));
            mainBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/tiny_dust"));

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "fill_level"), 0.5f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/dust/" + "small_dust")))
                    .end();

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "fill_level"), 1.0f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/dust/" + "full_dust")))
                    .end();

            new ItemModelContent<>(modid, name + "_dust", null, mainBuilder).save(false);
        }

        if(gem != null) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;

            new TCItemModelContent(modid, "chipped_gem", "gem")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/chipped_gem"))
                    .end()
                    .save(false);

            new TCItemModelContent(modid, "flawed_gem", "gem")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/flawed_gem"))
                    .end()
                    .save(false);

            new TCItemModelContent(modid, "gem", "gem")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/gem"))
                    .end()
                    .save(false);

            new TCItemModelContent(modid, "flawless_gem", "gem")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/flawless_gem"))
                    .end()
                    .save(false);

            new TCItemModelContent(modid, "legendary_gem", "gem")
                    .model()
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/legendary_gem"))
                    .end()
                    .save(false);

            TCItemModelBuilder mainBuilder = new TCItemModelBuilder(
                    ResourceLocation.fromNamespaceAndPath(modid, "models/item/" + "gem.json"));
            mainBuilder.parent(new ModelFile.UncheckedModelFile("item/generated"));
            mainBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/material_icons/" + material.icon.label() + "/chipped_gem"));

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "gem_quality"), 0.2f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/gem/" + "chipped_gem")))
                    .end();

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "gem_quality"), 0.4f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/gem/" + "flawed_gem")))
                    .end();

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "gem_quality"), 0.6f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/gem/" + "gem")))
                    .end();

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "gem_quality"), 0.8f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/gem/" + "flawless_gem")))
                    .end();

            mainBuilder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(modid, "gem_quality"), 1.0f)
                    .model(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(modid, "item/gem/" + "legendary_gem")))
                    .end();

            new ItemModelContent<>(modid, name + "_gem", null, mainBuilder).save(false);
        }

        return this;
    }

    public OreComponent<A> initItemProperties(){
        registerItemProperty(dust.get(),
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "fill_level"),
                (stack, level, entity, seed) ->
                        ((DustItem) (dust.get())).getFillLevel(stack));

        registerItemProperty(gem.get(),
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (gem.get())).getGemQuality(stack));
        return this;
    }

    public OreComponent<A> initClientRenderLayers(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event) {
        registerBasicItemColor(event, dust.get(), material.physical.getColor());
        registerBasicItemColor(event, gem.get(), material.physical.getColor());
        return this;
    }

    @Override
    public OreComponent<A> initLanguages(){
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        //dust
        AssetPackRegistries.safetyMSLT(false, dust,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_dust", LanguageContent.translateUpperCase(name) + " Dust")
                );

        //Gem
        AssetPackRegistries.safetyMSLT(false, gem,
                new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + name + "_gem", LanguageContent.translateUpperCase(name) + " Gem")
        );

        return this;
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
