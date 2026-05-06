package com.mightydanp.techcore.guitab.event;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.guitab.ScreenTab;
import com.mightydanp.techcore.guitab.components.ScreenTabBase;
import com.mightydanp.techcore.guitab.components.ScreenTabButton;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mightydanp.techcore.guitab.registries.ScreenTabRegistries.screenTabs;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, value = Dist.CLIENT)
public class ScreenTabEvent {
    public static KeyMapping tcInventoryKey = new KeyMapping("key." + CoreRef.MOD_ID + ".inventory", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_E, KeyMapping.CATEGORY_INVENTORY);

    private static int currentPage = 0;

    @SubscribeEvent
    public static void addTabsToInventoryScreen(ScreenEvent.Init.Pre event) {
        Screen screen = event.getScreen();

        if (screen instanceof CreativeModeInventoryScreen) {
            return;
        }

        if (screen instanceof AbstractContainerScreen<?> containerScreen && screen.getMinecraft().player != null) {
            Map<Class<? extends Screen>, ScreenTab> orderedScreenTabs = new LinkedHashMap<>();

            screenTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber != -1)
                    .sorted(Comparator.comparingInt(entry -> entry.getValue().priorityNumber))
                    .forEachOrdered(entry -> orderedScreenTabs.put(entry.getKey(), entry.getValue()));

            screenTabs.entrySet().stream()
                    .filter(entry -> entry.getValue().priorityNumber == -1)
                    .forEachOrdered(entry -> orderedScreenTabs.put(entry.getKey(), entry.getValue()));

            AtomicInteger count = new AtomicInteger(1);
            Map<Integer, List<ScreenTabButton>> pageButtonMap = new HashMap<>();

            int screenWidth = screen.width;
            int screenHeight = screen.height;
            int guiWidth = containerScreen.getXSize();
            int guiHeight = containerScreen.getYSize();
            int guiX = (screenWidth - guiWidth) / 2;
            int guiY = (screenHeight - guiHeight) / 2;

            orderedScreenTabs.forEach((screenClass, screenTab) -> {
                int buttonNumber = count.getAndIncrement();
                int pageNumber = (buttonNumber - 1) / 14;

                int group = (buttonNumber - 1) / 7;
                int indexInGroup = (buttonNumber - 1) % 7;
                boolean isTop = group % 2 == 0;

                int X = guiX + (ScreenTabBase.TAB_WIDTH * indexInGroup);
                int Y = guiY - ScreenTabBase.TAB_HEIGHT + (isTop ? 4 : guiHeight - 4);

                ScreenTabButton tabButton = ScreenTabButton.create(buttonNumber, screenTab, Minecraft.getInstance().player, screen, X, Y);
                pageButtonMap.computeIfAbsent(pageNumber, k -> new ArrayList<>()).add(tabButton);
            });

            int totalPages = pageButtonMap.size();
            if (currentPage >= totalPages) {
                currentPage = totalPages - 1;
            }
            if (currentPage < 0) {
                currentPage = 0;
            }

            List<ScreenTabButton> buttonsToShow = pageButtonMap.getOrDefault(currentPage, Collections.emptyList());
            buttonsToShow.forEach(event::addListener);

            if (totalPages > 1) {
                int navY = guiY + guiHeight + 5;

                if (currentPage > 0) {
                    Button prev = Button.builder(Component.literal("<"), btn -> {
                        currentPage--;
                        Minecraft.getInstance().setScreen(screen);
                    }).bounds(guiX, navY, 20, 20).build();
                    event.addListener(prev);
                }

                if (currentPage < totalPages - 1) {
                    Button next = Button.builder(Component.literal(">"), btn -> {
                        currentPage++;
                        Minecraft.getInstance().setScreen(screen);
                    }).bounds(guiX + guiWidth - 20, navY, 20, 20).build();
                    event.addListener(next);
                }

                Button label = Button.builder(Component.literal("Page " + (currentPage + 1) + "/" + totalPages), btn -> {
                        })
                        .bounds(guiX + guiWidth / 2 - 30, navY, 60, 20).build();
                label.active = false;
                event.addListener(label);
            }
        }
    }
}
