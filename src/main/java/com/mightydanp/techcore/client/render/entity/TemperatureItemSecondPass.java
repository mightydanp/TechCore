package com.mightydanp.techcore.client.render.entity;

import com.mightydanp.techcore.client.event.ItemRenderEventGuard;
import com.mightydanp.techcore.client.render.TemperatureRenderType;
import com.mightydanp.techcore.client.render.TemperatureVertexConsumer;
import com.mightydanp.techcore.mixin.client.ItemRendererAccessor;
import com.mightydanp.techcore.world.item.properties.Temperature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeHelper;
import org.jetbrains.annotations.NotNull;

public final class TemperatureItemSecondPass {
    private TemperatureItemSecondPass() {}

    public static void render(ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model) {
        if (ItemRenderEventGuard.isRenderingExtraPass()) return;

        float strength = TemperatureClientRender.getStrength(stack);

        if (strength <= 0.0F) return;

        Double itemTemperature = Temperature.getTemperature(stack);

        if(itemTemperature == null) return;

        int color = new Temperature(itemTemperature, Temperature.getScale()).getColor(stack);

        ItemRenderEventGuard.startExtraPass();

        poseStack.pushPose();

        try {
            if (stack.hasFoil()) {
                strength *= 0.65F;
            }

            RenderType temperatureRenderType = TemperatureRenderType.temperatureItem();

            if (bufferSource instanceof MultiBufferSource.BufferSource bufferSourceImpl) {
                flushVanillaItemRenderTypes(bufferSourceImpl, stack, model);
            }

            //TechCore.LOGGER.info("Temperature pass rendering {}", stack.getHoverName().getString());

            VertexConsumer baseConsumer = bufferSource.getBuffer(temperatureRenderType);

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

    private static void flushVanillaItemRenderTypes(
            MultiBufferSource.@NotNull BufferSource bufferSource,
            ItemStack stack,
            BakedModel model
    ) {
        /*
         * Flush the common vanilla item render type.
         * This covers most simple items.
         */
        bufferSource.endBatch(RenderTypeHelper.getFallbackItemRenderType(stack, model, true));

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