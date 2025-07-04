package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.properties.OreTypes;
import net.minecraft.world.item.Item;

public class OreComponent extends Component {
    public OreTypes.OreType oreType;
    public int maxDensity = 1;  // Default to 1 (non-dense ore)

    public Item crushedOre, purifiedOre, centrifugedOre;
    public Item impureDust, pureDust, dust, smallDust, tinyDust;
    public Item gem, chippedGem, flawedGem, flawlessGem, legendaryGem;
    public Item ingot;

    public OreComponent() {
        super("ore", "component",
                m -> { //server

            },  m -> { //client

        });
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