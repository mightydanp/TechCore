package com.mightydanp.techcore.guitab.registries;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.guitab.ScreenTab;
import com.mightydanp.techcore.guitab.tabs.InventoryScreenTab;
import com.mightydanp.techcore.guitab.libs.ScreenTabRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ScreenTabRegistries implements BaseRegistries {

    public static Map<Class<? extends Screen>, ScreenTab> screenTabs = new HashMap<>();

    public static ScreenTab playerInventory;

    @Override
    public void init() {
        screenTabs.put(InventoryScreen.class, playerInventory = new InventoryScreenTab(CoreRef.MOD_ID, ScreenTabRef.player_inventory_name).setPriorityNumber(1).setItem(new ItemStack(Items.GRASS_BLOCK)));
    }

    @Override
    public void initClient() {

    }

    @Override
    public void initLanguages() {
        AssetPackRegistries.safetyMSLT(false, screenTabs,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, "tab." + CoreRef.MOD_ID + "." + ScreenTabRef.player_inventory_name, LanguageContent.translateUpperCase(ScreenTabRef.player_inventory_name))
        );
    }
}
