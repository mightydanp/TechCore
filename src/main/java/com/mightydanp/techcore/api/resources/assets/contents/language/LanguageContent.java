package com.mightydanp.techcore.api.resources.assets.contents.language;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.TechCore;
import net.minecraft.resources.ResourceLocation;

public class LanguageContent {
    private final String modid;
    private final String name;
    private final JsonObject translations = new JsonObject();

    public LanguageContent(ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    public LanguageContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
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

    public LanguageContent addToTranslations(JsonObject json) {
        json.asMap().forEach((modid, translation) ->{
            if (!translations.has(name)) {
                translations.add(modid, translation);
            }
        });
        return this;
    }

    public static String translateUpperCase(String name) {
        if (name.isEmpty()) return "";
        StringBuilder translatedName = new StringBuilder();
        if (name.contains("_")) {
            int i = 0;
            String[] words = name.split("_");
            for (String word : words) {
                if (word.isEmpty()) continue;
                String firstChar = word.substring(0, 1).toUpperCase();
                String remainder = word.substring(1);
                String str = firstChar + remainder;
                if (i == 0) {
                    translatedName.append(str);
                    i++;
                } else {
                    translatedName.append(" ").append(str);
                }
            }
        } else {
            String firstChar = name.substring(0, 1).toUpperCase();
            String remainder = name.substring(1);
            String str = firstChar + remainder;

            if (str.contains(":")) {
                String[] parts = str.split(":");
                if (parts.length > 1) {
                    translatedName.append(parts[1]);
                }
            } else {
                translatedName.append(str);
            }
        }

        return translatedName.toString();
    }

    public record translation(String modID, String language, String translatable, String translation){}
}