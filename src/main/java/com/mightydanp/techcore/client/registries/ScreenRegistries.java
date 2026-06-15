package com.mightydanp.techcore.client.registries;

import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.client.ref.ScreenRef;

public class ScreenRegistries implements BaseRegistries<ScreenRegistries> {

    @Override
    public ScreenRegistries init() {
        return this;
    }

    @Override
    public ScreenRegistries initClient() {
        return this;
    }

    @Override
    public ScreenRegistries initLanguages() {
        ScreenRef.initLanguages();
        return this;
    }

}
