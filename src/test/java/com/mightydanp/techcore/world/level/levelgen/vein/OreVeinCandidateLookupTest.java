package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinCandidateLookupTest {
    private static final ResourceKey<Level> OVERWORLD = dimension("overworld");

    @Test
    void descriptorIsDeterministic() {
        OreVeinDefinition definition = definition("deterministic");
        Optional<OreVeinInstanceDescriptor> first = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, -3, 7, 0, definition);
        Optional<OreVeinInstanceDescriptor> second = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, -3, 7, 0, definition);

        assertEquals(first, second, "descriptor should be deterministic");
    }

    @Test
    void rotationIsDeterministicAndBounded() {
        OreVeinDefinition definition = definition("rotation");
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 1, 2, 0, definition).orElseThrow();

        assertTrue(descriptor.yaw() >= 0.0D && descriptor.yaw() < 360.0D, "yaw should be full-circle");
        assertTrue(Math.abs(descriptor.pitch()) <= definition.maxPitchDegrees(), "pitch should be bounded by definition");
        assertTrue(Math.abs(descriptor.roll()) <= definition.maxRollDegrees(), "roll should be bounded by definition");
    }

    @Test
    void centerIsNotSystematicallyChunkAligned() {
        OreVeinDefinition definition = definition("alignment");
        List<Integer> xMods = new ArrayList<>();

        for (int region = -8; region <= 8; region++) {
            OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, region, 0, 0, definition)
                    .map(OreVeinInstanceDescriptor::center)
                    .map(BlockPos::getX)
                    .map(x -> Math.floorMod(x, 16))
                    .ifPresent(xMods::add);
        }

        assertTrue(xMods.stream().distinct().count() > 4, "center X should vary across chunk-local coordinates");
    }

    @Test
    void negativeRegionCoordinatesAreStable() {
        int region = OreVeinGenerationMath.regionCoordinateForBlock(-1);

        assertEquals(-1, region, "negative block coordinates should use floor region math");
    }

    @Test
    void boundsIntersectionAllowsOverlaps() {
        OreVeinBounds first = new OreVeinBounds(0, 0, 0, 10, 10, 10);
        OreVeinBounds second = new OreVeinBounds(5, 5, 5, 15, 15, 15);
        OreVeinBounds nested = new OreVeinBounds(2, 2, 2, 3, 3, 3);

        assertTrue(first.intersects(second), "overlapping bounds should intersect");
        assertTrue(first.intersects(nested), "nested bounds should intersect");
    }

    @Test
    void candidatesAreReturnedInStableOrder() {
        OreVeinInstanceDescriptor first = descriptor("a", -1, 2);
        OreVeinInstanceDescriptor second = descriptor("b", 0, -2);
        OreVeinInstanceDescriptor third = descriptor("c", 0, 1);
        List<OreVeinInstanceDescriptor> descriptors = List.of(third, first, second).stream()
                .sorted(java.util.Comparator
                        .comparingInt(OreVeinInstanceDescriptor::originRegionX)
                        .thenComparingInt(OreVeinInstanceDescriptor::originRegionZ)
                        .thenComparingInt(OreVeinInstanceDescriptor::originIndex)
                        .thenComparing(descriptor -> descriptor.definitionId().toString()))
                .toList();

        assertEquals(List.of(first, second, third), descriptors, "candidate order should be stable");
    }

    @Test
    void candidateReachIncludesDistortionAndDerivedSparseReachExactlyOnce() throws Exception {
        OreVeinDefinitions.register(definition("reach"));
        java.lang.reflect.Method method = OreVeinCandidateLookup.class.getDeclaredMethod("maxCandidateReach", ResourceKey.class);
        method.setAccessible(true);
        int reach = (int) method.invoke(null, OVERWORLD);
        int uninflated = uninflatedReach(definition("reach"));
        int expectedExpansion = (int) Math.ceil(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS)
                + definition("reach").sparseReachBlocks();

        assertEquals(uninflated + expectedExpansion, reach);
    }

    @Test
    void activeSparseCandidateReachDoesNotUseHaloRadiusBlocks() throws Exception {
        OreVeinDefinitions.register(definition("reach_no_halo"));
        java.lang.reflect.Method method = OreVeinCandidateLookup.class.getDeclaredMethod("maxCandidateReach", ResourceKey.class);
        method.setAccessible(true);
        int reach = (int) method.invoke(null, OVERWORLD);
        int uninflated = uninflatedReach(definition("reach_no_halo"));
        int expectedExpansion = (int) Math.ceil(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS)
                + definition("reach_no_halo").sparseReachBlocks();

        assertEquals(uninflated + expectedExpansion, reach);
    }

    @Test
    void extremeHaloSettingsFailValidationInsteadOfOverflowing() {
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new OreVeinHaloSettings(4.0D, Double.POSITIVE_INFINITY, 320)
        );

        assertTrue(exception.getMessage().contains("haloRadiusBlocks"));
    }

    static OreVeinDefinition definition(String name) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OVERWORLD),
                80,
                -16,
                128,
                17,
                63,
                7,
                7,
                17,
                63,
                32,
                12.0D,
                12.0D,
                new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4),
                new OreVeinHaloSettings(4.0D, 9.0D, 320),
                List.of(new VeinOreEntry(() -> null, 1))
        );
    }

    private static int uninflatedReach(OreVeinDefinition definition) {
        int maxReach = 0;

        for (double pitch : List.of(-definition.maxPitchDegrees(), definition.maxPitchDegrees())) {
            for (double roll : List.of(-definition.maxRollDegrees(), definition.maxRollDegrees())) {
                OreVeinShapeEvaluator.HalfExtents halfExtents = OreVeinShapeEvaluator.rotatedHalfExtents(
                        definition.maxSizeX(),
                        definition.maxSizeY(),
                        definition.maxSizeZ(),
                        45.0D,
                        pitch,
                        roll
                );
                maxReach = Math.max(maxReach, Math.max(halfExtents.x(), halfExtents.z()));
            }
        }

        return maxReach;
    }

    static ResourceKey<Level> dimension(String path) {
        try {
            Constructor<ResourceKey> constructor = ResourceKey.class.getDeclaredConstructor(ResourceLocation.class, ResourceLocation.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    ResourceLocation.withDefaultNamespace("dimension"),
                    ResourceLocation.withDefaultNamespace(path)
            );
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to create test ResourceKey", exception);
        }
    }

    static OreVeinInstanceDescriptor descriptor(String name, int regionX, int regionZ) {
        return new OreVeinInstanceDescriptor(
                regionX,
                regionZ,
                0L,
                ResourceLocation.fromNamespaceAndPath("test", name),
                new BlockPos(0, 0, 0),
                6,
                6,
                6,
                0.0D,
                0.0D,
                0.0D,
                regionX,
                regionZ,
                0,
                new OreVeinBounds(0, 0, 0, 0, 0, 0),
                List.of()
        );
    }
}
