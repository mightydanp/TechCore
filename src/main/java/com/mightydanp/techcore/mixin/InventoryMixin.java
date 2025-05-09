package com.mightydanp.techcore.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Inventory.class)
public abstract class InventoryMixin implements Container, Nameable {

    @Unique
    public final int EXTRA_1 = 41;

    @Unique
    public final int SLOT_BACK = 42;

    @Unique
    public final int EXTRA_3 = 43;

    @Unique
    public final int EXTRA_4 = 44;

    @Final
    @Shadow
    public NonNullList<ItemStack> items;

    @Final
    @Shadow
    public NonNullList<ItemStack> armor;
    @Final
    @Shadow
    public NonNullList<ItemStack> offhand;

    @Unique
    public NonNullList<ItemStack> tech_Ascension_Workspace$extra1;

    @Unique
    public NonNullList<ItemStack> tech_Ascension_Workspace$back;

    @Unique
    public NonNullList<ItemStack> tech_Ascension_Workspace$extra3;

    @Unique
    public NonNullList<ItemStack> tech_Ascension_Workspace$extra4;


    @Mutable
    @Final
    @Shadow
    private List<NonNullList<ItemStack>> compartments;

    @Final
    @Shadow
    public Player player;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void Inventory(Player player, CallbackInfo ci) {
        compartments = ImmutableList.of(this.items, this.armor, this.offhand, tech_Ascension_Workspace$back);
    }

    @Inject(method = "save", at = @At("TAIL"), cancellable = true)
    public void save(ListTag listTag, CallbackInfoReturnable<ListTag> cir) {
        for (int k = 0; k < this.tech_Ascension_Workspace$extra1.size(); ++k) {
            if (!this.tech_Ascension_Workspace$extra1.get(k).isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte) (k + 200));
                listTag.add(this.tech_Ascension_Workspace$extra1.get(k).save(this.player.registryAccess(), compoundTag2));
                listTag.add(compoundTag2);
            }
        }
        for (int k = 0; k < this.tech_Ascension_Workspace$back.size(); ++k) {
            if (!this.tech_Ascension_Workspace$back.get(k).isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte) (k + 201));
                listTag.add(this.tech_Ascension_Workspace$back.get(k).save(this.player.registryAccess(), compoundTag2));
                listTag.add(compoundTag2);
            }
        }
        for (int k = 0; k < this.tech_Ascension_Workspace$extra3.size(); ++k) {
            if (!this.tech_Ascension_Workspace$extra3.get(k).isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte) (k + 202));
                listTag.add(this.tech_Ascension_Workspace$extra3.get(k).save(this.player.registryAccess(), compoundTag2));
                listTag.add(compoundTag2);
            }
        }
        for (int k = 0; k < this.tech_Ascension_Workspace$extra4.size(); ++k) {
            if (!this.tech_Ascension_Workspace$extra4.get(k).isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte) (k + 203));
                listTag.add(this.tech_Ascension_Workspace$extra4.get(k).save(this.player.registryAccess(), compoundTag2));
                listTag.add(compoundTag2);
            }
        }

        cir.setReturnValue(listTag);
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void load(ListTag listTag, CallbackInfo ci) {
        this.tech_Ascension_Workspace$extra1.clear();

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.player.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            if (!itemstack.isEmpty()) {
                if (j >= 200 && j < this.tech_Ascension_Workspace$extra1.size() + 200) {
                    this.tech_Ascension_Workspace$extra1.set(j - 200, itemstack);
                }
            }
        }

        this.tech_Ascension_Workspace$back.clear();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.player.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            if (!itemstack.isEmpty()) {
                if (j >= 201 && j < this.tech_Ascension_Workspace$back.size() + 201) {
                    this.tech_Ascension_Workspace$back.set(j - 201, itemstack);
                }
            }
        }

        this.tech_Ascension_Workspace$extra3.clear();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.player.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            if (!itemstack.isEmpty()) {
                if (j >= 202 && j < this.tech_Ascension_Workspace$extra3.size() + 202) {
                    this.tech_Ascension_Workspace$extra3.set(j - 202, itemstack);
                }
            }
        }

        this.tech_Ascension_Workspace$extra4.clear();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.player.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            if (!itemstack.isEmpty()) {
                if (j >= 203 && j < this.tech_Ascension_Workspace$extra4.size() + 203) {
                    this.tech_Ascension_Workspace$extra4.set(j - 203, itemstack);
                }
            }
        }

    }

    @Inject(method = "getContainerSize", at = @At("TAIL"), cancellable = true)
    public void getContainerSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + tech_Ascension_Workspace$extra1.size() + tech_Ascension_Workspace$back.size() + tech_Ascension_Workspace$extra3.size() + tech_Ascension_Workspace$extra4.size());
    }

    @Inject(method = "isEmpty", at = @At("TAIL"), cancellable = true)
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack itemstack : this.tech_Ascension_Workspace$extra1) {
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(false);
            }
        }
        for (ItemStack itemstack : this.tech_Ascension_Workspace$back) {
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(false);
            }
        }
        for (ItemStack itemstack : this.tech_Ascension_Workspace$extra3) {
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(false);
            }
        }
        for (ItemStack itemstack : this.tech_Ascension_Workspace$extra4) {
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(false);
            }
        }
    }

}

