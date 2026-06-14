package com.mightydanp.techcore.world.level.levelgen.feature;

import com.mightydanp.techcore.materials.Material;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class RockLayerFeature extends Feature<NoneFeatureConfiguration> {
    private static final double FREQUENCY_X = 0.009D;
    private static final double FREQUENCY_Y = 0.075D;
    private static final double FREQUENCY_Z = 0.009D;

    private static final Map<ResourceKey<Level>, List<Supplier<Material>>> ALLOWED_MATERIALS_BY_DIMENSION = new LinkedHashMap<>();

    public RockLayerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();

        // Get all rock layer materials that are allowed to generate in this dimension.
        List<RockLayer> layers = RockLayer.fromMaterials(getAllowedMaterials(level.getLevel().dimension()));

        // If there are no rock layers registered for this dimension, do not generate anything.
        if (layers.isEmpty()) {
            return false;
        }

        ChunkPos chunk = new ChunkPos(context.origin());
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        boolean placed = false;

        // Get the full build height for the current worldgen level.
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        // Loop through every X position in the chunk.
        for (int localX = 0; localX < 16; localX++) {
            int x = chunk.getMinBlockX() + localX;

            // Loop through every Z position in the chunk.
            for (int localZ = 0; localZ < 16; localZ++) {
                int z = chunk.getMinBlockZ() + localZ;

                // Walk the whole vertical column so the layer field is continuous.
                for (int y = minY; y < maxY; y++) {
                    cursor.set(x, y, z);

                    // Check if the current block is a stone type this feature is allowed to replace.
                    BlockState current = level.getBlockState(cursor);
                    Replacement replacement = Replacement.forState(current);

                    // If this is not a replaceable block, leave it alone.
                    if (replacement == Replacement.NONE) {
                        continue;
                    }

                    // Select the rock layer from absolute coordinates so regions cross chunk borders.
                    RockLayer layer = getOriginalRockLayer(level.getSeed(), layers, cursor);
                    if (layer == null) {
                        continue;
                    }

                    BlockState target = layer.stateFor(replacement);

                    // Replace the block only when the selected rock layer differs from the current block.
                    if (target != current) {
                        level.setBlock(cursor, target, Block.UPDATE_CLIENTS);
                        placed = true;
                    }
                }
            }
        }

        return placed;
    }

    public static List<Material> getAllowedMaterials(ResourceKey<Level> dimension) {
        List<Supplier<Material>> materialSuppliers = ALLOWED_MATERIALS_BY_DIMENSION.get(dimension);

        if (materialSuppliers == null || materialSuppliers.isEmpty()) {
            return List.of();
        }

        List<Material> materials = new ArrayList<>();

        for (Supplier<Material> materialSupplier : materialSuppliers) {
            materials.add(materialSupplier.get());
        }

        return Collections.unmodifiableList(materials);
    }

    public static @Nullable Material getOriginalRockMaterial(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        //return the original rock material from the dimension's allowed materials
        return getOriginalRockMaterial(worldSeed, getAllowedMaterials(dimension), position);
    }

    public static @Nullable BlockState getOriginalRockState(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        // Get the original rock material without checking the current live block.
        Material material = getOriginalRockMaterial(worldSeed, dimension, position);

        if (material == null) {
            //return nothing if the position has no original rock material
            return null;
        }

        // Return the normal stone variant because vanilla terrain state cannot be reconstructed here.
        RockLayer layer = RockLayer.fromMaterial(material);
        //return nothing if the material has no usable rock layer, otherwise return the stone state
        return layer == null ? null : layer.stone();
    }

    public static void setAllowedMaterials(ResourceKey<Level> dimension, List<Material> materials) {
        List<Supplier<Material>> materialSuppliers = new ArrayList<>();

        for (Material material : materials) {
            materialSuppliers.add(() -> material);
        }

        ALLOWED_MATERIALS_BY_DIMENSION.put(dimension, materialSuppliers);
    }

    public static void setAllowedMaterialSuppliers(ResourceKey<Level> dimension, List<Supplier<Material>> materials) {
        ALLOWED_MATERIALS_BY_DIMENSION.put(dimension, new ArrayList<>(materials));
    }

    public static void addAllowedMaterial(ResourceKey<Level> dimension, Material material) {
        addAllowedMaterial(dimension, () -> material);
    }

    public static void addAllowedMaterial(ResourceKey<Level> dimension, Supplier<Material> material) {
        ALLOWED_MATERIALS_BY_DIMENSION.computeIfAbsent(dimension, key -> new ArrayList<>()).add(material);
    }

    public static void removeAllowedMaterial(ResourceKey<Level> dimension, Material material) {
        List<Supplier<Material>> materialSuppliers = ALLOWED_MATERIALS_BY_DIMENSION.get(dimension);

        if (materialSuppliers == null) {
            return;
        }

        materialSuppliers.removeIf(materialSupplier -> Objects.equals(materialSupplier.get(), material));

        if (materialSuppliers.isEmpty()) {
            ALLOWED_MATERIALS_BY_DIMENSION.remove(dimension);
        }
    }

    static @Nullable Material getOriginalRockMaterial(long worldSeed, List<Material> materials, BlockPos position) {
        // Select from the supplied ordered material list using the same index as world generation.
        int index = getOriginalRockIndex(worldSeed, position, materials.size());
        //return nothing if no material can be selected, otherwise return the selected material
        return index < 0 ? null : materials.get(index);
    }

    private static @Nullable RockLayer getOriginalRockLayer(long worldSeed, List<RockLayer> layers, BlockPos position) {
        // Select from prebuilt rock layers using the same index exposed to material lookup callers.
        int index = getOriginalRockIndex(worldSeed, position, layers.size());
        //return nothing if no layer can be selected, otherwise return the selected layer
        return index < 0 ? null : layers.get(index);
    }

    static int getOriginalRockIndex(long worldSeed, BlockPos position, int size) {
        // Empty layer lists have no original rock owner for this coordinate.
        if (size <= 0) {
            //return no layer index
            return -1;
        }

        // Use absolute block coordinates so chunk borders and negative positions stay deterministic.
        //return the selected original rock layer index
        return layerIndex(worldSeed, position.getX(), position.getY(), position.getZ(), size);
    }

    private static int layerIndex(long seed, int x, int y, int z, int size) {
        // Convert GT6-like cellular noise into a valid layer list index.
        double value = cellularValue(seed, x * FREQUENCY_X, y * FREQUENCY_Y, z * FREQUENCY_Z);
        return Math.min(size - 1, (int) (value * size));
    }

    private static double cellularValue(long seed, double x, double y, double z) {
        // Find the current noise cell for the scaled world position.
        int cellX = fastFloor(x);
        int cellY = fastFloor(y);
        int cellZ = fastFloor(z);
        double bestDistance = Double.MAX_VALUE;
        long bestHash = 0L;

        // Check neighboring cells so the nearest random cell point can be found.
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            int sampleX = cellX + offsetX;

            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                int sampleY = cellY + offsetY;

                for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                    int sampleZ = cellZ + offsetZ;

                    // Get a stable pseudo-random point inside this cell.
                    double pointX = sampleX + unit(hash(seed, sampleX, sampleY, sampleZ, 0x1f4a7c15L));
                    double pointY = sampleY + unit(hash(seed, sampleX, sampleY, sampleZ, 0x5bf03635L));
                    double pointZ = sampleZ + unit(hash(seed, sampleX, sampleY, sampleZ, 0x8c1f9a9dL));
                    double distanceX = pointX - x;
                    double distanceY = pointY - y;
                    double distanceZ = pointZ - z;
                    double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

                    // Keep the closest cell point as the owner of this position.
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestHash = hash(seed, sampleX, sampleY, sampleZ, 0xd1b54a32L);
                    }
                }
            }
        }

        // Return a stable value for the owning cell.
        return unit(bestHash);
    }

    private static int fastFloor(double value) {
        // Match mathematical floor for negative world coordinates.
        int floor = (int) value;
        return value < floor ? floor - 1 : floor;
    }

    private static long hash(long seed, int x, int y, int z, long salt) {
        // Mix the seed, cell position, and salt into one deterministic value.
        long value = seed ^ salt;
        value ^= (long) x * 0x9E3779B97F4A7C15L;
        value ^= (long) y * 0xC2B2AE3D27D4EB4FL;
        value ^= (long) z * 0x165667B19E3779F9L;
        return mix(value);
    }

    private static long mix(long value) {
        // Finalize the hash so nearby cells do not produce simple repeating patterns.
        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }

    private static double unit(long value) {
        // Convert the high bits of the hash into a value from zero to one.
        return (value >>> 11) * 0x1.0p-53D;
    }

    private enum Replacement {
        NONE,
        STONE,
        COBBLE,
        MOSSY_COBBLE;

        private static Replacement forState(BlockState state) {
            // If this is vanilla cobblestone, use the current layer cobble variant.
            if (state.is(Blocks.COBBLESTONE)) {
                return COBBLE;
            }

            // If this is vanilla mossy cobblestone, use the current layer mossy cobble variant.
            if (state.is(Blocks.MOSSY_COBBLESTONE)) {
                return MOSSY_COBBLE;
            }

            // If this is natural stone or vanilla ore, use the current layer stone variant.
            if (isReplaceableStone(state)) {
                return STONE;
            }

            return NONE;
        }

        private static boolean isReplaceableStone(BlockState state) {
            return state.is(Blocks.STONE)
                    || state.is(Blocks.DEEPSLATE)
                    || state.is(Blocks.GRANITE)
                    || state.is(Blocks.DIORITE)
                    || state.is(Blocks.ANDESITE)
                    || state.is(Blocks.TUFF)
                    || state.is(Blocks.CALCITE);
        }
    }

    private record RockLayer(BlockState stone, BlockState cobble, BlockState mossyCobble) {
        private static List<RockLayer> fromMaterials(List<Material> materials) {
            List<RockLayer> layers = new ArrayList<>();

            // Convert material definitions into concrete block states usable during generation.
            for (Material material : materials) {
                RockLayer layer = fromMaterial(material);

                if (layer != null) {
                    layers.add(layer);
                }
            }

            return layers;
        }

        private static RockLayer fromMaterial(Material material) {
            // Get the main stone block for this material, using existing vanilla/mod blocks when configured.
            Block stone = first(material.rockLayer.existingRocklayerBlock, material.rockLayer.stoneBlock, Blocks.STONE);
            if (stone == null) {
                return null;
            }

            // Get the cobble and mossy cobble variants, falling back like GT6 does.
            Block cobble = first(material.rockLayer.existingCobbleBlock, material.rockLayer.cobbleBlock, stone);
            Block mossyCobble = first(null, material.rockLayer.mossyCobbleBlock, cobble);

            return new RockLayer(stone.defaultBlockState(), cobble.defaultBlockState(), mossyCobble.defaultBlockState());
        }

        private static Block first(Block existingBlock, Supplier<Block> generatedBlock, Block fallback) {
            // Prefer an existing configured block.
            if (existingBlock != null) {
                return existingBlock;
            }

            // Otherwise use the generated material block when it exists.
            if (generatedBlock != null) {
                return generatedBlock.get();
            }

            // If no specific block exists, use the supplied fallback.
            return fallback;
        }

        private BlockState stateFor(Replacement replacement) {
            // Select the correct variant for the vanilla block being replaced.
            return switch (replacement) {
                case COBBLE -> cobble;
                case MOSSY_COBBLE -> mossyCobble;
                case STONE, NONE -> stone;
            };
        }
    }
}
