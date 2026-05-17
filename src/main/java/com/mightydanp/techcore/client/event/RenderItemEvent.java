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
 * During is fired after vanilla item model rendering and before the render tail.
 * Post is fired at the tail of item rendering.
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
    private final ItemRenderPassPhase renderPassPhase;

    protected RenderItemEvent(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            BakedModel model,
            ItemRenderPassPhase renderPassPhase
    ) {
        this.stack = stack;
        this.displayContext = displayContext;
        this.leftHanded = leftHanded;
        this.poseStack = poseStack;
        this.bufferSource = bufferSource;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
        this.model = model;
        this.renderPassPhase = renderPassPhase;
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

    public ItemRenderPassPhase getRenderPassPhase() {
        return this.renderPassPhase;
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
            super(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model, ItemRenderPassPhase.BEFORE_EFFECTS);
        }
    }

    public static class During extends RenderItemEvent {
        public During(
                ItemStack stack,
                ItemDisplayContext displayContext,
                boolean leftHanded,
                PoseStack poseStack,
                MultiBufferSource bufferSource,
                int packedLight,
                int packedOverlay,
                BakedModel model
        ) {
            super(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model, ItemRenderPassPhase.EFFECTS);
        }
    }

    public static class Post extends RenderItemEvent {
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
            super(stack, displayContext, leftHanded, poseStack, bufferSource, packedLight, packedOverlay, model, ItemRenderPassPhase.AFTER_EFFECTS);
        }
    }
}
