package com.mightydanp.techcore.materials.lib;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;

import java.util.List;

public class MaterialRef {
    public static final String folder = "material";

    public static final String temperature = "temperature";
    public static final String quantity = "quantity";
    public static final String quality = "quality";
    public static final String purity = "purity";
    public static final String processed_stage = "process_stage";
    public static final String melting_point = "melting_point";
    public static final String boiling_point = "boiling_point";
    public static final String impurities = "impurities";
    public static final String gem_state = "gem_state";
    public static final String chipped = "chipped";
    public static final String flawed = "flawed";
    public static final String gem = "gem";
    public static final String flawless = "flawless";
    public static final String legendary = "legendary";
    public static final String temperature_translatable = LanguageContent.grabTranslatable(folder, temperature);
    public static final String quantity_translatable = LanguageContent.grabTranslatable(folder, quantity);
    public static final String quality_translatable = LanguageContent.grabTranslatable(folder, quality);
    public static final String purity_translatable = LanguageContent.grabTranslatable(folder, purity);
    public static final String processed_stage_translatable = LanguageContent.grabTranslatable(folder, processed_stage);
    public static final String melting_point_translatable = LanguageContent.grabTranslatable(folder, melting_point);
    public static final String boiling_point_translatable = LanguageContent.grabTranslatable(folder, boiling_point);
    public static final String impurities_translatable = LanguageContent.grabTranslatable(folder, impurities);
    public static final String gem_state_translatable = LanguageContent.grabTranslatable(folder, gem_state);
    public static final String chipped_translatable = LanguageContent.grabTranslatable(folder, chipped);
    public static final String flawed_translatable = LanguageContent.grabTranslatable(folder, flawed);
    public static final String gem_translatable = LanguageContent.grabTranslatable(folder, gem);
    public static final String flawless_translatable = LanguageContent.grabTranslatable(folder, flawless);
    public static final String legendary_translatable = LanguageContent.grabTranslatable(folder, legendary);
    private static final List<String> english_Translations = List.of(
            temperature, quantity, quality, purity, processed_stage, melting_point, boiling_point, impurities, gem_state, chipped, flawed, gem, flawless, legendary
    );

    public static void initLanguages() {
        for (String name : english_Translations) {
            AssetPackRegistries.registerLanguage(CoreRef.MOD_ID, LanguageCodes.english, folder, name);
        }
    }
}
    

