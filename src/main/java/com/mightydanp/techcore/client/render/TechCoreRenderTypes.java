package com.mightydanp.techcore.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.inventory.InventoryMenu;

public final class TechCoreRenderTypes extends RenderType {
    private static ShaderInstance temperatureItemShader;

    private static final ShaderStateShard TEMPERATURE_ITEM_SHADER =
            new ShaderStateShard(() -> temperatureItemShader);

    private TechCoreRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static void setTemperatureItemShader(ShaderInstance shader) {
        temperatureItemShader = shader;
    }

    public static RenderType temperatureItem() {
        CompositeState state = CompositeState.builder()
                .setShaderState(TEMPERATURE_ITEM_SHADER)
                .setTextureState(new TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, true))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(NO_OVERLAY)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(true);

        return create(
                "techcore_temperature_item",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                state
        );
    }
}