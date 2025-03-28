package com.mightydanp.techcore.data.event;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.gui.screens.inventory.TCPlayerInventoryScreen;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.world.inventory.TCPlayerInventoryMenu;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TCInventoryEvent {
    public static KeyMapping tcInventoryKey = new KeyMapping("key."+ CoreRef.MOD_ID +".inventory", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, InputConstants.KEY_E, KeyMapping.CATEGORY_INVENTORY);


    public static int extra1 = 1;

    public static int back = 2;

    public static int extra3 = 3;

    public static int extra4 = 4;

    public static int offhand = 5;

    public static Map<String, Integer> tcInventorySlotList = Map.of(
            "off_hand", offhand,
            "extra_1", extra1,
            "back", back,
            "extra_3", extra3,
            "extra_4", extra4
    );



    private static final Supplier<AttachmentType<ItemStackHandler>> tcInventorySlots = RegistriesHandler.ATTACHMENT_TYPE.register(
            "tc_inventory_slots", () -> AttachmentType.serializable(() -> new ItemStackHandler(tcInventorySlotList.size())).build());

    private static final ResourceLocation PLAYER_GUI_TEXTURES = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID,"textures/gui/container/inventory.png");
    private static final Function<IItemHandler, Slot> SLOT_CREATOR = i -> new SlotItemHandler(i, 0, 8, 54);

    public static void addSlotServerSide(PlayerContainerEvent.Open event) {
        AbstractContainerMenu container = event.getContainer();
        if (container instanceof TCPlayerInventoryMenu inventoryMenu) {
            Player player = event.getEntity();

            ItemStackHandler handler = player.getData(tcInventorySlots);

            //addSlotToPlayerContainer((InventoryMenu) container, handler, SLOT_CREATOR);
        }
    }

    public static void addSlotClientSide(ScreenEvent.Opening event) {
        Screen screen = Minecraft.getInstance().screen;

        if (screen instanceof TCPlayerInventoryScreen && screen.getMinecraft().player != null) {
            LocalPlayer player = screen.getMinecraft().player;
            IItemHandler itemHandler = player.getData(tcInventorySlots);

            //addSlotToPlayerContainer(((InventoryScreen) screen), itemHandler, SLOT_CREATOR);
        }
    }

    /*


    private static void addSlotToPlayerContainer(InventoryMenu container, IItemHandler handler, Function<IItemHandler, Slot> slotCreator) {
        try {
            Method methodAddSlot = Container.class.getDeclaredMethod("addSlot", Slot.class);
            methodAddSlot.setAccessible(true);
            methodAddSlot.invoke(container, slotCreator.apply(handler));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private static Player getPlayerFromField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            return (Player) ReflectionUtil.getFieldValue(field, instance);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return null;
    }
 */

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        // Key binding stuff
        event.register(tcInventoryKey);
        //NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> onClientTick(e));
    }

    public static void onTick(PlayerTickEvent.Post event) {
        while(tcInventoryKey.consumeClick()) {
            MenuProvider menu = new SimpleMenuProvider((windowID, inventory, player) -> new TCPlayerInventoryMenu(windowID, inventory), TCPlayerInventoryMenu.translation);

            if(event.getEntity() instanceof ServerPlayer player){
                if(player.containerMenu instanceof TCPlayerInventoryMenu){
                    event.getEntity().openMenu(null);
                }else {
                     event.getEntity().openMenu(menu);
                }
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
                    //event.getEntity().openMenu(menu);
            }
    }

}
