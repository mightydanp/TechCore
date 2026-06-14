package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

final class OreVeinDenseNodeLayout {
    private OreVeinDenseNodeLayout() {
    }

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

            if (overlapsAnyNode(candidate, acceptedNodes)) {
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
        for (int x = minDenseBlock(node.localCenterX(), node.radiusX()); x <= maxDenseBlock(node.localCenterX(), node.radiusX()); x++) {
            for (int y = minDenseBlock(node.localCenterY(), node.radiusY()); y <= maxDenseBlock(node.localCenterY(), node.radiusY()); y++) {
                for (int z = minDenseBlock(node.localCenterZ(), node.radiusZ()); z <= maxDenseBlock(node.localCenterZ(), node.radiusZ()); z++) {
                    if (!OreVeinOreCellEvaluator.isInsideDenseNodeVolume(node, x + 0.5D, y + 0.5D, z + 0.5D)) {
                        continue;
                    }

                    if (OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, x + 0.5D, y + 0.5D, z + 0.5D).signedBoundaryDistanceBlocks() > 0.0D) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean overlapsAnyNode(OreVeinDenseNode candidate, List<OreVeinDenseNode> acceptedNodes) {
        for (OreVeinDenseNode accepted : acceptedNodes) {
            if (overlapExists(candidate, accepted)) {
                return true;
            }
        }

        return false;
    }

    static boolean overlapExists(OreVeinDenseNode first, OreVeinDenseNode second) {
        int minX = Math.max(minDenseBlock(first.localCenterX(), first.radiusX()), minDenseBlock(second.localCenterX(), second.radiusX()));
        int maxX = Math.min(maxDenseBlock(first.localCenterX(), first.radiusX()), maxDenseBlock(second.localCenterX(), second.radiusX()));
        int minY = Math.max(minDenseBlock(first.localCenterY(), first.radiusY()), minDenseBlock(second.localCenterY(), second.radiusY()));
        int maxY = Math.min(maxDenseBlock(first.localCenterY(), first.radiusY()), maxDenseBlock(second.localCenterY(), second.radiusY()));
        int minZ = Math.max(minDenseBlock(first.localCenterZ(), first.radiusZ()), minDenseBlock(second.localCenterZ(), second.radiusZ()));
        int maxZ = Math.min(maxDenseBlock(first.localCenterZ(), first.radiusZ()), maxDenseBlock(second.localCenterZ(), second.radiusZ()));

        if (minX > maxX || minY > maxY || minZ > maxZ) {
            return false;
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    double sampleX = x + 0.5D;
                    double sampleY = y + 0.5D;
                    double sampleZ = z + 0.5D;

                    if (OreVeinOreCellEvaluator.isInsideDenseNodeVolume(first, sampleX, sampleY, sampleZ)
                            && OreVeinOreCellEvaluator.isInsideDenseNodeVolume(second, sampleX, sampleY, sampleZ)) {
                        return true;
                    }
                }
            }
        }

        return false;
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
}
