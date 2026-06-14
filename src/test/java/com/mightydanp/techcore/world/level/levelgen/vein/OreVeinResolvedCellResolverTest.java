package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import com.mightydanp.techcore.world.level.levelgen.feature.RockLayerFeature;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.OUTSIDE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.DENSE_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.HOST_ROCK;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.REGULAR_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.SPARSE_ORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinResolvedCellResolverTest {
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
    void noVeinReturnsOriginalHost() {
        Material host = host("granite");

        OreVeinResolvedCell resolved = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(0), host, Blocks.STONE.defaultBlockState(), List.of());

        assertFalse(resolved.replacement());
        assertEquals(Blocks.STONE.defaultBlockState(), resolved.resolvedBlockState());
    }

    @Test
    void incompatibleOreReturnsOriginalHostAndDoesNotSuppressCompatibleOre() {
        Material host = host("granite");
        Material compatible = ore("copper", 4);
        Material incompatible = ore("salt", 4, RockTypes.VOLCANIC_IGNEOUS.getType());
        compatible.ore.oreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        incompatible.ore.oreBlocks.put(host.name, () -> Blocks.GOLD_ORE);

        OreVeinResolvedCell onlyIncompatible = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(0), host, Blocks.STONE.defaultBlockState(), List.of(
                result(3L, "bad", incompatible, REGULAR_ORE, 1, -1.0D)
        ));
        OreVeinResolvedCell withCompatible = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(0), host, Blocks.STONE.defaultBlockState(), List.of(
                result(3L, "bad", incompatible, DENSE_ORE, 4, -4.0D),
                result(2L, "good", compatible, REGULAR_ORE, 1, -1.0D)
        ));

        assertFalse(onlyIncompatible.replacement());
        assertTrue(withCompatible.replacement());
        assertEquals(Blocks.IRON_ORE, withCompatible.resolvedBlockState().getBlock());
    }

    @Test
    void denseBeatsRegularAndSparseHigherDenseWinsAndRegularBeatsSparse() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        List<OreVeinOreCellResult> results = List.of(
                result(2L, "sparse", ore, SPARSE_ORE, 1, 2.0D),
                result(3L, "regular", ore, REGULAR_ORE, 1, -1.0D),
                result(4L, "dense3", ore, DENSE_ORE, 3, -0.5D),
                result(5L, "dense4", ore, DENSE_ORE, 4, -0.25D)
        );

        OreVeinOreCellResult winner = OreVeinResolvedCellResolver.pickWinner(
                1L,
                OVERWORLD,
                pos(1),
                OreVeinResolvedCellResolver.compatibleOreResults(results, host)
        );

        assertEquals("dense4", winner.definitionId().getPath());
    }

    @Test
    void sparseCloserToMainBodyWinsAndResultOrderingDoesNotChangeWinner() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        List<OreVeinOreCellResult> ordered = List.of(
                result(4L, "far", ore, SPARSE_ORE, 1, 8.0D),
                result(7L, "near", ore, SPARSE_ORE, 1, 2.0D)
        );
        List<OreVeinOreCellResult> reversed = List.of(ordered.get(1), ordered.get(0));

        OreVeinOreCellResult first = OreVeinResolvedCellResolver.pickWinner(11L, OVERWORLD, pos(5), OreVeinResolvedCellResolver.compatibleOreResults(ordered, host));
        OreVeinOreCellResult second = OreVeinResolvedCellResolver.pickWinner(11L, OVERWORLD, pos(5), OreVeinResolvedCellResolver.compatibleOreResults(reversed, host));

        assertEquals("near", first.definitionId().getPath());
        assertEquals(first.instanceId(), second.instanceId());
    }

    @Test
    void largerUnsignedWinnerHashWinsAndInputOrderDoesNotMatter() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        OreVeinOreCellResult first = result(41L, "first", ore, REGULAR_ORE, 1, -1.0D);
        OreVeinOreCellResult second = result(77L, "second", ore, REGULAR_ORE, 1, -1.0D);
        List<OreVeinOreCellResult> ordered = List.of(first, second);
        List<OreVeinOreCellResult> reversed = List.of(second, first);
        long firstHash = winnerHash(19L, OVERWORLD, pos(16), first.instanceId());
        long secondHash = winnerHash(19L, OVERWORLD, pos(16), second.instanceId());
        OreVeinOreCellResult expected = Long.compareUnsigned(firstHash, secondHash) > 0 ? first : second;

        OreVeinOreCellResult orderedWinner = OreVeinResolvedCellResolver.pickWinner(19L, OVERWORLD, pos(16), OreVeinResolvedCellResolver.compatibleOreResults(ordered, host));
        OreVeinOreCellResult reversedWinner = OreVeinResolvedCellResolver.pickWinner(19L, OVERWORLD, pos(16), OreVeinResolvedCellResolver.compatibleOreResults(reversed, host));

        assertEquals(expected.instanceId(), orderedWinner.instanceId());
        assertEquals(expected.instanceId(), reversedWinner.instanceId());
    }

    @Test
    void exactHostNameMapKeyAndSparseSelectionAreUsed() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        ore.ore.oreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        ore.ore.sparseOreBlocks.put(host.name, () -> Blocks.COAL_ORE);

        OreVeinResolvedCell regular = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(6), host, Blocks.STONE.defaultBlockState(), List.of(result(2L, "r", ore, REGULAR_ORE, 1, -1.0D)));
        OreVeinResolvedCell sparse = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(7), host, Blocks.STONE.defaultBlockState(), List.of(result(2L, "s", ore, SPARSE_ORE, 1, 2.0D)));

        assertEquals(Blocks.IRON_ORE, regular.resolvedBlockState().getBlock());
        assertEquals(Blocks.COAL_ORE, sparse.resolvedBlockState().getBlock());
    }

    @Test
    void mainBodyGapRollIsDeterministicNonCompoundingAndDoesNotUseHaloFailures() {
        Material host = host("granite");
        Material ore = ore("copper", 4);
        ore.ore.oreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        OreVeinDefinitions.registerOverlapSettings(OVERWORLD, new OreVeinOverlapSettings(512, 1024));

        List<OreVeinOreCellResult> withOneGap = List.of(
                result(10L, "ore", ore, REGULAR_ORE, 1, -1.0D),
                result(20L, "gap", ore, HOST_ROCK, 0, -0.25D)
        );
        List<OreVeinOreCellResult> withManyGaps = List.of(
                result(10L, "ore", ore, REGULAR_ORE, 1, -1.0D),
                result(30L, "gap3", ore, HOST_ROCK, 0, -0.25D),
                result(20L, "gap2", ore, HOST_ROCK, 0, -0.25D),
                result(20L, "gap2dup", ore, HOST_ROCK, 0, -0.25D),
                result(40L, "halo_fail", ore, HOST_ROCK, 0, 3.0D)
        );

        boolean sawHost = false;
        boolean sawOre = false;

        for (int i = 0; i < 128; i++) {
            OreVeinResolvedCell resolved = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(100 + i), host, Blocks.STONE.defaultBlockState(), withOneGap);
            sawHost |= !resolved.replacement();
            sawOre |= resolved.replacement();
        }

        OreVeinResolvedCell first = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(9), host, Blocks.STONE.defaultBlockState(), withManyGaps);
        OreVeinResolvedCell second = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(9), host, Blocks.STONE.defaultBlockState(), List.of(
                withManyGaps.get(4), withManyGaps.get(3), withManyGaps.get(2), withManyGaps.get(1), withManyGaps.get(0)
        ));

        assertTrue(sawHost);
        assertTrue(sawOre);
        assertEquals(List.of(20L, 30L), first.participatingMainBodyGapInstanceIds());
        assertEquals(first.overlapGapWon(), second.overlapGapWon());
    }

    @Test
    void invalidMappingsAndDenseStateFailuresThrowDescriptiveIllegalStateException() {
        Material host = host("granite");
        Material ore = ore("copper", 4);

        IllegalStateException missing = assertThrows(IllegalStateException.class,
                () -> OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(10), host, Blocks.STONE.defaultBlockState(), List.of(result(2L, "m", ore, REGULAR_ORE, 1, -1.0D))));
        assertTrue(missing.getMessage().contains("missing block mapping"));

        ore.ore.oreBlocks.put(host.name, () -> null);
        IllegalStateException nullSupplier = assertThrows(IllegalStateException.class,
                () -> OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(11), host, Blocks.STONE.defaultBlockState(), List.of(result(2L, "n", ore, REGULAR_ORE, 1, -1.0D))));
        assertTrue(nullSupplier.getMessage().contains("null block supplier"));

        ore.ore.denseOreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        assertThrows(IllegalStateException.class,
                () -> OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(12), host, Blocks.STONE.defaultBlockState(), List.of(result(2L, "p", ore, DENSE_ORE, 3, -1.0D))));
    }

    @Test
    void unsupportedDimensionReturnsEmptyAndSkipsGeologyLookup() {
        ResourceKey<Level> unsupported = OreVeinCandidateLookupTest.dimension("unsupported");

        Optional<OreVeinResolvedCell> resolved = OreVeinResolvedCellResolver.resolve(1L, unsupported, pos(13));

        assertTrue(resolved.isEmpty());
        assertFalse(OreVeinDefinitions.isSupportedDimension(unsupported));
    }

    @Test
    void unresolvedOriginalGeologyInSupportedDimensionThrows() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinitions.registerOverlapSettings(OVERWORLD, new OreVeinOverlapSettings(512, 1024));
        OreVeinDefinitions.register(new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", "supported"),
                List.of(OVERWORLD),
                1,
                0,
                1,
                6,
                6,
                6,
                6,
                6,
                6,
                0.0D,
                0.0D,
                new OreVeinDensitySettings(1, 1, 1, 1L, 0, 0, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1, 1),
                new OreVeinHaloSettings(1.0D, 1.0D, 0),
                List.of(new VeinOreEntry(() -> ore("stub", 1), 1))
        ));

        assertThrows(IllegalStateException.class, () -> OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(14)));
    }

    @Test
    void publicResolveUsesOriginalGeologyWithoutLiveWorldAccess() {
        Material host = host("granite");
        host.rockLayer.stoneBlock = () -> Blocks.STONE;
        RockLayerFeature.setAllowedMaterials(OVERWORLD, List.of(host));
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinitions.registerOverlapSettings(OVERWORLD, new OreVeinOverlapSettings(512, 1024));
        OreVeinDefinitions.register(new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", "supported"),
                List.of(OVERWORLD),
                1,
                0,
                1,
                6,
                6,
                6,
                6,
                6,
                6,
                0.0D,
                0.0D,
                new OreVeinDensitySettings(1, 1, 1, 1L, 0, 0, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1, 1),
                new OreVeinHaloSettings(1.0D, 1.0D, 0),
                List.of(new VeinOreEntry(() -> ore("stub", 1), 1))
        ));

        OreVeinResolvedCell resolved = OreVeinResolvedCellResolver.resolve(1L, OVERWORLD, pos(15)).orElseThrow();

        assertNotNull(resolved.originalHostMaterial());
        assertNotNull(resolved.originalHostState());
    }

    private static OreVeinOreCellResult result(long instanceId, String definitionPath, Material material, OreVeinOreCellResult.OreVariant variant, int finalDensity, double signedBoundaryDistanceBlocks) {
        return new OreVeinOreCellResult(
                instanceId,
                ResourceLocation.fromNamespaceAndPath("test", definitionPath),
                material,
                1,
                new OreVeinContribution(
                        instanceId,
                        ResourceLocation.fromNamespaceAndPath("test", definitionPath),
                        0.0D,
                        0.0D,
                        0.0D,
                        1.0D,
                        0.0D,
                        signedBoundaryDistanceBlocks,
                        signedBoundaryDistanceBlocks <= 0.0D ? INSIDE_MAIN_BODY : OUTSIDE
                ),
                Math.max(1, finalDensity),
                finalDensity,
                variant,
                0L,
                0.0D
        );
    }

    private static Material ore(String name, int maxDensity) {
        return ore(name, maxDensity, RockTypes.GENERIC.getType());
    }

    private static Material ore(String name, int maxDensity, RockTypes.RockType rockType) {
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
            rockTypes.set(material.ore, Collections.singletonList(rockType));
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
        return material;
    }

    private static Material host(String name) {
        Material material = new Material(name, null);
        material.rockLayer.isRockLayer = true;
        material.rockLayer.canContainOre = true;
        material.rockLayer.rockType = RockTypes.GENERIC.getType();
        material.rockLayer.stoneBlock = () -> Blocks.STONE;
        return material;
    }

    private static BlockPos pos(int x) {
        return new BlockPos(x, 16, -x);
    }

    private static long winnerHash(long worldSeed, ResourceKey<Level> dimension, BlockPos position, long instanceId) {
        long hash = OreVeinGenerationMath.hashSeedAndDimension(worldSeed, dimension, OreVeinResolvedCellResolver.WINNER_POSITION_SALT);
        hash = OreVeinGenerationMath.mix64(hash ^ position.getX());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getY());
        hash = OreVeinGenerationMath.mix64(hash ^ position.getZ());
        return OreVeinGenerationMath.mix64(hash ^ instanceId);
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
