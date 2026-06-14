package com.mightydanp.techcore.world.level.levelgen.vein;

import com.mightydanp.techcore.materials.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public record OreVeinResolvedCell(
        BlockPos position,
        Material originalHostMaterial,
        BlockState originalHostState,
        OreVeinOreCellResult winningOreCellResult,
        BlockState resolvedBlockState,
        boolean replacement,
        boolean overlapGapEvaluated,
        boolean overlapGapWon,
        List<Long> participatingMainBodyGapInstanceIds
) {
    public OreVeinResolvedCell {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(originalHostMaterial, "originalHostMaterial");
        Objects.requireNonNull(originalHostState, "originalHostState");
        Objects.requireNonNull(resolvedBlockState, "resolvedBlockState");
        participatingMainBodyGapInstanceIds = List.copyOf(participatingMainBodyGapInstanceIds);
    }
}
