package com.mightydanp.techcore.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NullSlot extends Slot {
    private final Player player;
    private int removeCount;

    public NullSlot(Player player, int slot, int xPosition, int yPosition) {
        super(player.getInventory(), slot, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
