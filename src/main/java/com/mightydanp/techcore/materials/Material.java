package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.ArrayList;
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
    public ProcessedComponent<Material> processed;

    private final List<Component<?>> components = new ArrayList<>();

    public Material(String name, Icons.Icon icon){
        this.name = name;
        this.icon = icon;

        chemical = addComponent(0, new ChemicalComponent<>(this));
        physical = addComponent(1, new PhysicalComponent<>(this));
        thermal = addComponent(2, new ThermalComponent<>(this));
        fluid = addComponent(3, new FluidComponent<>(this));
        stoneLayer = addComponent(4, new StoneLayerComponent<>(this));
        ore = addComponent(5, new OreComponent<>(this));
        processed = addComponent(6, new ProcessedComponent<>(this));
    }

    @Override
    public void init(){
        components.forEach(AbstractComponent::init);
    }

    @Override
    public void initClient(){
        components.forEach(AbstractComponent::initClient);
    }

    @Override
    public void initLanguages() {
        components.forEach(AbstractComponent::initLanguages);
    }

    public void initItemProperties() {
        components.forEach(AbstractComponent::initItemProperties);
    }

    public void initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        components.forEach(component -> component.initClientRenderLayers(event));
    }

    public <A extends Component<?>> A addComponent(int loadOrder, A component) {
        if (loadOrder < 0) {
            throw new IllegalArgumentException("loadOrder cannot be negative: " + loadOrder);
        }

        int insertIndex = Math.min(loadOrder, components.size());
        components.add(insertIndex, component);

        return component;
    }

    public <A extends Component<?>> A addComponent(A component) {
        return addComponent(components.size(), component);
    }
}

