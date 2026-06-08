package com.mightydanp.techcore.mixin.client;

import com.mightydanp.techcore.client.event.RenderItemEvent;
import com.mightydanp.techcore.client.event.ItemRenderEventGuard;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    static {
        System.out.println("TECHCORE ItemRendererMixin CLASS LOADED");
    }

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void preRenderItem(@NotNull ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Pre(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model));

        //System.out.println("TECHCORE ItemRendererMixin PRE HIT: " + stack.getHoverName().getString());
    }

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void duringRenderItem(@NotNull ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.During(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model));
    }

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("TAIL")
    )
    private void postRenderItem(@NotNull ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Post(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model));
    }
}
