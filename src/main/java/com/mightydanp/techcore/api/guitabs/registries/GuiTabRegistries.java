package com.mightydanp.techcore.api.guitabs.registries;

import com.mightydanp.techcore.api.guitabs.GuiTab;
import com.mightydanp.techcore.api.guitabs.Tabs.InventoryGuiTab;
import com.mightydanp.techcore.api.guitabs.libs.GuiTabRef;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GuiTabRegistries {

    public static Map<Class<? extends Screen>, GuiTab> guiTabs = new HashMap<>();

    public static GuiTab playerInventory;


    public static void init(){
        guiTabs.put(InventoryScreen.class, playerInventory = new InventoryGuiTab(Component.translatable(GuiTabRef.player_inventory_name)).setPriorityNumber(1).setItem(new ItemStack(Items.GRASS_BLOCK)));
    }

    public static void initResource(){
        LanguageContent enLang = AssetPackRegistries.getLanguage(CoreRef.MOD_ID, "en_us");
        enLang.addTranslation("tab." + CoreRef.MOD_ID + "."+ GuiTabRef.player_inventory_name, LanguageContent.translateUpperCase(GuiTabRef.player_inventory_name));

        AssetPackRegistries.saveLanguage(enLang, false);
    }

}
