package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StoneLayerComponent<A extends Material> extends Component<StoneLayerComponent<A>> {
    public Block layerBlock, rockBlock, smoothSlabBlock;
    public Item layerItemBlock, rockItemBlock, smoothSlabItemBlock;

    private final A material;

    public StoneLayerComponent(A material) {
        super("stone_layer", "component");
        this.material = material;
    }

    public A end(){
        return material;
    }
}