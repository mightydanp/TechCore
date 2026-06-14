package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;
import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.OUTSIDE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinShapeEvaluatorTest {
    @Test
    void centerIsInside() {
        OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor(17, 7, 17, 0.0D, 0.0D, 0.0D, 123L), new BlockPos(0, 0, 0));

        assertEquals(INSIDE_MAIN_BODY, contribution.state());
        assertTrue(contribution.signedBoundaryDistanceBlocks() < 0.0D);
    }

    @Test
    void distantPositionIsOutside() {
        OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor(17, 7, 17, 0.0D, 0.0D, 0.0D, 123L), new BlockPos(200, 0, 0));

        assertEquals(OUTSIDE, contribution.state());
    }

    @Test
    void rotationChangesMembership() {
        OreVeinInstanceDescriptor unrotated = descriptor(40, 6, 6, 0.0D, 0.0D, 0.0D, 456L);
        OreVeinInstanceDescriptor rotated = descriptor(40, 6, 6, 0.0D, 0.0D, 90.0D, 456L);
        BlockPos probe = new BlockPos(0, 10, 0);

        assertNotEquals(
                OreVeinShapeEvaluator.evaluate(unrotated, probe).state(),
                OreVeinShapeEvaluator.evaluate(rotated, probe).state()
        );
    }

    @Test
    void evaluationIsDeterministic() {
        OreVeinInstanceDescriptor descriptor = descriptor(17, 7, 17, 15.0D, 4.0D, -6.0D, 789L);
        BlockPos position = new BlockPos(3, 1, -2);

        assertEquals(
                OreVeinShapeEvaluator.evaluate(descriptor, position),
                OreVeinShapeEvaluator.evaluate(descriptor, position)
        );
    }

    @Test
    void neighboringChunksMatchAtBoundary() {
        OreVeinInstanceDescriptor descriptor = descriptor(24, 8, 24, 10.0D, 3.0D, 2.0D, 55L);
        OreVeinContribution first = OreVeinShapeEvaluator.evaluate(descriptor, new BlockPos(15, 0, 0));
        OreVeinContribution second = OreVeinShapeEvaluator.evaluate(descriptor, new BlockPos(15, 0, 0));

        assertEquals(first.signedBoundaryDistanceBlocks(), second.signedBoundaryDistanceBlocks());
    }

    @Test
    void shapesAreNotPerfectlySymmetric() {
        OreVeinInstanceDescriptor descriptor = descriptor(24, 12, 24, 0.0D, 0.0D, 0.0D, 101L);
        double positive = OreVeinShapeEvaluator.distortionBlocks(descriptor.shapeSeed(), 6.0D, 0.0D, 0.0D);
        double negative = OreVeinShapeEvaluator.distortionBlocks(descriptor.shapeSeed(), -6.0D, 0.0D, 0.0D);

        assertNotEquals(positive, negative);
    }

    @Test
    void inwardAndOutwardDistortionOccur() {
        OreVeinInstanceDescriptor descriptor = descriptor(24, 12, 24, 0.0D, 0.0D, 0.0D, 202L);
        boolean sawPositive = false;
        boolean sawNegative = false;

        for (int x = -20; x <= 20; x++) {
            double distortion = OreVeinShapeEvaluator.distortionBlocks(descriptor.shapeSeed(), x, x / 3.0D, -x / 4.0D);
            sawPositive |= distortion > 0.0D;
            sawNegative |= distortion < 0.0D;
        }

        assertTrue(sawPositive);
        assertTrue(sawNegative);
    }

    @Test
    void distortionNeverExceedsDeclaredBounds() {
        OreVeinInstanceDescriptor descriptor = descriptor(80, 20, 80, 0.0D, 0.0D, 0.0D, 303L);

        for (int x = -32; x <= 32; x++) {
            for (int y = -8; y <= 8; y++) {
                for (int z = -32; z <= 32; z += 4) {
                    double distortion = OreVeinShapeEvaluator.distortionBlocks(descriptor.shapeSeed(), x, y, z);
                    assertTrue(Math.abs(distortion) <= OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS);
                }
            }
        }
    }

    @Test
    void distortionDoesNotScaleWithLargerDimensions() {
        OreVeinInstanceDescriptor small = descriptor(16, 16, 16, 0.0D, 0.0D, 0.0D, 404L);
        OreVeinInstanceDescriptor large = descriptor(64, 64, 64, 0.0D, 0.0D, 0.0D, 404L);

        assertEquals(
                OreVeinShapeEvaluator.distortionBlocks(small.shapeSeed(), 5.0D, 2.0D, -1.0D),
                OreVeinShapeEvaluator.distortionBlocks(large.shapeSeed(), 5.0D, 2.0D, -1.0D)
        );
    }

    @Test
    void evenAndOddDimensionsWork() {
        assertEquals(INSIDE_MAIN_BODY, OreVeinShapeEvaluator.evaluate(descriptor(16, 6, 16, 0.0D, 0.0D, 0.0D, 1L), new BlockPos(0, 0, 0)).state());
        assertEquals(INSIDE_MAIN_BODY, OreVeinShapeEvaluator.evaluate(descriptor(17, 7, 17, 0.0D, 0.0D, 0.0D, 1L), new BlockPos(0, 0, 0)).state());
    }

    @Test
    void mirroredPositionsWithoutNoiseMatchBaseDistance() {
        double positive = OreVeinShapeEvaluator.baseDistanceBlocks(4.0D, 1.0D, 2.0D, 8.5D, 3.5D, 8.5D);
        double negative = OreVeinShapeEvaluator.baseDistanceBlocks(-4.0D, -1.0D, -2.0D, 8.5D, 3.5D, 8.5D);

        assertEquals(positive, negative);
    }

    @Test
    void blockCenterSamplingIsUsed() {
        OreVeinContribution centered = OreVeinShapeEvaluator.evaluate(descriptor(16, 16, 16, 0.0D, 0.0D, 0.0D, 5L), new BlockPos(0, 0, 0));
        OreVeinContribution shifted = OreVeinShapeEvaluator.evaluate(descriptorAt(0, 0, 1, 16, 16, 16, 0.0D, 0.0D, 0.0D, 5L), new BlockPos(0, 0, 0));

        assertNotEquals(centered.signedBoundaryDistanceBlocks(), shifted.signedBoundaryDistanceBlocks());
    }

    @Test
    void evaluatorAndBoundsShareRotationConvention() {
        OreVeinInstanceDescriptor descriptor = descriptor(31, 11, 17, 25.0D, 8.0D, -12.0D, 88L);
        OreVeinShapeEvaluator.HalfExtents extents = OreVeinShapeEvaluator.rotatedHalfExtents(30, 10, 12, 25.0D, 8.0D, -12.0D);
        List<OreVeinShapeEvaluator.RotatedVector> localPoints = List.of(
                new OreVeinShapeEvaluator.RotatedVector(4.0D, 1.0D, -2.0D),
                new OreVeinShapeEvaluator.RotatedVector(-5.0D, 2.0D, 3.0D),
                new OreVeinShapeEvaluator.RotatedVector(1.0D, -1.5D, 4.0D)
        );

        for (OreVeinShapeEvaluator.RotatedVector localPoint : localPoints) {
            OreVeinShapeEvaluator.RotatedVector worldOffset = forwardRotate(localPoint.x(), localPoint.y(), localPoint.z(), descriptor.yaw(), descriptor.pitch(), descriptor.roll());
            OreVeinShapeEvaluator.RotatedVector recovered = OreVeinShapeEvaluator.inverseRotate(worldOffset.x(), worldOffset.y(), worldOffset.z(), descriptor.yaw(), descriptor.pitch(), descriptor.roll());
            BlockPos position = new BlockPos(
                    (int) Math.floor(worldOffset.x()),
                    (int) Math.floor(worldOffset.y()),
                    (int) Math.floor(worldOffset.z())
            );
            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluateLocalPoint(descriptor, localPoint.x(), localPoint.y(), localPoint.z());

            assertEquals(INSIDE_MAIN_BODY, contribution.state());
            assertTrue(position.getX() >= -extents.x() && position.getX() <= extents.x());
            assertTrue(position.getY() >= -extents.y() && position.getY() <= extents.y());
            assertTrue(position.getZ() >= -extents.z() && position.getZ() <= extents.z());
            assertEquals(localPoint.x(), recovered.x(), 1.0E-9D);
            assertEquals(localPoint.y(), recovered.y(), 1.0E-9D);
            assertEquals(localPoint.z(), recovered.z(), 1.0E-9D);
        }
    }

    @Test
    void exactHashOutputsRemainStable() {
        assertEquals(-8429320743047842098L, OreVeinShapeEvaluator.latticeHash(123L, 0x6A09E667F3BCC909L, 4L, -2L, 9L));
        assertEquals(0.08609121378104012D, OreVeinShapeEvaluator.hashToSignedUnit(-8429320743047842098L));
    }

    private static OreVeinInstanceDescriptor descriptor(int sizeX, int sizeY, int sizeZ, double yaw, double pitch, double roll, long shapeSeed) {
        return descriptorAt(0, 0, 0, sizeX, sizeY, sizeZ, yaw, pitch, roll, shapeSeed);
    }

    private static OreVeinInstanceDescriptor descriptorAt(int centerX, int centerY, int centerZ, int sizeX, int sizeY, int sizeZ, double yaw, double pitch, double roll, long shapeSeed) {
        OreVeinShapeEvaluator.HalfExtents extents = OreVeinShapeEvaluator.rotatedHalfExtents(sizeX, sizeY, sizeZ, yaw, pitch, roll);

        return new OreVeinInstanceDescriptor(
                1L,
                2L,
                shapeSeed,
                ResourceLocation.fromNamespaceAndPath("test", "shape"),
                new BlockPos(centerX, centerY, centerZ),
                sizeX,
                sizeY,
                sizeZ,
                yaw,
                pitch,
                roll,
                0,
                0,
                0,
                new OreVeinBounds(
                        centerX - extents.x(),
                        centerY - extents.y(),
                        centerZ - extents.z(),
                        centerX + extents.x(),
                        centerY + extents.y(),
                        centerZ + extents.z()
                ),
                List.of()
        );
    }

    private static OreVeinShapeEvaluator.RotatedVector forwardRotate(double x, double y, double z, double yawDegrees, double pitchDegrees, double rollDegrees) {
        double yaw = Math.toRadians(yawDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double roll = Math.toRadians(rollDegrees);
        double cy = Math.cos(yaw);
        double sy = Math.sin(yaw);
        double cp = Math.cos(pitch);
        double sp = Math.sin(pitch);
        double cr = Math.cos(roll);
        double sr = Math.sin(roll);

        return new OreVeinShapeEvaluator.RotatedVector(
                (cy * cr + sy * sp * sr) * x + (-cy * sr + sy * sp * cr) * y + (sy * cp) * z,
                (cp * sr) * x + (cp * cr) * y + (-sp) * z,
                (-sy * cr + cy * sp * sr) * x + (sy * sr + cy * sp * cr) * y + (cy * cp) * z
        );
    }
}
