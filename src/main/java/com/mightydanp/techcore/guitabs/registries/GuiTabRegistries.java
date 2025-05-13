package com.mightydanp.techcore.guitabs.registries;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.guitabs.GuiTab;
import com.mightydanp.techcore.guitabs.Tabs.InventoryGuiTab;
import com.mightydanp.techcore.guitabs.libs.GuiTabRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GuiTabRegistries implements BaseRegistries {

    public static Map<Class<? extends Screen>, GuiTab> guiTabs = new HashMap<>();

    public static GuiTab playerInventory;


    public static void init() {
        guiTabs.put(InventoryScreen.class, playerInventory = new InventoryGuiTab(CoreRef.MOD_ID, GuiTabRef.player_inventory_name).setPriorityNumber(1).setItem(new ItemStack(Items.GRASS_BLOCK)));
    }

    public void initResource() {
        LanguageContent enLang = AssetPackRegistries.getLanguage(CoreRef.MOD_ID, "en_us");
        enLang.addTranslation("tab." + CoreRef.MOD_ID + "." + GuiTabRef.player_inventory_name, LanguageContent.translateUpperCase(GuiTabRef.player_inventory_name));

        AssetPackRegistries.saveLanguage(enLang, false);
    }

}
