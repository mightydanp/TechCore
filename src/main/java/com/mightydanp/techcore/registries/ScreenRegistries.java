package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.contents.language.LanguageContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.client.ref.ScreenRef;

public class ScreenRegistries implements BaseRegistries {

    @Override
    public void init() {}

    @Override
    public void initClient() {}

    @Override
    public void initLanguages() {
        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, ScreenRef.quantity_split_screen, ScreenRef.quantity_split_screen_english_translation));
        AssetPackRegistries.saveMSLT(false,
                new LanguageContent.translation(CoreRef.MOD_ID, LanguageCodes.english, ScreenRef.quantity_split_amount, ScreenRef.amount_english_translation + " : "));

    }
}
