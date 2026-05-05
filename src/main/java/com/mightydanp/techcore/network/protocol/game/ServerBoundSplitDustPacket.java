package com.mightydanp.techcore.network.protocol.game;

import com.mightydanp.techcore.materials.Item.DustItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundSplitDustPacket {
    public int slotIndex;
    public int amount;

    public ServerBoundSplitDustPacket(int slotIndex, int amount) {
        this.slotIndex = slotIndex;
        this.amount = amount;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.slotIndex);
        buffer.writeInt(this.amount);
    }

    public static ServerBoundSplitDustPacket decode(FriendlyByteBuf buffer) {
        int slotIndex = buffer.readInt();
        int amount  = buffer.readInt();

        return new ServerBoundSplitDustPacket(slotIndex, amount);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player == null) return;
            if (this.amount < 1) return;

            ItemStack cursor = player.containerMenu.getCarried();
            Slot slot = player.containerMenu.slots.get(slotIndex);

            if (slot.hasItem()){
                String curserName = cursor.getHoverName().getString();
                String hoveredName = slot.getItem().getHoverName().getString();

                if(!curserName.equals(hoveredName)) return;

                if (slot.getItem().getItem() instanceof DustItem slotDust && cursor.getItem() instanceof DustItem cursorDust){
                    int currentSlotQty = slotDust.getQuantity(slot.getItem());
                    int currentCursorQty = cursorDust.getQuantity(cursor);
                    int availableSpace = slotDust.getMaxQuantity() - currentSlotQty;

                    if (currentCursorQty >= this.amount && availableSpace >= this.amount) {
                        ItemStack cursorDustCopy = cursor.copy();
                        ItemStack slotDustCopy = slot.getItem().copy();

                        if (this.amount <= currentCursorQty && this.amount <= availableSpace) {
                            slotDust.setQuantity(slotDustCopy, currentSlotQty + this.amount);
                            cursorDust.setQuantity(cursorDustCopy, currentCursorQty - this.amount);
                            slot.set(slotDustCopy);

                            applyAndUpdateCursor(player, cursorDust, cursorDustCopy);
                        }
                    }
                }
            } else {
                if (cursor.getItem() instanceof DustItem cursorDust) {
                    int currentCursorQty = cursorDust.getQuantity(cursor);
                    if (currentCursorQty >= this.amount) {
                        ItemStack cursorDustCopy = cursor.copy();
                        ItemStack slotDustCopy = cursor.copy();

                        cursorDust.setQuantity(slotDustCopy, this.amount);
                        cursorDust.setQuantity(cursorDustCopy, currentCursorQty - this.amount);

                        slot.set(slotDustCopy);

                        applyAndUpdateCursor(player, cursorDust, cursorDustCopy);
                    }
                }
            }
        });

        contextSupplier.get().setPacketHandled(true);
    }

    private void applyAndUpdateCursor(ServerPlayer player, DustItem cursorDust, ItemStack cursorDustCopy) {
        if (cursorDust.getQuantity(cursorDustCopy) <= 0) {
            player.containerMenu.setCarried(ItemStack.EMPTY);
        } else {
            player.containerMenu.setCarried(cursorDustCopy);
        }
        player.containerMenu.broadcastChanges();
    }
}

