package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinContributionLookupTest {
    private static final ResourceKey<Level> OVERWORLD = dimension("overworld");

    @Test
    void lookupExcludesOutsideContributions() {
        List<OreVeinContribution> contributions = OreVeinContributionLookup.contributionsForPosition(1234L, OVERWORLD, new BlockPos(5000, 100, 5000));

        assertTrue(contributions.isEmpty());
    }

    @Test
    void candidateBoundsIncludeDistortedInsidePoint() {
        OreVeinDefinition definition = definition("bounds", 24, 8, 24);
        OreVeinInstanceDescriptor descriptor = OreVeinCandidateLookup.createDescriptor(1234L, OVERWORLD, 0, 0, 0, definition).orElseThrow();
        int margin = (int) Math.ceil(OreVeinDefinitions.MAX_BOUNDARY_DISTORTION_BLOCKS);

        assertTrue(descriptor.bounds().inflate(margin).intersects(OreVeinBounds.from(descriptor.center(), descriptor.center())));
    }

    @Test
    void overlappingAndNestedInsideContributionsAreRetained() {
        BlockPos position = new BlockPos(0, 0, 0);
        List<OreVeinContribution> contributions = new ArrayList<>();
        contributions.add(OreVeinShapeEvaluator.evaluate(descriptor(2L, "a", 20, 20, 20), position));
        contributions.add(OreVeinShapeEvaluator.evaluate(descriptor(1L, "b", 10, 10, 10), position));
        contributions.add(OreVeinShapeEvaluator.evaluate(descriptor(3L, "c", 6, 6, 6), position));
        contributions.removeIf(contribution -> contribution.state() != OreVeinContribution.ContributionState.INSIDE_MAIN_BODY);
        contributions.sort(Comparator.comparingLong(OreVeinContribution::instanceId).thenComparing(contribution -> contribution.definitionId().toString()));

        assertEquals(List.of(1L, 2L, 3L), contributions.stream().map(OreVeinContribution::instanceId).toList());
    }

    @Test
    void deterministicResultsCrossChunkBoundaries() {
        List<OreVeinContribution> first = OreVeinContributionLookup.contributionsForPosition(1234L, OVERWORLD, new BlockPos(15, 20, 15));
        List<OreVeinContribution> second = OreVeinContributionLookup.contributionsForPosition(1234L, OVERWORLD, new BlockPos(15, 20, 15));

        assertEquals(first, second);
    }

    private static OreVeinInstanceDescriptor descriptor(long instanceId, String name, int sizeX, int sizeY, int sizeZ) {
        OreVeinShapeEvaluator.HalfExtents extents = OreVeinShapeEvaluator.rotatedHalfExtents(sizeX, sizeY, sizeZ, 0.0D, 0.0D, 0.0D);

        return new OreVeinInstanceDescriptor(
                instanceId,
                0L,
                instanceId,
                ResourceLocation.fromNamespaceAndPath("test", name),
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
                List.of()
        );
    }

    private static OreVeinDefinition definition(String name, int sizeX, int sizeY, int sizeZ) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OVERWORLD),
                80,
                -16,
                128,
                sizeX,
                sizeX,
                sizeY,
                sizeY,
                sizeZ,
                sizeZ,
                12.0D,
                12.0D,
                new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 11.0D, 3.0D, 5.0D, 4.0D, 11.0D, 2, 4),
                new OreVeinHaloSettings(4.0D, 9.0D, 320),
                List.of(new VeinOreEntry(() -> null, 1))
        );
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
}
