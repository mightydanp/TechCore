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
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistriesHandler {
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

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CoreRef.MOD_ID);

    public static final ResourceKey<Registry<WoodType>> CREATIVE_MODE_TAB = ResourceKey.createRegistryKey(new ResourceLocation("wood_type"));

    public static void init(IEventBus IEventBus) {
        ITEMS.register(IEventBus);
        BLOCKS.register(IEventBus);
        BLOCK_ITEMS.register(IEventBus);
        BLOCK_ENTITIES.register(IEventBus);
        FLUIDS.register(IEventBus);
        MENU_TYPES.register(IEventBus);
        ENTITY_TYPES.register(IEventBus);
        FEATURES.register(IEventBus);
        RECIPE_TYPES.register(IEventBus);
        RECIPE_SERIALIZERS.register(IEventBus);
        CREATIVE_MODE_TABS.register(IEventBus);
    }
}
