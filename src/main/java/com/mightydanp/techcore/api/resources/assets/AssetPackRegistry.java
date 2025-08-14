package com.mightydanp.techcore.api.resources.assets;

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
public class AssetPackRegistry {
    public static AssetPack assetPack = new AssetPack(CoreRef.MOD_ID, "assets", true, Pack.Position.TOP, true);

    public static List<BaseRegistries> init = new ArrayList<>();

    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event) {
        PackType type = event.getPackType();

        init.forEach(BaseRegistries::initClient);

        AssetPackRegistries.init();

        if (type == PackType.CLIENT_RESOURCES) {
            event.addRepositorySource((packConsumer) -> {

                Pack pack = assetPack.createPack();

                packConsumer.accept(pack);
            });
        }
    }
}
