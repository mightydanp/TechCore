package com.mightydanp.techcore.world.level.levelgen.feature;

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
    static final ChunkPlanProvider PRODUCTION_CHUNK_PLAN_PROVIDER = OreVeinChunkPlanner::plan;
    final ChunkPlanProvider chunkPlanProvider;

    public OreVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        this(codec, PRODUCTION_CHUNK_PLAN_PROVIDER);
    }

    OreVeinFeature(Codec<NoneFeatureConfiguration> codec, ChunkPlanProvider chunkPlanProvider) {
        super(codec);
        this.chunkPlanProvider = chunkPlanProvider;
    }

    static boolean applyPlan(WorldGenLevel level, OreVeinChunkPlanner.ChunkPlan plan) {
        boolean changed = false;

        for (OreVeinChunkPlanner.ChunkPlan.PlannedReplacement replacement : plan.replacements()) {
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

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        long worldSeed = level.getSeed();
        ResourceKey<Level> dimension = level.getLevel().dimension();
        int minY = level.getMinBuildHeight();
        int maxYExclusive = level.getMaxBuildHeight();
        OreVeinChunkPlanner.ChunkPlan plan = chunkPlanProvider.plan(worldSeed, dimension, chunkPos, minY, maxYExclusive);
        return applyPlan(level, plan);
    }

    @FunctionalInterface
    interface ChunkPlanProvider {
        OreVeinChunkPlanner.ChunkPlan plan(long worldSeed, ResourceKey<Level> dimension, ChunkPos chunkPos, int minY, int maxYExclusive);
    }
}
