package com.mightydanp.techcore.api.resources.assets;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AssetPackRegistry {
    public static AssetHolder assetHolder = new AssetHolder();
    @SubscribeEvent
    public static void addResourcePack(AddPackFindersEvent event){
        PackType type = event.getPackType();
        if (type == PackType.CLIENT_RESOURCES){
            event.addRepositorySource((packConsumer) -> {
                Component desc = Component.literal(CoreRef.MOD_ID + ":assets");

                Pack.ResourcesSupplier resourcesSupplier = BuiltInPackSource.fixedResources(assetHolder);

                Pack pack = readMetaAndCreatePack(CoreRef.MOD_ID, desc, true, resourcesSupplier, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT);

                //Pack pack = Pack.create(CoreRef.MOD_ID, desc, true, resourcesSupplier, new Pack.Info(
                //        desc,
                //        PackCompatibility.COMPATIBLE,
                //        FeatureFlagSet.of(),
                //        List.of(),
                //        true
                //), Pack.Position.TOP, false, PackSource.DEFAULT);
                packConsumer.accept(pack);
            });
        }
    }

    @Nullable
    public static Pack readMetaAndCreatePack(
            String p_249649_,
            Component p_248632_,
            boolean p_251594_,
            Pack.ResourcesSupplier p_252210_,
            PackType p_250595_,
            Pack.Position p_248706_,
            PackSource p_251233_
    ) {
        int i = SharedConstants.getCurrentVersion().getPackVersion(p_250595_);
        Pack.Info pack$info = Pack.readPackInfo(p_249649_, p_252210_, i);
        return pack$info != null ? Pack.create(p_249649_, p_248632_, p_251594_, p_252210_, pack$info, p_248706_, false, p_251233_) : null;
    }
}
