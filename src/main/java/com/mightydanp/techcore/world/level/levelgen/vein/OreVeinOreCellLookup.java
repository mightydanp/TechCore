package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class OreVeinOreCellLookup {
    private static final Comparator<OreVeinOreCellResult> RESULT_ORDER = Comparator.comparingLong(OreVeinOreCellResult::instanceId).thenComparing(result -> result.definitionId().toString());

    public static @NotNull @Unmodifiable List<OreVeinOreCellResult> resultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        return resultsForPosition(
                worldSeed,
                dimension,
                position,
                OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position)
        );
    }

    public static @NotNull @Unmodifiable List<OreVeinOreCellResult> resultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");

        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        for (OreVeinInstanceDescriptor candidate : candidates) evaluationBounds.add(OreVeinCandidateLookup.evaluationBounds(candidate));

        return resultsForPosition(
                position,
                candidates,
                evaluationBounds,
                OreVeinOreCellEvaluator::evaluateCell
        );
    }

    private static @NotNull @Unmodifiable List<OreVeinOreCellResult> resultsForPosition(BlockPos position, List<OreVeinInstanceDescriptor> candidates, List<OreVeinBounds> evaluationBounds, OreCellEvaluator oreCellEvaluator) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(evaluationBounds, "evaluationBounds");
        Objects.requireNonNull(oreCellEvaluator, "oreCellEvaluator");

        if (candidates.size() != evaluationBounds.size()) throw new IllegalArgumentException("candidates and evaluationBounds must have the same size");

        List<OreVeinOreCellResult> results = new ArrayList<>();

        for (int i = 0; i < candidates.size(); i++) {
            OreVeinInstanceDescriptor descriptor = candidates.get(i);

            if (!evaluationBounds.get(i).contains(position)) continue;

            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);
            OreVeinDefinition definition = Objects.requireNonNull(
                    OreVeinDefinitions.getDefinition(descriptor.definitionId()),
                    "Missing ore vein definition: " + descriptor.definitionId()
            );

            if (contribution.signedBoundaryDistanceBlocks() > definition.sparseReachBlocks()) continue;

            results.add(oreCellEvaluator.evaluate(descriptor, definition, position, contribution));
        }

        results.sort(RESULT_ORDER);
        return List.copyOf(results);
    }

    @FunctionalInterface
    private interface OreCellEvaluator {
        OreVeinOreCellResult evaluate(OreVeinInstanceDescriptor descriptor, OreVeinDefinition definition, BlockPos position, OreVeinContribution contribution);
    }
}

