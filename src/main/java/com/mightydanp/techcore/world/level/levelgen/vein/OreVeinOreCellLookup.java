package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class OreVeinOreCellLookup {
    @FunctionalInterface
    interface OreCellEvaluator {
        OreVeinOreCellResult evaluate(
                OreVeinInstanceDescriptor descriptor,
                OreVeinDefinition definition,
                BlockPos position,
                OreVeinContribution contribution
        );
    }

    private static final Comparator<OreVeinOreCellResult> RESULT_ORDER =
            Comparator.comparingLong(OreVeinOreCellResult::instanceId)
                    .thenComparing(result -> result.definitionId().toString());

    private OreVeinOreCellLookup() {
    }

    public static List<OreVeinOreCellResult> resultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        return resultsForPosition(worldSeed, dimension, position, OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position));
    }

    public static List<OreVeinOreCellResult> resultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");
        List<OreVeinBounds> evaluationBounds = new ArrayList<>(candidates.size());

        for (OreVeinInstanceDescriptor candidate : candidates) {
            evaluationBounds.add(OreVeinCandidateLookup.evaluationBounds(candidate));
        }

        return resultsForPosition(
                worldSeed,
                dimension,
                position,
                candidates,
                evaluationBounds,
                OreVeinOreCellEvaluator::evaluateCell
        );
    }

    static List<OreVeinOreCellResult> resultsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position, List<OreVeinInstanceDescriptor> candidates, List<OreVeinBounds> evaluationBounds) {
        return resultsForPosition(
                worldSeed,
                dimension,
                position,
                candidates,
                evaluationBounds,
                OreVeinOreCellEvaluator::evaluateCell
        );
    }

    static List<OreVeinOreCellResult> resultsForPosition(
            long worldSeed,
            ResourceKey<Level> dimension,
            BlockPos position,
            List<OreVeinInstanceDescriptor> candidates,
            List<OreVeinBounds> evaluationBounds,
            OreCellEvaluator oreCellEvaluator
    ) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(evaluationBounds, "evaluationBounds");
        Objects.requireNonNull(oreCellEvaluator, "oreCellEvaluator");

        List<OreVeinOreCellResult> results = new ArrayList<>();

        for (int i = 0; i < candidates.size(); i++) {
            OreVeinInstanceDescriptor descriptor = candidates.get(i);

            if (!evaluationBounds.get(i).contains(position)) {
                continue;
            }

            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);
            OreVeinDefinition definition = Objects.requireNonNull(
                    OreVeinDefinitions.getDefinition(descriptor.definitionId()),
                    "Missing ore vein definition: " + descriptor.definitionId()
            );

            try {
                results.add(oreCellEvaluator.evaluate(descriptor, definition, position, contribution));
            } catch (IllegalArgumentException exception) {
                if ("position is outside halo".equals(exception.getMessage())) {
                    continue;
                }

                throw exception;
            } catch (NullPointerException exception) {
                if ("oreMaterial supplier returned null".equals(exception.getMessage())) {
                    continue;
                }

                throw exception;
            }
        }

        results.sort(RESULT_ORDER);
        return List.copyOf(results);
    }
}
