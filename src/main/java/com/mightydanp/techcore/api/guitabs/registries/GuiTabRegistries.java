package com.mightydanp.techcore.api.guitabs.registries;

import com.mightydanp.techcore.api.guitabs.GuiTab;
import com.mightydanp.techcore.api.guitabs.Tabs.InventoryGuiTab;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GuiTabRegistries {

    public static Map<Class<? extends Screen>, GuiTab> guiTabs = new HashMap<>();

    public static GuiTab playerInventory;


    public static void init(){
        guiTabs.put(InventoryScreen.class, playerInventory = new InventoryGuiTab("player_inventory").setPriorityNumber(1).setItem(new ItemStack(Items.GRASS_BLOCK)));
    }

}
