package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinChunkPlannerIntegrationTest {
    private static final ResourceKey<Level> OVERWORLD = OreVeinCandidateLookupTest.dimension("overworld");

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
    void logicalBitCountUsesExclusiveMaximumAndCheckedArithmetic() {
        assertEquals(16 * 16 * 384, OreVeinChunkPlanner.logicalBitCount(-64, 320));
        assertThrows(IllegalArgumentException.class, () -> OreVeinChunkPlanner.logicalBitCount(10, 10));
        assertThrows(IllegalArgumentException.class, () -> OreVeinChunkPlanner.logicalBitCount(11, 10));
        assertThrows(ArithmeticException.class, () -> OreVeinChunkPlanner.logicalBitCount(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void planningDependenciesAreCalledOncePerChunkAndBoundsOncePerCandidate() {
        ChunkPos chunkPos = new ChunkPos(0, 0);
        List<OreVeinInstanceDescriptor> candidates = List.of(
                descriptor("a", new OreVeinBounds(0, 0, 0, 1, 0, 1)),
                descriptor("b", new OreVeinBounds(1, 0, 1, 2, 0, 2))
        );
        AtomicInteger candidateCalls = new AtomicInteger();
        AtomicInteger boundsCalls = new AtomicInteger();
        AtomicInteger resolvedCalls = new AtomicInteger();
        Set<Long> resolvedPositions = new HashSet<>();
        OreVeinChunkPlanner.PlanningDependencies dependencies = new OreVeinChunkPlanner.PlanningDependencies(
                (seed, dimension, actualChunk, minY, maxYExclusive) -> {
                    candidateCalls.incrementAndGet();
                    return candidates;
                },
                descriptor -> {
                    boundsCalls.incrementAndGet();
                    return descriptor.bounds();
                },
                (seed, dimension, position, actualCandidates) -> {
                    resolvedCalls.incrementAndGet();
                    resolvedPositions.add(position.asLong());
                    return Optional.of(resolvedCell(position));
                }
        );

        OreVeinChunkPlan plan = OreVeinChunkPlanner.plan(1L, OVERWORLD, chunkPos, 0, 16, dependencies);

        assertEquals(1, candidateCalls.get());
        assertEquals(candidates.size(), boundsCalls.get());
        assertEquals(resolvedPositions.size(), resolvedCalls.get());
        assertTrue(resolvedPositions.size() <= OreVeinChunkPlanner.logicalBitCount(0, 16));
        assertEquals(plan.replacements().size(), resolvedPositions.size());
    }

    @Test
    void emptyCandidateListStillUsesOneProviderCallAndZeroBoundsCalls() {
        AtomicInteger candidateCalls = new AtomicInteger();
        AtomicInteger boundsCalls = new AtomicInteger();

        OreVeinChunkPlan plan = OreVeinChunkPlanner.plan(
                1L,
                OVERWORLD,
                new ChunkPos(0, 0),
                0,
                16,
                new OreVeinChunkPlanner.PlanningDependencies(
                        (seed, dimension, chunkPos, minY, maxYExclusive) -> {
                            candidateCalls.incrementAndGet();
                            return List.of();
                        },
                        descriptor -> {
                            boundsCalls.incrementAndGet();
                            return descriptor.bounds();
                        },
                        (seed, dimension, position, candidates) -> Optional.of(resolvedCell(position))
                )
        );

        assertTrue(plan.isEmpty());
        assertEquals(1, candidateCalls.get());
        assertEquals(0, boundsCalls.get());
    }

    private static OreVeinInstanceDescriptor descriptor(String path, OreVeinBounds bounds) {
        return new OreVeinInstanceDescriptor(
                path.hashCode(),
                1L,
                2L,
                ResourceLocation.fromNamespaceAndPath("test", path),
                new BlockPos(bounds.minX(), bounds.minY(), bounds.minZ()),
                6,
                6,
                6,
                0.0D,
                0.0D,
                0.0D,
                0,
                0,
                0,
                bounds,
                List.of()
        );
    }

    private static OreVeinResolvedCell resolvedCell(BlockPos position) {
        Material material = new Material("granite", null);
        return new OreVeinResolvedCell(position, material, Blocks.STONE.defaultBlockState(), null, Blocks.IRON_ORE.defaultBlockState(), true, false, false, List.of());
    }
}
