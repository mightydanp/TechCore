package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.List;


public class StoneLayerComponent<A extends Material> extends Component<StoneLayerComponent<A>> {
    public boolean isStoneLayer;

    public RockTypes.RockType rockType;

    public Block existingStonelayerBlock;
    public Block layerBlock, smoothSlabBlock;
    public Item layerItemBlock, smoothSlabItemBlock;

    private final A material;

    public StoneLayerComponent(A material) {
        super("stone_layer", "component");
        this.material = material;
    }

    public StoneLayerComponent<A> stoneLayer(Block existingStonelayerBlock, RockTypes.RockType rockType) {
        this.rockType = rockType;
        this.existingStonelayerBlock = existingStonelayerBlock;
        isStoneLayer = true;
        return this;
    }

    public StoneLayerComponent<A> stoneLayer(RockTypes.RockType rockType) {
        this.rockType = rockType;
        isStoneLayer = true;
        return this;
    }

    public static List<Material> getStoneLayerMaterials() {
        return RegistriesHandler.getMaterials().stream()
                .filter(material -> material.stoneLayer.isStoneLayer)
                .toList();
    }

    @Override
    public StoneLayerComponent<A> init() {
        return super.init();
    }

    @Override
    public StoneLayerComponent<A> initClient() {
        return super.initClient();
    }

    @Override
    public StoneLayerComponent<A> initLanguages() {
        return super.initLanguages();
    }

    @Override
    public StoneLayerComponent<A> initItemProperties() {
        return super.initItemProperties();
    }

    @Override
    public StoneLayerComponent<A> initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        return super.initClientRenderLayers(event);
    }

    public A end(){
        return material;
    }
}