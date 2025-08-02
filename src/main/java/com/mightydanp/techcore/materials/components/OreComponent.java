package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.materials.Item.GemItem;
import com.mightydanp.techcore.materials.Item.OreProductsItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class OreComponent extends Component {
    public OreTypes.OreType oreType;
    public int maxDensity = 1;  // Default to 1 (non-dense ore)

    public Supplier<Item> crushedOre, purifiedOre, centrifugedOre;
    public Supplier<Item> impureDust, pureDust, dust, smallDust, tinyDust;
    public Supplier<Item> gem, chippedGem, flawedGem, flawlessGem, legendaryGem;
    public Supplier<Item> ingot;

    public OreComponent() {
        super("ore", "component");
    }

    @Override
    public Component initServer(Material material) {
        if(oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()){
            if (oreType == OreTypes.ORE.oreType()) {
                gem = RegistriesHandler.ITEMS.register(material.name + "_gem", () -> new GemItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
                chippedGem = RegistriesHandler.ITEMS.register("chipped_" + material.name + "_gem", () -> new GemItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
                flawedGem = RegistriesHandler.ITEMS.register("flawed_" + material.name + "_gem", () -> new GemItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
                flawlessGem = RegistriesHandler.ITEMS.register("flawless_" + material.name + "_gem", () -> new GemItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
                legendaryGem = RegistriesHandler.ITEMS.register("legendary_" + material.name + "_gem", () -> new GemItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
            }
            crushedOre = RegistriesHandler.ITEMS.register("crushed_" + material.name + "_ore", () -> new OreProductsItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
            purifiedOre = RegistriesHandler.ITEMS.register("purified_" + material.name + "_ore", () -> new OreProductsItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
            centrifugedOre = RegistriesHandler.ITEMS.register("centrifuged_" + material.name + "_ore", () -> new OreProductsItem(new Item.Properties(), material.thermalComponent.boilingPoint, material.thermalComponent.meltingPoint, material.chemicalComponent.symbol));
        }

        return super.initServer(material);
    }

    @Override
    public Component initClient(Material material) {
        return super.initClient(material);
    }

    public OreComponent setOreType(OreTypes.OreType oreType) {
        this.oreType = oreType;
        return this;
    }

    public OreComponent setMaxDensity(int maxDensity) {
        this.maxDensity = maxDensity;
        return this;
    }
}