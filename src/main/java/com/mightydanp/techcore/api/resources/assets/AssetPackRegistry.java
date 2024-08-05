package com.mightydanp.techcore.api.resources.assets;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AssetPackRegistry {
    public static AssetPack assetPack = new AssetPack(CoreRef.MOD_ID, "assets", true, Pack.Position.TOP, true);
    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event){
        PackType type = event.getPackType();

        AssetPackRegistries.init();

        if (type == PackType.CLIENT_RESOURCES){
            event.addRepositorySource((packConsumer) -> {

                Pack pack = assetPack.createPack();

                packConsumer.accept(pack);
            });
        }
    }
}
