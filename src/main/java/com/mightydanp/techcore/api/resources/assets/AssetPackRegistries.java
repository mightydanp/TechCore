package com.mightydanp.techcore.api.resources.assets;

import com.mightydanp.techcore.api.resources.ResourcePackRegistry;
import com.mightydanp.techcore.api.resources.assets.content.blockstate.BlockStateContent;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.content.model.block.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.ItemModelContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class AssetPackRegistries {
    public static Map<ResourceLocation, BlockStateContent<?>> blockState = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, BlockModelContent<?>> blockModel = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, ItemModelContent<?>> itemModel = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, LanguageContent> language = new ConcurrentHashMap<>();

    public static void init() {
        blockState.forEach((r, b) -> ResourcePackRegistry.PACK.addAsset(ResourceLocation.fromNamespaceAndPath(
                r.getNamespace(),
                "blockstates/" + r.getPath() + ".json"),
                b.json()));
        blockModel.forEach((r, b) -> ResourcePackRegistry.PACK.addAsset(ResourceLocation.fromNamespaceAndPath(
                r.getNamespace(),
                "models/" + b.getModelType() + "/" +
                        (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/") +
                        r.getPath() + ".json"),
                b.json()));
        itemModel.forEach((r, b) -> ResourcePackRegistry.PACK.addAsset(ResourceLocation.fromNamespaceAndPath(
                r.getNamespace(),
                "models/" + b.getModelType() + "/" +
                        (b.getOrganizationPath() == null ? "" : b.getOrganizationPath() + "/") +
                        (r.getPath().contains(":") ? r.getPath().split(":")[1] : r.getPath()) +
                        ".json"),
                b.json()));
        language.forEach((r, b) -> ResourcePackRegistry.PACK.addAsset(ResourceLocation.fromNamespaceAndPath(
                r.getNamespace(),
                "lang/" + r.getPath() + ".json"),
                b.json()));
    }

    public static boolean saveBlockState(@NotNull BlockStateContent<?> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && AssetPackRegistries.blockState.containsKey(resourceLocation)) {
            return false;
        }

        AssetPackRegistries.blockState.put(resourceLocation, content);

        return true;
    }

    public static BlockStateContent<?> getBlockState(ResourceLocation name) {
        return blockState.getOrDefault(name, new BlockStateContent<>(name));
    }

    public static BlockStateContent<?> getBlockState(String modid, String name) {
        return blockState.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new BlockStateContent<>(modid, name));
    }

    public static boolean saveBlockModel(@NotNull BlockModelContent<?> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && AssetPackRegistries.blockModel.containsKey(resourceLocation)) {
            return false;
        }

        AssetPackRegistries.blockModel.put(resourceLocation, content);
        return true;
    }

    public static BlockModelContent<?> getBlockModel(ResourceLocation name) {
        return blockModel.getOrDefault(name, new BlockModelContent<>(name, ""));
    }

    public static BlockModelContent<?> getBlockModel(String modid, String name) {
        return blockModel.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new BlockModelContent<>(modid, name, ""));
    }

    public static boolean saveItemModel(@NotNull ItemModelContent<?> content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && AssetPackRegistries.itemModel.containsKey(resourceLocation)) {
            return false;
        }

        AssetPackRegistries.itemModel.put(resourceLocation, content);
        return true;
    }

    public static ItemModelContent<?> getItemModel(ResourceLocation name) {
        return itemModel.getOrDefault(name, new ItemModelContent<>(name, ""));
    }

    public static ItemModelContent<?> getItemModel(String modid, String name) {
        return itemModel.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new ItemModelContent<>(modid, name, ""));
    }

    public static boolean saveLanguage(@NotNull LanguageContent content, boolean override) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(content.modid(), content.name());

        if (!override && AssetPackRegistries.language.containsKey(resourceLocation)) {
            LanguageContent existingLanguage = getLanguage(content.modid(), content.name());
            content.addToTranslations(content.json());
        }

        AssetPackRegistries.language.put(resourceLocation, content);
        return true;
    }

    public static LanguageContent getLanguage(ResourceLocation name) {
        return language.getOrDefault(name, new LanguageContent(name));
    }

    public static LanguageContent getLanguage(String modid, String name) {
        return language.getOrDefault(ResourceLocation.fromNamespaceAndPath(modid, name), new LanguageContent(modid, name));
    }

    //Multiple Single Language Translations
    public static void saveMSLT(boolean override, LanguageContent.translation... translations) {
        Arrays.stream(translations).forEach(translation -> {
            LanguageContent savedTranslations = getLanguage(ResourceLocation.fromNamespaceAndPath(translation.modID(), translation.language()));
            savedTranslations.addTranslation(translation.translatable(), translation.translation());
            saveLanguage(savedTranslations, false);
        });
    }

    //Multiple Single Language Translations
    public static void safetyMSLT(boolean override, Object object, LanguageContent.translation... translations){
        if(object != null) {
            AssetPackRegistries.saveMSLT(override, translations);
        }
    }

    public static void registerSafetyLanguage(Supplier<Item> item, String modid, String language, String folder, String name) {
        registerSafetyLanguage(item, modid, language, folder, name, LanguageContent.toDisplayName(name));
    }

    public static void registerSafetyLanguage(Supplier<Item> item, String modid, String language, String folder, String name, String translatedName) {
        AssetPackRegistries.safetyMSLT(false, item,
                new LanguageContent.translation(modid, language,
                        folder + "." + modid + "." + name, translatedName)
        );
    }

    public static void registerLanguage(String modid, String language, String folder, String name) {
        registerLanguage(modid, language, folder, name, LanguageContent.toDisplayName(name));
    }

    public static void registerLanguage(String modid, String language, String folder, String name, String translatedName) {
        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(modid, language,
                        folder + "." + modid + "." + name, translatedName)
        );
    }
}