package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperatureItemSecondPass {
    private TemperatureItemSecondPass() {}

    public static void render(ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model) {
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        float strength = TemperatureClientRender.getStrength(stack);

        if (strength <= 0.0F) return;

        int color = TemperatureClientRender.getColor(stack);

        ItemRenderEventGuard.startExtraPass();

        poseStack.pushPose();

        try {
            if (stack.hasFoil()) {
                strength *= 0.65F;
            }

            VertexConsumer baseConsumer = bufferSource.getBuffer(
                    TechCoreRenderTypes.temperatureItem()
            );

            VertexConsumer temperatureConsumer = new TemperatureVertexConsumer(baseConsumer, color, strength);

            ((ItemRendererAccessor) Minecraft.getInstance().getItemRenderer()).techcore$renderModelLists(model, stack, packedLight, packedOverlay, poseStack, temperatureConsumer);
        } finally {
            poseStack.popPose();
            ItemRenderEventGuard.endExtraPass();
        }
    }
}