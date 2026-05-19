package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StoneLayerComponent<A extends Material> extends Component<StoneLayerComponent<A>> {


    public ResourceLocation existingStonelayerBlock;
    public Block layerBlock, smoothSlabBlock;
    public Item layerItemBlock, smoothSlabItemBlock;

    private final A material;

    public StoneLayerComponent(A material) {
        super("stone_layer", "component");
        this.material = material;
    }

    public StoneLayerComponent<A> stoneLayer(ResourceLocation existingStonelayerBlock) {
        this.existingStonelayerBlock = existingStonelayerBlock;
        return this;
    }

    public A end(){
        return material;
    }
}