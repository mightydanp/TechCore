package com.mightydanp.techcore.data.traits;

import com.google.gson.JsonElement;
import com.mightydanp.techcore.traits.Trait;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class TraitProvider<A extends Trait<A>> implements DataProvider {
    private final PackOutput.PathProvider provider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modId;
    private final Map<ResourceLocation, A> builders = new HashMap<>();
    private final ExistingFileHelper fileHelper;

    private final ResourceKey<? extends Registry<A>> resourceKey;

    public TraitProvider(PackOutput output, String modId, ResourceKey<? extends Registry<A>> resourceKey, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        this.modId = modId;
        this.resourceKey = resourceKey;
        this.fileHelper = fileHelper;
        this.registries = registries;
        this.provider = output.createPathProvider(PackOutput.Target.DATA_PACK, TraitProvider.getDir(resourceKey));
    }

    public abstract void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper);

    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput output) {
        return this.registries.thenCompose((p_255484_) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(p_255484_, this.fileHelper);
            this.builders.forEach((quest, builder) -> {
                Path path = this.provider.json(quest);
                JsonElement jsonelement = builder.codec()
                        .encodeStart(JsonOps.INSTANCE, builder)
                        .getOrThrow(false, errorMsg -> {
                            throw new IllegalStateException("Failed to encode builder: " + errorMsg);
                        });

                list.add(DataProvider.saveStable(output, jsonelement, path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final A register(ResourceLocation id, A trait) {
        return this.builders.computeIfAbsent(id, (k) -> trait);
    }

    @Override
    public @NotNull String getName() {
        return modId + " " + resourceKey.location().getPath() + " traits";
    }

    public static String getDir(ResourceKey<? extends Registry<?>> resourceKey) {
        return "traits/" + resourceKey.location().getNamespace();
    }
}