package com.mightydanp.techcore.materials.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Component<A extends Component<A>> extends AbstractComponent<A> {
    public Component(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public void registerBasicItemColor(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item event, Item item, int color) {
        if (item != null) {
            event.register((stack, tintIndex) -> tintIndex == 0 ? color : -1, item);
        }
    }

    public void registerItemProperty(Item item, ResourceLocation key, net.minecraft.client.renderer.item.ClampedItemPropertyFunction property) {
        if(item != null) {
            net.minecraft.client.renderer.item.ItemProperties.register(item, key, property);
        }
    }
}
