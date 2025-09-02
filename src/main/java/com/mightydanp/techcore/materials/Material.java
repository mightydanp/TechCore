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

    public ChemicalComponent chemical = new ChemicalComponent();
    public PhysicalComponent physical = new PhysicalComponent();
    public ThermalComponent thermal = new ThermalComponent();
    public FluidComponent fluid = new FluidComponent();
    public OreComponent ore = new OreComponent();
    public StoneLayerComponent stoneLayer = new StoneLayerComponent();

    public void initServer(){
        chemical.initServer(this);
        physical.initServer(this);
        thermal.initServer(this);
        fluid.initServer(this);
        stoneLayer.initServer(this);
        ore.initServer(this);

        components.forEach(component -> component.initServer(this));
    }

    @Override
    public void initClient(){
        chemical.initClient(this);
        physical.initClient(this);
        thermal.initClient(this);
        fluid.initClient(this);
        ore.initClient(this);
        stoneLayer.initClient(this);

        components.forEach(component -> component.initClient(this));
    };

}
