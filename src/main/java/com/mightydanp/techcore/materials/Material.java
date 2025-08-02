package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.properties.Icons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Material implements BaseRegistries {
    public String name;
    public Icons.Icon icon;

    public List<Component> components = new ArrayList<>();

    public Material(String name, Icons.Icon icon){
        this.name = name;
        this.icon = icon;
    }

    //keep in mind that this will initialize based off what you put first
    public Material additionalComponents(Component... components){
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    public ChemicalComponent chemicalComponent = new ChemicalComponent();
    public PhysicalComponent physicalComponent = new PhysicalComponent();
    public ThermalComponent thermalComponent = new ThermalComponent();
    public FluidComponent fluidComponent = new FluidComponent();
    public OreComponent oreComponent = new OreComponent();
    public StoneLayerComponent stoneLayerComponent = new StoneLayerComponent();

    public void initServer(){
        chemicalComponent.initServer(this);
        physicalComponent.initServer(this);
        thermalComponent.initServer(this);
        fluidComponent.initServer(this);
        stoneLayerComponent.initServer(this);
        oreComponent.initServer(this);

        components.forEach(component -> component.initServer(this));
    }

    @Override
    public void initClient(){
        chemicalComponent.initClient(this);
        physicalComponent.initClient(this);
        thermalComponent.initClient(this);
        fluidComponent.initClient(this);
        oreComponent.initClient(this);
        stoneLayerComponent.initClient(this);

        components.forEach(component -> component.initClient(this));
    };

}
