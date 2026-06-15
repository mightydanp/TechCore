package com.mightydanp.techcore.api.resources.assets.content.model.block;

import com.google.gson.JsonObject;
import com.mightydanp.techcore.api.resources.assets.content.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BlockModelBuilder<A> extends ModelBuilder<A> {

    public BlockModelBuilder(ResourceLocation location) {
        super(location);
    }

    public BlockModelBuilder(ResourceLocation location, @Nullable A parentRef) {
        super(location, parentRef);
    }

    public JsonObject toJson() {
        return super.toJson();
    }
}