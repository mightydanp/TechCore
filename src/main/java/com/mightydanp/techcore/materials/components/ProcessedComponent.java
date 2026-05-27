package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.contents.model.material.item.component.ProcessedItemModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ProcessedComponent<A extends Material> extends Component<ProcessedComponent<A>> {
    private final A material;

    public Supplier<Item> dust, pureDust;

    public Map<String, Supplier<Item>> impureDustItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> dustItems = new LinkedHashMap<>();

    public ProcessedComponent(A material) {
        super("processed", "component");
        this.material = material;
    }

    @Override
    public ProcessedComponent<A> init() {
        dust = registerDustItem(material.name + "_dust");

        if(!material.stoneLayer.isStoneLayer && material.ore.getOreType() != null){
            for (Material stoneLayer : material.ore.sameRockMaterials) {
                String stoneName = stoneLayer.name;

                impureDustItems.put(stoneName, registerDustItem("impure_" + stoneName + "_" + material.name + "_dust"));
                dustItems.put(stoneName, registerDustItem(stoneName + "_" + material.name + "_dust"));
            }

            pureDust = registerDustItem("pure_" + material.name + "_dust");
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
        registerDustProperties(pureDust);

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
        registerItemColor(event, dust, material.physical.getColor());

        registerDustColors(event, impureDustItems);
        registerDustColors(event, dustItems);
        registerItemColor(event, pureDust, material.physical.getColor());

        return this;
    }

    private void registerDustColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event, Map<String, Supplier<Item>> dustItems) {
        for(int i=0; i < material.ore.sameRockMaterials.size(); i++) {
            List<Supplier<Item>> dusts = dustItems.values().stream().toList();

            registerItemColor(event, dusts.get(i), material.physical.getColor(), material.ore.sameRockMaterials.get(i).physical.getColor());
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
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".impure_" + rockName + "_" + name + "_dust", "Impure " + LanguageContent.translateUpperCase(rockName + "_" + name) + " Dust")
                );

                AssetPackRegistries.safetyMSLT(false, dustItems.get(rockName),
                        new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + "." + rockName + "_" + name + "_dust", LanguageContent.translateUpperCase(rockName + "_" + name) + " Dust")
                );
            }

            AssetPackRegistries.safetyMSLT(false, pureDust,
                    new LanguageContent.translation(modid, LanguageCodes.english, "item." + modid + ".pure_" + name + "_dust", "Pure " + LanguageContent.translateUpperCase(name) + " Dust")
            );
        }

        return this;
    }

    private void initDustModels(String modid, String name) {
        if (this.dust != null) {
            ProcessedItemModelContent dust = new ProcessedItemModelContent(modid, name + "_dust", "dust")
                    .saveDustItemModel(material.icon, "")
                    .saveStageDustItemModel("");
        }

        for (Material stoneLayer : material.ore.sameRockMaterials) {
            String rockName = stoneLayer.name;

            ProcessedItemModelContent impureDust = new ProcessedItemModelContent(modid, "impure_" + rockName + "_" + name + "_dust", "dust")
                    .saveDustItemModel(material.icon, "impure_")
                    .saveStageDustItemModel("impure_");
            ProcessedItemModelContent dust = new ProcessedItemModelContent(modid, rockName + "_" + name + "_dust", "dust")
                    .saveDustItemModel(material.icon, "")
                    .saveStageDustItemModel("");
        }

        if (this.pureDust != null) {
            ProcessedItemModelContent pureDust = new ProcessedItemModelContent(modid, "pure_" + name + "_dust", "dust")
                    .saveDustItemModel(material.icon, "pure_")
                    .saveStageDustItemModel("pure_");
        }
    }

    private Supplier<Item> registerDustItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new DustItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    public A end() {
        return material;
    }
}