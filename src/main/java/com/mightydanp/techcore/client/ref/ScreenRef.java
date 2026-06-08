package com.mightydanp.techcore.client.ref;

import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;

import java.util.List;

public class ScreenRef {
    public static final String folder = "screen";

    public static final String quantity_split_screen = "quantity_split";
    public static final String quantity_split_amount = "quantity_split_amount";

    public static final String quantity_split_screen_translatable =
            LanguageContent.grabTranslatable(folder, quantity_split_screen);
    public static final String quantity_split_amount_translatable =
            LanguageContent.grabTranslatable(folder, quantity_split_amount);

    private static final List<String> ENGLISH_TRANSLATIONS = List.of(
            quantity_split_screen, quantity_split_amount
    );

    public static void initLanguages() {
        for (String name : ENGLISH_TRANSLATIONS) {
            AssetPackRegistries.registerLanguage(CoreRef.MOD_ID, LanguageCodes.english, folder, name);
        }
    }

}
