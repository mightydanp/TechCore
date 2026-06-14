package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public record OreVeinChunkPlan(
        ChunkPos chunkPos,
        ResourceKey<Level> dimension,
        List<PlannedReplacement> replacements
) {
    public OreVeinChunkPlan {
        Objects.requireNonNull(chunkPos, "chunkPos");
        Objects.requireNonNull(dimension, "dimension");
        replacements = List.copyOf(replacements);
    }

    public boolean isEmpty() {
        return replacements.isEmpty();
    }

    public record PlannedReplacement(BlockPos position, OreVeinResolvedCell resolvedCell) {
        public PlannedReplacement {
            position = position.immutable();
            Objects.requireNonNull(resolvedCell, "resolvedCell");
        }
    }
}
