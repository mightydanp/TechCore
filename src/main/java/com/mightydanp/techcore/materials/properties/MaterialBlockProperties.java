package com.mightydanp.techcore.materials.properties;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class MaterialBlockProperties extends BlockBehaviour.Properties {
    public static MaterialBlockProperties of() {
        return new MaterialBlockProperties();
    }

    public BlockBehaviour.Properties properties() {
        return this;
    }

    public @NotNull MaterialBlockProperties mapColor(@NotNull DyeColor dyeColor) {
        super.mapColor(dyeColor);
        return this;
    }

    public @NotNull MaterialBlockProperties mapColor(@NotNull MapColor mapColor) {
        super.mapColor(mapColor);
        return this;
    }

    public @NotNull MaterialBlockProperties mapColor(@NotNull Function<BlockState, MapColor> mapColor) {
        super.mapColor(mapColor);
        return this;
    }

    public @NotNull MaterialBlockProperties noCollission() {
        super.noCollission();
        return this;
    }

    public @NotNull MaterialBlockProperties noOcclusion() {
        super.noOcclusion();
        return this;
    }

    public @NotNull MaterialBlockProperties friction(float friction) {
        super.friction(friction);
        return this;
    }

    public @NotNull MaterialBlockProperties speedFactor(float speedFactor) {
        super.speedFactor(speedFactor);
        return this;
    }

    public @NotNull MaterialBlockProperties jumpFactor(float jumpFactor) {
        super.jumpFactor(jumpFactor);
        return this;
    }

    public @NotNull MaterialBlockProperties sound(@NotNull SoundType soundType) {
        super.sound(soundType);
        return this;
    }

    public @NotNull MaterialBlockProperties lightLevel(@NotNull ToIntFunction<BlockState> blockStateToInt) {
        super.lightLevel(blockStateToInt);
        return this;
    }

    public @NotNull MaterialBlockProperties strength(float destroyTime, float explodingResistance) {
        super.strength(destroyTime, explodingResistance);
        return this;
    }

    public @NotNull MaterialBlockProperties instabreak() {
        super.instabreak();
        return this;
    }

    public @NotNull MaterialBlockProperties strength(float strength) {
        super.strength(strength);
        return this;
    }

    public @NotNull MaterialBlockProperties randomTicks() {
        super.randomTicks();
        return this;
    }

    public @NotNull MaterialBlockProperties dynamicShape() {
        super.dynamicShape();
        return this;
    }

    public @NotNull MaterialBlockProperties noLootTable() {
        super.noLootTable();
        return this;
    }

    public @NotNull MaterialBlockProperties lootFrom(@NotNull Supplier<? extends Block> blockIn) {
        super.lootFrom(blockIn);
        return this;
    }

    public @NotNull MaterialBlockProperties ignitedByLava() {
        super.ignitedByLava();
        return this;
    }

    public @NotNull MaterialBlockProperties liquid() {
        super.liquid();
        return this;
    }

    public @NotNull MaterialBlockProperties forceSolidOn() {
        super.forceSolidOn();
        return this;
    }

    public @NotNull MaterialBlockProperties pushReaction(@NotNull PushReaction pushReaction) {
        super.pushReaction(pushReaction);
        return this;
    }

    public @NotNull MaterialBlockProperties air() {
        super.air();
        return this;
    }

    public @NotNull MaterialBlockProperties isValidSpawn(BlockBehaviour.@NotNull StateArgumentPredicate<EntityType<?>> predicate) {
        super.isValidSpawn(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties isRedstoneConductor(BlockBehaviour.@NotNull StatePredicate predicate) {
        super.isRedstoneConductor(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties isSuffocating(BlockBehaviour.@NotNull StatePredicate predicate) {
        super.isSuffocating(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties isViewBlocking(BlockBehaviour.@NotNull StatePredicate predicate) {
        super.isViewBlocking(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties hasPostProcess(BlockBehaviour.@NotNull StatePredicate predicate) {
        super.hasPostProcess(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties emissiveRendering(BlockBehaviour.@NotNull StatePredicate predicate) {
        super.emissiveRendering(predicate);
        return this;
    }

    public @NotNull MaterialBlockProperties requiresCorrectToolForDrops() {
        super.requiresCorrectToolForDrops();
        return this;
    }

    public @NotNull MaterialBlockProperties destroyTime(float destroyTime) {
        super.destroyTime(destroyTime);
        return this;
    }

    public @NotNull MaterialBlockProperties explosionResistance(float explosionResistance) {
        super.explosionResistance(explosionResistance);
        return this;
    }

    public @NotNull MaterialBlockProperties offsetType(BlockBehaviour.@NotNull OffsetType offsetType) {
        super.offsetType(offsetType);
        return this;
    }
}
