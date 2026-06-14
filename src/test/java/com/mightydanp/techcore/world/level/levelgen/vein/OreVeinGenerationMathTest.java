package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinGenerationMathTest {
    private static final ResourceKey<Level> OVERWORLD = dimension("overworld");
    private static final ResourceKey<Level> NETHER = dimension("the_nether");

    @Test
    void effectiveWeightPenalizesLargerVolumes() {
        OreVeinDefinition reference = definition("reference", 80, 16, 16, 16, 16, 16, 16);
        OreVeinDefinition larger = definition("larger", 80, 32, 32, 32, 32, 32, 32);

        assertTrue(
                OreVeinGenerationMath.effectiveWeightQ16(reference).compareTo(OreVeinGenerationMath.effectiveWeightQ16(larger)) > 0,
                "larger vein should have lower effective weight"
        );
    }

    @Test
    void effectiveWeightPreservesQ16ForLargeVeins() {
        OreVeinDefinition largeA = definition("large_a", 80, 48, 48, 48, 48, 48, 48);
        OreVeinDefinition largeB = definition("large_b", 81, 48, 48, 48, 48, 48, 48);

        assertTrue(
                OreVeinGenerationMath.effectiveWeightQ16(largeB).compareTo(OreVeinGenerationMath.effectiveWeightQ16(largeA)) > 0,
                "Q16 effective weights should preserve adjacent base-weight differences"
        );
    }

    @Test
    void originBudgetUsesQ16Units() {
        OreVeinDimensionGenerationSettings settings = new OreVeinDimensionGenerationSettings(OVERWORLD, 1000);

        assertEquals(BigInteger.valueOf(1000L).multiply(OreVeinGenerationMath.Q16), OreVeinGenerationMath.budgetQ16(settings), "budget should use Q16 units");
    }

    @Test
    void deterministicRollChangesByDimensionAndRegion() {
        BigInteger budget = BigInteger.valueOf(1000L).multiply(OreVeinGenerationMath.Q16);
        BigInteger overworld = OreVeinGenerationMath.rollQ16(1234L, OVERWORLD, -2, 5, 0, budget);
        BigInteger nether = OreVeinGenerationMath.rollQ16(1234L, NETHER, -2, 5, 0, budget);
        BigInteger otherRegion = OreVeinGenerationMath.rollQ16(1234L, OVERWORLD, -1, 5, 0, budget);

        assertTrue(!overworld.equals(nether), "dimension identity should affect roll");
        assertTrue(!overworld.equals(otherRegion), "origin region should affect roll");
    }

    @Test
    void sizeRangesAreIndependentAndInclusive() {
        OreVeinDefinition definition = definition("sizes", 80, 17, 63, 7, 7, 17, 63);
        int sizeX = OreVeinGenerationMath.sizeX(1234L, OVERWORLD, 1, 2, 0, definition);
        int sizeY = OreVeinGenerationMath.sizeY(1234L, OVERWORLD, 1, 2, 0, definition);
        int sizeZ = OreVeinGenerationMath.sizeZ(1234L, OVERWORLD, 1, 2, 0, definition);

        assertTrue(sizeX >= 17 && sizeX <= 63, "sizeX should be inclusive");
        assertEquals(7, sizeY, "sizeY should use its own fixed range");
        assertTrue(sizeZ >= 17 && sizeZ <= 63, "sizeZ should be inclusive");
    }

    static OreVeinDefinition definition(String name, int generationWeight, int minSizeX, int maxSizeX, int minSizeY, int maxSizeY, int minSizeZ, int maxSizeZ) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OVERWORLD),
                generationWeight,
                -16,
                128,
                minSizeX,
                maxSizeX,
                minSizeY,
                maxSizeY,
                minSizeZ,
                maxSizeZ,
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
