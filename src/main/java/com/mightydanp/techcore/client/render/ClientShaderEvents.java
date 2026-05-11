package com.mightydanp.techcore.client.render;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import java.io.IOException;

@Mod.EventBusSubscriber(
        modid = CoreRef.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class ClientShaderEvents {
    private ClientShaderEvents() {}

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "rendertype_temperature_item"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                TechCoreRenderTypes::setTemperatureItemShader
        );
    }
}