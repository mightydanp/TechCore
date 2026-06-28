package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record OreVeinInstanceDescriptor(long instanceId, long instanceSeed, long shapeSeed, ResourceLocation definitionId, BlockPos center, int sizeX, int sizeY, int sizeZ, double yaw, double pitch, double roll, int originRegionX, int originRegionZ, int originIndex, OreVeinBounds bounds, List<FeatureState> featureStates) {
    public OreVeinInstanceDescriptor {
        // Validate the sampled descriptor data before storing it
        Objects.requireNonNull(definitionId, "definitionId");
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(bounds, "bounds");
        featureStates = List.copyOf(Objects.requireNonNull(featureStates, "featureStates"));

        if (sizeX < 6 || sizeY < 6 || sizeZ < 6) throw new IllegalArgumentException("sizes must be at least 6");

        if (!Double.isFinite(yaw) || !Double.isFinite(pitch) || !Double.isFinite(roll))
            throw new IllegalArgumentException("rotation must be finite");
    }

    public <T> T featureState(ResourceLocation featureId, Class<T> type) {
        for (FeatureState featureState : featureStates)
            if (featureState.featureId().equals(featureId)) return type.cast(featureState.state());

        return null;
    }

    @Contract("_, _ -> new")
    public @NotNull OreVeinInstanceDescriptor withFeatureState(ResourceLocation featureId, Object state) {
        List<FeatureState> updated = new ArrayList<>(featureStates);
        updated.removeIf(existing -> existing.featureId().equals(featureId));
        updated.add(new FeatureState(featureId, state));

        return new OreVeinInstanceDescriptor(
                instanceId,
                instanceSeed,
                shapeSeed,
                definitionId,
                center,
                sizeX,
                sizeY,
                sizeZ,
                yaw,
                pitch,
                roll,
                originRegionX,
                originRegionZ,
                originIndex,
                bounds,
                updated
        );
    }

    public record FeatureState(ResourceLocation featureId, Object state) {
        public FeatureState {
            Objects.requireNonNull(featureId, "featureId");
            Objects.requireNonNull(state, "state");
        }
    }
}
