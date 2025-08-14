package com.mightydanp.techcore.guitabs.components;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;

public interface GuiTabBase {

    int TAB_HEIGHT = 32;
    int TAB_WIDTH = 26;

    void openTargetScreen(Player player);

    boolean isCurrentlyUsed(Screen currentScreen);
}
