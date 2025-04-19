package com.mightydanp.techcore.api.guitabs;

import com.mightydanp.techcore.api.guitabs.components.GuiTabBase;
import com.mightydanp.techcore.api.guitabs.registries.GuiTabRegistries;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GuiTab implements GuiTabBase {
    public Component name;

    public int width;
    public int height;

    public int priorityNumber = -1;

    public float red = 0;
    public float green = 0;
    public float blue = 0;
    public float alpha = 1;

    public ResourceLocation image = null;
    public ItemStack item = null;

    public WidgetSprites sprites = null;
    public Component narration = CommonComponents.EMPTY;
    public Tooltip tooltip = null;

    public GuiTab(Component name, int width, int height){
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public GuiTab setPriorityNumber(int priorityNumber) {
        // Check for invalid negative numbers
        if (priorityNumber < -1) {
            throw new IllegalArgumentException("Priority number cannot be less than -1.");
        }

        // Check if the priority number is already in use (except -1)
        if (priorityNumber != -1) {
            boolean priorityExists = GuiTabRegistries.guiTabs.values().stream().anyMatch(tab -> tab.priorityNumber == priorityNumber);

            if (priorityExists) {
                throw new IllegalArgumentException("Priority number " + priorityNumber + " is already in use.");
            }
        }

        this.priorityNumber = priorityNumber;
        return this;
    }


    public GuiTab setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public GuiTab setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        return this;
    }

    public GuiTab setImage(ResourceLocation image) {
        this.image = image;
        return this;
    }

    public GuiTab setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public GuiTab setNarration(Component narration) {
        this.narration = narration;
        return this;
    }

    public GuiTab setCustomTexture(WidgetSprites sprites) {
        this.sprites = sprites;
        return this;
    }


    public void openTargetScreen(Player player) {
        System.out.println("test");
    }

    public boolean isCurrentlyUsed(Screen currentScreen) {
        return false;
    }

    public GuiTab setToolTip(Tooltip tooltip){
        this.tooltip = tooltip;
        return this;
    }
}
