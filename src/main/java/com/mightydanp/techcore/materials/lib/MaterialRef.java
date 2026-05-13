package com.mightydanp.techcore.materials.lib;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;

public class MaterialRef {
    public static String temperature_translatable = CoreRef.MOD_ID+ "." + "temperature";
    public static String quantity_translatable = CoreRef.MOD_ID+ "." + "quantity";
    public static String quality_translatable = CoreRef.MOD_ID+ "." + "quality";
    public static String purity_translatable = CoreRef.MOD_ID + "." + "purity";

    public static String melting_point_translatable = CoreRef.MOD_ID + "." + "melting_point";
    public static String boiling_point_translatable = CoreRef.MOD_ID + "." + "boiling_point";

    public static String gem_state_translatable = CoreRef.MOD_ID + "." + "gem_state";

    public static String chipped_translatable = CoreRef.MOD_ID + "." + "chipped";
    public static String flawed_translatable = CoreRef.MOD_ID + "." + "flawed";
    public static String gem_translatable = CoreRef.MOD_ID + "." + "gem";
    public static String flawless_translatable = CoreRef.MOD_ID + "." + "flawless";
    public static String legendary_translatable = CoreRef.MOD_ID + "." + "legendary";

    //English Translation
    public static String temperature_english_translation = "Temperature";
    public static String quantity_english_translation = "Quantity";
    public static String quality_english_translation = "Quality";
    public static String purity_english_translation = "Purity";

    public static String melting_point_english_translation = "Melting Point";
    public static String boiling_point_english_translation = "Boiling Point";

    public static String gem_state_english_translation = "Gem State";

    public static String chipped_english_translation = "Chipped";
    public static String flawed_english_translation = "Flawed";
    public static String gem_english_translation = "Gem";
    public static String flawless_english_translation = "Flawless";
    public static String legendary_english_translation = "Legendary";

    public static void initLanguages(){
        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, temperature_translatable, temperature_english_translation)
        );
        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, quantity_translatable, quantity_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, quality_translatable, quality_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, purity_translatable, purity_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, melting_point_translatable, melting_point_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, boiling_point_translatable, boiling_point_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, boiling_point_translatable, boiling_point_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, gem_state_translatable, gem_state_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, chipped_translatable, chipped_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, flawed_translatable, flawed_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, gem_translatable, gem_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, flawless_translatable, flawless_english_translation)
        );

        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, legendary_translatable, legendary_english_translation)
        );
    }
}
