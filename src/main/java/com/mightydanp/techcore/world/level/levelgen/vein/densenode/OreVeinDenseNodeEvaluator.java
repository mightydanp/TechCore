package com.mightydanp.techcore.world.level.levelgen.vein.densenode;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;
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
    private static final int DENSE_NODE_PRIORITY = 30;

    public static OreVeinOreCellEvaluator.OreCellResult applyMainBodyCell(OreVeinInstanceDescriptor descriptor, BlockPos position, DenseNodeVeinFeature.Config config, DenseNodeVeinFeature.@NotNull State state, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, @NotNull OreVeinOreCellEvaluator.SelectedOreEntry selectedOreEntry, OreVeinOreCellEvaluator.OreCellResult currentResult) {
        DenseNodeOutcome denseNodeOutcome = denseNodeOutcome(state.nodes(), config, contribution, selectedOreEntry.material());
        if (denseNodeOutcome.nodeId() == 0L) return currentResult;

        int candidateDensity = candidateDensity(config, denseNodeOutcome, selectedOreEntry.material());
        if (candidateDensity <= currentResult.finalDensity()) return currentResult;

        if (currentResult.finalDensity() <= 0 && !denseFillPasses(descriptor, position, config)) return currentResult;

        return OreVeinOreCellEvaluator.createResult(
                descriptor,
                selectedOreEntry,
                contribution,
                candidateDensity,
                candidateDensity,
                OreVeinOreCellEvaluator.OreCellResult.OreVariant.FEATURE_ORE,
                new DenseNodeOreReplacement(denseNodeOutcome.nodeId(), denseNodeOutcome.influence())
        );
    }

    private static @NotNull DenseNodeOutcome denseNodeOutcome(List<DenseNodeVeinFeature.Node> nodes, DenseNodeVeinFeature.Config config, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, Material selectedMaterial) {
        if (contribution.state() != INSIDE_MAIN_BODY || contribution.signedBoundaryDistanceBlocks() > 0.0D) return DenseNodeOutcome.NONE;

        int maxReachableDensity = maxReachableDensity(config, selectedMaterial);
        if (maxReachableDensity <= 1) return DenseNodeOutcome.NONE;

        double bestInfluence = 0.0D;
        long bestNodeId = 0L;

        for (DenseNodeVeinFeature.Node node : nodes) {
            double influence = nodeInfluence(node, contribution, maxReachableDensity);

            if (influence > bestInfluence) {
                bestInfluence = influence;
                bestNodeId = node.nodeId();
            }
        }

        return bestInfluence > 0.0D ? new DenseNodeOutcome(bestNodeId, bestInfluence) : DenseNodeOutcome.NONE;
    }

    private static int candidateDensity(@NotNull DenseNodeVeinFeature.Config config, @NotNull DenseNodeOutcome denseNodeOutcome, Material selectedMaterial) {
        int maxReachableDensity = maxReachableDensity(config, selectedMaterial);
        int candidateDensity = 1 + (int) Math.round(denseNodeOutcome.influence());
        return Math.max(1, Math.min(maxReachableDensity, candidateDensity));
    }

    private static boolean denseFillPasses(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull BlockPos position, @NotNull DenseNodeVeinFeature.Config config) {
        if (config.maxFillNumerator() <= 0) return false;

        return OreVeinOreCellEvaluator.occupancyRoll(
                descriptor,
                position,
                OreVeinOreCellEvaluator.occupancySalt(),
                OreVeinOreCellEvaluator.baseMainBodyFillDenominator()
        ) < config.maxFillNumerator();
    }

    private static int maxReachableDensity(@NotNull DenseNodeVeinFeature.Config config, @NotNull Material selectedMaterial) {
        return Math.min(config.maxPeakDensity(), Math.min(DenseOre.MAX_DENSITY_PROPERTY, selectedMaterial.ore.getMaxDensity()));
    }

    public static long nodeCountSalt() {
        return NODE_COUNT_SALT;
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

    private static double nodeInfluence(DenseNodeVeinFeature.@NotNull Node node, @NotNull OreVeinShapeEvaluator.ShapeContribution contribution, int maxReachableDensity) {
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

    public record DenseNodeOreReplacement(long nodeId, double influence) implements OreVeinOreCellEvaluator.OreReplacement {
        public DenseNodeOreReplacement {
            if (!Double.isFinite(influence)) throw new IllegalArgumentException("influence must be finite");
        }

        @Override
        public int priority() {
            return DENSE_NODE_PRIORITY;
        }

        @Override
        public @NotNull BlockState resolve(ResourceKey<Level> dimension, BlockPos position, Material originalHostMaterial, OreVeinOreCellEvaluator.OreCellResult winner) {
            Supplier<Block> supplier = OreVeinOreCellEvaluator.supplierFor(winner.selectedMaterial().ore.getDenseOreBlocks(), originalHostMaterial.name, dimension, position, originalHostMaterial, winner);
            Block block = supplier.get();

            if (block == null) throw OreVeinOreCellEvaluator.invalidReplacement("null block supplier", dimension, position, originalHostMaterial, winner);

            BlockState state = block.defaultBlockState();

            if (!state.hasProperty(DenseOre.DENSITY))
                throw OreVeinOreCellEvaluator.invalidReplacement("dense block missing DenseOre.DENSITY", dimension, position, originalHostMaterial, winner);

            if (!DenseOre.DENSITY.getPossibleValues().contains(winner.finalDensity()))
                throw OreVeinOreCellEvaluator.invalidReplacement("unsupported exact density value", dimension, position, originalHostMaterial, winner);

            return state.setValue(DenseOre.DENSITY, winner.finalDensity());
        }
    }
}
