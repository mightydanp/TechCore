package com.mightydanp.techcore.data.data;

import com.mightydanp.techcore.api.traits.block.BlockTrait;
import com.mightydanp.techcore.api.traits.item.ItemTrait;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CoreRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    }

    public static void addListeners(@NotNull AddReloadListenerEvent event) {
        event.addListener(BlockTrait.getInstance());
        event.addListener(ItemTrait.getInstance());
    }
}
