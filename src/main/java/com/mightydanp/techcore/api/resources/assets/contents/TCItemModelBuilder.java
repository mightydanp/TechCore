package com.mightydanp.techcore.api.resources.assets.contents;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TCItemModelBuilder extends TCModelBuilder<TCItemModelBuilder> {
    private final List<OverrideBuilder> overrides = new ArrayList<>();

    public TCItemModelBuilder(ResourceLocation location) {
        super(location);
    }

    public OverrideBuilder override() {
        OverrideBuilder ret = new OverrideBuilder();
        overrides.add(ret);
        return ret;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = super.toJson();
        if (!overrides.isEmpty()) {
            JsonArray overridesJson = new JsonArray();
            overrides.stream().map(OverrideBuilder::toJson).forEach(overridesJson::add);
            root.add("overrides", overridesJson);
        }
        return root;
    }

    public class OverrideBuilder {
        private ModelFile model;
        private final Map<ResourceLocation, Float> predicates = new LinkedHashMap<>();

        public OverrideBuilder model(ModelFile model) {
            this.model = model;
            return this;
        }

        public OverrideBuilder predicate(ResourceLocation key, float value) {
            predicates.put(key, value);
            return this;
        }

        public TCItemModelBuilder end() {
            return TCItemModelBuilder.this;
        }

        JsonObject toJson() {
            JsonObject ret = new JsonObject();
            JsonObject predicatesJson = new JsonObject();
            predicates.forEach((key, val) -> predicatesJson.addProperty(key.toString(), val));
            ret.add("predicate", predicatesJson);
            ret.addProperty("model", model.getLocation().toString());
            return ret;
        }
    }
}
