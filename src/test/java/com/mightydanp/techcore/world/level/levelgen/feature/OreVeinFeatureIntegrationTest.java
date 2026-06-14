package com.mightydanp.techcore.world.level.levelgen.feature;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinChunkPlan;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinResolvedCell;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class OreVeinFeatureIntegrationTest {
    private static final ResourceKey<Level> OVERWORLD = dimension("overworld");

    static {
        SharedConstants.tryDetectVersion();
        try {
            java.lang.reflect.Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
            bootstrapped.setAccessible(true);
            bootstrapped.setBoolean(null, true);
            try {
                BuiltInRegistries.bootStrap();
            } catch (RuntimeException ignored) {
            }
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void publicConstructorUsesProductionChunkPlanProviderConstant() {
        OreVeinFeature feature = new OreVeinFeature(NoneFeatureConfiguration.CODEC);

        assertSame(OreVeinFeature.PRODUCTION_CHUNK_PLAN_PROVIDER, feature.chunkPlanProvider);
    }

    @Test
    void placePassesSeedDimensionChunkAndBuildHeightToInjectedProvider() {
        WorldGenLevel level = mock(WorldGenLevel.class);
        ServerLevel serverLevel = mock(ServerLevel.class);
        ChunkGenerator chunkGenerator = mock(ChunkGenerator.class);
        BlockPos origin = new BlockPos(31, 45, -17);
        long seed = 4242L;
        int minY = -64;
        int maxYExclusive = 320;
        AtomicReference<Long> capturedSeed = new AtomicReference<>();
        AtomicReference<ResourceKey<Level>> capturedDimension = new AtomicReference<>();
        AtomicReference<ChunkPos> capturedChunk = new AtomicReference<>();
        AtomicReference<Integer> capturedMinY = new AtomicReference<>();
        AtomicReference<Integer> capturedMaxYExclusive = new AtomicReference<>();

        when(level.getSeed()).thenReturn(seed);
        when(level.getLevel()).thenReturn(serverLevel);
        when(serverLevel.dimension()).thenReturn(OVERWORLD);
        when(level.getMinBuildHeight()).thenReturn(minY);
        when(level.getMaxBuildHeight()).thenReturn(maxYExclusive);

        OreVeinFeature feature = new OreVeinFeature(NoneFeatureConfiguration.CODEC, (worldSeed, dimension, chunkPos, actualMinY, actualMaxYExclusive) -> {
            capturedSeed.set(worldSeed);
            capturedDimension.set(dimension);
            capturedChunk.set(chunkPos);
            capturedMinY.set(actualMinY);
            capturedMaxYExclusive.set(actualMaxYExclusive);
            return new OreVeinChunkPlan(chunkPos, dimension, List.of());
        });

        boolean changed = feature.place(new FeaturePlaceContext<>(
                java.util.Optional.empty(),
                level,
                chunkGenerator,
                RandomSource.create(1L),
                origin,
                NoneFeatureConfiguration.INSTANCE
        ));

        assertFalse(changed);
        assertEquals(seed, capturedSeed.get());
        assertEquals(OVERWORLD, capturedDimension.get());
        assertEquals(new ChunkPos(origin), capturedChunk.get());
        assertEquals(minY, capturedMinY.get());
        assertEquals(maxYExclusive, capturedMaxYExclusive.get());
    }

    @Test
    void placeReadsOnlyPlannedPositionsAppliesUpdateClientsAndSkipsMismatchedStates() {
        WorldGenLevel level = mock(WorldGenLevel.class);
        ServerLevel serverLevel = mock(ServerLevel.class);
        ChunkGenerator chunkGenerator = mock(ChunkGenerator.class);
        Material hostMaterial = new Material("granite", null);
        BlockState hostState = Blocks.STONE.defaultBlockState();
        BlockState replacementState = Blocks.IRON_ORE.defaultBlockState();
        BlockPos replace = new BlockPos(0, 20, 0);
        BlockPos skip = new BlockPos(1, 20, 0);

        when(level.getSeed()).thenReturn(99L);
        when(level.getLevel()).thenReturn(serverLevel);
        when(serverLevel.dimension()).thenReturn(OVERWORLD);
        when(level.getMinBuildHeight()).thenReturn(-64);
        when(level.getMaxBuildHeight()).thenReturn(320);
        when(level.getBlockState(replace)).thenReturn(hostState);
        when(level.getBlockState(skip)).thenReturn(Blocks.DIRT.defaultBlockState());
        when(level.setBlock(eq(replace), eq(replacementState), eq(Block.UPDATE_CLIENTS))).thenReturn(true);

        OreVeinResolvedCell replaceCell = new OreVeinResolvedCell(replace, hostMaterial, hostState, null, replacementState, true, false, false, List.of());
        OreVeinResolvedCell skipCell = new OreVeinResolvedCell(skip, hostMaterial, hostState, null, replacementState, true, false, false, List.of());
        OreVeinFeature feature = new OreVeinFeature(NoneFeatureConfiguration.CODEC, (seed, dimension, chunkPos, minY, maxYExclusive) ->
                new OreVeinChunkPlan(chunkPos, dimension, List.of(
                        new OreVeinChunkPlan.PlannedReplacement(replace, replaceCell),
                        new OreVeinChunkPlan.PlannedReplacement(skip, skipCell)
                ))
        );

        boolean changed = feature.place(new FeaturePlaceContext<>(
                java.util.Optional.empty(),
                level,
                chunkGenerator,
                RandomSource.create(2L),
                new BlockPos(0, 0, 0),
                NoneFeatureConfiguration.INSTANCE
        ));

        assertTrue(changed);
        verify(level, times(1)).getBlockState(replace);
        verify(level, times(1)).getBlockState(skip);
        verify(level, never()).getBlockState(new BlockPos(2, 20, 0));
        verify(level, times(1)).setBlock(replace, replacementState, Block.UPDATE_CLIENTS);
        verify(level, never()).setBlock(eq(skip), any(), any(Integer.class));
    }

    @Test
    void placePropagatesInjectedChunkPlanProviderFailureUnchanged() {
        WorldGenLevel level = mock(WorldGenLevel.class);
        ServerLevel serverLevel = mock(ServerLevel.class);
        ChunkGenerator chunkGenerator = mock(ChunkGenerator.class);
        IllegalStateException failure = new IllegalStateException("deterministic test failure");

        when(level.getSeed()).thenReturn(1L);
        when(level.getLevel()).thenReturn(serverLevel);
        when(serverLevel.dimension()).thenReturn(OVERWORLD);
        when(level.getMinBuildHeight()).thenReturn(0);
        when(level.getMaxBuildHeight()).thenReturn(16);

        OreVeinFeature feature = new OreVeinFeature(NoneFeatureConfiguration.CODEC, (seed, dimension, chunkPos, minY, maxYExclusive) -> {
            throw failure;
        });

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> feature.place(new FeaturePlaceContext<>(
                        java.util.Optional.empty(),
                        level,
                        chunkGenerator,
                        RandomSource.create(3L),
                        new BlockPos(0, 0, 0),
                        NoneFeatureConfiguration.INSTANCE
                ))
        );

        assertSame(failure, thrown);
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<Level> dimension(String path) {
        try {
            Constructor<ResourceKey> constructor = ResourceKey.class.getDeclaredConstructor(net.minecraft.resources.ResourceLocation.class, net.minecraft.resources.ResourceLocation.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    net.minecraft.resources.ResourceLocation.withDefaultNamespace("dimension"),
                    net.minecraft.resources.ResourceLocation.withDefaultNamespace(path)
            );
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to create test ResourceKey", exception);
        }
    }
}
