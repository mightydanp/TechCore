package com.mightydanp.techcore.client.ref;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;

import java.util.List;

public class CreativeTabsRef {
    public static final String folder = "creative_tab";

    public static final String block_tab = "block_tab";
    public static final String item_tab = "item_tab";
    public static final String ore_tab = "ore_tab";
    public static final String plant_tab = "plant_tab";
    public static final String gem_tab = "gem_tab";
    public static final String ore_products_tab = "ore_products_tab";
    public static final String tool_parts_tab = "tool_parts_tab";
    public static final String tool_tab = "tool_tab";
    public static final String tree_tab = "tree_tab";
    public static final String fluid_tab = "fluid_tab";
    public static final String stone_layer_tab = "stone_layer_tab";

    public static final String block_tab_translatable = LanguageContent.grabTranslatable(folder, block_tab);
    public static final String item_tab_translatable = LanguageContent.grabTranslatable(folder, item_tab);
    public static final String ore_tab_translatable = LanguageContent.grabTranslatable(folder, ore_tab);
    public static final String plant_tab_translatable = LanguageContent.grabTranslatable(folder, plant_tab);
    public static final String gem_tab_translatable = LanguageContent.grabTranslatable(folder, gem_tab);
    public static final String ore_products_tab_translatable = LanguageContent.grabTranslatable(folder, ore_products_tab);
    public static final String tool_parts_tab_translatable = LanguageContent.grabTranslatable(folder, tool_parts_tab);
    public static final String tool_tab_translatable = LanguageContent.grabTranslatable(folder, tool_tab);
    public static final String tree_tab_translatable = LanguageContent.grabTranslatable(folder, tree_tab);
    public static final String fluid_tab_translatable = LanguageContent.grabTranslatable(folder, fluid_tab);
    public static final String stone_layer_tab_translatable = LanguageContent.grabTranslatable(folder, stone_layer_tab);

    private static final List<String> ENGLISH_TRANSLATIONS = List.of(
            "block_tab", "item_tab", "ore_tab", "plant_tab", "gem_tab",
            "ore_products_tab", "tool_parts_tab", "tool_tab", "tree_tab",
            "fluid_tab", "stone_layer_tab"
    );

    public static void initLanguages() {
        for (String name : ENGLISH_TRANSLATIONS) {
            AssetPackRegistries.registerLanguage(CoreRef.MOD_ID, LanguageCodes.english, folder, name);
        }
    }
}