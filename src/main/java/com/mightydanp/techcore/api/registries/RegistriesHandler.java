package com.mightydanp.techcore.api.registries;

import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RegistriesHandler {
    private static final Map<ResourceLocation, registryHolder> additionalRegistries = new HashMap<>();
    private static final Map<ResourceLocation, finalizedRegistryHolder> finalizedDeferredRegister = new HashMap<>();


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CoreRef.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, CoreRef.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(Registries.ITEM, CoreRef.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CoreRef.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CoreRef.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, CoreRef.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, CoreRef.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, CoreRef.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CoreRef.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CoreRef.MOD_ID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CoreRef.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CoreRef.MOD_ID);

    public static final ResourceKey<Registry<WoodType>> WOOD_TYPE_KEY = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("wood_type"));
    public static final DeferredRegister<WoodType> WOOD_TYPE = DeferredRegister.create(WOOD_TYPE_KEY, CoreRef.MOD_ID);

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        FLUIDS.register(bus);
        MENU_TYPES.register(bus);
        ENTITY_TYPES.register(bus);
        FEATURES.register(bus);
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        ATTACHMENT_TYPE.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        WOOD_TYPE.register(bus);

        additionalRegistries.forEach((resourceLocation, registryHolder) -> {
            additionalRegistries.remove(resourceLocation);

            ResourceKey<Registry<Object>> key = ResourceKey.createRegistryKey(resourceLocation);
            DeferredRegister<?> register = DeferredRegister.create(registryHolder.key(), resourceLocation.getNamespace());

            register.register(bus);
            finalizedDeferredRegister.put(resourceLocation, new finalizedRegistryHolder(key, register));
        });
    }

    public void addDeferredRegister(ResourceLocation name, registryHolder holder){
        additionalRegistries.put(name, holder);
    }

    public finalizedRegistryHolder getDeferredRegister(ResourceLocation name){
        if(!additionalRegistries.containsKey(name)){
            return finalizedDeferredRegister.get(name);
        }

        throw new Error("DeferredRegister : " + name.toString() + " : was not registered!");
    }



    public record registryHolder(Registry<?> key){}

    public record finalizedRegistryHolder(ResourceKey<Registry<Object>> resourceKey, DeferredRegister<?> registry){}
}
