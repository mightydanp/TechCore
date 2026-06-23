package com.mightydanp.techcore.world.level.levelgen.vein.densenode;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinDefinition;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.sparsetransition.OreVeinSparseTransitionEvaluator;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator.OreCellResult.OreVariant.*;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator.ShapeContribution.ContributionState.INSIDE_MAIN_BODY;

public final class OreVeinDenseNodeEvaluator {
    private static final long NODE_COUNT_SALT = 0x4F1BBCDCBFA54001L;
    private static final long NODE_ID_SALT = 0x0F7A2098E561F123L;
    private static final long NODE_CENTER_X_SALT = 0x2C1B3C6D5E7F8101L;
    private static final long NODE_CENTER_Y_SALT = 0x3D2C4B5A69788711L;
    private static final long NODE_CENTER_Z_SALT = 0x5A4C3E2F17081921L;
    private static final long NODE_RADIUS_X_SALT = 0x6B5D4C3B2A190837L;
    private static final long NODE_RADIUS_Y_SALT = 0x7C6E5D4C3B2A1941L;
    private static final long NODE_RADIUS_Z_SALT = 0x8D7F6E5D4C3B2A51L;
    private static final long NODE_PEAK_SALT = 0x9E807F6E5D4C3B61L;
    private static final int NODE_CENTER_MAX_ATTEMPTS = 32;

