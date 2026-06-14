package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class OreVeinDefinitionBuilderTest {
    private static final ResourceKey<Level> OVERWORLD = OreVeinCandidateLookupTest.dimension("overworld");
    private static final ResourceKey<Level> NETHER = OreVeinCandidateLookupTest.dimension("the_nether");

    @Test
    void canonicalAndBuilderDefinitionsAreEqual() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("test", "builder");
        List<ResourceKey<Level>> dimensions = List.of(OVERWORLD, NETHER);
        List<VeinOreEntry> ores = List.of(entry("first", 8), entry("second", 4), entry("third", 2));
        OreVeinDefinition canonical = new OreVeinDefinition(
                id,
                dimensions,
                80,
                12,
                27,
                17,
                63,
                7,
                7,
                17,
                63,
                32,
                12.0D,
                12.0D,
                defaultDensity(),
                defaultHalo(),
                ores
        );
        OreVeinDefinition built = OreVeinDefinition.builder(id)
                .dimensions(dimensions)
                .generationWeight(80)
                .centerY(12, 27)
                .sizeX(17, 63)
                .sizeY(7, 7)
                .sizeZ(17, 63)
                .sparseReachBlocks(32)
                .ores(ores)
                .build();

        assertEquals(canonical, built);
        assertEquals(canonical.id(), built.id());
        assertEquals(canonical.dimensions(), built.dimensions());
        assertEquals(canonical.generationWeight(), built.generationWeight());
        assertEquals(canonical.minCenterY(), built.minCenterY());
        assertEquals(canonical.maxCenterYExclusive(), built.maxCenterYExclusive());
        assertEquals(canonical.minSizeX(), built.minSizeX());
        assertEquals(canonical.maxSizeX(), built.maxSizeX());
        assertEquals(canonical.minSizeY(), built.minSizeY());
        assertEquals(canonical.maxSizeY(), built.maxSizeY());
        assertEquals(canonical.minSizeZ(), built.minSizeZ());
        assertEquals(canonical.maxSizeZ(), built.maxSizeZ());
        assertEquals(canonical.sparseReachBlocks(), built.sparseReachBlocks());
        assertEquals(canonical.maxPitchDegrees(), built.maxPitchDegrees());
        assertEquals(canonical.maxRollDegrees(), built.maxRollDegrees());
        assertEquals(canonical.densitySettings(), built.densitySettings());
        assertEquals(canonical.haloSettings(), built.haloSettings());
        assertEquals(canonical.oreEntries(), built.oreEntries());
    }

    @Test
    void builderUsesFixedEngineOwnedDefaultsAndExposesNoRestrictedMethods() {
        List<String> methodNames = Arrays.stream(OreVeinDefinition.Builder.class.getDeclaredMethods())
                .map(Method::getName)
                .toList();

        assertFalse(methodNames.contains("rotation"));
        assertFalse(methodNames.contains("pitch"));
        assertFalse(methodNames.contains("roll"));
        assertFalse(methodNames.contains("maxPitch"));
        assertFalse(methodNames.contains("maxRoll"));
        assertFalse(methodNames.contains("density"));
        assertFalse(methodNames.contains("fill"));
        assertFalse(methodNames.contains("node"));
        assertFalse(methodNames.contains("radius"));
        assertFalse(methodNames.contains("peakDensity"));
        assertFalse(methodNames.contains("halo"));
        assertFalse(methodNames.contains("transition"));

        OreVeinDefinition built = OreVeinDefinition.builder(ResourceLocation.fromNamespaceAndPath("test", "rotation"))
                .dimensions(OVERWORLD)
                .generationWeight(80)
                .centerY(12, 27)
                .sizeX(17, 63)
                .sizeY(7, 7)
                .sizeZ(17, 63)
                .ores(entry("first", 8), entry("second", 4), entry("third", 2))
                .build();

        assertEquals(12.0D, built.maxPitchDegrees());
        assertEquals(12.0D, built.maxRollDegrees());
        assertEquals(defaultDensity(), built.densitySettings());
        assertEquals(2, built.densitySettings().minPeakDensity());
        assertEquals(4, built.densitySettings().maxPeakDensity());
        assertEquals(defaultHalo(), built.haloSettings());
        assertEquals(32, built.sparseReachBlocks());
        assertEquals(List.of(OVERWORLD), built.dimensions());
        assertEquals(List.of("first", "second", "third"), built.oreEntries().stream().map(entry -> entry.oreMaterial().get().name).toList());
        assertEquals(List.of(8, 4, 2), built.oreEntries().stream().map(VeinOreEntry::distributionWeight).toList());
    }

    @Test
    void builderRejectsNegativeSparseReachBlocks() {
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> OreVeinDefinition.builder(ResourceLocation.fromNamespaceAndPath("test", "invalid_sparse_reach"))
                        .dimensions(OVERWORLD)
                        .generationWeight(80)
                        .centerY(12, 27)
                        .sizeX(17, 63)
                        .sizeY(7, 7)
                        .sizeZ(17, 63)
                        .sparseReachBlocks(-1)
                        .ores(entry("first", 8))
                        .build()
        );

        assertEquals("sparseReachBlocks must be at least 0", exception.getMessage());
    }

    @Test
    void builderExposesNoSpreadSizeApi() {
        List<String> methodNames = Arrays.stream(OreVeinDefinition.Builder.class.getDeclaredMethods())
                .map(Method::getName)
                .toList();

        assertFalse(methodNames.contains("spreadSize"));
        assertFalse(Arrays.stream(OreVeinDefinition.class.getRecordComponents()).anyMatch(component -> component.getName().equals("spreadSize")));
        assertFalse(Arrays.stream(OreVeinDefinition.class.getDeclaredMethods()).anyMatch(method -> method.getName().equals("maximumSpreadReachBlocks")));
    }

    private static OreVeinDensitySettings defaultDensity() {
        return new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 8.5D, 3.0D, 3.5D, 4.0D, 8.5D, 2, 4);
    }

    private static OreVeinHaloSettings defaultHalo() {
        return new OreVeinHaloSettings(4.0D, 9.0D, 320);
    }

    private static VeinOreEntry entry(String name, int weight) {
        return new VeinOreEntry(() -> ore(name, 4), weight);
    }

    private static Material ore(String name, int maxDensity) {
        Material material = new Material(name, null);
        material.ore.setOre(OreTypes.ORE.oreType(), maxDensity, RockTypes.PLUTONIC_IGNEOUS.getType());
        return material;
    }
}
