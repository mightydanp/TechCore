package com.mightydanp.techcore.client.event;

import com.mightydanp.techcore.client.gui.screens.split.DustSplitScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TCClientEvents {

    @SubscribeEvent
    public static void splitEvent(ScreenEvent.MouseButtonPressed.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen){
            // Get the item currently held in the cursor
            ItemStack cursorStack = containerScreen.getMenu().getCarried();

            // Get the slot currently under the mouse
            Slot hoveredSlot = containerScreen.hoveredSlot;

            // Not a right-click — let vanilla handle it
            if (event.getButton() != 1) return;

            // Cursor isn't holding a DustItem — let vanilla handle it
            if (cursorStack.getItem() instanceof DustItem dust){
                if(dust.getQuantity(cursorStack) <= 1){
                    return;
                }
            }else{
                return;
            }

            // No slot under the mouse — do nothing
            if (hoveredSlot == null) return;

            // Slot already has an item — do nothing
            if (hoveredSlot.hasItem()) return;

            // Empty slot with dust in cursor — cancel vanilla click and open split screen
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(
                    new DustSplitScreen(containerScreen, cursorStack, hoveredSlot.index)
            );
        }
    }
}
