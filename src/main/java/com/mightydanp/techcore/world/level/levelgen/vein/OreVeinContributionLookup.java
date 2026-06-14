package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.mightydanp.techcore.world.level.levelgen.vein.OreVeinContribution.ContributionState.INSIDE_MAIN_BODY;

public final class OreVeinContributionLookup {
    private static final Comparator<OreVeinContribution> CONTRIBUTION_ORDER =
            Comparator.comparingLong(OreVeinContribution::instanceId)
                    .thenComparing(contribution -> contribution.definitionId().toString());

    private OreVeinContributionLookup() {
    }

    public static List<OreVeinContribution> contributionsForPosition(long worldSeed, ResourceKey<Level> dimension, BlockPos position) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(position, "position");

        List<OreVeinContribution> contributions = new ArrayList<>();

        for (OreVeinInstanceDescriptor descriptor : OreVeinCandidateLookup.candidatesForBlock(worldSeed, dimension, position)) {
            OreVeinContribution contribution = OreVeinShapeEvaluator.evaluate(descriptor, position);

            if (contribution.state() == INSIDE_MAIN_BODY) {
                contributions.add(contribution);
            }
        }

        contributions.sort(CONTRIBUTION_ORDER);
        return List.copyOf(contributions);
    }
}
