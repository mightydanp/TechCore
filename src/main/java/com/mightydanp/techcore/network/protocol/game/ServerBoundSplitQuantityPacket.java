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
        int amount = buffer.readInt();
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

            if (slot.hasItem()) {
                if (slot.getItem().getItem() instanceof MaterialItem && cursorStack.getItem() instanceof MaterialItem) {

                    if (!canMergeQuantityStacks(cursorStack, slot.getItem())) return;


                    Quantity currentSlotQty = Quantity.stack(slot.getItem()).get();
                    Quantity currentCursorQty = Quantity.stack(cursorStack).get();

                    if (currentSlotQty != null && currentCursorQty != null) {
                        int availableSpace = currentSlotQty.maxQuantity() - currentSlotQty.quantity();

                        if (currentCursorQty.quantity() >= this.amount && availableSpace >= this.amount) {
                            ItemStack cursorCopy = cursorStack.copy();
                            ItemStack slotCopy = slot.getItem().copy();

                            Quantity.stack(slotCopy).set(currentSlotQty.quantity() + this.amount, currentSlotQty.maxQuantity());
                            Quantity.stack(cursorCopy).set(currentCursorQty.quantity() - this.amount, currentCursorQty.maxQuantity());
                            slot.set(slotCopy);

                            applyAndUpdateCursor(player, cursorCopy);
                        }
                    }
                }
            } else {
                if (cursorStack.getItem() instanceof MaterialItem) {
                    Quantity currentCursorQty = Quantity.stack(cursorStack).get();

                    if (currentCursorQty != null && currentCursorQty.quantity() >= this.amount) {
                        ItemStack cursorCopy = cursorStack.copy();
                        ItemStack slotCopy = cursorStack.copy();

                        Quantity.stack(slotCopy).set(this.amount, currentCursorQty.maxQuantity());
                        Quantity.stack(cursorCopy).set(currentCursorQty.quantity() - this.amount, currentCursorQty.maxQuantity());

                        slot.set(slotCopy);

                        applyAndUpdateCursor(player, cursorCopy);
                    }
                }
            }
        });

        contextSupplier.get().setPacketHandled(true);
    }

    private void applyAndUpdateCursor(ServerPlayer player, ItemStack cursorCopy) {
        Quantity copyQuantity = Quantity.stack(cursorCopy).get();

        if (copyQuantity != null) {
            if (copyQuantity.quantity() <= 0) {
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
            tag.remove(Quantity.MAX_TAG);

            if (tag.isEmpty()) {
                copy.setTag(null);
            }
        }

        return copy;
    }

}
