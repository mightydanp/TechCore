package com.mightydanp.techcore.api.resources.assets.content.material;

import com.mightydanp.techcore.client.ref.CoreRef;

public class MaterialResources {
    public static final MaterialTextureResolver TEXTURES = new MaterialTextureResolver();

    private static boolean indexed = false;

    public static void init() {
        if (indexed) {
            return;
        }

        TEXTURES.indexNamespace(CoreRef.MOD_ID);

        indexed = true;
    }
}
