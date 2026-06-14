package com.mightydanp.techcore.world.level.levelgen.feature;

import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinChunkPlan;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinChunkPlanner;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class OreVeinFeature extends Feature<NoneFeatureConfiguration> {
    @FunctionalInterface
    interface ChunkPlanProvider {
        OreVeinChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive);
    }

    static final ChunkPlanProvider PRODUCTION_CHUNK_PLAN_PROVIDER = OreVeinChunkPlanner::plan;

    final ChunkPlanProvider chunkPlanProvider;

    public OreVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        this(codec, PRODUCTION_CHUNK_PLAN_PROVIDER);
    }

    OreVeinFeature(Codec<NoneFeatureConfiguration> codec, ChunkPlanProvider chunkPlanProvider) {
        super(codec);
        this.chunkPlanProvider = chunkPlanProvider;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        long worldSeed = level.getSeed();
        ResourceKey<Level> dimension = level.getLevel().dimension();
        int minY = level.getMinBuildHeight();
        int maxYExclusive = level.getMaxBuildHeight();
        OreVeinChunkPlan plan = chunkPlanProvider.plan(worldSeed, dimension, chunkPos, minY, maxYExclusive);
        return applyPlan(level, plan);
    }

    static boolean applyPlan(WorldGenLevel level, OreVeinChunkPlan plan) {
        boolean changed = false;

        for (OreVeinChunkPlan.PlannedReplacement replacement : plan.replacements()) {
            BlockPos position = replacement.position();
            BlockState liveState = level.getBlockState(position);

            if (replacement.resolvedCell().replacement()
                    && liveState.equals(replacement.resolvedCell().originalHostState())) {
                level.setBlock(position, replacement.resolvedCell().resolvedBlockState(), Block.UPDATE_CLIENTS);
                changed = true;
            }
        }

        return changed;
    }
}
