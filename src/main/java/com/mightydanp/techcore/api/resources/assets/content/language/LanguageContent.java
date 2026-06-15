package com.mightydanp.techcore.api.resources.assets.content.language;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.TechCore;
import com.mightydanp.techcore.client.ref.CoreRef;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LanguageContent {
    private final String modid;
    private final String name;
    private final JsonObject translations = new JsonObject();

    public LanguageContent(@NotNull ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    public LanguageContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public static String grabTranslatable(String folder, String translatable) {
        return folder + "." + CoreRef.MOD_ID + "." + translatable;
    }

    public static @NotNull String toDisplayName(@NotNull String input) {
        if (input.isEmpty()) return input;

        return Arrays.stream(input.split("_+"))
                .filter(s -> !s.isEmpty())
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public String modid() {
        return modid;
    }

    public String name() {
        return name;
    }

    public JsonObject json() {
        return translations;
    }

    public LanguageContent addTranslation(String name, String translation) {
        if (!translations.has(name)) {
            translations.addProperty(name, translation);
        } else {
            TechCore.LOGGER.warn("Cannot add translation, [{}], because the localization, [{}], already exist for {}", translation, name, name);
        }
        return this;
    }

    public LanguageContent addToTranslations(@NotNull JsonObject json) {
        json.asMap().forEach((modid, translation) -> {
            if (!translations.has(name)) {
                translations.add(modid, translation);
            }
        });
        return this;
    }

    public record translation(String modID, String language, String translatable, String translation) {
    }
}