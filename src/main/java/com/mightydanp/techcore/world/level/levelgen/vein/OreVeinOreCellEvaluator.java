package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellResult.OreVariant.*;

public final class OreVeinOreCellEvaluator {
    private static final long MATERIAL_SELECTION_SALT = 0xD1B54A32D192ED03L;
    private static final long NODE_COUNT_SALT = 0x4F1BBCDCBFA54001L;
    private static final long NODE_ID_SALT = 0x0F7A2098E561F123L;
    private static final long NODE_CENTER_X_SALT = 0x2C1B3C6D5E7F8101L;
    private static final long NODE_CENTER_Y_SALT = 0x3D2C4B5A69788711L;
    private static final long NODE_CENTER_Z_SALT = 0x5A4C3E2F17081921L;
    private static final long NODE_RADIUS_X_SALT = 0x6B5D4C3B2A190837L;
    private static final long NODE_RADIUS_Y_SALT = 0x7C6E5D4C3B2A1941L;
    private static final long NODE_RADIUS_Z_SALT = 0x8D7F6E5D4C3B2A51L;
    private static final long NODE_PEAK_SALT = 0x9E807F6E5D4C3B61L;
    private static final long OCCUPANCY_SALT = 0x8CB92BA72F3D8DD7L;
    private static final long TRANSITION_VARIANT_SALT = 0xC6A4A7935BD1E995L;
    private static final long HALO_OCCUPANCY_SALT = 0xDB4F0B9175AE2165L;
    private static final int SPARSE_OCCUPANCY_DENOMINATOR = 1024;
    private static final int SPARSE_INITIAL_OCCUPANCY_NUMERATOR = 512;

    private static final int NODE_CENTER_MAX_ATTEMPTS = 32;

    private static final long X_HASH_MULTIPLIER = 0x9E3779B97F4A7C15L;
    private static final long Y_HASH_MULTIPLIER = 0xC2B2AE3D27D4EB4FL;
    private static final long Z_HASH_MULTIPLIER = 0x165667B19E3779F9L;

