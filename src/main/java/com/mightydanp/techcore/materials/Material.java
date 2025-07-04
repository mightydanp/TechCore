package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.Item.FluidStates;
import com.mightydanp.techcore.materials.properties.OreTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public String name;

    public List<Component> components = new ArrayList<>();

    public ChemicalComponent chemicalComponent = new ChemicalComponent();
    public PhysicalComponent physicalComponent = new PhysicalComponent();
    public ThermalComponent thermalComponent = new ThermalComponent();
    public OreComponent oreComponent = new OreComponent();
    public FluidComponent fluidComponent = new FluidComponent();
    public StoneLayerComponent stoneLayerComponent = new StoneLayerComponent();

    public void initServer(){
        chemicalComponent.initServer(this);
        physicalComponent.initServer(this);
        thermalComponent.initServer(this);
        oreComponent.initServer(this);
        fluidComponent.initServer(this);
        stoneLayerComponent.initServer(this);

        components.forEach(component -> component.initServer(this));
    }

    public void initClient(){
        chemicalComponent.initClient(this);
        physicalComponent.initClient(this);
        thermalComponent.initClient(this);
        oreComponent.initClient(this);
        fluidComponent.initClient(this);
        stoneLayerComponent.initClient(this);

        components.forEach(component -> component.initClient(this));
    };

}
