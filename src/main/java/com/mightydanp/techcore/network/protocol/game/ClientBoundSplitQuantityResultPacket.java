package com.mightydanp.techcore.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientBoundSplitQuantityResultPacket {
    public int clientSlotIndex;
    public ItemStack slotStack;
    public ItemStack cursorStack;

    public ClientBoundSplitQuantityResultPacket(int clientSlotIndex, ItemStack slotStack, ItemStack cursorStack) {
        this.clientSlotIndex = clientSlotIndex;
        this.slotStack = slotStack;
        this.cursorStack = cursorStack;
    }

    public void encode(@NotNull FriendlyByteBuf buffer) {
        // Writes the client menu slot index that should be updated.
        buffer.writeInt(this.clientSlotIndex);

        // Writes the server-accepted stack for the target slot.
        buffer.writeItem(Objects.requireNonNullElse(this.slotStack, ItemStack.EMPTY));

        // Writes the server-accepted stack for the cursor.
        buffer.writeItem(Objects.requireNonNullElse(this.cursorStack, ItemStack.EMPTY));
    }

    public static @NotNull ClientBoundSplitQuantityResultPacket decode(@NotNull FriendlyByteBuf buffer) {
        // Reads the client menu slot index that should be updated.
        int clientSlotIndex = buffer.readInt();

        // Reads the server-accepted stack for the target slot.
        ItemStack slotStack = buffer.readItem();

        // Reads the server-accepted stack for the cursor.
        ItemStack cursorStack = buffer.readItem();

        return new ClientBoundSplitQuantityResultPacket(clientSlotIndex, slotStack, cursorStack);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    // Gets the client player. If there is no player, there is no open menu to update.
                    net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
                    if (player == null) return;

                    // Gets the currently open client menu.
                    net.minecraft.world.inventory.AbstractContainerMenu menu = player.containerMenu;

                    // If the slot index is not in the current client menu, do not update the cursor either.
                    if (clientSlotIndex < 0 || clientSlotIndex >= menu.slots.size()) return;

                    // Gets the client slot that matched the original hovered slot.
                    net.minecraft.world.inventory.Slot slot = menu.slots.get(clientSlotIndex);

                    // Sets the slot to the stack accepted by the server.
                    slot.set(slotStack);

                    // Marks the slot as changed so the screen refreshes the itemstack.
                    slot.setChanged();

                    // Sets the cursor to the stack accepted by the server.
                    menu.setCarried(cursorStack);
                })
        );

        contextSupplier.get().setPacketHandled(true);
    }
}
