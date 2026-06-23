package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record OreVeinBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public OreVeinBounds {
        // If the minimum bounds are greater than the maximum bounds return an error
        if (minX > maxX || minY > maxY || minZ > maxZ) throw new IllegalArgumentException("minimum bounds must not exceed maximum bounds");
    }

    @Contract("_, _ -> new")
    public static @NotNull OreVeinBounds from(@NotNull BlockPos minInclusive, @NotNull BlockPos maxInclusive) {
        int minX = Math.min(minInclusive.getX(), maxInclusive.getX());
        int minY = Math.min(minInclusive.getY(), maxInclusive.getY());
        int minZ = Math.min(minInclusive.getZ(), maxInclusive.getZ());
        int maxX = Math.max(minInclusive.getX(), maxInclusive.getX());
        int maxY = Math.max(minInclusive.getY(), maxInclusive.getY());
        int maxZ = Math.max(minInclusive.getZ(), maxInclusive.getZ());

        return new OreVeinBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean intersects(OreVeinBounds other) {
        Objects.requireNonNull(other, "other");

        // Check whether these two inclusive bounding boxes overlap on every axis.
        return maxX >= other.minX
                && minX <= other.maxX
                && maxY >= other.minY
                && minY <= other.maxY
                && maxZ >= other.minZ
                && minZ <= other.maxZ;
    }

    public @Nullable OreVeinBounds intersect(OreVeinBounds other) {
        // Return nothing when the two bounds do not overlap.
        if (!intersects(other)) return null;

        // Build the shared overlapping bounds from the tightest min/max values.
        return new OreVeinBounds(
                Math.max(minX, other.minX),
                Math.max(minY, other.minY),
                Math.max(minZ, other.minZ),
                Math.min(maxX, other.maxX),
                Math.min(maxY, other.maxY),
                Math.min(maxZ, other.maxZ)
        );
    }

    public boolean contains(BlockPos position) {
        Objects.requireNonNull(position, "position");

        // Check whether the block position stays inside these inclusive bounds.
        return position.getX() >= minX && position.getX() <= maxX
                && position.getY() >= minY && position.getY() <= maxY
                && position.getZ() >= minZ && position.getZ() <= maxZ;
    }

    @Contract("_ -> new")
    public @NotNull OreVeinBounds inflate(int amount) {
        // Expand these bounds outward by the same amount on every axis.
        return new OreVeinBounds(
                Math.subtractExact(minX, amount),
                Math.subtractExact(minY, amount),
                Math.subtractExact(minZ, amount),
                Math.addExact(maxX, amount),
                Math.addExact(maxY, amount),
                Math.addExact(maxZ, amount)
        );
    }
}
