package com.mightydanp.techcore.guitab;

import com.mightydanp.techcore.guitab.components.ScreenTabBase;
import com.mightydanp.techcore.guitab.registries.ScreenTabRegistries;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ScreenTab implements ScreenTabBase {
    public String modId;
    public String name;

    public int width;
    public int height;

    public int priorityNumber = -1;

    public float red = 0;
    public float green = 0;
    public float blue = 0;
    public float alpha = 1;

    public ResourceLocation image = null;
    public ItemStack item = null;

    public ResourceLocation sprites = null;
    public Component narration = CommonComponents.EMPTY;
    public Tooltip tooltip = null;

    public ScreenTab(String modId, String name, int width, int height) {
        this.modId = modId;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public ScreenTab setPriorityNumber(int priorityNumber) {
        if (priorityNumber < -1) {
            throw new IllegalArgumentException("Priority number cannot be less than -1.");
        }

        if (priorityNumber != -1) {
            boolean priorityExists = ScreenTabRegistries.screenTabs.values().stream().anyMatch(tab -> tab.priorityNumber == priorityNumber);

            if (priorityExists) {
                throw new IllegalArgumentException("Priority number " + priorityNumber + " is already in use.");
            }
        }

        this.priorityNumber = priorityNumber;
        return this;
    }

    public ScreenTab setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public ScreenTab setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        return this;
    }

    public ScreenTab setImage(ResourceLocation image) {
        this.image = image;
        return this;
    }

    public ScreenTab setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ScreenTab setNarration(Component narration) {
        this.narration = narration;
        return this;
    }

    public ScreenTab setCustomTexture(ResourceLocation image) {
        this.sprites = image;
        return this;
    }

    public void openTargetScreen(Player player) {
        System.out.println("test");
    }

    public boolean isCurrentlyUsed(Screen currentScreen) {
        return false;
    }

    public ScreenTab setToolTip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }
}
