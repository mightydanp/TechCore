package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.gui.screens.inventory.TCPlayerInventoryScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.world.inventory.TCPlayerInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MenuRegistries {
    public static final Supplier<MenuType<TCPlayerInventoryMenu>> TC_PLAYER_INVENTORY_MENU = RegistriesHandler.MENU_TYPES.register("tc_player_inventory",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new TCPlayerInventoryMenu(windowId, inv)));

    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event) {
        event.register(TC_PLAYER_INVENTORY_MENU.get(), TCPlayerInventoryScreen::new);
    }
}
