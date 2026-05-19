package com.mightydanp.techcore.network.protocol.game;

import com.mightydanp.techcore.materials.Item.MaterialItem;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ServerBoundSplitQuantityPacket {
    public int slotIndex;
    public int amount;
    public ItemStack cursorStack;

    public ServerBoundSplitQuantityPacket(int slotIndex, int amount, ItemStack cursorStack) {
        this.slotIndex = slotIndex;
        this.amount = amount;
        this.cursorStack = cursorStack;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.slotIndex);
        buffer.writeInt(this.amount);
        buffer.writeItem(Objects.requireNonNullElse(this.cursorStack, ItemStack.EMPTY));
    }

    public static ServerBoundSplitQuantityPacket decode(FriendlyByteBuf buffer) {
        int slotIndex = buffer.readInt();
        int amount  = buffer.readInt();
        ItemStack itemStack = buffer.readItem();

        return new ServerBoundSplitQuantityPacket(slotIndex, amount, itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player == null) return;
            if (this.amount < 1) return;
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) return;
            Slot slot = player.containerMenu.slots.get(slotIndex);

            if (slot.hasItem()){
                if (slot.getItem().getItem() instanceof MaterialItem slotMaterial && cursorStack.getItem() instanceof MaterialItem cursorMaterial){

                    if (!canMergeQuantityStacks(cursorStack, slot.getItem())) return;


                    Integer currentSlotQty = Quantity.getQuantity(slot.getItem());
                    Integer currentCursorQty = Quantity.getQuantity(cursorStack);

                    if(currentSlotQty != null && currentCursorQty != null) {
                        int availableSpace = slotMaterial.getMaxQuantity() - currentSlotQty;

                        if (currentCursorQty >= this.amount && availableSpace >= this.amount) {
                            ItemStack cursorCopy = cursorStack.copy();
                            ItemStack slotCopy = slot.getItem().copy();

                            if (this.amount <= currentCursorQty && this.amount <= availableSpace) {
                                Quantity.setQuantity(slotCopy, currentSlotQty + this.amount);
                                Quantity.setQuantity(cursorCopy, currentCursorQty - this.amount);
                                slot.set(slotCopy);

                                applyAndUpdateCursor(player, cursorCopy);
                            }
                        }
                    }
                }
            } else {
                if (cursorStack.getItem() instanceof MaterialItem cursorMaterial) {
                    Integer currentCursorQty = Quantity.getQuantity(cursorStack);

                    if (currentCursorQty != null && currentCursorQty >= this.amount) {
                        ItemStack cursorCopy = cursorStack.copy();
                        ItemStack slotCopy = cursorStack.copy();

                        Quantity.setQuantity(slotCopy, this.amount);
                        Quantity.setQuantity(cursorCopy, currentCursorQty - this.amount);

                        slot.set(slotCopy);

                        applyAndUpdateCursor(player, cursorCopy);
                    }
                }
            }
        });

        contextSupplier.get().setPacketHandled(true);
    }

    private void applyAndUpdateCursor(ServerPlayer player, ItemStack cursorCopy) {
        Integer copyQuantity = Quantity.getQuantity(cursorCopy);

        if (copyQuantity != null) {
            if (copyQuantity <= 0) {
                player.containerMenu.setCarried(ItemStack.EMPTY);
            } else {
                player.containerMenu.setCarried(cursorCopy);
            }
            player.containerMenu.broadcastChanges();
        }
    }

    private static boolean canMergeQuantityStacks(ItemStack cursorStack, ItemStack slotStack) {
        if (!ItemStack.isSameItem(cursorStack, slotStack)) {
            return false;
        }

        return ItemStack.isSameItemSameTags(withoutQuantity(cursorStack), withoutQuantity(slotStack));
    }

    private static ItemStack withoutQuantity(ItemStack stack) {
        ItemStack copy = stack.copy();
        CompoundTag tag = copy.getTag();

        if (tag != null) {
            tag.remove(Quantity.TAG);

            if (tag.isEmpty()) {
                copy.setTag(null);
            }
        }

        return copy;
    }

}
