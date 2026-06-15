package com.mightydanp.techcore.world.level.levelgen.vein;

import net.minecraft.core.BlockPos;

import java.util.Objects;

public record OreVeinBounds(
        int minX,
        int minY,
        int minZ,
        int maxX,
        int maxY,
        int maxZ
) {
    public OreVeinBounds {
        if (minX > maxX || minY > maxY || minZ > maxZ) {
            throw new IllegalArgumentException("minimum bounds must not exceed maximum bounds");
        }
    }

    public static OreVeinBounds from(BlockPos minInclusive, BlockPos maxInclusive) {
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

        return maxX >= other.minX
                && minX <= other.maxX
                && maxY >= other.minY
                && minY <= other.maxY
                && maxZ >= other.minZ
                && minZ <= other.maxZ;
    }

    public OreVeinBounds intersect(OreVeinBounds other) {
        if (!intersects(other)) {
            return null;
        }

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

        return position.getX() >= minX && position.getX() <= maxX
                && position.getY() >= minY && position.getY() <= maxY
                && position.getZ() >= minZ && position.getZ() <= maxZ;
    }

    public OreVeinBounds inflate(int amount) {
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
