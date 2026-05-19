package com.mightydanp.techcore.client.event;

import com.mightydanp.techcore.client.gui.screens.split.QuantitySplitScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.MaterialItem;
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

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void splitEvent(ScreenEvent.MouseButtonPressed.Pre event){
        // Checks if the screen is an AbstractContainerScreen
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)) return;

        // Not a right-click — let vanilla handle it
        if (event.getButton() != 1) return;

        ItemStack heldStack = containerScreen.getMenu().getCarried();

        // Change this for splitting other things
        if (!(heldStack.getItem() instanceof MaterialItem dust)) return;


        // Get the slot currently under the mouse and check if you can place it in that slot
        Slot hoveredSlot = containerScreen.hoveredSlot;
        if (hoveredSlot == null) return;
        if (!hoveredSlot.mayPlace(heldStack)) return;

        // Get quantity
        Integer heldQuantity = Quantity.getQuantity(heldStack);

        // If it equals null — let vanilla handle it
        if (heldQuantity == null) return;

        // If it is less than 1 — let vanilla handle it
        if (heldQuantity < 1) return;

        // Get the itemstack in the hovered slot
        ItemStack hoveredStack = hoveredSlot.getItem();

        int maxAmount = heldQuantity;

        // If the hovered slot doesn't have an item then the max quantity the itemstack can have is the quantity in the held
        if (hoveredSlot.hasItem() && hoveredStack.getItem() instanceof MaterialItem slotDust) {
            if(!canMergeQuantityStacks(heldStack, hoveredStack)) return;

            // If the held itemstack is not the same name as the hovered slot itemstack do nothing
            if (!heldStack.getHoverName().getString().equals(hoveredStack.getHoverName().getString())) return;

            // Grabs the quantity in the hovered slot itemstack
            Integer slotQuantity = Quantity.getQuantity(hoveredStack);

            // If — let vanilla handle it
            if (slotQuantity == null) return;

            // If the slots itemstack is its max quantity it can have return nothing
            if (slotQuantity.equals(slotDust.getMaxQuantity())) return;

            // Takes the slots max quantity and takes away the slot itemstack quantity.
            // After it will check which one is smaller heldQuantity or slotDust.getMaxQuantity() - slotQuantity
            // Use the smaller value: the held quantity or the slot's remaining capacity.
            maxAmount = Math.min(heldQuantity, slotDust.getMaxQuantity() - slotQuantity);
        }

        // Get the slot index from the hovered slot
        int serverSlotIndex = getServerSlotIndex(containerScreen, hoveredSlot);

        // If the slot index is negative do nothing
        if (serverSlotIndex < 0) return;

        // Cancel vanilla click handling because this interaction is handled here.
        event.setCanceled(true);

        // If player is shifting or the amount that can be given is one
        if (Screen.hasShiftDown() || heldQuantity == 1) {
            // Server handles the splitting.
            TCNetworkChannel.INSTANCE.sendToServer(new ServerBoundSplitQuantityPacket(serverSlotIndex, maxAmount, heldStack));

            // If you are in the creative inventory screen
            if (containerScreen instanceof CreativeModeInventoryScreen) {
                // Takes away the max amount that can be given from the held itemstack quantity
                int newQuantity = heldQuantity - maxAmount;

                // If the new quantity of the held itemstack is 0 or negative
                if (newQuantity <= 0) {
                    // Sets the carried itemstack to empty
                    containerScreen.getMenu().setCarried(ItemStack.EMPTY);
                } else {
                    //grabs a copy of the held itemstack
                    ItemStack newHeld = heldStack.copy();

                    //sets the quantity of the copied itemstack
                    Quantity.setQuantity(newHeld, newQuantity);

                    //sets the carried item to the copy
                    containerScreen.getMenu().setCarried(newHeld);
                }
            }
        } else {
            //opens the quantity split screen
            Minecraft.getInstance().setScreen(new QuantitySplitScreen(containerScreen, heldStack, hoveredSlot, serverSlotIndex));
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
            //return the hovered slot's container slot int
            return hoveredSlot.getContainerSlot();
        }

        //if the hovered slots index of greater than or equals 45 and the hovered slots index is less than 54
        if (hoveredSlot.index >= 45 && hoveredSlot.index < 54) {
            //return the hovered slots container slot int and add 36 to it
            return 36 + hoveredSlot.getContainerSlot();
        }

        // Creative screen slot could not be mapped to a server slot.
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
        heldCopy.getOrCreateTag().remove("quantity");
        //remove hovered from held itemstack
        hoveredCopy.getOrCreateTag().remove("quantity");

        //compare and return boolean
        return ItemStack.isSameItemSameTags(heldCopy, hoveredCopy);
    }
}