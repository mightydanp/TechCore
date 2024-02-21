package com.mightydanp.techcore.api.resources.assets;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

//@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AssetsPackRegistry {
    /*
    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event){
        PackType type = event.getPackType();

        if (type == PackType.CLIENT_RESOURCES){
            event.addRepositorySource((packConsumer, constructor) -> {
                Pack pack = Pack.create(CoreRef.MOD_ID + ":assets", true, ()-> TechAscension.assetHolder, constructor, Pack.Position.TOP, PackSource.DEFAULT);
                packConsumer.accept(pack);
            });
        }
    }

     */
}
