package com.mightydanp.techcore.client.event;

import com.mightydanp.techcore.client.gui.screens.split.QuantitySplitScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.network.TCNetworkChannel;
import com.mightydanp.techcore.network.protocol.game.ServerBoundSplitQuantityPacket;
import com.mightydanp.techcore.world.item.properties.Quantity;
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
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void splitEvent(ScreenEvent.MouseButtonPressed.@NotNull Pre event) {
        // Checks if the screen is an AbstractContainerScreen
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)) return;

        // Not a right-click — let vanilla handle it
        if (event.getButton() != 1) return;

        ItemStack heldStack = containerScreen.getMenu().getCarried();

        // Get the slot currently under the mouse and check if you can place it in that slot
        Slot hoveredSlot = containerScreen.hoveredSlot;
        if (hoveredSlot == null) return;
        if (!hoveredSlot.mayPlace(heldStack)) return;

        // Get quantity
        Quantity heldQuantity = Quantity.stack(heldStack).get();

        // If it equals null — let vanilla handle it
        if (heldQuantity == null) return;

        // If it is less than 1 — let vanilla handle it
        if (heldQuantity.quantity() < 1) return;

        // Get the itemstack in the hovered slot
        ItemStack hoveredStack = hoveredSlot.getItem();

        int maxAmount = heldQuantity.quantity();

        // If the hovered slot already has an item, validate it before canceling vanilla behavior
        if (hoveredSlot.hasItem()) {
            // Grabs the quantity in the hovered slot itemstack
            Quantity slotQuantity = Quantity.stack(hoveredStack).get();

            // If the hovered itemstack does not have quantity data — let vanilla handle it
            if (slotQuantity == null) return;

            if (!canMergeQuantityStacks(heldStack, hoveredStack)) return;

            // If the held itemstack is not the same name as the hovered slot itemstack do nothing
            if (!heldStack.getHoverName().getString().equals(hoveredStack.getHoverName().getString())) return;

            // If the slots itemstack is its max quantity it can have return nothing
            if (slotQuantity.quantity() >= slotQuantity.maxQuantity()) return;

            // Use the smaller value: the held quantity or the slot's remaining capacity.
            maxAmount = Math.min(heldQuantity.quantity(), slotQuantity.maxQuantity() - slotQuantity.quantity());
        }

        // Get the slot index the server must use for player.containerMenu.slots.
        int serverSlotIndex = getServerSlotIndex(containerScreen, hoveredSlot);

        // If the server slot index is negative do nothing
        if (serverSlotIndex < 0) return;

        // Get the slot index the client response must use for the currently open client menu.
        int clientSlotIndex = containerScreen.getMenu().slots.indexOf(hoveredSlot);

        // If the client slot could not be found do nothing
        if (clientSlotIndex < 0) return;

        // Cancel vanilla click handling because this interaction is handled here.
        event.setCanceled(true);

        // If player is shifting or the amount that can be given is one
        if (Screen.hasShiftDown() || heldQuantity.quantity() == 1) {
            // Server handles the splitting. The client waits for the accepted result packet before changing cursor or slot state.
            TCNetworkChannel.INSTANCE.sendToServer(new ServerBoundSplitQuantityPacket(serverSlotIndex, clientSlotIndex, maxAmount, heldStack));
        } else {
            //opens the quantity split screen
            Minecraft.getInstance().setScreen(new QuantitySplitScreen(containerScreen, heldStack, hoveredSlot, serverSlotIndex, clientSlotIndex));
        }
    }

    private static int getServerSlotIndex(AbstractContainerScreen<?> containerScreen, Slot hoveredSlot) {
        //if the screen is not creative inventory screen
        if (!(containerScreen instanceof CreativeModeInventoryScreen creativeScreen)) {
            //returns hovered slot's index
            return hoveredSlot.index;
        }

        //if the creative inventory screen is open
        if (creativeScreen.isInventoryOpen()) {
            //creative inventory wraps player inventory slots, so convert the backing player inventory slot
            //to the matching server InventoryMenu slot index
            return getInventoryMenuSlotIndex(hoveredSlot.getContainerSlot());
        }

        //if the hovered slots index of greater than or equals 45 and the hovered slots index is less than 54
        if (hoveredSlot.index >= 45 && hoveredSlot.index < 54) {
            //return the hovered slots container slot int and add 36 to it
            return 36 + hoveredSlot.getContainerSlot();
        }

        // Creative screen slot could not be mapped to a server slot.
        return -1;
    }

    private static int getInventoryMenuSlotIndex(int containerSlot) {
        // Player inventory hotbar slots are container slots 0-8, but server menu slots 36-44.
        if (containerSlot >= 0 && containerSlot < 9) {
            return 36 + containerSlot;
        }

        // Player inventory main slots are container slots 9-35 and server menu slots 9-35.
        if (containerSlot >= 9 && containerSlot < 36) {
            return containerSlot;
        }

        // Player inventory armor slots are container slots 36-39, but server menu slots 8-5.
        if (containerSlot >= 36 && containerSlot < 40) {
            return 44 - containerSlot;
        }

        // Player inventory offhand is container slot 40, but server menu slot 45.
        if (containerSlot == 40) {
            return 45;
        }

        // Some creative wrapped slots already report their server menu slot index.
        if (containerSlot >= 41 && containerSlot <= 45) {
            return containerSlot;
        }

        // Unknown player inventory slot.
        return -1;
    }

    private static boolean canMergeQuantityStacks(ItemStack heldStack, ItemStack hoveredStack) {
        //if held itemstack and hovered itemstack is not the same
        if (!ItemStack.isSameItem(heldStack, hoveredStack)) {
            return false;
        }

        //get a copy of held itemstack
        ItemStack heldCopy = heldStack.copy();
        //get a copy of hovered itemstack
        ItemStack hoveredCopy = hoveredStack.copy();

        //remove quantity from held itemstack
        heldCopy.getOrCreateTag().remove(Quantity.TAG);
        heldCopy.getOrCreateTag().remove(Quantity.MAX_TAG);
        //remove hovered from held itemstack
        hoveredCopy.getOrCreateTag().remove(Quantity.TAG);
        hoveredCopy.getOrCreateTag().remove(Quantity.MAX_TAG);

        //compare and return boolean
        return ItemStack.isSameItemSameTags(heldCopy, hoveredCopy);
    }
}