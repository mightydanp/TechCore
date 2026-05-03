package com.mightydanp.techcore.api.resources;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcePackRegistry {
    public static final ResourcePack PACK = new ResourcePack(CoreRef.MOD_ID, "dynamic", true, Pack.Position.TOP, true);

    public static List<BaseRegistries> init = new CopyOnWriteArrayList<>();
    private static boolean clientInitialized = false;

    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event) {
        PackType type = event.getPackType();

        if (!clientInitialized) {
            RegistriesHandler.getMaterials().forEach(material -> ResourcePackRegistry.init.add(material.get()));

            //RegistriesHandler.getMaterials().forEach(m -> m.get().initClient());
            init.forEach(BaseRegistries::initClient);
            init.forEach(BaseRegistries::initLanguages);
            clientInitialized = true;
        }

        AssetPackRegistries.init();

        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            event.addRepositorySource(consumer -> consumer.accept(PACK.createAssetPack()));
        } else if (event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource(consumer -> consumer.accept(PACK.createDataPack()));
        }
    }
}