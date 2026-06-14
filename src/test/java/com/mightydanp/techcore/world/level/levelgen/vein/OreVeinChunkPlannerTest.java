package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinChunkPlannerTest {
    private static final ResourceKey<Level> OVERWORLD = OreVeinCandidateLookupTest.dimension("overworld");

    static {
        SharedConstants.tryDetectVersion();
        try {
            Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
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

    @AfterEach
    void resetState() throws Exception {
        clearRegistry("DEFINITIONS");
        clearRegistry("GENERATION_SETTINGS");
        clearRegistry("OVERLAP_SETTINGS");
        clearRockLayers();
    }

    @Test
    void unsupportedDimensionDoesNothing() {
        OreVeinChunkPlan plan = OreVeinChunkPlanner.plan(1L, OreVeinCandidateLookupTest.dimension("unsupported"), new ChunkPos(0, 0), -64, 320);

        assertTrue(plan.isEmpty());
    }

    @Test
    void bitIndexMatchesExpectedFormula() {
        assertEquals(0, OreVeinChunkPlanner.bitIndex(0, -64, 0, 0, 0, -64));
        assertEquals(17, OreVeinChunkPlanner.bitIndex(1, -64, 1, 0, 0, -64));
        assertEquals(256, OreVeinChunkPlanner.bitIndex(0, -63, 0, 0, 0, -64));
    }

    @Test
    void planIsDeterministicAndPositionsStayWithinChunkWithoutDuplicates() {
        setupGeneration();
        long seed = 1234L;
        ChunkPos chunk = new ChunkPos(0, 0);

        List<OreVeinInstanceDescriptor> candidates = OreVeinCandidateLookup.candidatesForChunk(seed, OVERWORLD, chunk, -64, 320);
        OreVeinChunkPlan first = OreVeinChunkPlanner.plan(seed, OVERWORLD, chunk, -64, 320, candidates);
        OreVeinChunkPlan second = OreVeinChunkPlanner.plan(seed, OVERWORLD, chunk, -64, 320, candidates);

        assertEquals(first, second);
        assertEquals(first.replacements().size(), first.replacements().stream().map(replacement -> replacement.position().asLong()).distinct().count());
        assertTrue(first.replacements().stream().allMatch(replacement ->
                replacement.position().getX() >= chunk.getMinBlockX()
                        && replacement.position().getX() <= chunk.getMaxBlockX()
                        && replacement.position().getZ() >= chunk.getMinBlockZ()
                        && replacement.position().getZ() <= chunk.getMaxBlockZ()
                        && replacement.position().getY() >= -64
                        && replacement.position().getY() < 320
        ));
    }

    @Test
    void adjacentChunksReconstructCrossingVeinIndependently() {
        setupGeneration();
        long seed = 1234L;
        ChunkPos leftChunk = new ChunkPos(0, 0);
        ChunkPos rightChunk = new ChunkPos(1, 0);

        OreVeinChunkPlan leftPlan = OreVeinChunkPlanner.plan(seed, OVERWORLD, leftChunk, -64, 320);
        OreVeinChunkPlan rightPlan = OreVeinChunkPlanner.plan(seed, OVERWORLD, rightChunk, -64, 320);

        assertTrue(leftPlan.replacements().stream().allMatch(replacement -> replacement.position().getX() <= 15));
        assertTrue(rightPlan.replacements().stream().allMatch(replacement -> replacement.position().getX() >= 16));
        assertNotEquals(leftPlan, rightPlan);
    }

    private static void setupGeneration() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        host.rockLayer.stoneBlock = () -> Blocks.STONE;
        ore.ore.oreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        ore.ore.sparseOreBlocks.put(host.name, () -> Blocks.COAL_ORE);
        ore.ore.denseOreBlocks.put(host.name, () -> Blocks.DEEPSLATE_IRON_ORE);
        RockLayerFeature.setAllowedMaterials(OVERWORLD, List.of(host));
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinitions.registerOverlapSettings(OVERWORLD, new OreVeinOverlapSettings(512, 1024));
        OreVeinDefinitions.register(new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", "planner"),
                List.of(OVERWORLD),
                1_000_000,
                -16,
                128,
                96,
                96,
                24,
                24,
                96,
                96,
                0.0D,
                0.0D,
                new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4),
                new OreVeinHaloSettings(4.0D, 9.0D, 320),
                List.of(new VeinOreEntry(() -> ore, 1))
        ));
    }

    private static Material host(String name) {
        Material material = new Material(name, null);
        material.rockLayer.stoneBlock = () -> Blocks.STONE;
        return material;
    }

    private static Material ore(String name, int maxDensity) {
        Material material = new Material(name, null);
        try {
            Field oreType = material.ore.getClass().getDeclaredField("oreType");
            Field density = material.ore.getClass().getDeclaredField("maxDensity");
            Field rockTypes = material.ore.getClass().getDeclaredField("rockTypes");
            oreType.setAccessible(true);
            density.setAccessible(true);
            rockTypes.setAccessible(true);
            oreType.set(material.ore, OreTypes.ORE.oreType());
            density.setInt(material.ore, maxDensity);
            rockTypes.set(material.ore, Collections.singletonList(RockTypes.GENERIC.getType()));
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
        return material;
    }

    @SuppressWarnings("unchecked")
    private static void clearRegistry(String fieldName) throws Exception {
        Field field = OreVeinDefinitions.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        ((Map<?, ?>) field.get(null)).clear();
    }

    @SuppressWarnings("unchecked")
    private static void clearRockLayers() throws Exception {
        Field field = RockLayerFeature.class.getDeclaredField("ALLOWED_MATERIALS_BY_DIMENSION");
        field.setAccessible(true);
        ((Map<?, ?>) field.get(null)).clear();
    }
}
