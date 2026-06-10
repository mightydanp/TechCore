package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.ArrayList;
import java.util.List;

public class Material implements BaseRegistries<Material> {
    public String name;
    public Icons.Icon icon;

    public ChemicalComponent<Material> chemical;
    public PhysicalComponent<Material> physical;
    public ThermalComponent<Material> thermal;
    public FluidComponent<Material> fluid;
    public OreComponent<Material> ore;
    public RockLayerComponent<Material> rockLayer;
    public ProcessedComponent<Material> processed;
    public ToolComponent<Material> tool;

    private final List<Component<?, ?>> components = new ArrayList<>();

    public Material(String name, Icons.Icon icon){
        this.name = name;
        this.icon = icon;

        chemical = addComponent(0, new ChemicalComponent<>(this));
        physical = addComponent(1, new PhysicalComponent<>(this));
        thermal = addComponent(2, new ThermalComponent<>(this));
        fluid = addComponent(3, new FluidComponent<>(this));
        rockLayer = addComponent(4, new RockLayerComponent<>(this));
        ore = addComponent(5, new OreComponent<>(this));
        processed = addComponent(6, new ProcessedComponent<>(this));
        tool = addComponent(7, new ToolComponent<>(this));
    }

    @Override
    public Material init(){
        components.forEach(AbstractComponent::init);
        return this;
    }

    @Override
    public Material initClient(){
        components.forEach(AbstractComponent::initClient);
        return this;
    }

    @Override
    public Material initLanguages() {
        components.forEach(AbstractComponent::initLanguages);
        return this;
    }

    @Override
    public Material initTags() {
        components.forEach(AbstractComponent::initTags);
        return this;
    }

    @Override
    public Material initItemProperties() {
        components.forEach(AbstractComponent::initItemProperties);
        return this;
    }

    @Override
    public Material initBlockProperties(RegisterColorHandlersEvent.Block event) {
        components.forEach(component -> component.initBlockProperties(event));

        return this;
    }


    @Override
    public Material initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        components.forEach(component -> component.initClientRenderLayers(event));

        return this;
    }

    public <A extends Component<?, ?>> A addComponent(int loadOrder, A component) {
        if (loadOrder < 0) {
            throw new IllegalArgumentException("loadOrder cannot be negative: " + loadOrder);
        }

        int insertIndex = Math.min(loadOrder, components.size());
        components.add(insertIndex, component);

        return component;
    }

    public <A extends Component<?, ?>> A addComponent(A component) {
        return addComponent(components.size(), component);
    }
}

