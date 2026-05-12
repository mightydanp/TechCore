package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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

            RenderType temperatureRenderType = TechCoreRenderTypes.temperatureItem();

            if (bufferSource instanceof MultiBufferSource.BufferSource bufferSourceImpl) {
                flushVanillaItemRenderTypes(bufferSourceImpl, stack, model);
            }

            //scaleAroundItemCenter(poseStack, 1.001F);

            //TechCore.LOGGER.info("Temperature pass rendering {}", stack.getHoverName().getString());

            VertexConsumer baseConsumer = bufferSource.getBuffer(
                    TechCoreRenderTypes.temperatureItem()
            );

            VertexConsumer temperatureConsumer = new TemperatureVertexConsumer(baseConsumer, color, strength);

            ((ItemRendererAccessor) Minecraft.getInstance().getItemRenderer()).techcore$renderModelLists(model, stack, packedLight, packedOverlay, poseStack, temperatureConsumer);

            if (bufferSource instanceof MultiBufferSource.BufferSource bufferSourceImpl) {
                bufferSourceImpl.endBatch(temperatureRenderType);
            }
        } finally {
            poseStack.popPose();
            ItemRenderEventGuard.endExtraPass();
        }
    }

    private static void scaleAroundItemCenter(PoseStack poseStack, float scale) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
    }

    private static void flushVanillaItemRenderTypes(
            MultiBufferSource.BufferSource bufferSource,
            ItemStack stack,
            BakedModel model
    ) {
        /*
         * Flush the common vanilla item render type.
         * This covers most simple items.
         */
        bufferSource.endBatch(ItemBlockRenderTypes.getRenderType(stack, true));

        /*
         * Flush Forge model render passes.
         * This covers models that expose one or more render types.
         */
        for (BakedModel renderPass : model.getRenderPasses(stack, false)) {
            for (RenderType renderType : renderPass.getRenderTypes(stack, false)) {
                bufferSource.endBatch(renderType);
            }
        }
    }
}