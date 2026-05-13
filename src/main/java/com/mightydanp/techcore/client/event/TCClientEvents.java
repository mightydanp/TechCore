package com.mightydanp.techcore.client.event;

import com.mightydanp.techcore.client.gui.screens.split.DustSplitScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import com.mightydanp.techcore.network.TCNetworkChannel;
import com.mightydanp.techcore.network.protocol.game.ServerBoundSplitDustPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TCClientEvents {

    @SubscribeEvent
    public static void splitEvent(ScreenEvent.MouseButtonPressed.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen){
            // Get the item currently held in the cursor
            ItemStack cursorStack = containerScreen.getMenu().getCarried();

            // Get the slot currently under the mouse
            Slot hoveredSlot = containerScreen.hoveredSlot;

            // No slot under the mouse — do nothing
            if (hoveredSlot == null) return;

            // Not a right-click — let vanilla handle it
            if (event.getButton() != 1) return;

            // Cursor isn't holding a DustItem — let vanilla handle it
            if (cursorStack.getItem() instanceof DustItem dust){
                String curserName = cursorStack.getHoverName().getString();
                String hoveredName = hoveredSlot.getItem().getHoverName().getString();

                if(hoveredSlot.hasItem() && !curserName.equals(hoveredName)) return;

                if(hoveredSlot.hasItem() && hoveredSlot.getItem().getItem() instanceof DustItem dustItem && Objects.equals(dustItem.getQuantity(hoveredSlot.getItem()), dustItem.getMaxQuantity())) return;

                if(dust.getQuantity(cursorStack) < 1) return;
                if(!hoveredSlot.mayPlace(cursorStack)) return;

                int cursorQty = dust.getQuantity(cursorStack);
                int maxAmount;

                if (!hoveredSlot.hasItem()) {
                    maxAmount = cursorQty;
                } else if (hoveredSlot.getItem().getItem() instanceof DustItem slotDust) {
                    int slotQty = slotDust.getQuantity(hoveredSlot.getItem());
                    maxAmount = Math.min(cursorQty, slotDust.getMaxQuantity() - slotQty);
                } else {
                    return;
                }

                int serverSlotIndex;
                if (containerScreen instanceof CreativeModeInventoryScreen creativeScreen) {
                    if (creativeScreen.isInventoryOpen()) {
                        serverSlotIndex = hoveredSlot.getContainerSlot();
                    } else if (hoveredSlot.index >= 45 && hoveredSlot.index < 54) {
                        serverSlotIndex = 36 + hoveredSlot.getContainerSlot();
                    } else {
                        return;
                    }
                } else {
                    serverSlotIndex = hoveredSlot.index;
                }

                event.setCanceled(true);

                if (Screen.hasShiftDown() || maxAmount == 1) {
                    TCNetworkChannel.INSTANCE.sendToServer(new ServerBoundSplitDustPacket(serverSlotIndex, maxAmount, cursorStack));
                    if (containerScreen instanceof CreativeModeInventoryScreen) {
                        int newQty = cursorQty - maxAmount;
                        if (newQty <= 0) {
                            containerScreen.getMenu().setCarried(ItemStack.EMPTY);
                        } else {
                            ItemStack newCursor = cursorStack.copy();
                            dust.setQuantity(newCursor, newQty);
                            containerScreen.getMenu().setCarried(newCursor);
                        }
                    }
                } else {
                    Minecraft.getInstance().setScreen(new DustSplitScreen(containerScreen, cursorStack, hoveredSlot, serverSlotIndex));
                }
            }
        }
    }
}
