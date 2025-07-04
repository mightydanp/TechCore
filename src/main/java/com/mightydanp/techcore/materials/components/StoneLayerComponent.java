package com.mightydanp.techcore.materials.components;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StoneLayerComponent extends Component {
    public Block layerBlock, rockBlock, smoothSlabBlock;
    public Item layerItemBlock, rockItemBlock, smoothSlabItemBlock;

    public StoneLayerComponent() {
        super("stone_layer", "component",
                m -> { //server

            },  m -> { //client

        });
    }
}