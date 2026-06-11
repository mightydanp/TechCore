package com.mightydanp.techcore.api.resources.assets;

import com.google.gson.JsonObject;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AssetPackRegistries {
    public static Map<ResourceLocation, BlockStateContent<?>> blockState = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, BlockModelContent<?>> blockModel = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, ItemModelContent<?>> itemModel = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, LanguageContent> language = new ConcurrentHashMap<>();

    public static void init() {
        addAsset(blockState,
                location -> "blockstates/" + location.getPath() + ".json",
                BlockStateContent::json
        );

        addAsset(blockModel,
                (location, content) ->
                        "models/" + content.getModelType() + "/" + organizationPath(content.getOrganizationPath()) + location.getPath() + ".json",
                BlockModelContent::json
        );

        addAsset(itemModel,
                (location, content) ->
                        "models/" + content.getModelType() + "/" + organizationPath(content.getOrganizationPath()) + cleanPath(location) + ".json",
                ItemModelContent::json
        );

        addAsset(language,
                location -> "lang/" + location.getPath() + ".json",
                LanguageContent::json
        );
    }

    public static boolean saveBlockState(@NotNull BlockStateContent<?> content, boolean override) {
        return save(blockState, location(content.modid(), content.name()), content, override);
    }

    public static BlockStateContent<?> getBlockState(ResourceLocation name) {
        return get(blockState, name, () -> new BlockStateContent<>(name));
    }

    public static BlockStateContent<?> getBlockState(String modid, String name) {
        return getBlockState(location(modid, name));
    }

    public static boolean saveBlockModel(@NotNull BlockModelContent<?> content, boolean override) {
        return save(blockModel, location(content.modid(), content.name()), content, override);
    }

    public static BlockModelContent<?> getBlockModel(ResourceLocation name) {
        return get(blockModel, name, () -> new BlockModelContent<>(name, ""));
    }

    public static BlockModelContent<?> getBlockModel(String modid, String name) {
        return getBlockModel(location(modid, name));
    }

    public static boolean saveItemModel(@NotNull ItemModelContent<?> content, boolean override) {
        return save(itemModel, location(content.modid(), content.name()), content, override);
    }

    public static ItemModelContent<?> getItemModel(ResourceLocation name) {
        return get(itemModel, name, () -> new ItemModelContent<>(name, ""));
    }

    public static ItemModelContent<?> getItemModel(String modid, String name) {
        return getItemModel(location(modid, name));
    }

    public static boolean saveLanguage(@NotNull LanguageContent content, boolean override) {
        ResourceLocation location = location(content.modid(), content.name());

        if (!override && language.containsKey(location)) {
            getLanguage(location).addToTranslations(content.json());
            return true;
        }

        language.put(location, content);
        return true;
    }

    public static LanguageContent getLanguage(ResourceLocation name) {
        return get(language, name, () -> new LanguageContent(name));
    }

    public static LanguageContent getLanguage(String modid, String name) {
        return getLanguage(location(modid, name));
    }

    // Multiple Single Language Translations
    public static void saveMSLT(boolean override, LanguageContent.translation... translations) {
        Arrays.stream(translations).forEach(translation -> {
            LanguageContent savedTranslations = getLanguage(
                    location(translation.modID(), translation.language())
            );

            savedTranslations.addTranslation(
                    translation.translatable(),
                    translation.translation()
            );

            saveLanguage(savedTranslations, override);
        });
    }

    // Multiple Single Language Translations
    public static void safetyMSLT(boolean override, Object object, LanguageContent.translation... translations) {
        if (object != null) {
            saveMSLT(override, translations);
        }
    }

    public static void registerSafetyLanguage(Supplier<Item> item, String modid, String language, String folder, String name) {
        registerSafetyLanguage(
                item,
                modid,
                language,
                folder,
                name,
                LanguageContent.toDisplayName(name)
        );
    }

    public static void registerSafetyLanguage(Supplier<Item> item, String modid, String language, String folder, String name, String translatedName) {
        safetyMSLT(
                false,
                item,
                translation(modid, language, folder, name, translatedName)
        );
    }

    public static void registerLanguage(String modid, String language, String folder, String name) {
        registerLanguage(
                modid,
                language,
                folder,
                name,
                LanguageContent.toDisplayName(name)
        );
    }

    public static void registerLanguage(String modid, String language, String folder, String name, String translatedName) {
        saveMSLT(
                false,
                translation(modid, language, folder, name, translatedName)
        );
    }

    private static LanguageContent.translation translation(String modid, String language, String folder, String name, String translatedName) {
        return new LanguageContent.translation(
                modid,
                language,
                folder + "." + modid + "." + name,
                translatedName
        );
    }

    private static ResourceLocation location(String modid, String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    private static <T> boolean save(Map<ResourceLocation, T> map, ResourceLocation location, T content, boolean override) {
        if (!override && map.containsKey(location)) {
            return false;
        }

        map.put(location, content);
        return true;
    }

    private static <T> T get(Map<ResourceLocation, T> map, ResourceLocation location, Supplier<T> fallback) {
        return map.getOrDefault(location, fallback.get());
    }

    private static <T> void addAsset(Map<ResourceLocation, T> map, Function<ResourceLocation, String> pathBuilder, Function<T, JsonObject> jsonGetter) {
        map.forEach((location, content) -> ResourcePackRegistry.PACK.addAsset(
                ResourceLocation.fromNamespaceAndPath(
                        location.getNamespace(),
                        pathBuilder.apply(location)
                ),
                jsonGetter.apply(content)
        ));
    }

    private static <T> void addAsset(Map<ResourceLocation, T> map, BiFunction<ResourceLocation, T, String> pathBuilder, Function<T, JsonObject> jsonGetter) {
        map.forEach((location, content) -> ResourcePackRegistry.PACK.addAsset(
                ResourceLocation.fromNamespaceAndPath(
                        location.getNamespace(),
                        pathBuilder.apply(location, content)
                ),
                jsonGetter.apply(content)
        ));
    }

    private static String organizationPath(String path) {
        return path == null || path.isBlank() ? "" : path + "/";
    }

    private static String cleanPath(ResourceLocation location) {
        String path = location.getPath();
        return path.contains(":") ? path.split(":", 2)[1] : path;
    }
}