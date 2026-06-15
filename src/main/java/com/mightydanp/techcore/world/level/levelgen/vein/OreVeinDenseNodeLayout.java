package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

final class OreVeinDenseNodeLayout {
    static List<OreVeinDenseNode> generate(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition) {
        int requestedNodeCount = OreVeinOreCellEvaluator.nodeCount(descriptor, definition.densitySettings());

        if (requestedNodeCount == 0) {
            return List.of();
        }

        List<OreVeinDenseNode> acceptedNodes = new ArrayList<>(requestedNodeCount);

        for (int nodeIndex = 0; nodeIndex < requestedNodeCount; nodeIndex++) {
            OreVeinDenseNode node = candidateNode(descriptor, definition, nodeIndex, acceptedNodes);
            if (node != null) {
                acceptedNodes.add(node);
            }
        }

        return List.copyOf(acceptedNodes);
    }

    private static OreVeinDenseNode candidateNode(
            OreVeinInstanceDescriptor descriptor,
            OreVeinDefinition definition,
            int nodeIndex,
            List<OreVeinDenseNode> acceptedNodes
    ) {
        long nodeTerm = (long) nodeIndex * OreVeinOreCellEvaluator.xHashMultiplier();
        double radiusX = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeRadiusXSalt() ^ nodeTerm),
                definition.densitySettings().minNodeRadiusX(),
                definition.densitySettings().maxNodeRadiusX()
        );
        double radiusY = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeRadiusYSalt() ^ nodeTerm),
                definition.densitySettings().minNodeRadiusY(),
                definition.densitySettings().maxNodeRadiusY()
        );
        double radiusZ = OreVeinOreCellEvaluator.sampleDouble(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeRadiusZSalt() ^ nodeTerm),
                definition.densitySettings().minNodeRadiusZ(),
                definition.densitySettings().maxNodeRadiusZ()
        );
        int configuredPeakDensity = OreVeinOreCellEvaluator.sampleIntInclusive(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodePeakSalt() ^ nodeTerm),
                definition.densitySettings().minPeakDensity(),
                definition.densitySettings().maxPeakDensity()
        );

        for (int attemptIndex = 0; attemptIndex < OreVeinOreCellEvaluator.nodeCenterMaxAttempts(); attemptIndex++) {
            OreVeinDenseNode candidate = candidateAt(descriptor, nodeIndex, attemptIndex, radiusX, radiusY, radiusZ, configuredPeakDensity);

            if (candidate == null) {
                continue;
            }

            if (!fullyContained(descriptor, candidate)) {
                continue;
            }

            if (overlapsAnyNode(descriptor, candidate, acceptedNodes)) {
                continue;
            }

            return candidate;
        }

        return null;
    }

    private static OreVeinDenseNode candidateAt(
            OreVeinInstanceDescriptor descriptor,
            int nodeIndex,
            int attemptIndex,
            double radiusX,
            double radiusY,
            double radiusZ,
            int configuredPeakDensity
    ) {
        double halfX = descriptor.sizeX() / 2.0D;
        double halfY = descriptor.sizeY() / 2.0D;
        double halfZ = descriptor.sizeZ() / 2.0D;
        double availableX = halfX - radiusX;
        double availableY = halfY - radiusY;
        double availableZ = halfZ - radiusZ;

        if (availableX < 0.0D || availableY < 0.0D || availableZ < 0.0D) {
            return null;
        }

        long nodeTerm = (long) nodeIndex * OreVeinOreCellEvaluator.xHashMultiplier();
        long attemptTerm = (long) attemptIndex * OreVeinOreCellEvaluator.yHashMultiplier();
        double u = OreVeinShapeEvaluator.hashToSignedUnit(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeCenterXSalt() ^ nodeTerm ^ attemptTerm)
        );
        double v = OreVeinShapeEvaluator.hashToSignedUnit(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeCenterYSalt() ^ nodeTerm ^ attemptTerm)
        );
        double w = OreVeinShapeEvaluator.hashToSignedUnit(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeCenterZSalt() ^ nodeTerm ^ attemptTerm)
        );

        if (u * u + v * v + w * w > 1.0D) {
            return null;
        }

        return new OreVeinDenseNode(
                OreVeinOreCellEvaluator.mix64(descriptor.instanceSeed() ^ OreVeinOreCellEvaluator.nodeIdSalt() ^ nodeTerm),
                u * availableX,
                v * availableY,
                w * availableZ,
                radiusX,
                radiusY,
                radiusZ,
                configuredPeakDensity
        );
    }

    private static boolean fullyContained(OreVeinInstanceDescriptor descriptor, OreVeinDenseNode node) {
        OreVeinBounds bounds = worldBounds(descriptor, node);
        BlockPos.MutableBlockPos position =
                new BlockPos.MutableBlockPos();

        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
                    position.set(x, y, z);

                    OreVeinContribution contribution =
                            OreVeinShapeEvaluator.evaluate(
                                    descriptor,
                                    position
                            );

                    if (!OreVeinOreCellEvaluator.isInsideDenseNodeVolume(
                            node,
                            contribution.localX(),
                            contribution.localY(),
                            contribution.localZ()
                    )) {
                        continue;
                    }

                    if (contribution.signedBoundaryDistanceBlocks() > 0.0D) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean overlapsAnyNode(OreVeinInstanceDescriptor descriptor, OreVeinDenseNode candidate, List<OreVeinDenseNode> acceptedNodes) {
        for (OreVeinDenseNode accepted : acceptedNodes) {
            if (overlapExists(descriptor, candidate, accepted)) {
                return true;
            }
        }

        return false;
    }

    static boolean overlapExists(OreVeinInstanceDescriptor descriptor, OreVeinDenseNode first, OreVeinDenseNode second) {
        OreVeinBounds overlapBounds = worldBounds(descriptor, first)
                .intersect(worldBounds(descriptor, second));

        if (overlapBounds == null) {
            return false;
        }

        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();

        for (int x = overlapBounds.minX();
             x <= overlapBounds.maxX();
             x++) {

            for (int y = overlapBounds.minY();
                 y <= overlapBounds.maxY();
                 y++) {

                for (int z = overlapBounds.minZ();
                     z <= overlapBounds.maxZ();
                     z++) {

                    position.set(x, y, z);

                    OreVeinContribution contribution =
                            OreVeinShapeEvaluator.evaluate(
                                    descriptor,
                                    position
                            );

                    boolean insideFirst =
                            OreVeinOreCellEvaluator
                                    .isInsideDenseNodeVolume(
                                            first,
                                            contribution.localX(),
                                            contribution.localY(),
                                            contribution.localZ()
                                    );

                    if (!insideFirst) {
                        continue;
                    }

                    boolean insideSecond =
                            OreVeinOreCellEvaluator
                                    .isInsideDenseNodeVolume(
                                            second,
                                            contribution.localX(),
                                            contribution.localY(),
                                            contribution.localZ()
                                    );

                    if (insideSecond) return true;
                }
            }
        }

        return false;
    }

    private static OreVeinBounds worldBounds(OreVeinInstanceDescriptor descriptor, OreVeinDenseNode node) {
        double yaw = descriptor.yaw();
        double pitch = descriptor.pitch();
        double roll = descriptor.roll();

        OreVeinShapeEvaluator.RotatedVector centerOffset =
                OreVeinShapeEvaluator.forwardRotate(
                        node.localCenterX(),
                        node.localCenterY(),
                        node.localCenterZ(),
                        yaw,
                        pitch,
                        roll
                );

        double worldCenterX =
                descriptor.center().getX() + 0.5D + centerOffset.x();
        double worldCenterY =
                descriptor.center().getY() + 0.5D + centerOffset.y();
        double worldCenterZ =
                descriptor.center().getZ() + 0.5D + centerOffset.z();

        OreVeinShapeEvaluator.RotatedVector xAxis =
                OreVeinShapeEvaluator.forwardRotate(
                        node.radiusX(),
                        0.0D,
                        0.0D,
                        yaw,
                        pitch,
                        roll
                );

        OreVeinShapeEvaluator.RotatedVector yAxis =
                OreVeinShapeEvaluator.forwardRotate(
                        0.0D,
                        node.radiusY(),
                        0.0D,
                        yaw,
                        pitch,
                        roll
                );

        OreVeinShapeEvaluator.RotatedVector zAxis =
                OreVeinShapeEvaluator.forwardRotate(
                        0.0D,
                        0.0D,
                        node.radiusZ(),
                        yaw,
                        pitch,
                        roll
                );

        double extentX = Math.sqrt(
                square(xAxis.x())
                        + square(yAxis.x())
                        + square(zAxis.x())
        );

        double extentY = Math.sqrt(
                square(xAxis.y())
                        + square(yAxis.y())
                        + square(zAxis.y())
        );

        double extentZ = Math.sqrt(
                square(xAxis.z())
                        + square(yAxis.z())
                        + square(zAxis.z())
        );

        return new OreVeinBounds(
                minDenseBlock(worldCenterX, extentX),
                minDenseBlock(worldCenterY, extentY),
                minDenseBlock(worldCenterZ, extentZ),
                maxDenseBlock(worldCenterX, extentX),
                maxDenseBlock(worldCenterY, extentY),
                maxDenseBlock(worldCenterZ, extentZ)
        );
    }

    static double normalizedSeparation(OreVeinDenseNode first, OreVeinDenseNode second) {
        double dx = first.localCenterX() - second.localCenterX();
        double dy = first.localCenterY() - second.localCenterY();
        double dz = first.localCenterZ() - second.localCenterZ();
        double combinedRadiusX = first.radiusX() + second.radiusX();
        double combinedRadiusY = first.radiusY() + second.radiusY();
        double combinedRadiusZ = first.radiusZ() + second.radiusZ();

        return dx * dx / (combinedRadiusX * combinedRadiusX)
                + dy * dy / (combinedRadiusY * combinedRadiusY)
                + dz * dz / (combinedRadiusZ * combinedRadiusZ);
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
