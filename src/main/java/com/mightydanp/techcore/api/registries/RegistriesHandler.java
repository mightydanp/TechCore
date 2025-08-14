package com.mightydanp.techcore.api.registries;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Material;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.HashMap;
import java.util.Map;


@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
    //public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CoreRef.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CoreRef.MOD_ID);

    //public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CoreRef.MOD_ID);

    public static final ResourceKey<Registry<Material>> MATERIAL_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "material"));
    public static final DeferredRegister<Material> MATERIALS = DeferredRegister.create(MATERIAL_KEY, CoreRef.MOD_ID);

    public static final ResourceKey<Registry<WoodType>> WOOD_TYPE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "wood_type"));
    public static final DeferredRegister<WoodType> WOOD_TYPES = DeferredRegister.create(WOOD_TYPE_KEY, CoreRef.MOD_ID);


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
        //ATTACHMENT_TYPE.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        //DATA_COMPONENT_TYPES.register(bus);
        MATERIALS.register(bus);
        WOOD_TYPES.register(bus);

        additionalRegistries.forEach((resourceLocation, registryHolder) -> {
            additionalRegistries.remove(resourceLocation);

            ResourceKey<Registry<Object>> key = ResourceKey.createRegistryKey(resourceLocation);
            DeferredRegister<?> register = DeferredRegister.create(registryHolder.key().key(), resourceLocation.getNamespace());

            register.register(bus);
            finalizedDeferredRegister.put(resourceLocation, new finalizedRegistryHolder(key, register));
        });
    }

    public void addDeferredRegister(ResourceLocation name, registryHolder holder) {
        additionalRegistries.put(name, holder);
    }

    public finalizedRegistryHolder getDeferredRegister(ResourceLocation name) {
        if (!additionalRegistries.containsKey(name)) {
            return finalizedDeferredRegister.get(name);
        }

        throw new Error("DeferredRegister : " + name.toString() + " : was not registered!");
    }

    public record registryHolder(Registry<?> key) {
    }

    public record finalizedRegistryHolder(ResourceKey<Registry<Object>> resourceKey, DeferredRegister<?> registry) {
    }
}
