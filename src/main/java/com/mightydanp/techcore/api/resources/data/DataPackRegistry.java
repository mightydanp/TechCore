package com.mightydanp.techcore.api.resources.data;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataPackRegistry {
    public static DataPack dataPack = new DataPack(CoreRef.MOD_ID, "data", true, Pack.Position.TOP, true);
    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event){
        PackType type = event.getPackType();

        DataPackRegistries.init();

        if (type == PackType.SERVER_DATA){
            event.addRepositorySource((packConsumer) -> {
                Pack pack = dataPack.createPack();

                packConsumer.accept(pack);
            });
        }
    }
}
