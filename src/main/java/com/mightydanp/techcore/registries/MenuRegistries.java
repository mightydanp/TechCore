package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.gui.screens.inventory.TCPlayerInventoryScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.world.inventory.TCPlayerInventoryMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MenuRegistries {
    public static final Supplier<MenuType<TCPlayerInventoryMenu>> TC_PLAYER_INVENTORY_MENU =
            RegistriesHandler.MENU_TYPES.register(
                    "tc_player_inventory",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new TCPlayerInventoryMenu(windowId, inv))
            );

    // Client setup method instead of RegisterMenuScreensEvent
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Schedule for client thread
        event.enqueueWork(() -> {
            MenuScreens.register(TC_PLAYER_INVENTORY_MENU.get(), TCPlayerInventoryScreen::new);
        });
    }
}
