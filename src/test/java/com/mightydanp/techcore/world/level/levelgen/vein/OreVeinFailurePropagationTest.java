package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class OreVeinFailurePropagationTest {
    private static final ResourceKey<Level> OVERWORLD = ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION,
            ResourceLocation.withDefaultNamespace("overworld")
    );

    @Test
    void plannerPropagatesResolvedCellFailureUnchanged() {
        IllegalStateException failure = new IllegalStateException("deterministic test failure");
        OreVeinChunkPlanner.PlanningDependencies dependencies = new OreVeinChunkPlanner.PlanningDependencies(
                (seed, dimension, chunkPos, minY, maxYExclusive) -> List.of(new OreVeinInstanceDescriptor(
                        1L,
                        2L,
                        3L,
                        ResourceLocation.fromNamespaceAndPath("test", "candidate"),
                        new BlockPos(0, 0, 0),
                        6,
                        6,
                        6,
                        0.0D,
                        0.0D,
                        0.0D,
                        0,
                        0,
                        0,
                        new OreVeinBounds(0, 0, 0, 0, 0, 0),
                        List.of()
                )),
                OreVeinInstanceDescriptor::bounds,
                (seed, dimension, position, candidates) -> {
                    throw failure;
                }
        );

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> OreVeinChunkPlanner.plan(1L, OVERWORLD, new ChunkPos(0, 0), 0, 16, dependencies)
        );

        assertSame(failure, thrown);
    }
}
