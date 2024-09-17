package com.mightydanp.techcore.world.inventory;

import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.mixin.InventoryMixin;
import com.mightydanp.techcore.registries.MenuRegistries;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TCPlayerInventoryMenu extends AbstractContainerMenu {
    public static final Component translation = Component.translatable("menu.title." + CoreRef.MOD_ID + ".inventory");
    public static final int ARMOR_SLOT_START = 0;
    public static final int ARMOR_SLOT_COUNT = 4;
    public static final int ARMOR_SLOT_END = 3;
    public static final int EXTRA_1 = 4;
    public static final int BACK = 5;
    public static final int EXTRA_3 = 6;
    public static final int EXTRA_4 = 7;
    public static final int INV_SLOT_START = 3;
    public static final int INV_SLOT_END = 30;
    public static final int USE_ROW_SLOT_START = 30;
    public static final int USE_ROW_SLOT_END = 39;
    public static final int OFF_HAND_SLOT = 40;
    public static final int MAIN_HAND_SLOT = 41;
    public static final int RESULT_SLOT = 42;
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_HAND_SLOT_LEFT = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "item/empty_hand_slot_left");
    public static final ResourceLocation EMPTY_HAND_SLOT_RIGHT = ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "item/empty_hand_slot_right");
    private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS;
    private static final EquipmentSlot[] SLOT_IDS;
    private final ResultContainer resultSlots = new ResultContainer();
    //public final boolean active;
    private final Player player;

    private List<String> inventorySecretary = new ArrayList<>();

    public TCPlayerInventoryMenu(int windowId, Inventory playerInventory) {
        super(MenuRegistries.TC_PLAYER_INVENTORY_MENU.get(), windowId);
        this.player = playerInventory.player;
        int i1;
        int j1;

        for(i1 = 0; i1 < 4; ++i1) {
            EquipmentSlot equipmentslot = SLOT_IDS[i1];
            ResourceLocation resourcelocation = TEXTURE_EMPTY_SLOTS.get(equipmentslot);
            this.addSlot(equipmentslot.getName(), new TCArmorSlot(playerInventory, player, equipmentslot, 39 - i1, 8, 8 + i1 * 18, resourcelocation));
        }

        for(i1 = 0; i1 < 3; ++i1) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot("inventory", new Slot(playerInventory, j1 + (i1 + 1) * 9, 8 + j1 * 18, 84 + i1 * 18));
            }
        }

        for(i1 = 0; i1 < 9; ++i1) {
            this.addSlot("hotbar", new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }

        String mainArm = player.getMainArm().getSerializedName();

        int leftX = (mainArm.equals("right") ? 98 : 116);
        int rightX = (mainArm.equals("right") ? 116 : 98);

        this.addSlot("off_hand", new Slot(playerInventory, 40, leftX, 62) {
            public void setByPlayer(@NotNull ItemStack newItem, @NotNull ItemStack oldItem) {
                player.onEquipItem(mainArm.equals("right") ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND, oldItem, newItem);
                super.setByPlayer(newItem, oldItem);
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(TCPlayerInventoryMenu.BLOCK_ATLAS, mainArm.equals("right") ? TCPlayerInventoryMenu.EMPTY_HAND_SLOT_LEFT : TCPlayerInventoryMenu.EMPTY_HAND_SLOT_RIGHT);
            }
        });

        this.addSlot("main_hand", new TCMainHandSlot(playerInventory.player, 42, rightX, 62) {
            public void setByPlayer(@NotNull ItemStack newItem, @NotNull ItemStack oldItem) {
                player.onEquipItem(mainArm.equals("right") ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, oldItem, newItem);
                super.setByPlayer(newItem, oldItem);
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(TCPlayerInventoryMenu.BLOCK_ATLAS, mainArm.equals("right") ? TCPlayerInventoryMenu.EMPTY_HAND_SLOT_RIGHT : TCPlayerInventoryMenu.EMPTY_HAND_SLOT_LEFT);
            }
        });

        this.addSlot("extra_1", new NullSlot(playerInventory.player, 41, 24, 8));
        this.addSlot("back", new BackSlotButton(playerInventory.player, 42, 24, 8));
        this.addSlot("extra_3", new NullSlot(playerInventory.player, 43, 24, 8));
        this.addSlot("extra_4", new NullSlot(playerInventory.player, 44, 24, 8));

        this.addSlot("result", new TCInventoryResultSlot(playerInventory.player, 45, 152, 8));
    }

    protected @NotNull Slot addSlot(String name, @NotNull Slot slot) {
        inventorySecretary.add(slot.getSlotIndex() + " : " + name);
        Collections.sort(inventorySecretary);
        return super.addSlot(slot);
    }

    public static boolean isHotbarSlot(int index) {
        return index >= 36 && index < 45 || index == 45;
    }

    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        //this.craftSlots.fillStackedContents(itemHelper);
    }

    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        //this.craftSlots.clearContent();
    }

    public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
        //return ((CraftingRecipe)recipe.value()).matches(this.craftSlots.asCraftInput(), this.owner.level());
        return false;
    }

    public void slotsChanged(@NotNull Container inventory) {
        //CraftingMenu.slotChangedCraftingGrid(this, this.owner.level(), this.owner, this.craftSlots, this.resultSlots, (RecipeHolder)null);
    }

    public void removed(@NotNull Player player) {
        super.removed(player);
        this.resultSlots.clearContent();
        if (!player.level().isClientSide) {
            //this.clearContainer(player, this.craftSlots);
        }

    }

    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        TechCore.LOGGER.info(String.valueOf(index));
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            EquipmentSlot equipmentslot = player.getEquipmentSlotForItem(itemstack);
            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 4, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }  else if (index >= 0 && index < 3) {
                if (!this.moveItemStackTo(itemstack1, 4, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !((Slot)this.slots.get(3 - equipmentslot.getIndex())).hasItem()) {
                int i = 3 - equipmentslot.getIndex();
                if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot == EquipmentSlot.OFFHAND && !((Slot)this.slots.get(40)).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 40, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 4 && index < 31) {
                if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 31 && index < 40) {
                if (!this.moveItemStackTo(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY, itemstack);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getSize() {
        return 5;
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }

    static {
        TEXTURE_EMPTY_SLOTS = Map.of(EquipmentSlot.FEET, EMPTY_ARMOR_SLOT_BOOTS, EquipmentSlot.LEGS, EMPTY_ARMOR_SLOT_LEGGINGS, EquipmentSlot.CHEST, EMPTY_ARMOR_SLOT_CHESTPLATE, EquipmentSlot.HEAD, EMPTY_ARMOR_SLOT_HELMET);
        SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    }
}