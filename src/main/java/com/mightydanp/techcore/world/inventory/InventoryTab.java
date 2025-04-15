package com.mightydanp.techcore.world.inventory;

import com.mightydanp.techcore.api.inventorytabs.components.InventoryImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class InventoryTab {
    public float red = 0;
    public float green = 0;
    public float blue = 0;
    public float alpha = 1;

    public ResourceLocation image = null;
    public ItemStack item = null;

    public final WidgetSprites sprites;
    public Component narration = CommonComponents.EMPTY;
    public final Button.OnPress onPress;

    public InventoryTab setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public InventoryTab setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        return this;
    }

    public InventoryTab setImage(ResourceLocation image) {
        this.image = image;
        return this;
    }

    public InventoryTab setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public InventoryTab setNarration(Component narration) {
        this.narration = narration;
        return this;
    }

    private InventoryTab(WidgetSprites sprites, Button.OnPress onPress) {
        this.sprites = sprites;
        this.onPress = onPress;
    }


}
