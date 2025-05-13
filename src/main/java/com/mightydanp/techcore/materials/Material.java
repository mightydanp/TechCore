package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.materials.Item.FluidStates;
import com.mightydanp.techcore.materials.properties.MaterialFlags;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.PureSubstances;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public String name;

    public List<MaterialFlags.MaterialFlag> flags = new ArrayList<>();

    public PureSubstances.PureSubstance type;
    public int atomicNumber;
    public String symbol;
    public float molecularWeight;
    public float atomicWeight;

    public OreTypes.OreType oreType;
    public Integer harvestLevel = null;

    public double meltingPoint;
    public double boilingPoint;

    public Float fluidAcceleration = 0.014F;
    public FluidStates.FluidState fluidState = null;
    public Integer fluidDensity = null;
    public Integer fluidViscosity = null;
    public Integer fluidLuminosity = null;

    public Integer durability = null;
    public Integer attackSpeed = null;
    public Float attackDamage = null;


    public Item ingot, gem, chippedGem, flawedGem, flawlessGem, legendaryGem, crushedOre, purifiedOre, centrifugedOre, impureDust, pureDust, dust, smallDust, tinyDust;


}
