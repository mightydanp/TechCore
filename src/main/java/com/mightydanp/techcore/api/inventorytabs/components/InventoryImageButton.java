package com.mightydanp.techcore.api.inventorytabs.components;

import com.mightydanp.techcore.world.inventory.InventoryTab;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InventoryImageButton extends Button {

    private final InventoryTab inventoryTab;
    protected final Button.OnPress onPress;

    protected final float red;
    protected final float green;
    protected final float blue;
    protected final float alpha;

    protected final ResourceLocation image;
    protected final ItemStack item;

    protected final WidgetSprites sprites;

    private InventoryImageButton(int x, int y, int width, int height, InventoryTab inventoryTab) {
        super(x, y, width, height, inventoryTab.narration, inventoryTab.onPress, DEFAULT_NARRATION);
        this.inventoryTab = inventoryTab;
        this.onPress = inventoryTab.onPress;

        this.red = inventoryTab.red;
        this.green = inventoryTab.green;
        this.blue = inventoryTab.blue;
        this.alpha = inventoryTab.alpha;

        this.image = inventoryTab.image;
        this.item = inventoryTab.item;

        this.sprites = inventoryTab.sprites;


    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        ResourceLocation resourcelocation = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);

        if(red > 0 || green > 0 || blue > 0 || alpha != 1) {
            guiGraphics.setColor(this.red, this.green, this.blue, this.alpha);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
        }

        if(image != null){
            guiGraphics.blitSprite(image, this.getX(), this.getY(), this.width, this.height);
        }

        if(item != null){
            guiGraphics.renderFakeItem(item, this.getX(), this.getY());
        }

        guiGraphics.pose().pushPose();
    }
}
