package com.mightydanp.techcore.api.guitabs.components;

import com.mightydanp.techcore.api.guitabs.GuiTab;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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

    public static GuiTabButton create(int registryButtonNumber, GuiTab guiTab, Player player, Screen screen, int x, int y){
        int height = screen.height;
        int width = screen.width;
        return new GuiTabButton(registryButtonNumber, guiTab, player, screen, x, y);
    }

    private GuiTabButton(int registryButtonNumber, GuiTab guiTab, Player player, Screen screen, int x, int y) {
        super(x, y, GuiTabBase.TAB_WIDTH, GuiTabBase.TAB_HEIGHT, guiTab.narration, button -> {}, DEFAULT_NARRATION);
        this.guiTab = guiTab;
        this.player = player;
        this.screen = screen;

        this.buttonNumber = guiTab.priorityNumber == -1? registryButtonNumber : guiTab.priorityNumber;

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
        if(guiTab.tooltip != null) {
            super.setTooltip(tooltip);
        }
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        if(sprites != null) {
            ResourceLocation resourcelocation = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        } else {

            int group = (buttonNumber - 1) / 7;
            int indexInGroup = (buttonNumber - 1) % 7; // index from 0–6

            boolean isTop = group % 2 == 0;

            if (isTop) {
                WidgetSprites top = new WidgetSprites(SELECTED_TOP_TABS[indexInGroup], UNSELECTED_TOP_TABS[indexInGroup], SELECTED_TOP_TABS[indexInGroup], UNSELECTED_TOP_TABS[indexInGroup]);
                ResourceLocation resourcelocation = top.get(this.isActive(), this.isHoveredOrFocused());
                guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);

            } else {
                WidgetSprites bottom = new WidgetSprites(SELECTED_BOTTOM_TABS[indexInGroup], UNSELECTED_BOTTOM_TABS[indexInGroup], SELECTED_BOTTOM_TABS[indexInGroup], UNSELECTED_BOTTOM_TABS[indexInGroup]);
                ResourceLocation resourcelocation = bottom.get(this.isActive(), this.isHoveredOrFocused());
                guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
            }
        }

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
