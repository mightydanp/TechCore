package com.mightydanp.techcore.mixin;

import com.mightydanp.techcore.world.inventory.BackSlotButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingContainer> {

    public InventoryMenuMixin(MenuType<?> p_40115_, int p_40116_) {
        super(p_40115_, p_40116_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void InventoryMenu(Inventory inventory, boolean p_39707_, Player player, CallbackInfo ci){
        this.addSlot(new BackSlotButton(inventory, 41, 134, 62));
    }
}
