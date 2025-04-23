package com.mightydanp.techcore.api.resources.data;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataPackRegistry {
    public static DataPack dataPack = new DataPack(CoreRef.MOD_ID, "data", true, Pack.Position.TOP, true);

    public static List<BaseRegistries> init = new ArrayList<>();

    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event){
        PackType type = event.getPackType();

        init.forEach(BaseRegistries::initResource);

        DataPackRegistries.init();

        if (type == PackType.SERVER_DATA){
            event.addRepositorySource((packConsumer) -> {
                Pack pack = dataPack.createPack();

                packConsumer.accept(pack);
            });
        }
    }
}
