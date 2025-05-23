package com.mightydanp.techcore.guitabs.components;

import com.mightydanp.techcore.guitabs.GuiTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class GuiTabButton extends Button {

    public int buttonNumber;

    protected final GuiTab guiTab;
    public final Player player;
    protected final Screen screen;

    protected final float red;
    protected final float green;
    protected final float blue;
    protected final float alpha;

    protected final ResourceLocation image;
    protected final ItemStack item;

    protected final WidgetSprites sprites;

    private static final ResourceLocation[] UNSELECTED_TOP_TABS = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_1"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_3"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_4"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_5"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_6"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_unselected_7")
    };
    private static final ResourceLocation[] SELECTED_TOP_TABS = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_1"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_2"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_3"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_4"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_5"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_6"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_top_selected_7")
    };
    private static final ResourceLocation[] UNSELECTED_BOTTOM_TABS = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_1"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_2"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_3"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_4"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_5"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_6"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_7")
    };
    private static final ResourceLocation[] SELECTED_BOTTOM_TABS = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_1"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_2"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_3"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_4"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_5"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_6"),
            ResourceLocation.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_7")
    };

    public static GuiTabButton create(int registryButtonNumber, GuiTab guiTab, Player player, Screen screen, int x, int y) {

        return new GuiTabButton(registryButtonNumber, guiTab, player, screen, x, y);
    }

    private GuiTabButton(int registryButtonNumber, GuiTab guiTab, Player player, Screen screen, int x, int y) {
        super(x, y, GuiTabBase.TAB_WIDTH, GuiTabBase.TAB_HEIGHT, guiTab.narration, button -> {
        }, DEFAULT_NARRATION);
        this.guiTab = guiTab;
        this.player = player;
        this.screen = screen;

        this.buttonNumber = guiTab.priorityNumber == -1 ? registryButtonNumber : guiTab.priorityNumber;

        this.red = guiTab.red;
        this.green = guiTab.green;
        this.blue = guiTab.blue;
        this.alpha = guiTab.alpha;

        this.image = guiTab.image;
        this.item = guiTab.item;

        this.sprites = guiTab.sprites;
    }


    @Override
    public void onPress() {
        guiTab.openTargetScreen(this.player);
        super.onPress();
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        if (guiTab.tooltip == null) {
            super.setTooltip(tooltip);
        }
    }

    @Override
    public Tooltip getTooltip() {
        if (guiTab.tooltip != null) {
            super.getTooltip();
            return guiTab.tooltip;
        }

        return super.getTooltip();
    }

    @Override
    public boolean isActive() {
        return guiTab.isCurrentlyUsed(screen);
    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return;

        guiGraphics.pose().pushPose();

        // Set color only if you want to apply a tint
        if (red > 0 || green > 0 || blue > 0 || alpha != 1f) {
            guiGraphics.setColor(this.red, this.green, this.blue, this.alpha);
        }

        // Render the button sprite or custom sprite logic based on state
        if (sprites != null) {
            ResourceLocation resourcelocation = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        } else {
            // Handle top/bottom tab logic
            int group = (buttonNumber - 1) / 7;
            int indexInGroup = (buttonNumber - 1) % 7;
            boolean isTop = group % 2 == 0;

            if (isTop) {
                WidgetSprites top = new WidgetSprites(SELECTED_TOP_TABS[indexInGroup], UNSELECTED_TOP_TABS[indexInGroup], SELECTED_TOP_TABS[indexInGroup], UNSELECTED_TOP_TABS[indexInGroup]);
                ResourceLocation resourcelocation = top.get(this.isActive(), this.isHoveredOrFocused());
                guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY() - (this.isActive() ? 0 : 1), this.width, this.height);

            } else {
                WidgetSprites bottom = new WidgetSprites(SELECTED_BOTTOM_TABS[indexInGroup], UNSELECTED_BOTTOM_TABS[indexInGroup], SELECTED_BOTTOM_TABS[indexInGroup], UNSELECTED_BOTTOM_TABS[indexInGroup]);
                ResourceLocation resourcelocation = bottom.get(this.isActive(), this.isHoveredOrFocused());
                guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY() + (this.isActive() ? 0 : 1), this.width, this.height);
            }
        }

        // Reset color to default (transparent) to prevent it affecting the image
        guiGraphics.setColor(1f, 1f, 1f, 1f); // Reset to default color (no tint)

        // Render the image without any color tint (this should not be affected by the color)
        if (image != null) {
            guiGraphics.blitSprite(image, this.getX(), this.getY(), this.width, this.height);
        }

        // Render the item (without any tinting applied)
        if (item != null) {
            guiGraphics.renderFakeItem(item, this.getX() + 5, this.getY() + 9);
        }

        // Render tooltip (without any tinting applied)
        if (guiTab != null && this.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tab." + guiTab.modId + "." + guiTab.name), mouseX, mouseY);
        }


        guiGraphics.pose().popPose(); // Clean up pose after rendering

    }
}
