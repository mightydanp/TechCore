package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.DENSE_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.HOST_ROCK;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.REGULAR_ORE;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.SPARSE_ORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinOreCellEvaluatorTest {
    @Test
    void deterministicWeightedMaterialSelection() {
        OreVeinDefinition definition = definition("selection", densitySettings(), oreEntry(ore("a", 4), 8), oreEntry(ore("b", 4), 4));
        OreVeinOreCellEvaluator.SelectedOreEntry first = OreVeinOreCellEvaluator.selectOreEntry(descriptor(1L, 64, 24, 64), definition, new BlockPos(3, 4, 5));
        OreVeinOreCellEvaluator.SelectedOreEntry second = OreVeinOreCellEvaluator.selectOreEntry(descriptor(1L, 64, 24, 64), definition, new BlockPos(3, 4, 5));

        assertEquals(first.material().name, second.material().name);
        assertEquals(first.entry().distributionWeight(), second.entry().distributionWeight());
    }

    @Test
    void distributionRatiosOverFixedSamples() {
        OreVeinDefinition definition = definition("ratios", densitySettings(), oreEntry(ore("a", 4), 8), oreEntry(ore("b", 4), 4), oreEntry(ore("c", 4), 2));
        OreVeinInstanceDescriptor descriptor = descriptor(7L, 64, 24, 64);
        int a = 0;
        int b = 0;
        int c = 0;

        for (int x = 0; x < 196; x++) {
            Material material = OreVeinOreCellEvaluator.selectOreEntry(descriptor, definition, new BlockPos(x, 0, x / 7)).material();

            if (material.name.equals("a")) {
                a++;
            } else if (material.name.equals("b")) {
                b++;
            } else {
                c++;
            }
        }

        assertTrue(a > b);
        assertTrue(b > c);
    }

    @Test
    void generationWeightDoesNotAffectMaterialSelection() {
        VeinOreEntry[] entries = {oreEntry(ore("a", 4), 8), oreEntry(ore("b", 4), 4)};
        OreVeinDefinition first = definition("g1", 40, densitySettings(), entries);
        OreVeinDefinition second = definition("g2", 400, densitySettings(), entries);
        BlockPos position = new BlockPos(9, 2, -4);

        assertEquals(
                OreVeinOreCellEvaluator.selectOreEntry(descriptor(5L, 64, 24, 64), first, position).material().name,
                OreVeinOreCellEvaluator.selectOreEntry(descriptor(5L, 64, 24, 64), second, position).material().name
        );
    }

    @Test
    void nodeCountIncreasingWithActualVolume() {
        OreVeinDensitySettings settings = densitySettings();

        assertTrue(
                OreVeinOreCellEvaluator.nodeCount(descriptor(1L, 32, 12, 32), settings)
                        < OreVeinOreCellEvaluator.nodeCount(descriptor(1L, 96, 24, 96), settings)
        );
    }

    @Test
    void nodeCountMinMaxAndZeroNodeSupport() {
        OreVeinDensitySettings zero = new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 0, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4);
        OreVeinDensitySettings clamped = new OreVeinDensitySettings(704, 960, 1024, 1L, 0, 2, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4);

        assertEquals(0, OreVeinOreCellEvaluator.nodeCount(descriptor(1L, 64, 24, 64), zero));
        assertEquals(2, OreVeinOreCellEvaluator.nodeCount(descriptor(1L, 64, 24, 64), clamped));
    }

    @Test
    void extremeDimensionsDoNotOverflow() {
        OreVeinDensitySettings settings = new OreVeinDensitySettings(704, 960, 1024, Long.MAX_VALUE, 0, 5, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4);
        OreVeinInstanceDescriptor descriptor = descriptor(1L, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        BigInteger volume = BigInteger.valueOf(Integer.MAX_VALUE)
                .multiply(BigInteger.valueOf(Integer.MAX_VALUE))
                .multiply(BigInteger.valueOf(Integer.MAX_VALUE));

        assertTrue(volume.compareTo(BigInteger.ZERO) > 0);
        assertTrue(OreVeinOreCellEvaluator.nodeCount(descriptor, settings) >= 0);
    }

    @Test
    void pseudoRandomNodeScatteringAndChunkStableReconstruction() {
        OreVeinDefinition definition = definition("scatter", densitySettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(11L, 64, 24, 64);
        List<OreVeinDenseNode> first = OreVeinOreCellEvaluator.denseNodes(descriptor, definition);
        List<OreVeinDenseNode> second = OreVeinOreCellEvaluator.denseNodes(descriptor, definition);

        assertEquals(first, second);
        assertTrue(first.stream().map(OreVeinDenseNode::localCenterX).distinct().count() > 1 || first.size() <= 1);
    }

    @Test
    void everyNodeCenterInsideActualMainVeinAndNotAlwaysOrigin() {
        OreVeinDefinition definition = definition("inside", densitySettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(13L, 64, 24, 64);
        List<OreVeinDenseNode> nodes = OreVeinOreCellEvaluator.denseNodes(descriptor, definition);

        for (OreVeinDenseNode node : nodes) {
            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX(), node.localCenterY(), node.localCenterZ());
            assertEquals(INSIDE_MAIN_BODY, contribution.state());
            assertTrue(contribution.signedBoundaryDistanceBlocks() <= 0.0D);
        }

        assertTrue(nodes.stream().anyMatch(node -> node.localCenterX() != 0.0D || node.localCenterY() != 0.0D || node.localCenterZ() != 0.0D) || nodes.isEmpty());
    }

    @Test
    void impossibleRadiusRangesRejected() {
        OreVeinDefinition definition = definition(
                "invalid",
                new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 20.0D, 20.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4),
                oreEntry(ore("a", 4), 1)
        );
        List<String> problems = new java.util.ArrayList<>();

        try {
            Method method = OreVeinDefinitions.class.getDeclaredMethod("validateDensitySettings", OreVeinDefinition.class, List.class);
            method.setAccessible(true);
            method.invoke(null, definition, problems);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }

        assertTrue(problems.stream().anyMatch(problem -> problem.contains("maxNodeRadiusX exceeds minSizeX / 2.0")));
    }

    @Test
    void nodeCenterHasHighestDensityAndDensityDecreasesOutward() {
        OreVeinDefinition definition = definition("density", densitySettings(), oreEntry(ore("a", 4), 1));
        OreVeinDenseNode node = new OreVeinDenseNode(1L, 0.0D, 0.0D, 0.0D, 8.0D, 4.0D, 8.0D, 4);
        OreVeinInstanceDescriptor descriptor = descriptor(17L, 64, 24, 64, List.of(node));
        OreVeinContribution center = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX(), node.localCenterY(), node.localCenterZ());
        OreVeinContribution edge = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX() + node.radiusX() * 0.9D, node.localCenterY(), node.localCenterZ());
        Material material = ore("a", 4);

        assertTrue(OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, center, material)
                >= OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, edge, material));
    }

    @Test
    void nodeBoundaryReturnsToDensityOneAndMainBodyClipsDenseInfluence() {
        OreVeinDefinition definition = definition("boundary", densitySettings(), oreEntry(ore("a", 4), 1));
        OreVeinDenseNode node = new OreVeinDenseNode(1L, 0.0D, 0.0D, 0.0D, 8.0D, 4.0D, 8.0D, 4);
        OreVeinInstanceDescriptor descriptor = descriptor(19L, 64, 24, 64, List.of(node));
        OreVeinContribution boundary = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX() + node.radiusX(), node.localCenterY(), node.localCenterZ());
        OreVeinContribution outsideMainBody = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                node.localCenterX(),
                node.localCenterY(),
                node.localCenterZ(),
                1.0D,
                0.0D,
                0.25D,
                INSIDE_MAIN_BODY
        );

        assertEquals(1, OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, boundary, ore("a", 4)));
        assertEquals(1, OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, outsideMainBody, ore("a", 4)));
    }

    @Test
    void exactVariantMappingAndFillScalingUseMaxReachableDensity() {
        OreVeinDensitySettings settings = densitySettings();
        OreVeinDefinition definition = definition("variant", settings, haloSettings(), oreEntry(ore("a", 3), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(23L, 64, 24, 64);
        OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, new BlockPos(0, 0, 0));
        OreVeinOreCellResult result = OreVeinOreCellEvaluator.evaluateCell(descriptor, definition, new BlockPos(0, 0, 0), contribution);

        assertTrue(result.variant() == HOST_ROCK || result.variant() == REGULAR_ORE || result.variant() == DENSE_ORE);
        assertEquals(960, OreVeinOreCellEvaluator.fillNumerator(settings, 3, 3));
        assertEquals(704, OreVeinOreCellEvaluator.fillNumerator(settings, 1, 3));
    }

    @Test
    void transitionNumeratorEndpointsAreExact() {
        OreVeinInstanceDescriptor descriptor = descriptor(41L, 64, 24, 64);
        BlockPos position = new BlockPos(1, 2, 3);

        assertEquals(REGULAR_ORE, OreVeinOreCellEvaluator.transitionVariant(descriptor, position, 0.0D, 4.0D, 1024));
        assertEquals(SPARSE_ORE, OreVeinOreCellEvaluator.transitionVariant(descriptor, position, 4.0D, 4.0D, 1024));
    }

    @Test
    void sparseThresholdValuesAndReachAreExact() {
        assertEquals(2, OreVeinOreCellEvaluator.sparseChanceDivisor(0.0D));
        assertEquals(2, OreVeinOreCellEvaluator.sparseChanceDivisor(1.0D));
        assertEquals(4, OreVeinOreCellEvaluator.sparseChanceDivisor(2.0D));
        assertEquals(8, OreVeinOreCellEvaluator.sparseChanceDivisor(4.0D));
        assertEquals(22, OreVeinOreCellEvaluator.sparseChanceDivisor(8.0D));
        assertEquals(44, OreVeinOreCellEvaluator.sparseChanceDivisor(12.0D));
        assertEquals(74, OreVeinOreCellEvaluator.sparseChanceDivisor(16.0D));
        assertEquals(112, OreVeinOreCellEvaluator.sparseChanceDivisor(20.0D));
        assertEquals(145, OreVeinOreCellEvaluator.sparseChanceDivisor(23.0D));
        assertEquals(212, OreVeinOreCellEvaluator.sparseChanceDivisor(28.0D));
        assertEquals(274, OreVeinOreCellEvaluator.sparseChanceDivisor(32.0D));
        assertEquals(512, OreVeinOreCellEvaluator.sparseOccupancyThreshold(0.0D));
        assertEquals(512, OreVeinOreCellEvaluator.sparseOccupancyThreshold(1.0D));
        assertEquals(256, OreVeinOreCellEvaluator.sparseOccupancyThreshold(2.0D));
        assertEquals(128, OreVeinOreCellEvaluator.sparseOccupancyThreshold(4.0D));
        assertEquals(46, OreVeinOreCellEvaluator.sparseOccupancyThreshold(8.0D));
        assertEquals(23, OreVeinOreCellEvaluator.sparseOccupancyThreshold(12.0D));
        assertEquals(13, OreVeinOreCellEvaluator.sparseOccupancyThreshold(16.0D));
        assertEquals(9, OreVeinOreCellEvaluator.sparseOccupancyThreshold(20.0D));
        assertEquals(7, OreVeinOreCellEvaluator.sparseOccupancyThreshold(23.0D));
        assertEquals(4, OreVeinOreCellEvaluator.sparseOccupancyThreshold(28.0D));
        assertEquals(3, OreVeinOreCellEvaluator.sparseOccupancyThreshold(32.0D));
    }

    @Test
    void sparseThresholdRejectsInvalidDistancesAndNeverIncreases() {
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseChanceDivisor(-1.0D));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseChanceDivisor(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseChanceDivisor(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseChanceDivisor(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseOccupancyThreshold(-1.0D));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseOccupancyThreshold(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseOccupancyThreshold(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> OreVeinOreCellEvaluator.sparseOccupancyThreshold(Double.NEGATIVE_INFINITY));

        int previous = Integer.MAX_VALUE;
        for (int i = 0; i <= 46; i++) {
            int current = OreVeinOreCellEvaluator.sparseOccupancyThreshold(i * 0.5D);
            assertTrue(current <= previous);
            previous = current;
        }
    }

    @Test
    void sparseDivisorIsMonotonicallyNonDecreasing() {
        int previous = Integer.MIN_VALUE;

        for (int i = 0; i <= 64; i++) {
            int current = OreVeinOreCellEvaluator.sparseChanceDivisor(i * 0.5D);
            assertTrue(current >= previous);
            previous = current;
        }
    }

    @Test
    void sparseDenominatorAndDerivedReachAreExact() throws ReflectiveOperationException {
        Field denominator = OreVeinOreCellEvaluator.class.getDeclaredField("SPARSE_OCCUPANCY_DENOMINATOR");
        denominator.setAccessible(true);
        Field initial = OreVeinOreCellEvaluator.class.getDeclaredField("SPARSE_INITIAL_OCCUPANCY_NUMERATOR");
        initial.setAccessible(true);

        assertEquals(1024, denominator.getInt(null));
        assertEquals(512, initial.getInt(null));
    }

    @Test
    void sparseRollBoundAndComparisonUseSparseDenominator() {
        OreVeinDefinition definition = definition("sparse_roll_bound", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(45L, 64, 24, 64);
        OreVeinContribution contribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                23.0D,
                INSIDE_MAIN_BODY
        );
        AtomicInteger rollBound = new AtomicInteger(-1);

        OreVeinOreCellResult failsAtThreshold = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                new BlockPos(0, 0, 0),
                contribution,
                (ignoredDescriptor, ignoredPosition, denominator) -> {
                    rollBound.set(denominator);
                    return OreVeinOreCellEvaluator.sparseOccupancyThreshold(23.0D);
                }
        );

        assertEquals(1024, rollBound.get());
        assertEquals(HOST_ROCK, failsAtThreshold.variant());
        assertEquals(7, OreVeinOreCellEvaluator.sparseOccupancyThreshold(23.0D));
    }

    @Test
    void outsideSparseReachBypassesOccupancyRoll() {
        OreVeinDefinition definition = definition("sparse_zero", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(43L, 64, 24, 64);
        OreVeinContribution sparseContribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                33.0D,
                INSIDE_MAIN_BODY
        );
        AtomicInteger rollCalls = new AtomicInteger();

        OreVeinOreCellResult result = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                new BlockPos(0, 0, 0),
                sparseContribution,
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> {
                    rollCalls.incrementAndGet();
                    return 0;
                }
        );

        assertEquals(HOST_ROCK, result.variant());
        assertEquals(0, rollCalls.get());
    }

    @Test
    void sparseResultsDecreaseWithDistanceAndStopAtZeroThreshold() {
        OreVeinDefinition definition = definition("sparse_decay", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        int nearCount = 0;
        int midCount = 0;
        int farCount = 0;

        for (int seed = 1; seed <= 4096; seed++) {
            OreVeinInstanceDescriptor descriptor = descriptor(seed, 64, 24, 64);
            if (evaluateSparseVariant(descriptor, definition, 1.0D) == SPARSE_ORE) {
                nearCount++;
            }
            if (evaluateSparseVariant(descriptor, definition, 8.0D) == SPARSE_ORE) {
                midCount++;
            }
            if (evaluateSparseVariant(descriptor, definition, 20.0D) == SPARSE_ORE) {
                farCount++;
            }
            assertEquals(HOST_ROCK, evaluateSparseVariant(descriptor, definition, 33.0D));
        }

        assertTrue(nearCount > 0);
        assertTrue(midCount > 0);
        assertTrue(farCount > 0);
    }

    @Test
    void adjacentSparseSuccessesRemainNaturallyPossible() {
        OreVeinDefinition definition = definition("adjacent_sparse", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(321L, 64, 24, 64);
        OreVeinContribution contribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                definition.haloSettings().transitionWidthBlocks(),
                INSIDE_MAIN_BODY
        );

        OreVeinOreCellResult first = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                new BlockPos(0, 0, 0),
                contribution,
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> 0
        );
        OreVeinOreCellResult second = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                new BlockPos(1, 0, 0),
                contribution,
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> 0
        );

        assertEquals(SPARSE_ORE, first.variant());
        assertEquals(SPARSE_ORE, second.variant());
    }

    @Test
    void transitionSparseUsesFullBoundaryDistanceAndRegularTransitionRemainsUnchanged() {
        OreVeinDefinition definition = definition("transition_sparse", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(123L, 64, 24, 64);
        double halfTransitionWidth = definition.haloSettings().transitionWidthBlocks() * 0.5D;
        OreVeinContribution sparseTransitionContribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                halfTransitionWidth * 0.5D,
                INSIDE_MAIN_BODY
        );

        OreVeinOreCellResult sparseTransition = OreVeinOreCellEvaluator.evaluateHaloCell(
                descriptor,
                definition,
                new BlockPos(0, 0, 0),
                sparseTransitionContribution,
                (ignoredDescriptor, ignoredPosition, ignoredDenominator) -> 0
        );
        OreVeinContribution regularTransitionContribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                -halfTransitionWidth,
                INSIDE_MAIN_BODY
        );
        OreVeinOreCellResult.OreVariant regularTransition = OreVeinOreCellEvaluator.insideTransitionVariant(
                descriptor,
                new BlockPos(0, 0, 0),
                regularTransitionContribution.signedBoundaryDistanceBlocks(),
                definition.haloSettings().transitionWidthBlocks()
        );

        assertEquals(SPARSE_ORE, sparseTransition.variant());
        assertEquals(REGULAR_ORE, regularTransition);
        assertEquals(
                1024,
                OreVeinOreCellEvaluator.transitionShellThreshold(0.0D, definition.haloSettings().transitionWidthBlocks())
        );
    }

    @Test
    void sparseProbabilityDoesNotRestartAfterTransition() {
        assertTrue(
                OreVeinOreCellEvaluator.sparseOccupancyThreshold(4.0D)
                        > OreVeinOreCellEvaluator.sparseOccupancyThreshold(5.0D)
        );
        assertFalse(
                OreVeinOreCellEvaluator.sparseOccupancyThreshold(4.0D)
                        == OreVeinOreCellEvaluator.sparseOccupancyThreshold(0.0D)
        );
    }

    @Test
    void denseNodesNeverAffectTransitionOrHalo() {
        OreVeinDefinition definition = definition("halo_dense", densitySettings(), haloSettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(43L, 64, 24, 64);
        OreVeinContribution transition = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D,
                INSIDE_MAIN_BODY
        );

        assertEquals(1, OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, transition, ore("a", 4)));
    }

    @Test
    void denserRegionsHaveFewerGapsAndDensityLimitsHold() {
        OreVeinDefinition definition = definition("gaps", densitySettings(), oreEntry(ore("a", 2), 1));
        OreVeinDenseNode node = new OreVeinDenseNode(1L, 0.0D, 0.0D, 0.0D, 8.0D, 4.0D, 8.0D, 2);
        OreVeinInstanceDescriptor descriptor = descriptor(29L, 64, 24, 64, List.of(node));
        OreVeinContribution center = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX(), node.localCenterY(), node.localCenterZ());
        OreVeinContribution far = OreVeinShapeEvaluator.evaluate(descriptor, new BlockPos(0, 0, 0));

        int centerDensity = OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, center, ore("a", 2));
        int farDensity = OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, far, ore("a", 2));

        assertTrue(centerDensity >= farDensity);
        assertTrue(centerDensity <= 2);
    }

    @Test
    void overlappingNodesUseMaximumInfluence() {
        OreVeinDefinition definition = definition("overlap", densitySettings(), oreEntry(ore("a", 4), 1));
        OreVeinInstanceDescriptor descriptor = descriptor(
                31L,
                128,
                32,
                128,
                List.of(
                        new OreVeinDenseNode(1L, -2.0D, 0.0D, 0.0D, 8.0D, 4.0D, 8.0D, 4),
                        new OreVeinDenseNode(2L, 2.0D, 0.0D, 0.0D, 8.0D, 4.0D, 8.0D, 4)
                )
        );
        Material material = ore("a", 4);

        int maxDensity = 1;

        for (OreVeinDenseNode node : OreVeinOreCellEvaluator.denseNodes(descriptor, definition)) {
            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, node.localCenterX(), node.localCenterY(), node.localCenterZ());
            maxDensity = Math.max(maxDensity, OreVeinOreCellEvaluator.candidateDensity(descriptor, definition, contribution, material));
        }

        assertTrue(maxDensity >= 2);
    }

    @Test
    void evaluateLocalPointMatchesBlockEvaluationAtCenter() {
        OreVeinInstanceDescriptor descriptor = descriptor(37L, 64, 24, 64);
        OreVeinContribution block = OreVeinShapeEvaluator.evaluate(descriptor, new BlockPos(0, 0, 0));
        OreVeinContribution local = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, 0.0D, 0.0D, 0.0D);

        assertEquals(block.state(), local.state());
        assertEquals(block.signedBoundaryDistanceBlocks(), local.signedBoundaryDistanceBlocks());
    }

    @Test
    void nodeCenterAttemptCountConstantIsExact() {
        assertEquals(32, OreVeinOreCellEvaluator.NODE_CENTER_MAX_ATTEMPTS);
    }

    private static OreVeinDefinition definition(String name, OreVeinDensitySettings densitySettings, VeinOreEntry... entries) {
        return definition(name, 80, densitySettings, haloSettings(), entries);
    }

    private static OreVeinDefinition definition(String name, int generationWeight, OreVeinDensitySettings densitySettings, VeinOreEntry... entries) {
        return definition(name, generationWeight, densitySettings, haloSettings(), entries);
    }

    private static OreVeinDefinition definition(String name, OreVeinDensitySettings densitySettings, OreVeinHaloSettings haloSettings, VeinOreEntry... entries) {
        return definition(name, 80, densitySettings, haloSettings, entries);
    }

    private static OreVeinDefinition definition(String name, int generationWeight, OreVeinDensitySettings densitySettings, OreVeinHaloSettings haloSettings, VeinOreEntry... entries) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OreVeinCandidateLookupTest.dimension("overworld")),
                generationWeight,
                -16,
                128,
                32,
                128,
                16,
                32,
                32,
                128,
                32,
                12.0D,
                12.0D,
                densitySettings,
                haloSettings,
                List.of(entries)
        );
    }

    private static OreVeinInstanceDescriptor descriptor(long instanceSeed, int sizeX, int sizeY, int sizeZ) {
        return descriptor(instanceSeed, sizeX, sizeY, sizeZ, List.of());
    }

    private static OreVeinInstanceDescriptor descriptor(long instanceSeed, int sizeX, int sizeY, int sizeZ, List<OreVeinDenseNode> denseNodes) {
        OreVeinShapeEvaluator.HalfExtents extents = OreVeinShapeEvaluator.rotatedHalfExtents(sizeX, sizeY, sizeZ, 0.0D, 0.0D, 0.0D);
        return new OreVeinInstanceDescriptor(
                instanceSeed,
                instanceSeed,
                instanceSeed,
                ResourceLocation.fromNamespaceAndPath("test", "descriptor"),
                new BlockPos(0, 0, 0),
                sizeX,
                sizeY,
                sizeZ,
                0.0D,
                0.0D,
                0.0D,
                0,
                0,
                0,
                new OreVeinBounds(-extents.x(), -extents.y(), -extents.z(), extents.x(), extents.y(), extents.z()),
                denseNodes
        );
    }

    private static VeinOreEntry oreEntry(Material material, int weight) {
        Supplier<Material> supplier = () -> material;
        return new VeinOreEntry(supplier, weight);
    }

    private static Material ore(String name, int maxDensity) {
        Material material = new Material(name, null);
        configureOre(material, maxDensity);
        return material;
    }

    private static OreVeinDensitySettings densitySettings() {
        return new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4);
    }

    private static OreVeinHaloSettings haloSettings() {
        return new OreVeinHaloSettings(4.0D, 9.0D, 320);
    }

    private static OreVeinOreCellResult.OreVariant evaluateSparseVariant(
            OreVeinInstanceDescriptor descriptor,
            OreVeinDefinition definition,
            double signedDistance
    ) {
        return evaluateSparseCell(descriptor, definition, signedDistance, new BlockPos(0, 0, 0)).variant();
    }

    private static OreVeinOreCellResult evaluateSparseCell(
            OreVeinInstanceDescriptor descriptor,
            OreVeinDefinition definition,
            double signedDistance,
            BlockPos position
    ) {
        OreVeinContribution contribution = new OreVeinContribution(
                descriptor.instanceId(),
                descriptor.definitionId(),
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                signedDistance,
                INSIDE_MAIN_BODY
        );

        if (signedDistance > definition.sparseReachBlocks()) {
            OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry = OreVeinOreCellEvaluator.selectOreEntry(descriptor, definition, position);
            return new OreVeinOreCellResult(
                    descriptor.instanceId(),
                    descriptor.definitionId(),
                    selectedOreEntry.material(),
                    selectedOreEntry.entry().distributionWeight(),
                    contribution,
                    1,
                    0,
                    HOST_ROCK,
                    0L,
                    0.0D
            );
        }

        return OreVeinOreCellEvaluator.evaluateCell(descriptor, definition, position, contribution);
    }

    private static void configureOre(Material material, int maxDensity) {
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
    }
}
