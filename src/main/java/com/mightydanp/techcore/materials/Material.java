package com.mightydanp.techcore.materials;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.materials.components.*;
import com.mightydanp.techcore.materials.lib.MaterialRef;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

public class Material implements BaseRegistries {
    private static final List<MaterialComponentFactory> GLOBAL_COMPONENTS = new ArrayList<>();

    public String name;
    public Icons.Icon icon;

    public ChemicalComponent<Material> chemical;
    public PhysicalComponent<Material> physical;
    public ThermalComponent<Material> thermal;
    public FluidComponent<Material> fluid;
    public OreComponent<Material> ore;
    public StoneLayerComponent<Material> stoneLayer;

    public List<OrderedComponent> components = new ArrayList<>();
    private boolean globalComponentsLoaded;

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

    @Override
    public void init(){
        orderedComponents().forEach(AbstractComponent::init);
    }

    @Override
    public void initClient(){
        orderedComponents().forEach(AbstractComponent::initClient);
    }

    @Override
    public void initLanguages() {
        orderedComponents().forEach(AbstractComponent::initLanguages);
        MaterialRef.initLanguages();
    }

    public void initItemProperties() {
        orderedComponents().forEach(AbstractComponent::initItemProperties);
    }

    public void initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        orderedComponents().forEach(component -> component.initClientRenderLayers(event));
    }

    private List<Component<?>> orderedComponents() {
        loadGlobalComponents();

        TreeMap<Integer, Component<?>> componentsByOrder = new TreeMap<>();

        insertComponent(componentsByOrder, 0, chemical);
        insertComponent(componentsByOrder, 1, physical);
        insertComponent(componentsByOrder, 2, thermal);
        insertComponent(componentsByOrder, 3, fluid);
        insertComponent(componentsByOrder, 4, stoneLayer);
        insertComponent(componentsByOrder, 5, ore);

        for (OrderedComponent orderedComponent : components) {
            insertComponent(componentsByOrder, orderedComponent.loadOrder(), orderedComponent.component());
        }

        return new ArrayList<>(componentsByOrder.values());
    }

    public static void registerComponent(int loadOrder, Function<Material, Component<?>> factory) {
        GLOBAL_COMPONENTS.add(new MaterialComponentFactory(loadOrder, factory));
    }

    public Material additionalComponent(int loadOrder, Function<Material, Component<?>> factory){
        this.components.add(new OrderedComponent(loadOrder, factory.apply(this)));
        return this;
    }

    private void loadGlobalComponents() {
        if (globalComponentsLoaded) return;

        for (MaterialComponentFactory factory : GLOBAL_COMPONENTS) {
            components.add(new OrderedComponent(factory.loadOrder(), factory.factory().apply(this)));
        }

        globalComponentsLoaded = true;
    }

    private void insertComponent(TreeMap<Integer, Component<?>> componentsByOrder, int loadOrder, Component<?> component) {
        if (loadOrder == Integer.MAX_VALUE && componentsByOrder.containsKey(loadOrder)) {
            throw new IllegalStateException("Cannot shift component past max load order: " + component);
        }

        Component<?> shifted = componentsByOrder.put(loadOrder, component);
        if (shifted != null) {
            insertComponent(componentsByOrder, loadOrder + 1, shifted);
        }
    }

    public record OrderedComponent(int loadOrder, Component<?> component) {}

    private record MaterialComponentFactory(int loadOrder, Function<Material, Component<?>> factory) {}
}
