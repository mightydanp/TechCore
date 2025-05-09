package com.mightydanp.techcore.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackSlotButton extends Slot {
    public final Player player;

    public BackSlotButton(Player player, int slot, int x, int y) {
        super(player.getInventory(), slot, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }
}
