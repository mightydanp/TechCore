package com.mightydanp.techcore.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TCInventoryResultSlot extends Slot {
    private final Player player;
    private int removeCount;

    public TCInventoryResultSlot(Player player, int slot, int xPosition, int yPosition) {
        super(player.getInventory(), slot, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
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
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        this.removeCount += numItemsCrafted;
    }

    /*
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.removeCount > 0) {-
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            EventHooks.firePlayerCraftingEvent(this.player, stack, this.craftSlots);
        }

        Container var3 = this.container;
        if (var3 instanceof RecipeCraftingHolder recipecraftingholder) {
            recipecraftingholder.awardUsedRecipes(this.player, this.craftSlots.getItems());
        }

        this.removeCount = 0;
    }
     */

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        this.checkTakeAchievements(stack);
        //CraftingInput.Positioned craftinginput$positioned = this.craftSlots.asPositionedCraftInput();
        //CraftingInput craftinginput = craftinginput$positioned.input();
        //int i = craftinginput$positioned.left();
        //int j = craftinginput$positioned.top();
        //CommonHooks.setCraftingPlayer(player);
        //NonNullList<ItemStack> nonnulllist = player.level().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, craftinginput, player.level());
        //CommonHooks.setCraftingPlayer((Player)null);

        /*
        for(int k = 0; k < craftinginput.height(); ++k) {
            for(int l = 0; l < craftinginput.width(); ++l) {
                int i1 = l + i + (k + j) * this.craftSlots.getWidth();
                ItemStack itemstack = this.craftSlots.getItem(i1);
                ItemStack itemstack1 = (ItemStack)nonnulllist.get(l + k * craftinginput.width());
                if (!itemstack.isEmpty()) {
                    this.craftSlots.removeItem(i1, 1);
                    itemstack = this.craftSlots.getItem(i1);
                }

                if (!itemstack1.isEmpty()) {
                    if (itemstack.isEmpty()) {
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (ItemStack.isSameItemSameComponents(itemstack, itemstack1)) {
                        itemstack1.grow(itemstack.getCount());
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (!this.player.getInventory().add(itemstack1)) {
                        this.player.drop(itemstack1, false);
                    }
                }
            }
        }
        */

    }
}
