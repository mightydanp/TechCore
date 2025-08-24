package com.mightydanp.techcore.guitabs.components;

import com.mightydanp.techcore.guitabs.GuiTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    protected final ResourceLocation customImage;

    private static final ResourceLocation CREATIVE_TABS_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tabs.png");

    // size of ONE tab cell in the sheet
    private static final int CELL_W = 26;
    private static final int CELL_H = 32;

    // grid layout
    private static final int COLS = 7; // 7 across
    private static final int ROWS = 4;

    // full texture size
    private static final int SHEET_W = 256;
    private static final int SHEET_H = 256;

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

        this.customImage = guiTab.sprites;
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
        if (customImage != null) {
            guiGraphics.blit(customImage, this.getX(), this.getY(), 0, 0, this.width, this.height, 1, 1);
        } else {
            // Handle top/bottom tab logic
            int group = (buttonNumber - 1) / COLS;
            int col = (buttonNumber - 1) % COLS;
            boolean isTop = group % 2 == 0;
            boolean selected = this.isActive();
            int row;

            if (isTop) {
                row = selected ? 1 : 0;     // top selected / unselected
            } else {
                row = selected ? 3 : 2;     // bottom selected / unselected
            }

            // Pixel offsets inside the sheet
            int frameW = this.width;
            int frameH = this.height;
            int u = col * frameW;
            int v = row * frameH;

            int drawY = this.getY() + (isTop ? (selected ? 0 : -1) : (selected ? 0 : 1));

            // Draw the chosen cell from the sheet
            guiGraphics.blit(CREATIVE_TABS_LOCATION, this.getX(), drawY, u, v, frameW, frameH, SHEET_W, SHEET_H);
        }

        // Reset color to default (transparent) to prevent it affecting the image
        guiGraphics.setColor(1f, 1f, 1f, 1f); // Reset to default color (no tint)

        if (item != null) {
            guiGraphics.renderFakeItem(item, this.getX() + 5, this.getY() + 9);
        }

        if (guiTab != null && this.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tab." + guiTab.modId + "." + guiTab.name), mouseX, mouseY);
        }

        guiGraphics.pose().popPose(); // Clean up pose after rendering

    }
}
