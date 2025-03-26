package com.mightydanp.techcore.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TCMainHandSlot extends Slot {
    private final Player player;
    private int removeCount;

    public TCMainHandSlot(Player player, int slot, int xPosition, int yPosition) {
        super(player.getInventory(), slot, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return player.getMainHandItem();
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }



    @Override
    public boolean isFake() {
        return true;
    }
}