    public static OreVeinOreCellEvaluator.OreCellResult evaluateMainBodyCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinShapeEvaluator.ShapeContribution contribution, @NotNull OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry) {
        // Dense nodes increase the density and fill chance for blocks in the main body
        DenseNodeOutcome denseNodeOutcome = denseNodeOutcome(descriptor, definition, contribution, selectedOreEntry.material());
        int candidateDensity = candidateDensity(definition, denseNodeOutcome, selectedOreEntry.material());
        int maxReachableDensity = maxReachableDensity(definition.densitySettings(), selectedOreEntry.material());
        int fillNumerator = fillNumerator(definition.densitySettings(), candidateDensity, maxReachableDensity);
        int occupancyRoll = OreVeinOreCellEvaluator.occupancyRoll(descriptor, position, OreVeinOreCellEvaluator.occupancySalt(), definition.densitySettings().fillDenominator());
        int finalDensity = occupancyRoll < fillNumerator ? candidateDensity : 0;

        if (finalDensity <= 0)
            return OreVeinOreCellEvaluator.createResult(descriptor, selectedOreEntry, contribution, candidateDensity, finalDensity, HOST_ROCK, denseNodeOutcome.nodeId(), denseNodeOutcome.influence());
        if (denseNodeOutcome.nodeId() != 0L)
            return OreVeinOreCellEvaluator.createResult(descriptor, selectedOreEntry, contribution, candidateDensity, finalDensity, DENSE_ORE, denseNodeOutcome.nodeId(), denseNodeOutcome.influence());

        if (OreVeinSparseTransitionEvaluator.isInsideTransitionHalf(contribution.signedBoundaryDistanceBlocks(), definition.haloSettings().transitionWidthBlocks())) {
            OreVeinOreCellEvaluator.OreCellResult.OreVariant transitionVariant = OreVeinSparseTransitionEvaluator.insideTransitionVariant(
                    descriptor,
                    position,
                    contribution.signedBoundaryDistanceBlocks(),
                    definition.haloSettings().transitionWidthBlocks()
            );

            if (transitionVariant == SPARSE_ORE) return OreVeinOreCellEvaluator.sparseOreResult(descriptor, selectedOreEntry, contribution);
        }

        return OreVeinOreCellEvaluator.createResult(
                descriptor,
                selectedOreEntry,
                contribution,
                candidateDensity,
                finalDensity,
                finalDensity == 1 ? OreVeinOreCellEvaluator.OreCellResult.OreVariant.REGULAR_ORE : DENSE_ORE,
                denseNodeOutcome.nodeId(),
                denseNodeOutcome.influence()
        );
    }

    public static int nodeCount(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition.DensitySettings densitySettings) {
        // Convert the descriptor volume into a clamped dense-node count.
        BigInteger volume = BigInteger.valueOf(descriptor.sizeX()).multiply(BigInteger.valueOf(descriptor.sizeY())).multiply(BigInteger.valueOf(descriptor.sizeZ()));
        BigInteger count = getBigInteger(descriptor, densitySettings, volume);
        BigInteger min = BigInteger.valueOf(densitySettings.minNodeCount());
        BigInteger max = BigInteger.valueOf(densitySettings.maxNodeCount());

        if (count.compareTo(min) < 0) count = min;
        if (count.compareTo(max) > 0) count = max;

        return count.intValueExact();
    }

    private static @NotNull BigInteger getBigInteger(@NotNull OreVeinInstanceDescriptor descriptor, OreVeinDefinition.@NotNull DensitySettings densitySettings, @NotNull BigInteger volume) {
        BigInteger blocksPerDenseNode = BigInteger.valueOf(densitySettings.blocksPerDenseNode());
        BigInteger baseCount = volume.divide(blocksPerDenseNode);
        BigInteger remainder = volume.remainder(blocksPerDenseNode);
        long roll = Math.floorMod(OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ NODE_COUNT_SALT), densitySettings.blocksPerDenseNode());
        BigInteger extra = BigInteger.valueOf(roll).compareTo(remainder) < 0 ? BigInteger.ONE : BigInteger.ZERO;
        return baseCount.add(extra);
    }

    public static @NotNull DenseNodeOutcome denseNodeOutcome(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, Material selectedMaterial) {
        // Dense nodes only matter for cells that are still inside the main body.
        if (contribution.state() != INSIDE_MAIN_BODY || contribution.signedBoundaryDistanceBlocks() > 0.0D) return DenseNodeOutcome.NONE;

        int maxReachableDensity = maxReachableDensity(definition.densitySettings(), selectedMaterial);

        if (maxReachableDensity <= 1) return DenseNodeOutcome.NONE;

        double bestInfluence = 0.0D;
        long bestNodeId = 0L;

        for (OreVeinInstanceDescriptor.DenseNode node : descriptor.denseNodes()) {
            double influence = nodeInfluence(node, contribution, maxReachableDensity);

            if (influence > bestInfluence) {
                bestInfluence = influence;
                bestNodeId = node.nodeId();
            }
        }

        return bestInfluence > 0.0D ? new DenseNodeOutcome(bestNodeId, bestInfluence) : DenseNodeOutcome.NONE;
    }

    public static int candidateDensity(@NotNull OreVeinDefinition definition, @NotNull DenseNodeOutcome denseNodeOutcome, Material selectedMaterial) {
        // Convert the strongest dense-node influence into a valid density tier.
        int maxReachableDensity = maxReachableDensity(definition.densitySettings(), selectedMaterial);
        int candidateDensity = 1 + (int) Math.round(denseNodeOutcome.influence());
        return Math.max(1, Math.min(maxReachableDensity, candidateDensity));
    }

    public static int maxReachableDensity(@NotNull OreVeinDefinition.DensitySettings densitySettings, @NotNull Material selectedMaterial) {
        return Math.min(densitySettings.maxPeakDensity(), Math.min(DenseOre.MAX_DENSITY_PROPERTY, selectedMaterial.ore.getMaxDensity()));
    }

    public static int fillNumerator(@NotNull OreVeinDefinition.DensitySettings densitySettings, int candidateDensity, int maxReachableDensity) {
        return densitySettings.regularFillNumerator()
                + Math.floorDiv(
                (candidateDensity - 1) * (densitySettings.maximumFillNumerator() - densitySettings.regularFillNumerator()),
                Math.max(1, maxReachableDensity - 1)
        );
    }

    public static boolean isInsideDenseNodeVolume(OreVeinInstanceDescriptor.@NotNull DenseNode node, double localX, double localY, double localZ) {
        // Check whether the local point falls inside the node's ellipsoid volume.
        double dx = localX - node.localCenterX();
        double dy = localY - node.localCenterY();
        double dz = localZ - node.localCenterZ();
        double normalizedDistanceSquared = square(dx / node.radiusX()) + square(dy / node.radiusY()) + square(dz / node.radiusZ());

        return normalizedDistanceSquared < 1.0D;
    }

    public static long nodeIdSalt() {
        return NODE_ID_SALT;
    }

    public static long nodeCenterXSalt() {
        return NODE_CENTER_X_SALT;
    }

    public static long nodeCenterYSalt() {
        return NODE_CENTER_Y_SALT;
    }

    public static long nodeCenterZSalt() {
        return NODE_CENTER_Z_SALT;
    }

    public static long nodeRadiusXSalt() {
        return NODE_RADIUS_X_SALT;
    }

    public static long nodeRadiusYSalt() {
        return NODE_RADIUS_Y_SALT;
    }

    public static long nodeRadiusZSalt() {
        return NODE_RADIUS_Z_SALT;
    }

    public static long nodePeakSalt() {
        return NODE_PEAK_SALT;
    }

    public static int nodeCenterMaxAttempts() {
        return NODE_CENTER_MAX_ATTEMPTS;
    }

    private static double nodeInfluence(OreVeinInstanceDescriptor.@NotNull DenseNode node, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, int maxReachableDensity) {
        double dx = contribution.localX() - node.localCenterX();
        double dy = contribution.localY() - node.localCenterY();
        double dz = contribution.localZ() - node.localCenterZ();
        double normalizedDistanceSquared = square(dx / node.radiusX()) + square(dy / node.radiusY()) + square(dz / node.radiusZ());

        if (normalizedDistanceSquared >= 1.0D) return 0.0D;

        int materialPeak = Math.min(node.configuredPeakDensity(), maxReachableDensity);

        if (materialPeak <= 1) return 0.0D;

        double t = 1.0D - Math.sqrt(normalizedDistanceSquared);
        double falloff = t * t * (3.0D - 2.0D * t);

        return falloff * (materialPeak - 1);
    }

    private static double square(double value) {
        return value * value;
    }

    public record DenseNodeOutcome(long nodeId, double influence) {
        public static final DenseNodeOutcome NONE = new DenseNodeOutcome(0L, 0.0D);
    }
}
