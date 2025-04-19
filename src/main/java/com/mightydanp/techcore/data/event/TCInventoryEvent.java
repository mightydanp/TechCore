package com.mightydanp.techcore.data.event;

import com.mightydanp.techcore.api.guitabs.components.GuiTabBase;
import com.mightydanp.techcore.api.guitabs.components.GuiTabButton;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.api.guitabs.GuiTab;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mightydanp.techcore.api.guitabs.registries.GuiTabRegistries.guiTabs;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TCInventoryEvent {
    public static KeyMapping tcInventoryKey = new KeyMapping("key."+ CoreRef.MOD_ID +".inventory", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_E, KeyMapping.CATEGORY_INVENTORY);

    //saved for later
    public static int currentPage = 0;

    public static void addTabsToInventoryScreen(ScreenEvent.Init.Pre event) {
        Screen screen = event.getScreen();

        if(screen instanceof CreativeModeInventoryScreen){
            return;
        }

        if (screen instanceof AbstractContainerScreen<?> containerScreen && screen.getMinecraft().player != null) {
            Map<Class<? extends Screen>, GuiTab> orderedGuiTabs = new LinkedHashMap<>();

            // First, sort and add priority entries
            guiTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber != -1)
                    .sorted(Comparator.comparingInt(entry -> entry.getValue().priorityNumber)) // Optional: sort by priority
                    .forEachOrdered(entry -> orderedGuiTabs.put(entry.getKey(), entry.getValue()));

            // Then, add no-priority entries
            guiTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber == -1)
                    .forEachOrdered(entry -> orderedGuiTabs.put(entry.getKey(), entry.getValue()));

            AtomicInteger count = new AtomicInteger(1);

            // Map of page number to list of buttons
            Map<Integer, List<GuiTabButton>> pageButtonMap = new HashMap<>();

            // iterate over the ordered map
            orderedGuiTabs.forEach((screenClass, guiTab) -> {
                int buttonNumber = count.getAndIncrement();
                int pageNumber = (buttonNumber - 1) / 14;

                int group = (buttonNumber - 1) / 7;
                int indexInGroup = (buttonNumber - 1) % 7;
                boolean isTop = group % 2 == 0;

                System.out.println("Screen: " + screenClass.getSimpleName()
                        + ", Tab: " + guiTab.name
                        + ", Button #: " + buttonNumber
                        + ", Page #: " + pageNumber);

                int screenWidth = screen.width;
                int screenHeight = screen.height;

                int guiWidth = containerScreen.getXSize();
                int guiHeight = containerScreen.getYSize();

                // Calculate GUI top-left corner (centered)
                int guiX = (screenWidth - guiWidth) / 2;
                int guiY = (screenHeight - guiHeight) / 2;

                // Tab's position: aligned to top-left of GUI, but moved down by 4 pixels
                int X = guiX + (GuiTabBase.TAB_WIDTH * indexInGroup);
                int Y = guiY - GuiTabBase.TAB_HEIGHT + (isTop? 4 : guiHeight - 4);

                GuiTabButton tabButton = GuiTabButton.create(buttonNumber, guiTab, Minecraft.getInstance().player, screen, X, Y);
                pageButtonMap.computeIfAbsent(pageNumber, k -> new ArrayList<>()).add(tabButton);
            });

            // Only display buttons from current page
            List<GuiTabButton> buttonsToShow = pageButtonMap.getOrDefault(currentPage, Collections.emptyList());
            buttonsToShow.forEach(event::addListener);
        }
    }
}
