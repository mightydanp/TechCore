package com.mightydanp.techcore.data.event;

import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.gui.screens.inventory.TCPlayerInventoryScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.world.inventory.TCPlayerInventoryMenu;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TCInventoryEvent {
    public static KeyMapping tcInventoryKey = new KeyMapping("key."+ CoreRef.MOD_ID +".inventory", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_E, KeyMapping.CATEGORY_INVENTORY);

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        // Key binding stuff
        event.register(tcInventoryKey);
        //NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> onClientTick(e));
    }

    public static void onTick(PlayerTickEvent.Post event) {
        while (tcInventoryKey.consumeClick()) {
            MenuProvider menu = new SimpleMenuProvider((windowID, inventory, player) -> new TCPlayerInventoryMenu(windowID, inventory), TCPlayerInventoryMenu.translation);

            if(event.getEntity() instanceof ServerPlayer player){
                event.getEntity().openMenu(menu);
            }
        }
    }

    public static void onPlayerOpenInventory(ScreenEvent.Opening event){
        //TechCore.LOGGER.info(event.getScreen().toString());
        if(event.getScreen() instanceof InventoryScreen screen){
            event.setCanceled(true);
        }
    }

    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
            MenuProvider menu = new SimpleMenuProvider((windowID, inventory, player) -> new TCPlayerInventoryMenu(windowID, inventory), TCPlayerInventoryMenu.translation);

                if(event.getEntity().getMainHandItem().getItem() == Items.STICK){
                    event.getEntity().openMenu(menu);
            }
    }

}
