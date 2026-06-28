package com.mightydanp.techcore.registries;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.BaseRegistries;
import com.mightydanp.techcore.world.level.levelgen.vein.VeinFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.densenode.DenseNodeVeinFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsehalo.SparseHaloVeinFeature;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition.SparseTransitionVeinFeature;
import net.minecraftforge.registries.RegistryObject;

public final class VeinFeaturesRegistries implements BaseRegistries<VeinFeaturesRegistries> {
    public static final RegistryObject<VeinFeature> DENSE_NODE = RegistriesHandler.VEIN_FEATURES.register("dense_node", DenseNodeVeinFeature::new);
    public static final RegistryObject<VeinFeature> SPARSE_TRANSITION = RegistriesHandler.VEIN_FEATURES.register("sparse_transition", SparseTransitionVeinFeature::new);
    public static final RegistryObject<VeinFeature> SPARSE_HALO = RegistriesHandler.VEIN_FEATURES.register("sparse_halo", SparseHaloVeinFeature::new);

    @Override
    public VeinFeaturesRegistries init() {
        return BaseRegistries.super.init();
    }
}
