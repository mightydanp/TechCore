package com.mightydanp.techcore.mixin.client;

import com.mightydanp.techcore.client.event.RenderItemEvent;
import com.mightydanp.techcore.client.render.ItemRenderEventGuard;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void preRenderItem(ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Pre(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void postRenderItem(ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Post(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model
        ));
    }
}
