package com.mightydanp.techcore.api.data;

import com.mightydanp.techcore.api.traits.block.BlockTrait;
import com.mightydanp.techcore.api.traits.item.ItemTrait;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    public static List<PreparableReloadListener> listeners = new ArrayList<>();
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


    }

    @SubscribeEvent
    public static void addListeners(AddReloadListenerEvent event){
        event.addListener(BlockTrait.getInstance());
        event.addListener(ItemTrait.getInstance());

        listeners.forEach(event::addListener);
    }
}
