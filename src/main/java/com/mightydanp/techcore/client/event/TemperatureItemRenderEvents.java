package com.mightydanp.techcore.client.event;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.client.render.entity.TemperatureClientRender;
import com.mightydanp.techcore.client.render.entity.TemperatureItemSecondPass;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = CoreRef.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class TemperatureItemRenderEvents {
    private TemperatureItemRenderEvents() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRenderItemDuring(RenderItemEvent.During event) {
        ItemStack stack = event.getStack();

        if (!TemperatureClientRender.shouldRender(stack, event.getDisplayContext())) {
            return;
        }

        BakedModel model = event.getModel();

        // Important compatibility guard.
        // Special renderers may not be represented correctly by rendering baked quads again.
        if (model.isCustomRenderer()) {
            return;
        }

        TemperatureItemSecondPass.render(
                stack,
                event.getDisplayContext(),
                event.isLeftHanded(),
                event.getPoseStack(),
                event.getBufferSource(),
                event.getPackedLight(),
                event.getPackedOverlay(),
                model
        );
    }
}
