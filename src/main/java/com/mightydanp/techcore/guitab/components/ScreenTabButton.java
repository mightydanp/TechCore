package com.mightydanp.techcore.guitab.components;

import com.mightydanp.techcore.guitab.ScreenTab;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ScreenTabButton extends Button {

    public int buttonNumber;

    protected final ScreenTab screenTab;
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

    private static final int CELL_W = 26;
    private static final int CELL_H = 32;

    private static final int COLS = 7;
    private static final int ROWS = 4;

    private static final int SHEET_W = 256;
    private static final int SHEET_H = 256;

    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull ScreenTabButton create(int registryButtonNumber, ScreenTab screenTab, Player player, Screen screen, int x, int y) {
        return new ScreenTabButton(registryButtonNumber, screenTab, player, screen, x, y);
    }

    private ScreenTabButton(int registryButtonNumber, @NotNull ScreenTab screenTab, Player player, Screen screen, int x, int y) {
        super(x, y, ScreenTabBase.TAB_WIDTH, ScreenTabBase.TAB_HEIGHT, screenTab.narration, button -> {
        }, DEFAULT_NARRATION);
        this.screenTab = screenTab;
        this.player = player;
        this.screen = screen;

        this.buttonNumber = screenTab.priorityNumber == -1 ? registryButtonNumber : screenTab.priorityNumber;

        this.red = screenTab.red;
        this.green = screenTab.green;
        this.blue = screenTab.blue;
        this.alpha = screenTab.alpha;

        this.image = screenTab.image;
        this.item = screenTab.item;

        this.customImage = screenTab.sprites;
    }

    @Override
    public void onPress() {
        screenTab.openTargetScreen(this.player);
        super.onPress();
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        if (screenTab.tooltip == null) {
            super.setTooltip(tooltip);
        }
    }

    @Override
    public Tooltip getTooltip() {
        if (screenTab.tooltip != null) {
            return screenTab.tooltip;
        }

        return super.getTooltip();
    }

    @Override
    public boolean isActive() {
        return screenTab.isCurrentlyUsed(screen);
    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return;

        guiGraphics.pose().pushPose();

        if (red > 0 || green > 0 || blue > 0 || alpha != 1f) {
            guiGraphics.setColor(this.red, this.green, this.blue, this.alpha);
        }

        if (customImage != null) {
            guiGraphics.blit(customImage, this.getX(), this.getY(), 0, 0, this.width, this.height, 1, 1);
        } else {
            int group = (buttonNumber - 1) / COLS;
            int col = (buttonNumber - 1) % COLS;
            boolean isTop = group % 2 == 0;
            boolean selected = this.isActive();
            int row;

            if (isTop) {
                row = selected ? 1 : 0;
            } else {
                row = selected ? 3 : 2;
            }

            int frameW = this.width;
            int frameH = this.height;
            int u = col * frameW;
            int v = row * frameH;

            int drawY = this.getY() + (isTop ? (selected ? 0 : -1) : (selected ? 0 : 1));

            guiGraphics.blit(CREATIVE_TABS_LOCATION, this.getX(), drawY, u, v, frameW, frameH, SHEET_W, SHEET_H);
        }

        guiGraphics.setColor(1f, 1f, 1f, 1f);

        if (item != null) {
            guiGraphics.renderFakeItem(item, this.getX() + 5, this.getY() + 9);
        }

        if (screenTab != null && this.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tab." + screenTab.modId + "." + screenTab.name), mouseX, mouseY);
        }

        guiGraphics.pose().popPose();
    }
}
