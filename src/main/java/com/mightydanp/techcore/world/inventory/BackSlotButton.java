package com.mightydanp.techcore.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackSlotButton extends Slot {
    public BackSlotButton(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return false;
    }
}
