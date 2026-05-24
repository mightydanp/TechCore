package com.mightydanp.techcore.materials.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import java.util.function.Supplier;

public class Component<A extends Component<A>> extends AbstractComponent<A> {
    public Component(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public void registerBasicItemColor(RegisterColorHandlersEvent.Item event, Supplier<Item> item, int color) {
        if (item != null) {
            event.register((stack, tintIndex) -> tintIndex == 0 ? color : -1, item.get());
        }
    }

    public void registerMultiItemColor(RegisterColorHandlersEvent.Item event, Supplier<Item> item, int... layer) {
        if (item != null) {
            event.register((stack, tintIndex) -> tintIndex >= 0 && tintIndex < layer.length ? layer[tintIndex] : -1
            , item.get());
        }
    }

    public void registerItemProperty(Supplier<Item> item, ResourceLocation key, net.minecraft.client.renderer.item.ClampedItemPropertyFunction property) {
        if(item != null) {
            net.minecraft.client.renderer.item.ItemProperties.register(item.get(), key, property);
        }
    }
}
