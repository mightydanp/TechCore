package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import java.util.function.Supplier;

public class Component<A extends Material, C extends Component<A, C>> extends AbstractComponent<C>{
    protected final A material;

    public Component(String prefix, String suffix, A material) {
        super(prefix, suffix);
        this.material = material;
    }

    public A end() {
        return material;
    }

    public void registerItemColor(RegisterColorHandlersEvent.Item event, Supplier<Item> item, int... layer) {
        if (item != null) {
            event.register((stack, tintIndex) ->
                            tintIndex >= 0 && tintIndex < layer.length ? layer[tintIndex] : -1,
                    item.get()
            );
        }
    }

    public void registerBlockColor(RegisterColorHandlersEvent.Block event, Supplier<Block> block, int... layer) {
        if (block != null) {
            event.register((state, level, pos, tintIndex) ->
                            tintIndex >= 0 && tintIndex < layer.length ? layer[tintIndex] : -1,
                    block.get()
            );
        }
    }


    public void registerItemProperty(Supplier<Item> item, ResourceLocation key, net.minecraft.client.renderer.item.ClampedItemPropertyFunction property) {
        if(item != null) {
            net.minecraft.client.renderer.item.ItemProperties.register(item.get(), key, property);
        }
    }
}


