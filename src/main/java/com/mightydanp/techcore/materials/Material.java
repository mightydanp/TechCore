package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Material implements BaseRegistries {
    public String name;
    public Icons.Icon icon;

    public ChemicalComponent<Material> chemical;
    public PhysicalComponent<Material> physical;
    public ThermalComponent<Material> thermal;
    public FluidComponent<Material> fluid;
    public OreComponent<Material> ore;
    public StoneLayerComponent<Material> stoneLayer;

    public List<Component<?>> components = new ArrayList<>();

    public Material(String name, Icons.Icon icon){
        this.name = name;
        this.icon = icon;

        chemical = new ChemicalComponent<>(this);
        physical = new PhysicalComponent<>(this);
        thermal = new ThermalComponent<>(this);
        fluid = new FluidComponent<>(this);
        ore = new OreComponent<>(this);
        stoneLayer = new StoneLayerComponent<>(this);
    }

    //keep in mind that this will initialize based off what you put first
    public Material additionalComponents(Component<?>... components){
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    @Override
    public void init(){
        chemical.init();
        physical.init();
        thermal.init();
        fluid.init();
        stoneLayer.init();
        ore.init();

        components.forEach(AbstractComponent::init);
    }

    @Override
    public void initClient(){
        chemical.initClient();
        physical.initClient();
        thermal.initClient();
        fluid.initClient();
        ore.initClient();
        stoneLayer.initClient();

        components.forEach(AbstractComponent::initClient);
    }

    @Override
    public void initLanguages() {
        chemical.initLanguages();
        physical.initLanguages();
        thermal.initLanguages();
        fluid.initLanguages();
        ore.initLanguages();
        stoneLayer.initLanguages();
        MaterialRef.initLanguages();

        components.forEach(AbstractComponent::initLanguages);
    }

    public void initItemProperties() {
        chemical.initItemProperties();
        physical.initItemProperties();
        thermal.initItemProperties();
        fluid.initItemProperties();
        ore.initItemProperties();
        stoneLayer.initItemProperties();

        components.forEach(AbstractComponent::initItemProperties);
    }

    public void initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        chemical.initClientRenderLayers(event);
        physical.initClientRenderLayers(event);
        thermal.initClientRenderLayers(event);
        fluid.initClientRenderLayers(event);
        ore.initClientRenderLayers(event);
        stoneLayer.initClientRenderLayers(event);

        components.forEach(c -> c.initClientRenderLayers(event));
    }

}
