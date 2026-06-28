package com.mightydanp.techcore.world.level.levelgen.vein.densenode;

import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinBounds;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinInstanceDescriptor;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinShapeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class OreVeinDenseNodeLayout {
    public static List<DenseNodeVeinFeature.Node> generate(OreVeinInstanceDescriptor descriptor, @NotNull DenseNodeVeinFeature.Config config) {
        // Try to generate the requested amount of dense nodes
        int requestedNodeCount = nodeCount(descriptor, config);

        if (requestedNodeCount == 0) return List.of();

        List<DenseNodeVeinFeature.Node> acceptedNodes = new ArrayList<>(requestedNodeCount);

        for (int nodeIndex = 0; nodeIndex < requestedNodeCount; nodeIndex++) {
            DenseNodeVeinFeature.Node node = candidateNode(descriptor, config, nodeIndex, acceptedNodes);

            if (node != null) acceptedNodes.add(node);
        }

        return List.copyOf(acceptedNodes);
    }

    private static int nodeCount(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull DenseNodeVeinFeature.Config config) {
        BigInteger volume = BigInteger.valueOf(descriptor.sizeX()).multiply(BigInteger.valueOf(descriptor.sizeY())).multiply(BigInteger.valueOf(descriptor.sizeZ()));
        BigInteger blocksPerDenseNode = BigInteger.valueOf(config.blocksPerDenseNode());
        BigInteger[] quotientAndRemainder = volume.divideAndRemainder(blocksPerDenseNode);
        BigInteger count = quotientAndRemainder[0];
        BigInteger remainder = quotientAndRemainder[1];
        BigInteger min = BigInteger.valueOf(config.minNodeCount());
        BigInteger max = BigInteger.valueOf(config.maxNodeCount());

        if (remainder.signum() > 0) {
            long remainderRoll = Math.floorMod(
                    OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodeCountSalt()),
                    blocksPerDenseNode.longValueExact()
            );

            if (BigInteger.valueOf(remainderRoll).compareTo(remainder) < 0) count = count.add(BigInteger.ONE);
        }

        if (count.compareTo(min) < 0) count = min;
        if (count.compareTo(max) > 0) count = max;

        return count.intValueExact();
    }

    private static @Nullable DenseNodeVeinFeature.Node candidateNode(@NotNull OreVeinInstanceDescriptor descriptor, @NotNull DenseNodeVeinFeature.Config config, int nodeIndex, List<DenseNodeVeinFeature.Node> acceptedNodes) {
        // Sample one dense-node shape first, then keep retrying only its center point.
        double allowedMaxRadiusX = Math.min(
                config.maxNodeRadiusX(),
                descriptor.sizeX() / 2.0D
        );

        double allowedMaxRadiusY = Math.min(
                config.maxNodeRadiusY(),
                descriptor.sizeY() / 2.0D
        );

        double allowedMaxRadiusZ = Math.min(
                config.maxNodeRadiusZ(),
                descriptor.sizeZ() / 2.0D
        );

        if (allowedMaxRadiusX < config.minNodeRadiusX() || allowedMaxRadiusY < config.minNodeRadiusY() || allowedMaxRadiusZ < config.minNodeRadiusZ()) return null;

        long nodeTerm = (long) nodeIndex * OreVeinOreCellEvaluator.xHashMultiplier();

        double radiusX = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodeRadiusXSalt() ^ nodeTerm),
                config.minNodeRadiusX(),
                allowedMaxRadiusX
        );
        double radiusY = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodeRadiusYSalt() ^ nodeTerm),
                config.minNodeRadiusY(),
                allowedMaxRadiusY
        );
        double radiusZ = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodeRadiusZSalt() ^ nodeTerm),
                config.minNodeRadiusZ(),
                allowedMaxRadiusZ
        );
        int configuredPeakDensity = OreVeinOreCellEvaluator.sampleIntInclusive(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodePeakSalt() ^ nodeTerm),
                config.minPeakDensity(),
                config.maxPeakDensity()
        );

        for (int attemptIndex = 0; attemptIndex < OreVeinDenseNodeEvaluator.nodeCenterMaxAttempts(); attemptIndex++) {
            DenseNodeVeinFeature.Node candidate = candidateAt(descriptor, nodeIndex, attemptIndex, radiusX, radiusY, radiusZ, configuredPeakDensity);

            if (candidate == null) continue;
            if (!fullyContained(descriptor, candidate)) continue;
            if (overlapsAnyNode(descriptor, candidate, acceptedNodes)) continue;

            return candidate;
        }

        return null;
    }

    private static @Nullable DenseNodeVeinFeature.Node candidateAt(@NotNull OreVeinInstanceDescriptor descriptor, int nodeIndex, int attemptIndex, double radiusX, double radiusY, double radiusZ, int configuredPeakDensity) {
        // Shrink the available center range so the node radii stay inside the base size.
        double halfX = descriptor.sizeX() / 2.0D;
        double halfY = descriptor.sizeY() / 2.0D;
        double halfZ = descriptor.sizeZ() / 2.0D;
        double availableX = halfX - radiusX;
        double availableY = halfY - radiusY;
        double availableZ = halfZ - radiusZ;

        if (availableX < 0.0D || availableY < 0.0D || availableZ < 0.0D) return null;

        long nodeTerm = (long) nodeIndex * OreVeinOreCellEvaluator.xHashMultiplier();
        long attemptTerm = (long) attemptIndex * OreVeinOreCellEvaluator.yHashMultiplier();
        long instanceSeed = descriptor.instanceSeed();
        double u = sampleNodeCoordinate(instanceSeed, OreVeinDenseNodeEvaluator.nodeCenterXSalt(), nodeTerm, attemptTerm);
        double v = sampleNodeCoordinate(instanceSeed, OreVeinDenseNodeEvaluator.nodeCenterYSalt(), nodeTerm, attemptTerm);
        double w = sampleNodeCoordinate(instanceSeed, OreVeinDenseNodeEvaluator.nodeCenterZSalt(), nodeTerm, attemptTerm);

        if (u * u + v * v + w * w > 1.0D) return null;

        return new DenseNodeVeinFeature.Node(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinDenseNodeEvaluator.nodeIdSalt() ^ nodeTerm),
                u * availableX,
                v * availableY,
                w * availableZ,
                radiusX,
                radiusY,
                radiusZ,
                configuredPeakDensity
        );
    }

    private static double sampleNodeCoordinate(long instanceSeed, long salt, long nodeTerm, long attemptTerm) {
        return OreVeinShapeEvaluator.hashToSignedUnit(OreVeinOreCellEvaluator.mix64(instanceSeed ^ salt ^ nodeTerm ^ attemptTerm));
    }

    private static boolean fullyContained(OreVeinInstanceDescriptor descriptor, DenseNodeVeinFeature.Node node) {
        // Check every block in the node bounds to make sure the node stays inside the main body.
        OreVeinBounds bounds = worldBounds(descriptor, node);
        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();

        for (int x = bounds.minX(); x <= bounds.maxX(); x++)
            for (int y = bounds.minY(); y <= bounds.maxY(); y++)
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
                    position.set(x, y, z);

                    OreVeinShapeEvaluator.ShapeContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);

                    if (!isInsideDenseNodeVolume(node, contribution.localX(), contribution.localY(), contribution.localZ()))
                        continue;
                    if (contribution.signedBoundaryDistanceBlocks() > 0.0D) return false;
                }

        return true;
    }

    private static boolean overlapsAnyNode(OreVeinInstanceDescriptor descriptor, DenseNodeVeinFeature.Node candidate, @NotNull List<DenseNodeVeinFeature.Node> acceptedNodes) {
        for (DenseNodeVeinFeature.Node accepted : acceptedNodes)
            if (overlapExists(descriptor, candidate, accepted)) return true;

        return false;
    }

    private static boolean overlapExists(OreVeinInstanceDescriptor descriptor, DenseNodeVeinFeature.Node first, DenseNodeVeinFeature.Node second) {
        // Scan the shared bounds to see whether both dense nodes occupy any same block space.
        OreVeinBounds overlapBounds = worldBounds(descriptor, first).intersect(worldBounds(descriptor, second));

        if (overlapBounds == null) return false;

        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();

        for (int x = overlapBounds.minX(); x <= overlapBounds.maxX(); x++)
            for (int y = overlapBounds.minY(); y <= overlapBounds.maxY(); y++)
                for (int z = overlapBounds.minZ(); z <= overlapBounds.maxZ(); z++) {
                    position.set(x, y, z);

                    OreVeinShapeEvaluator.ShapeContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);
                    boolean insideFirst = isInsideDenseNodeVolume(first, contribution.localX(), contribution.localY(), contribution.localZ());

                    if (!insideFirst) continue;

                    boolean insideSecond = isInsideDenseNodeVolume(second, contribution.localX(), contribution.localY(), contribution.localZ());

                    if (insideSecond) return true;
                }

        return false;
    }

    private static @NotNull OreVeinBounds worldBounds(@NotNull OreVeinInstanceDescriptor descriptor, DenseNodeVeinFeature.@NotNull Node node) {
        // Rotate the node center and radii into world space before building inclusive bounds.
        double yaw = descriptor.yaw();
        double pitch = descriptor.pitch();
        double roll = descriptor.roll();

        OreVeinShapeEvaluator.RotatedVector centerOffset = OreVeinShapeEvaluator.forwardRotate(node.localCenterX(), node.localCenterY(), node.localCenterZ(), yaw, pitch, roll);
        OreVeinShapeEvaluator.RotatedVector veinCenter = OreVeinShapeEvaluator.geometricCenter(descriptor);

        double worldCenterX = veinCenter.x() + centerOffset.x();
        double worldCenterY = veinCenter.y() + centerOffset.y();
        double worldCenterZ = veinCenter.z() + centerOffset.z();

        OreVeinShapeEvaluator.RotatedVector xAxis = OreVeinShapeEvaluator.forwardRotate(node.radiusX(), 0.0D, 0.0D, yaw, pitch, roll);
        OreVeinShapeEvaluator.RotatedVector yAxis = OreVeinShapeEvaluator.forwardRotate(0.0D, node.radiusY(), 0.0D, yaw, pitch, roll);
        OreVeinShapeEvaluator.RotatedVector zAxis = OreVeinShapeEvaluator.forwardRotate(0.0D, 0.0D, node.radiusZ(), yaw, pitch, roll);

        double extentX = Math.sqrt(square(xAxis.x()) + square(yAxis.x()) + square(zAxis.x()));
        double extentY = Math.sqrt(square(xAxis.y()) + square(yAxis.y()) + square(zAxis.y()));
        double extentZ = Math.sqrt(square(xAxis.z()) + square(yAxis.z()) + square(zAxis.z()));

        return new OreVeinBounds(
                minDenseBlock(worldCenterX, extentX),
                minDenseBlock(worldCenterY, extentY),
                minDenseBlock(worldCenterZ, extentZ),
                maxDenseBlock(worldCenterX, extentX),
                maxDenseBlock(worldCenterY, extentY),
                maxDenseBlock(worldCenterZ, extentZ)
        );
    }

    private static boolean isInsideDenseNodeVolume(DenseNodeVeinFeature.@NotNull Node node, double localX, double localY, double localZ) {
        double dx = localX - node.localCenterX();
        double dy = localY - node.localCenterY();
        double dz = localZ - node.localCenterZ();
        double normalizedDistanceSquared = square(dx / node.radiusX()) + square(dy / node.radiusY()) + square(dz / node.radiusZ());

        return normalizedDistanceSquared < 1.0D;
    }

    private static int minDenseBlock(double center, double radius) {
        return Mth.floor(center - radius - 0.5D) + 1;
    }

    private static int maxDenseBlock(double center, double radius) {
        return Mth.ceil(center + radius - 0.5D) - 1;
    }

    private static double square(double value) {
        return value * value;
    }
}
