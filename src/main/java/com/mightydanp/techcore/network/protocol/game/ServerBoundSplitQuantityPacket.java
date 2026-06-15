package com.mightydanp.techcore.network.protocol.game;

import com.mightydanp.techcore.network.TCNetworkChannel;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class ServerBoundSplitQuantityPacket {
    public int slotIndex;
    public int clientSlotIndex;
    public int amount;
    public ItemStack cursorStack;

    public ServerBoundSplitQuantityPacket(int slotIndex, int clientSlotIndex, int amount, ItemStack cursorStack) {
        this.slotIndex = slotIndex;
        this.clientSlotIndex = clientSlotIndex;
        this.amount = amount;
        this.cursorStack = cursorStack;
    }

    public static @NotNull ServerBoundSplitQuantityPacket decode(@NotNull FriendlyByteBuf buffer) {
        // Reads the real server slot index.
        int slotIndex = buffer.readInt();

        // Reads the client menu slot index that the result packet should update.
        int clientSlotIndex = buffer.readInt();

        // Reads the amount being split.
        int amount = buffer.readInt();

        // Reads the cursor stack the client is trying to split.
        ItemStack itemStack = buffer.readItem();

        return new ServerBoundSplitQuantityPacket(slotIndex, clientSlotIndex, amount, itemStack);
    }

    private static boolean canMergeQuantityStacks(ItemStack cursorStack, ItemStack slotStack) {
        if (!ItemStack.isSameItem(cursorStack, slotStack)) {
            return false;
        }

        return ItemStack.isSameItemSameTags(withoutQuantity(cursorStack), withoutQuantity(slotStack));
    }

    private static @NotNull ItemStack withoutQuantity(@NotNull ItemStack stack) {
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

    public void encode(@NotNull FriendlyByteBuf buffer) {
        // Writes the real server slot index.
        buffer.writeInt(this.slotIndex);

        // Writes the client menu slot index that the result packet should update.
        buffer.writeInt(this.clientSlotIndex);

        // Writes the amount being split.
        buffer.writeInt(this.amount);

        // Writes the cursor stack the client is trying to split.
        buffer.writeItem(Objects.requireNonNullElse(this.cursorStack, ItemStack.EMPTY));
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player == null) return;
            if (this.amount < 1) return;
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) return;
            Slot slot = player.containerMenu.slots.get(slotIndex);

            if (slot.hasItem()) {
                Quantity currentSlotQty = Quantity.stack(slot.getItem()).get();
                Quantity currentCursorQty = Quantity.stack(cursorStack).get();

                // If either stack does not have quantity data, do not handle the custom split.
                if (currentSlotQty != null && currentCursorQty != null) {
                    if (!canMergeQuantityStacks(cursorStack, slot.getItem())) return;

                    int availableSpace = currentSlotQty.maxQuantity() - currentSlotQty.quantity();

                    if (currentCursorQty.quantity() >= this.amount && availableSpace >= this.amount) {
                        ItemStack cursorCopy = cursorStack.copy();
                        ItemStack slotCopy = slot.getItem().copy();

                        Quantity.stack(slotCopy).set(currentSlotQty.quantity() + this.amount, currentSlotQty.maxQuantity());
                        Quantity.stack(cursorCopy).set(currentCursorQty.quantity() - this.amount, currentCursorQty.maxQuantity());
                        slot.set(slotCopy);
                        slot.setChanged();

                        applyAndUpdateCursor(player, cursorCopy);
                        sendAcceptedResult(player, slotCopy, cursorCopy);
                    }
                }
            } else {
                Quantity currentCursorQty = Quantity.stack(cursorStack).get();

                // If the cursor stack does not have quantity data, do not handle the custom split.
                if (currentCursorQty != null && currentCursorQty.quantity() >= this.amount) {
                    ItemStack cursorCopy = cursorStack.copy();
                    ItemStack slotCopy = cursorStack.copy();

                    Quantity.stack(slotCopy).set(this.amount, currentCursorQty.maxQuantity());
                    Quantity.stack(cursorCopy).set(currentCursorQty.quantity() - this.amount, currentCursorQty.maxQuantity());

                    slot.set(slotCopy);
                    slot.setChanged();

                    applyAndUpdateCursor(player, cursorCopy);
                    sendAcceptedResult(player, slotCopy, cursorCopy);
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

    private void sendAcceptedResult(ServerPlayer player, ItemStack slotCopy, ItemStack cursorCopy) {
        // The client should receive an empty cursor stack when the remaining quantity is zero.
        ItemStack clientCursorStack = cursorCopy;
        Quantity cursorQuantity = Quantity.stack(cursorCopy).get();

        // If the cursor quantity is zero or less, send ItemStack.EMPTY to the client.
        if (cursorQuantity != null && cursorQuantity.quantity() <= 0) {
            clientCursorStack = ItemStack.EMPTY;
        }

        // Sends the server-accepted slot and cursor stacks back to the same player.
        TCNetworkChannel.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ClientBoundSplitQuantityResultPacket(clientSlotIndex, slotCopy, clientCursorStack)
        );
    }

}
