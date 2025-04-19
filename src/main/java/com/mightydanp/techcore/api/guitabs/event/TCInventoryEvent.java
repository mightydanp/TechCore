package com.mightydanp.techcore.api.guitabs.event;

import com.mightydanp.techcore.api.guitabs.components.GuiTabBase;
import com.mightydanp.techcore.api.guitabs.components.GuiTabButton;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.api.guitabs.GuiTab;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mightydanp.techcore.api.guitabs.registries.GuiTabRegistries.guiTabs;

@EventBusSubscriber(modid = CoreRef.MOD_ID, value = Dist.CLIENT)
public class TCInventoryEvent {
    public static KeyMapping tcInventoryKey = new KeyMapping("key."+ CoreRef.MOD_ID +".inventory", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_E, KeyMapping.CATEGORY_INVENTORY);

    private static int currentPage = 0;

    @SubscribeEvent
    public static void addTabsToInventoryScreen(ScreenEvent.Init.Pre event) {
        Screen screen = event.getScreen();

        if (screen instanceof CreativeModeInventoryScreen) {
            return;
        }

        if (screen instanceof AbstractContainerScreen<?> containerScreen && screen.getMinecraft().player != null) {
            Map<Class<? extends Screen>, GuiTab> orderedGuiTabs = new LinkedHashMap<>();

            // Sort and add tabs with priority
            guiTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber != -1)
                    .sorted(Comparator.comparingInt(entry -> entry.getValue().priorityNumber))
                    .forEachOrdered(entry -> orderedGuiTabs.put(entry.getKey(), entry.getValue()));

            // Add tabs without priority
            guiTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber == -1)
                    .forEachOrdered(entry -> orderedGuiTabs.put(entry.getKey(), entry.getValue()));

            AtomicInteger count = new AtomicInteger(1);
            Map<Integer, List<GuiTabButton>> pageButtonMap = new HashMap<>();

            int screenWidth = screen.width;
            int screenHeight = screen.height;
            int guiWidth = containerScreen.getXSize();
            int guiHeight = containerScreen.getYSize();
            int guiX = (screenWidth - guiWidth) / 2;
            int guiY = (screenHeight - guiHeight) / 2;

            // Create tab buttons and group by page
            orderedGuiTabs.forEach((screenClass, guiTab) -> {
                int buttonNumber = count.getAndIncrement();
                int pageNumber = (buttonNumber - 1) / 14;

                int group = (buttonNumber - 1) / 7;
                int indexInGroup = (buttonNumber - 1) % 7;
                boolean isTop = group % 2 == 0;

                int X = guiX + (GuiTabBase.TAB_WIDTH * indexInGroup);
                int Y = guiY - GuiTabBase.TAB_HEIGHT + (isTop ? 4 : guiHeight - 4);

                GuiTabButton tabButton = GuiTabButton.create(buttonNumber, guiTab, Minecraft.getInstance().player, screen, X, Y);
                pageButtonMap.computeIfAbsent(pageNumber, k -> new ArrayList<>()).add(tabButton);
            });

            // Clamp current page to bounds
            int totalPages = pageButtonMap.size();
            if (currentPage >= totalPages) {
                currentPage = totalPages - 1;
            }
            if (currentPage < 0) {
                currentPage = 0;
            }

            // Add buttons for current page
            List<GuiTabButton> buttonsToShow = pageButtonMap.getOrDefault(currentPage, Collections.emptyList());
            buttonsToShow.forEach(event::addListener);

            // Add page navigation buttons
            if (totalPages > 1) {
                int navY = guiY + guiHeight + 5;

                // Previous
                if (currentPage > 0) {
                    Button prev = Button.builder(Component.literal("<"), btn -> {
                        currentPage--;
                        Minecraft.getInstance().setScreen(screen); // reload
                    }).bounds(guiX, navY, 20, 20).build();
                    event.addListener(prev);
                }

                // Next
                if (currentPage < totalPages - 1) {
                    Button next = Button.builder(Component.literal(">"), btn -> {
                        currentPage++;
                        Minecraft.getInstance().setScreen(screen); // reload
                    }).bounds(guiX + guiWidth - 20, navY, 20, 20).build();
                    event.addListener(next);
                }

                // Page Indicator
                Button label = Button.builder(Component.literal("Page " + (currentPage + 1) + "/" + totalPages), btn -> {})
                        .bounds(guiX + guiWidth / 2 - 30, navY, 60, 20).build();
                label.active = false;
                event.addListener(label);
            }
        }
    }

}
