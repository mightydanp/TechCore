package com.mightydanp.techcore.guitabs.tabs;

import com.mightydanp.techcore.guitabs.ScreenTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;

public class InventoryScreenTab extends ScreenTab {
    public Player player;

    public InventoryScreenTab(String modID, String name) {
        super(modID, name, 176, 166);
    }

    @Override
    public void openTargetScreen(Player player) {
        InventoryScreen newGui = new InventoryScreen(player);
        Minecraft.getInstance().setScreen(newGui);
    }

    @Override
    public boolean isCurrentlyUsed(Screen currentScreen) {
        return currentScreen instanceof InventoryScreen;
    }
}
