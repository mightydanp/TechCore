package com.mightydanp.techcore.api.guitabs.Tabs;

import com.mightydanp.techcore.api.guitabs.GuiTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class InventoryGuiTab extends GuiTab {
    public Player player;

    public InventoryGuiTab(Component name){
        super(name, 176, 166);
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
