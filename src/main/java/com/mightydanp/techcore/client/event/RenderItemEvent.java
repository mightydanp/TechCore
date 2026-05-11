package com.mightydanp.techcore.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired from ItemRenderer#render on the Forge event bus.
 * Pre is fired before vanilla item model rendering.
 * Post is fired after vanilla item model rendering.
 * This event is intended for additional rendering only.
 * Subscribers should not mutate the ItemStack, replace the model, or flush the
 * provided MultiBufferSource.
 * Subscribers that modify the PoseStack must push/pop their changes.
 * Subscribers should not call ItemRenderer#render from this event unless they
 * guard against recursion.
 */
public abstract class RenderItemEvent extends Event {
    private final ItemStack stack;
    private final ItemDisplayContext displayContext;
    private final boolean leftHanded;
    private final PoseStack poseStack;
    private final MultiBufferSource bufferSource;
    private final int packedLight;
    private final int packedOverlay;
    private final BakedModel model;

    protected RenderItemEvent(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            BakedModel model
    ) {
        this.stack = stack;
        this.displayContext = displayContext;
        this.leftHanded = leftHanded;
        this.poseStack = poseStack;
        this.bufferSource = bufferSource;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
        this.model = model;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public ItemDisplayContext getDisplayContext() {
        return this.displayContext;
    }

    public boolean isLeftHanded() {
        return this.leftHanded;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getBufferSource() {
        return this.bufferSource;
    }

    public int getPackedLight() {
        return this.packedLight;
    }

    public int getPackedOverlay() {
        return this.packedOverlay;
    }

    public BakedModel getModel() {
        return this.model;
    }

    public static class Pre extends RenderItemEvent {
        public Pre(
                ItemStack stack,
                ItemDisplayContext displayContext,
                boolean leftHanded,
                PoseStack poseStack,
                MultiBufferSource bufferSource,
                int packedLight,
                int packedOverlay,
                BakedModel model
        ) {
            super(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model);
        }
    }

    public static class Post extends RenderItemEvent {
        //private final ItemRenderPassPhase phase;

        public Post(
                ItemStack stack,
                ItemDisplayContext displayContext,
                boolean leftHanded,
                PoseStack poseStack,
                MultiBufferSource bufferSource,
                int packedLight,
                int packedOverlay,
                BakedModel model
        ) {
            super(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model);
        }
    }
}