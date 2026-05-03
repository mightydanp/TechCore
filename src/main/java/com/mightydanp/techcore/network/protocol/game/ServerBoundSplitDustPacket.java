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

            ItemStack cursor = player.containerMenu.getCarried();
            Slot slot = player.containerMenu.slots.get(slotIndex);

            if (slot.hasItem()) return;

            if(cursor.getItem() instanceof DustItem dustItem){
                int currentQuantity = dustItem.getQuantity(cursor);
                if(currentQuantity >= this.amount){
                    ItemStack dustCopy = cursor.copy();

                    dustItem.setQuantity(dustCopy , amount);
                    dustItem.setQuantity(cursor, currentQuantity - amount);

                    slot.set(dustCopy );
                }
            }



        });

        contextSupplier.get().setPacketHandled(true);

    }
}

