package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.world.inventory.TCPlayerInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public class MenuRegistries {
    public static final Supplier<MenuType<TCPlayerInventoryMenu>> TC_PLAYER_INVENTORY = RegistriesHandler.MENU_TYPES.register("tc_player_inventory",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new TCPlayerInventoryMenu(windowId, inv, inv.player)));

}
