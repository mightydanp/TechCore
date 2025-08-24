package com.mightydanp.techcore;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = "techcore")
public final class DebugReloadHook {

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager rm, ProfilerFiller pf) { return null; }

            @Override
            protected void apply(Void v, ResourceManager rm, ProfilerFiller pf) {
                // A) Is our pack selected?
                Minecraft.getInstance().getResourcePackRepository().getSelectedPacks()
                        .forEach(p -> com.mightydanp.techcore.TechCore.LOGGER
                                .info("[DEBUG] Selected pack: id={}, title={}", p.getId(), p.getTitle().getString()));

                // B) Can ResourceManager see our lang file(s)?
                Map<ResourceLocation, Resource> langs =
                        rm.listResources("lang", name -> name.getPath().endsWith(".json"));
                langs.keySet().forEach(rl ->
                        com.mightydanp.techcore.TechCore.LOGGER.info("[DEBUG] lang resource visible: {}", rl));

                ResourceLocation id = ResourceLocation.fromNamespaceAndPath("techcore", "lang/en_us.json");
                boolean present = rm.getResource(id).isPresent();
                com.mightydanp.techcore.TechCore.LOGGER.info("[DEBUG] lang/en_us.json present? {}", present);

                // C) Does the translation resolve right now?
                String translated = Component.translatable("tab.techcore.player_inventory").getString();
                com.mightydanp.techcore.TechCore.LOGGER.info("[DEBUG] translate(tab.techcore.player_inventory) -> {}", translated);
            }
        });
    }
}
