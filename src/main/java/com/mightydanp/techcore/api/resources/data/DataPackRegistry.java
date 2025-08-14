package com.mightydanp.techcore.api.resources.data;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataPackRegistry {
    public static DataPack dataPack = new DataPack(CoreRef.MOD_ID, "data", true, Pack.Position.TOP, true);

    public static List<BaseRegistries> init = new ArrayList<>();

    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event) {
        PackType type = event.getPackType();

        RegistriesHandler.MATERIALS.getEntries().forEach(C -> C.get().initClient());

        init.forEach(BaseRegistries::initClient);

        DataPackRegistries.init();

        if (type == PackType.SERVER_DATA) {
            event.addRepositorySource((packConsumer) -> {
                Pack pack = dataPack.createPack();

                packConsumer.accept(pack);
            });
        }
    }
}
