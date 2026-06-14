package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.OreTypes;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class OreVeinCompatibilityParityTest {
    private static final ResourceKey<Level> OVERWORLD = OreVeinCandidateLookupTest.dimension("overworld");

    static {
        SharedConstants.tryDetectVersion();
        try {
            Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
            bootstrapped.setAccessible(true);
            bootstrapped.setBoolean(null, true);
            try {
                BuiltInRegistries.bootStrap();
            } catch (RuntimeException ignored) {
            }
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void candidateDescriptorsOreCellResultsAndResolvedCellsRemainIdentical() {
        assertEquals(1, OreVeinGenerationMath.GENERATOR_VERSION);

        Material host = host("granite");
        Material ore = ore("copper", 1);
        ore.ore.oreBlocks.put(host.name, () -> Blocks.IRON_ORE);
        ore.ore.denseOreBlocks.put(host.name, () -> Blocks.DEEPSLATE_IRON_ORE);
        ore.ore.sparseOreBlocks.put(host.name, () -> Blocks.COPPER_ORE);

        OreVeinDefinition canonical = canonicalDefinition("parity", ore);
        OreVeinDefinition built = builtDefinition("parity", ore);
        assertEquals(canonical.densitySettings(), built.densitySettings());
        assertEquals(canonical.haloSettings(), built.haloSettings());
        long seed = 1234L;
        OreVeinInstanceDescriptor canonicalDescriptor = OreVeinCandidateLookup.createDescriptor(seed, OVERWORLD, 0, 0, 0, canonical).orElseThrow();
        OreVeinInstanceDescriptor builtDescriptor = OreVeinCandidateLookup.createDescriptor(seed, OVERWORLD, 0, 0, 0, built).orElseThrow();

        assertEquals(canonicalDescriptor, builtDescriptor);

        BlockPos densePos = new BlockPos(0, 0, 0);
        BlockPos sparsePos = new BlockPos(1, 0, 0);
        OreVeinContribution denseContribution = new OreVeinContribution(canonicalDescriptor.instanceId(), canonical.id(), 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, -1.0D, OreVeinContribution.ContributionState.INSIDE_MAIN_BODY);
        OreVeinContribution sparseContribution = new OreVeinContribution(canonicalDescriptor.instanceId(), canonical.id(), 0.0D, 0.0D, 0.0D, 1.1D, 0.0D, 0.5D, OreVeinContribution.ContributionState.INSIDE_MAIN_BODY);

        OreVeinOreCellResult canonicalDense = OreVeinOreCellEvaluator.evaluateCell(canonicalDescriptor, canonical, densePos, denseContribution);
        OreVeinOreCellResult builtDense = OreVeinOreCellEvaluator.evaluateCell(builtDescriptor, built, densePos, denseContribution);
        OreVeinOreCellResult canonicalSparse = OreVeinOreCellEvaluator.evaluateCell(canonicalDescriptor, canonical, sparsePos, sparseContribution);
        OreVeinOreCellResult builtSparse = OreVeinOreCellEvaluator.evaluateCell(builtDescriptor, built, sparsePos, sparseContribution);

        assertEquals(canonicalDense, builtDense);
        assertEquals(canonicalSparse, builtSparse);

        List<OreVeinOreCellResult> canonicalOverlap = List.of(
                canonicalDense,
                new OreVeinOreCellResult(
                        99L,
                        canonical.id(),
                        ore,
                        8,
                        new OreVeinContribution(99L, canonical.id(), 0.0D, 0.0D, 0.0D, 0.5D, 0.0D, -0.1D, OreVeinContribution.ContributionState.INSIDE_MAIN_BODY),
                        1,
                        0,
                        OreVeinOreCellResult.OreVariant.HOST_ROCK,
                        0L,
                        0.0D
                )
        );
        List<OreVeinOreCellResult> builtOverlap = List.of(
                builtDense,
                new OreVeinOreCellResult(
                        99L,
                        built.id(),
                        ore,
                        8,
                        new OreVeinContribution(99L, built.id(), 0.0D, 0.0D, 0.0D, 0.5D, 0.0D, -0.1D, OreVeinContribution.ContributionState.INSIDE_MAIN_BODY),
                        1,
                        0,
                        OreVeinOreCellResult.OreVariant.HOST_ROCK,
                        0L,
                        0.0D
                )
        );

        assertEquals(
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, densePos, host, Blocks.STONE.defaultBlockState(), List.of(canonicalDense)),
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, densePos, host, Blocks.STONE.defaultBlockState(), List.of(builtDense))
        );
        assertEquals(
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, sparsePos, host, Blocks.STONE.defaultBlockState(), List.of(canonicalSparse)),
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, sparsePos, host, Blocks.STONE.defaultBlockState(), List.of(builtSparse))
        );
        assertEquals(
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, new BlockPos(2, 0, 0), host, Blocks.STONE.defaultBlockState(), canonicalOverlap),
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, new BlockPos(2, 0, 0), host, Blocks.STONE.defaultBlockState(), builtOverlap)
        );
        assertEquals(
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, new BlockPos(3, 0, 0), host, Blocks.STONE.defaultBlockState(), List.of()),
                OreVeinResolvedCellResolver.resolve(seed, OVERWORLD, new BlockPos(3, 0, 0), host, Blocks.STONE.defaultBlockState(), List.of())
        );
    }

    private static OreVeinDefinition canonicalDefinition(String name, Material ore) {
        return new OreVeinDefinition(
                ResourceLocation.fromNamespaceAndPath("test", name),
                List.of(OVERWORLD),
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
                new OreVeinDensitySettings(704, 960, 1024, 4096L, 0, 5, 4.0D, 8.5D, 3.0D, 3.5D, 4.0D, 8.5D, 2, 4),
                new OreVeinHaloSettings(4.0D, 9.0D, 320),
                List.of(new VeinOreEntry(() -> ore, 8))
        );
    }

    private static OreVeinDefinition builtDefinition(String name, Material ore) {
        return OreVeinDefinition.builder(ResourceLocation.fromNamespaceAndPath("test", name))
                .dimensions(OVERWORLD)
                .generationWeight(80)
                .centerY(12, 27)
                .sizeX(17, 63)
                .sizeY(7, 7)
                .sizeZ(17, 63)
                .sparseReachBlocks(32)
                .ores(new VeinOreEntry(() -> ore, 8))
                .build();
    }

    private static Material host(String name) {
        Material material = new Material(name, null);
        material.rockLayer.rockLayer(RockTypes.PLUTONIC_IGNEOUS.getType());
        return material;
    }

    private static Material ore(String name, int maxDensity) {
        Material material = new Material(name, null);
        material.ore.setOre(OreTypes.ORE.oreType(), maxDensity, RockTypes.PLUTONIC_IGNEOUS.getType());
        return material;
    }
}
