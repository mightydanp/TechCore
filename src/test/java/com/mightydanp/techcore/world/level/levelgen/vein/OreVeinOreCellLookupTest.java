package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoublePredicate;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.HOST_ROCK;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.REGULAR_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.SPARSE_ORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Isolated
final class OreVeinOreCellLookupTest {
    private static final ResourceKey<Level> OVERWORLD = OreVeinCandidateLookupTest.dimension("overworld");

    @AfterEach
    void resetRegistries() throws Exception {
        clearRegistry("DEFINITIONS");
        clearRegistry("GENERATION_SETTINGS");
        clearRegistry("OVERLAP_SETTINGS");
    }

    @Test
    void overlappingHostRockAndOreResultsBothRetained() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("first", 1L, ore("a", 4));
        OreVeinDefinitions.register(definition);
        BlockPos overlap = findMixedOverlapPosition(1234L, definition);

        List<OreVeinOreCellResult> results = OreVeinOreCellLookup.resultsForPosition(1234L, OVERWORLD, overlap);

        assertTrue(results.size() >= 2);
        assertTrue(results.stream().anyMatch(result -> result.variant() == HOST_ROCK));
        assertTrue(results.stream().anyMatch(result -> result.variant() != HOST_ROCK));
        assertEquals(results.stream().map(OreVeinOreCellResult::instanceId).sorted().toList(), results.stream().map(OreVeinOreCellResult::instanceId).toList());
    }

    @Test
    void noChunkLoadingBlockReadsOrMutationAndHostRockPreserved() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinitions.register(definition("host", 3L, ore("host", 1)));

        List<OreVeinOreCellResult> results = OreVeinOreCellLookup.resultsForPosition(1234L, OVERWORLD, new BlockPos(0, 0, 0));

        assertTrue(results.stream().allMatch(result -> result.variant() == HOST_ROCK || result.finalDensity() >= 1));
    }

    private static OreVeinDefinition definition(String name, long id, Material material) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OVERWORLD),
                1000000,
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
                List.of(new VeinOreEntry(() -> material, 1))
        );
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
            throw new AssertionError("Unable to configure test ore material", exception);
        }
        return material;
    }

    @SuppressWarnings("unchecked")
    private static void clearRegistry(String fieldName) throws Exception {
        Field field = OreVeinDefinitions.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        ((Map<?, ?>) field.get(null)).clear();
    }

    private static BlockPos findMixedOverlapPosition(long worldSeed, OreVeinDefinition definition) {
        List<OreVeinInstanceDescriptor> descriptors = new java.util.ArrayList<>();

        for (int regionX = -1; regionX <= 1; regionX++) {
            for (int regionZ = -1; regionZ <= 1; regionZ++) {
                OreVeinCandidateLookup.createDescriptor(worldSeed, OVERWORLD, regionX, regionZ, OreVeinGenerationMath.ORIGIN_INDEX, definition)
                        .ifPresent(descriptors::add);
            }
        }

        for (OreVeinInstanceDescriptor descriptor : descriptors) {
            BlockPos center = descriptor.center();

            for (int dx = -24; dx <= 24; dx += 4) {
                for (int dz = -24; dz <= 24; dz += 4) {
                    BlockPos position = center.offset(dx, 0, dz);
                    List<OreVeinOreCellResult> direct = new java.util.ArrayList<>();

                    for (OreVeinInstanceDescriptor candidate : descriptors) {
                        OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(candidate, position);

                        if (contribution.state() == OreVeinContribution.ContributionState.INSIDE_MAIN_BODY) {
                            direct.add(OreVeinOreCellEvaluator.evaluateCell(candidate, definition, position, contribution));
                        }
                    }

                    if (direct.size() >= 2
                            && direct.stream().anyMatch(result -> result.variant() == HOST_ROCK)
                            && direct.stream().anyMatch(result -> result.variant() != HOST_ROCK)) {
                        return position;
                    }
                }
            }
        }

        throw new AssertionError("No mixed overlap position found");
    }

    @Test
    void haloCellsFoundEvenThoughContributionLookupExcludesThem() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("halo", 5L, ore("halo", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        BlockPos haloPos = findPosition(descriptor, signedDistance -> signedDistance > 1.0D
                && signedDistance < definition.sparseReachBlocks());

        assertTrue(OreVeinContributionLookup.contributionsForPosition(1234L, OVERWORLD, haloPos).isEmpty());
        assertFalse(OreVeinOreCellLookup.resultsForPosition(1234L, OVERWORLD, haloPos).isEmpty());
    }

    @Test
    void outsideHaloCandidatesReturnNoResult() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("outside", 6L, ore("outside", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        BlockPos outside = findPosition(descriptor, signedDistance -> signedDistance > definition.sparseReachBlocks());

        assertTrue(OreVeinOreCellLookup.resultsForPosition(1234L, OVERWORLD, outside, List.of(descriptor)).isEmpty());
    }

    @Test
    void candidateBoundsUseDerivedSparseReachExactlyOnce() {
        OreVeinDefinition definition = definition("bounds", 7L, ore("bounds", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        OreVeinBounds evaluationBounds = OreVeinCandidateLookup.evaluationBounds(descriptor);
        int distortionReach = OreVeinDefinitions.checkedCeilToInt(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS, "MAX_BOUNDARY_DISTORTION_BLOCKS");
        int expectedReach = Math.addExact(distortionReach, definition.sparseReachBlocks());

        assertEquals(descriptor.bounds().minX() - expectedReach, evaluationBounds.minX());
        assertEquals(descriptor.bounds().maxX() + expectedReach, evaluationBounds.maxX());
    }

    @Test
    void activeSparseCandidateBoundsDoNotUseHaloRadiusBlocks() throws Exception {
        Field haloRadius = OreVeinHaloSettings.class.getDeclaredField("haloRadiusBlocks");
        haloRadius.setAccessible(true);
        OreVeinDefinition definition = definition("no_halo_radius", 9L, ore("no_halo_radius", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        OreVeinBounds evaluationBounds = OreVeinCandidateLookup.evaluationBounds(descriptor);
        int distortionReach = OreVeinDefinitions.checkedCeilToInt(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS, "MAX_BOUNDARY_DISTORTION_BLOCKS");
        int expectedReach = Math.addExact(distortionReach, definition.sparseReachBlocks());

        assertEquals(9.0D, haloRadius.getDouble(definition.haloSettings()));
        assertEquals(descriptor.bounds().maxX() + expectedReach, evaluationBounds.maxX());
    }

    @Test
    void noOccupancyRollOccursWhenSparseThresholdIsZero() {
        OreVeinDefinition definition = definition("zero_roll", 8L, ore("zero_roll", 4));
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        double signedDistance = definition.sparseReachBlocks() + 1.0D;
        OreVeinContribution contribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                signedDistance,
                OreVeinContribution.ContributionState.INSIDE_MAIN_BODY
        );
        AtomicInteger calls = new AtomicInteger();

        OreVeinOreCellResult result = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                descriptor.center(),
                contribution,
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> {
                    calls.incrementAndGet();
                    return 0;
                }
        );

        assertEquals(HOST_ROCK, result.variant());
        assertEquals(0, calls.get());
    }

    @Test
    void lookupPathReachesSparseEvaluationThroughDistanceThirtyTwo() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("lookup_sparse_reach", 10L, ore("lookup_sparse_reach", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        Map<Integer, DistanceSample> samples = distanceSamples(descriptor, 8, 12, 16, 20, 23, 28, 32, 33);
        AtomicInteger rollCalls = new AtomicInteger();
        AtomicInteger lastRollBound = new AtomicInteger(-1);
        AtomicInteger evaluatedBeyondLegacyHalo = new AtomicInteger();

        OreVeinOreCellLookup.OreCellEvaluator forcingEvaluator = (candidate, candidateDefinition, position, contribution) -> {
            if (contribution.signedBoundaryDistanceBlocks() > candidateDefinition.haloSettings().haloRadiusBlocks()) {
                evaluatedBeyondLegacyHalo.incrementAndGet();
            }

            if (contribution.signedBoundaryDistanceBlocks() > 0.0D) {
                return OreVeinOreCellEvaluator.evaluateHaloCell(
                        candidate,
                        candidateDefinition,
                        position,
                        contribution,
                        (ignoredDescriptor, ignoredPosition, denominator) -> {
                            lastRollBound.set(denominator);
                            rollCalls.incrementAndGet();
                            return 0;
                        }
                );
            }

            return OreVeinOreCellEvaluator.evaluateCell(candidate, candidateDefinition, position, contribution);
        };

        for (int distance : List.of(8, 12, 16, 20, 23, 28, 32)) {
            DistanceSample sample = samples.get(distance);
            assertNotNull(sample);
            if (distance > definition.haloSettings().haloRadiusBlocks()) {
                assertTrue(sample.contribution().signedBoundaryDistanceBlocks() > definition.haloSettings().haloRadiusBlocks());
            }
            assertTrue(OreVeinCandidateLookup.candidatesForBlock(1234L, OVERWORLD, sample.position()).contains(descriptor));

            List<OreVeinOreCellResult> results = OreVeinOreCellLookup.resultsForPosition(
                    1234L,
                    OVERWORLD,
                    sample.position(),
                    List.of(descriptor),
                    List.of(OreVeinCandidateLookup.evaluationBounds(descriptor)),
                    forcingEvaluator
            );

            assertFalse(results.isEmpty());
            assertEquals(SPARSE_ORE, results.get(0).variant());
            assertEquals(1024, lastRollBound.get());
        }

        assertTrue(evaluatedBeyondLegacyHalo.get() >= 4);
        assertTrue(rollCalls.get() >= 7);
        assertEquals(32, definition.sparseReachBlocks());
    }

    @Test
    void distanceThirtyThreeDirectEvaluatorAndLookupBothBlockSparseOre() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("lookup_sparse_outside", 11L, ore("lookup_sparse_outside", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        DistanceSample sample = distanceSamples(descriptor, 33).get(33);
        AtomicInteger directRollCalls = new AtomicInteger();

        OreVeinOreCellResult direct = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                sample.position(),
                sample.contribution(),
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> {
                    directRollCalls.incrementAndGet();
                    return 0;
                }
        );

        assertEquals(HOST_ROCK, direct.variant());
        assertEquals(0, directRollCalls.get());

        AtomicInteger lookupRollCalls = new AtomicInteger();
        List<OreVeinOreCellResult> lookupResults = OreVeinOreCellLookup.resultsForPosition(
                1234L,
                OVERWORLD,
                sample.position(),
                List.of(descriptor),
                List.of(OreVeinCandidateLookup.evaluationBounds(descriptor)),
                (candidate, candidateDefinition, position, contribution) -> {
                    if (contribution.signedBoundaryDistanceBlocks() > 0.0D) {
                        return OreVeinOreCellEvaluator.evaluateHaloCell(
                                candidate,
                                candidateDefinition,
                                position,
                                contribution,
                                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> {
                                    lookupRollCalls.incrementAndGet();
                                    return 0;
                                }
                        );
                    }

                    return OreVeinOreCellEvaluator.evaluateCell(candidate, candidateDefinition, position, contribution);
                }
        );

        assertTrue(
                lookupResults.isEmpty()
                        || lookupResults.stream().noneMatch(result -> result.variant() == SPARSE_ORE)
        );
        assertEquals(0, lookupRollCalls.get());
    }

    @Test
    void directContributionBeyondLegacyHaloNowReachesEvaluation() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("legacy_gate_removed", 12L, ore("legacy_gate_removed", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        DistanceSample sample = distanceSamples(descriptor, 12).get(12);
        AtomicInteger evaluated = new AtomicInteger();

        List<OreVeinOreCellResult> results = OreVeinOreCellLookup.resultsForPosition(
                1234L,
                OVERWORLD,
                sample.position(),
                List.of(descriptor),
                List.of(OreVeinCandidateLookup.evaluationBounds(descriptor)),
                (candidate, candidateDefinition, position, contribution) -> {
                    evaluated.incrementAndGet();
                    return OreVeinOreCellEvaluator.evaluateCell(candidate, candidateDefinition, position, contribution);
                }
        );

        assertEquals(1, evaluated.get());
        assertFalse(results.isEmpty());
    }

    @Test
    void regularAndDenseOutputsRemainUnchanged() {
        OreVeinDefinitions.registerGenerationSettings(new OreVeinDimensionGenerationSettings(OVERWORLD, 1000));
        OreVeinDefinition definition = definition("regular_dense_unchanged", 13L, ore("regular_dense_unchanged", 4));
        OreVeinDefinitions.register(definition);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        BlockPos center = descriptor.center();

        List<OreVeinOreCellResult> lookupResults = OreVeinOreCellLookup.resultsForPosition(1234L, OVERWORLD, center, List.of(descriptor));
        OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, center);
        OreVeinOreCellResult direct = OreVeinOreCellEvaluator.evaluateCell(descriptor, definition, center, contribution);

        assertEquals(1, lookupResults.size());
        assertEquals(direct.variant(), lookupResults.get(0).variant());
        assertTrue(lookupResults.get(0).variant() == REGULAR_ORE || lookupResults.get(0).variant() == HOST_ROCK || lookupResults.get(0).variant() == SPARSE_ORE);
        assertEquals(direct.finalDensity(), lookupResults.get(0).finalDensity());
    }

    private static BlockPos findPosition(OreVeinInstanceDescriptor descriptor, DoublePredicate predicate) {
        BlockPos center = descriptor.center();

        for (int dx = -96; dx <= 96; dx++) {
            BlockPos position = center.offset(dx, 0, 0);
            double signedDistance = OreVeinShapeEvaluator.evaluate(descriptor, position).signedBoundaryDistanceBlocks();

            if (predicate.test(signedDistance)) {
                return position;
            }
        }

        throw new AssertionError("No matching position found");
    }

    private static Map<Integer, DistanceSample> distanceSamples(OreVeinInstanceDescriptor descriptor, int... distances) {
        Map<Integer, DistanceSample> samples = new LinkedHashMap<>();

        for (int distance : distances) {
            BlockPos position = findPosition(
                    descriptor,
                    distanceBand(distance)
            );
            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);
            samples.put(distance, new DistanceSample(position, contribution));
        }

        return samples;
    }

    private static DoublePredicate distanceBand(int distance) {
        if (distance == 23) {
            return signedDistance -> signedDistance >= 22.0D && signedDistance <= 23.0D;
        }

        if (distance == 32) {
            return signedDistance -> signedDistance > 31.0D && signedDistance <= 32.0D;
        }

        if (distance == 33) {
            return signedDistance -> signedDistance > 32.0D && signedDistance < 33.0D;
        }

        return signedDistance -> signedDistance >= distance && signedDistance < distance + 1.0D;
    }

    private record DistanceSample(BlockPos position, OreVeinContribution contribution) {
    }
}