    public static OreVeinOreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, BlockPos position, OreVeinContribution contribution) {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(contribution, "contribution");

        OreVeinDefinition definition = Objects.requireNonNull(
                OreVeinDefinitions.getDefinition(descriptor.definitionId()),
                "Missing ore vein definition: " + descriptor.definitionId()
        );

        return evaluateCell(descriptor, definition, position, contribution);
    }

    public static OreVeinOreCellResult evaluateCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, @NotNull OreVeinContribution contribution) {
        double signedBoundaryDistanceBlocks = contribution.signedBoundaryDistanceBlocks();

        SelectedOreEntry selectedOreEntry = selectOreEntry(descriptor, definition, position);
        DenseNodeOutcome denseNodeOutcome = denseNodeOutcome(descriptor, definition, contribution, selectedOreEntry.material());
        if (signedBoundaryDistanceBlocks <= 0.0D) {
            int occupancyRoll = occupancyRoll(descriptor, position, definition.densitySettings().fillDenominator());
            return evaluateMainBodyCell(
                    descriptor,
                    definition,
                    position,
                    contribution,
                    selectedOreEntry,
                    denseNodeOutcome,
                    occupancyRoll
            );
        }

        return evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry);
    }

    private static OreVeinOreCellResult evaluateMainBodyCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinContribution contribution, @NotNull SelectedOreEntry selectedOreEntry, DenseNodeOutcome denseNodeOutcome, int occupancyRoll) {
        int candidateDensity = candidateDensity(definition, denseNodeOutcome, selectedOreEntry.material());
        int maxReachableDensity = maxReachableDensity(definition.densitySettings(), selectedOreEntry.material());
        int fillNumerator = fillNumerator(definition.densitySettings(), candidateDensity, maxReachableDensity);
        int finalDensity = occupancyRoll < fillNumerator ? candidateDensity : 0;
        CellResultFactory results = new CellResultFactory(descriptor, selectedOreEntry, contribution);

        if (finalDensity <= 0) return results.create(
                candidateDensity,
                finalDensity,
                HOST_ROCK,
                denseNodeOutcome.nodeId(),
                denseNodeOutcome.influence()
        );

        if (denseNodeOutcome.nodeId() != 0L) return results.create(
                candidateDensity,
                finalDensity,
                DENSE_ORE,
                denseNodeOutcome.nodeId(),
                denseNodeOutcome.influence()
        );

        if (isInsideTransitionHalf(contribution.signedBoundaryDistanceBlocks(), definition.haloSettings().transitionWidthBlocks())) {
            OreVeinOreCellResult.OreVariant transitionVariant = insideTransitionVariant(
                    descriptor,
                    position,
                    contribution.signedBoundaryDistanceBlocks(),
                    definition.haloSettings().transitionWidthBlocks()
            );

            if (transitionVariant == SPARSE_ORE) return results.sparseOre();
        }

        return results.create(
                candidateDensity,
                finalDensity,
                variant(finalDensity),
                denseNodeOutcome.nodeId(),
                denseNodeOutcome.influence()
        );
    }

    private static OreVeinOreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinContribution contribution, SelectedOreEntry selectedOreEntry) {
        return evaluateHaloCell(descriptor, definition, position, contribution, selectedOreEntry,
                (rollDescriptor, rollPosition, fillDenominator) -> occupancyRoll(rollDescriptor, rollPosition, HALO_OCCUPANCY_SALT, fillDenominator));
    }

    private static OreVeinOreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinContribution contribution, HaloOccupancyRollProvider occupancyRollProvider) {
        return evaluateHaloCell(
                descriptor,
                definition,
                position,
                contribution,
                selectOreEntry(descriptor, definition, position),
                occupancyRollProvider
        );
    }

    private static OreVeinOreCellResult evaluateHaloCell(OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, BlockPos position, @NotNull OreVeinContribution contribution, SelectedOreEntry selectedOreEntry, HaloOccupancyRollProvider occupancyRollProvider) {
        double signedBoundaryDistanceBlocks = contribution.signedBoundaryDistanceBlocks();
        double transitionWidthBlocks = definition.haloSettings().transitionWidthBlocks();
        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        CellResultFactory results = new CellResultFactory(descriptor, selectedOreEntry, contribution);

        if (transitionWidthBlocks <= 0.0D || signedBoundaryDistanceBlocks > halfTransitionWidth) {
            return sparseResult(
                    descriptor,
                    definition,
                    position,
                    contribution,
                    selectedOreEntry,
                    occupancyRollProvider
            );
        }

        int threshold = transitionShellThreshold(signedBoundaryDistanceBlocks, transitionWidthBlocks);
        int sparseOccupancyRoll = occupancyRollProvider.roll(descriptor, position, SPARSE_OCCUPANCY_DENOMINATOR);

        if (sparseOccupancyRoll >= threshold) return results.hostRock();

        return results.sparseOre();
    }


    private static int sparseChanceDivisor(double distanceFromMainBody) {
        if (!Double.isFinite(distanceFromMainBody) || distanceFromMainBody < 0.0D) throw new IllegalArgumentException(
                    "distanceFromMainBody must be finite and non-negative"
        );


        return 2 + Mth.floor(
                (distanceFromMainBody
                                * (distanceFromMainBody + 2.0D)
                ) / 4.0D
        );
    }

    private static int sparseOccupancyThreshold(double distanceFromMainBody) {
        return Math.floorDiv(
                SPARSE_OCCUPANCY_DENOMINATOR,
                sparseChanceDivisor(distanceFromMainBody)
        );
    }

    @Contract("_, _, _ -> new")
    private static @NotNull SelectedOreEntry selectOreEntry(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, @NotNull BlockPos position) {
        long hash = descriptor.instanceId() ^ MATERIAL_SELECTION_SALT;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;
        hash = mix64(hash);

        long totalWeight = definition.totalDistributionWeight();
        long roll = Math.floorMod(hash, totalWeight);
        long cursor = 0L;

        for (VeinOreEntry entry : definition.oreEntries()) {
            cursor += entry.distributionWeight();

            if (roll < cursor) {
                Material material = Objects.requireNonNull(entry.oreMaterial().get(), "oreMaterial supplier returned null");
                return new SelectedOreEntry(entry, material);
            }
        }

        throw new IllegalStateException("Failed to select ore entry for " + definition.id());
    }

    public static int nodeCount(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDensitySettings densitySettings) {
        BigInteger volume = BigInteger.valueOf(descriptor.sizeX())
                .multiply(BigInteger.valueOf(descriptor.sizeY()))
                .multiply(BigInteger.valueOf(descriptor.sizeZ()));
        BigInteger blocksPerDenseNode = BigInteger.valueOf(densitySettings.blocksPerDenseNode());
        BigInteger baseCount = volume.divide(blocksPerDenseNode);
        BigInteger remainder = volume.remainder(blocksPerDenseNode);
        long roll = Math.floorMod(mix64(descriptor.instanceSeed() ^ NODE_COUNT_SALT), densitySettings.blocksPerDenseNode());
        BigInteger extra = BigInteger.valueOf(roll).compareTo(remainder) < 0 ? BigInteger.ONE : BigInteger.ZERO;
        BigInteger count = baseCount.add(extra);
        BigInteger min = BigInteger.valueOf(densitySettings.minNodeCount());
        BigInteger max = BigInteger.valueOf(densitySettings.maxNodeCount());

        if (count.compareTo(min) < 0) count = min;

        if (count.compareTo(max) > 0) count = max;

        return count.intValueExact();
    }

    @Contract(pure = true)
    private static List<OreVeinInstanceDescriptor.DenseNode> denseNodes(@NotNull OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition) {
        return descriptor.denseNodes();
    }

    private static DenseNodeOutcome denseNodeOutcome(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, @NotNull OreVeinContribution contribution, Material selectedMaterial) {
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

    private static int candidateDensity(@NotNull OreVeinDefinition definition, @NotNull DenseNodeOutcome denseNodeOutcome, Material selectedMaterial) {
        int maxReachableDensity = maxReachableDensity(definition.densitySettings(), selectedMaterial);
        int candidateDensity = 1 + (int) Math.round(denseNodeOutcome.influence());
        return Math.max(1, Math.min(maxReachableDensity, candidateDensity));
    }

    private static int candidateDensity(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, OreVeinContribution contribution, Material selectedMaterial) {
        return candidateDensity(definition, denseNodeOutcome(descriptor, definition, contribution, selectedMaterial), selectedMaterial);
    }

    private static int maxReachableDensity(@NotNull OreVeinDensitySettings densitySettings, @NotNull Material selectedMaterial) {
        return Math.min(
                densitySettings.maxPeakDensity(),
                Math.min(DenseOre.MAX_DENSITY_PROPERTY, selectedMaterial.ore.getMaxDensity())
        );
    }

    private static int fillNumerator(@NotNull OreVeinDensitySettings densitySettings, int candidateDensity, int maxReachableDensity) {
        return densitySettings.regularFillNumerator()
                + Math.floorDiv(
                (candidateDensity - 1) * (densitySettings.maximumFillNumerator() - densitySettings.regularFillNumerator()),
                Math.max(1, maxReachableDensity - 1)
        );
    }

    private static OreVeinOreCellResult.OreVariant insideTransitionVariant(OreVeinInstanceDescriptor descriptor, BlockPos position, double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double insideTransitionProgress = Mth.clamp(
                (signedBoundaryDistanceBlocks + halfTransitionWidth) / halfTransitionWidth,
                0.0D,
                1.0D
        );
        int sparseFormThreshold = Mth.floor(insideTransitionProgress * 1024.0D);
        int variantRoll = occupancyRoll(descriptor, position, TRANSITION_VARIANT_SALT, SPARSE_OCCUPANCY_DENOMINATOR);

        return variantRoll < sparseFormThreshold ? SPARSE_ORE : REGULAR_ORE;
    }

    public static long mix64(long value) {
        long mixed = value;
        mixed ^= mixed >>> 30;
        mixed *= 0xBF58476D1CE4E5B9L;
        mixed ^= mixed >>> 27;
        mixed *= 0x94D049BB133111EBL;
        mixed ^= mixed >>> 31;
        return mixed;
    }

    public static boolean isInsideDenseNodeVolume(OreVeinInstanceDescriptor.@NotNull DenseNode node, double localX, double localY, double localZ) {
        double dx = localX - node.localCenterX();
        double dy = localY - node.localCenterY();
        double dz = localZ - node.localCenterZ();
        double normalizedDistanceSquared =
                square(dx / node.radiusX())
                        + square(dy / node.radiusY())
                        + square(dz / node.radiusZ());

        return normalizedDistanceSquared < 1.0D;
    }

    private static double nodeInfluence(OreVeinInstanceDescriptor.@NotNull DenseNode node, @NotNull OreVeinContribution contribution, int maxReachableDensity) {
        double dx = contribution.localX() - node.localCenterX();
        double dy = contribution.localY() - node.localCenterY();
        double dz = contribution.localZ() - node.localCenterZ();
        double normalizedDistanceSquared =
                square(dx / node.radiusX())
                        + square(dy / node.radiusY())
                        + square(dz / node.radiusZ());

        if (normalizedDistanceSquared >= 1.0D) return 0.0D;

        int materialPeak = Math.min(node.configuredPeakDensity(), maxReachableDensity);

        if (materialPeak <= 1) return 0.0D;

        double t = 1.0D - Math.sqrt(normalizedDistanceSquared);
        double falloff = t * t * (3.0D - 2.0D * t);

        return falloff * (materialPeak - 1);
    }

    private static OreVeinOreCellResult sparseResult(OreVeinInstanceDescriptor descriptor, @NotNull OreVeinDefinition definition, BlockPos position, @NotNull OreVeinContribution contribution, SelectedOreEntry selectedOreEntry, HaloOccupancyRollProvider occupancyRollProvider) {
        double distanceFromMainBody = contribution.signedBoundaryDistanceBlocks();
        int sparseReachBlocks = definition.sparseReachBlocks();
        CellResultFactory results = new CellResultFactory(descriptor, selectedOreEntry, contribution);

        if (distanceFromMainBody > sparseReachBlocks) return results.hostRock();

        int threshold = sparseOccupancyThreshold(distanceFromMainBody);

        if (threshold == 0) return results.hostRock();

        int sparseOccupancyRoll = occupancyRollProvider.roll(descriptor, position, SPARSE_OCCUPANCY_DENOMINATOR);

        if (sparseOccupancyRoll >= threshold) return results.hostRock();

        return results.sparseOre();
    }

    private static boolean isInsideTransitionHalf(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        if (transitionWidthBlocks <= 0.0D) return false;

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;

        return signedBoundaryDistanceBlocks > -halfTransitionWidth && signedBoundaryDistanceBlocks <= 0.0D;
    }

    private static int transitionShellThreshold(double signedBoundaryDistanceBlocks, double transitionWidthBlocks) {
        if (transitionWidthBlocks <= 0.0D) return sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        double halfTransitionWidth = transitionWidthBlocks * 0.5D;
        double outsideTransitionProgress = Mth.clamp(
                signedBoundaryDistanceBlocks / halfTransitionWidth,
                0.0D,
                1.0D
        );

        int baseExteriorThreshold = sparseOccupancyThreshold(signedBoundaryDistanceBlocks);

        return Mth.floor(Mth.lerp(outsideTransitionProgress, 1024.0D, baseExteriorThreshold));
    }

    private static int occupancyRoll(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull BlockPos position, long salt, int fillDenominator) {
        long hash = descriptor.instanceSeed() ^ salt;
        hash ^= (long) position.getX() * X_HASH_MULTIPLIER;
        hash ^= (long) position.getY() * Y_HASH_MULTIPLIER;
        hash ^= (long) position.getZ() * Z_HASH_MULTIPLIER;

        return Math.floorMod(mix64(hash), fillDenominator);
    }

    private static int occupancyRoll(OreVeinInstanceDescriptor descriptor, BlockPos position, int fillDenominator) {
        return occupancyRoll(descriptor, position, OCCUPANCY_SALT, fillDenominator);
    }

    public static double sampleDouble(long hash, double minValue, double maxValue) {
        if (minValue == maxValue)
            return minValue;

        return minValue + unit(hash) * (maxValue - minValue);
    }

    public static int sampleIntInclusive(long hash, int minValue, int maxValue) {
        if (minValue == maxValue) return minValue;

        return minValue + Math.floorMod(hash, maxValue - minValue + 1);
    }

    private static double unit(long hash) {
        return (OreVeinShapeEvaluator.hashToSignedUnit(hash) + 1.0D) * 0.5D;
    }

    private static double square(double value) {
        return value * value;
    }

    private static OreVeinOreCellResult.OreVariant variant(int finalDensity) {
        if (finalDensity <= 0) return HOST_ROCK;

        if (finalDensity == 1) return REGULAR_ORE;

        return DENSE_ORE;
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

    public static long xHashMultiplier() {
        return X_HASH_MULTIPLIER;
    }

    public static long yHashMultiplier() {
        return Y_HASH_MULTIPLIER;
    }

    public static int nodeCenterMaxAttempts() {
        return NODE_CENTER_MAX_ATTEMPTS;
    }

    @FunctionalInterface
    private interface HaloOccupancyRollProvider {
        int roll(OreVeinInstanceDescriptor descriptor, BlockPos position, int fillDenominator);
    }

    private record CellResultFactory(OreVeinInstanceDescriptor descriptor, SelectedOreEntry selectedOreEntry, OreVeinContribution contribution) {
        private CellResultFactory {
            Objects.requireNonNull(descriptor, "descriptor");
            Objects.requireNonNull(selectedOreEntry, "selectedOreEntry");
            Objects.requireNonNull(contribution, "contribution");
        }

        @Contract("_, _, _, _, _ -> new")
        private @NotNull OreVeinOreCellResult create(int candidateDensity, int finalDensity, OreVeinOreCellResult.OreVariant variant, long nodeId, double nodeInfluence) {
            return new OreVeinOreCellResult(
                    descriptor.instanceId(),
                    descriptor.definitionId(),
                    selectedOreEntry.material(),
                    selectedOreEntry.entry().distributionWeight(),
                    contribution,
                    candidateDensity,
                    finalDensity,
                    variant,
                    nodeId,
                    nodeInfluence
            );
        }

        @Contract(" -> new")
        private @NotNull OreVeinOreCellResult hostRock() {
            return create(1, 0, HOST_ROCK, 0L, 0.0D);
        }

        @Contract(" -> new")
        private @NotNull OreVeinOreCellResult sparseOre() {
            return create(1, 1, SPARSE_ORE, 0L, 0.0D);
        }
    }

    private record SelectedOreEntry(VeinOreEntry entry, Material material) {
    }

    private record DenseNodeOutcome(long nodeId, double influence) {
        static final DenseNodeOutcome NONE = new DenseNodeOutcome(0L, 0.0D);
    }
}
