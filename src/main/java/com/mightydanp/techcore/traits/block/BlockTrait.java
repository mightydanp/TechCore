package com.mightydanp.techcore.traits.block;

import com.mightydanp.techcore.traits.Trait;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;

public class BlockTrait extends Trait<BlockTrait> {
    private int color = 0;
    private Double kilograms = 0.0;
    private Boolean canPickUp = false;

    public BlockTrait() {
        super(Registries.BLOCK);

    }

    public BlockTrait(Integer color, Double kilograms, Boolean canPickUp) {
        super(Registries.BLOCK);
        this.color = color;
        this.kilograms = kilograms;
        this.canPickUp = canPickUp;
    }

    @Override
    public Codec<BlockTrait> codec() {
        return RecordCodecBuilder.create((instance) -> instance.group(
                Codec.INT.optionalFieldOf("color", color).forGetter(BlockTrait::color),
                Codec.DOUBLE.optionalFieldOf("kilograms", kilograms).forGetter(BlockTrait::kilograms),
                Codec.BOOL.optionalFieldOf("can_pick_up", canPickUp).forGetter(BlockTrait::canPickUp)
        ).apply(instance, BlockTrait::new));
    }

    public static BlockTrait getInstance() {
        return new BlockTrait();
    }

    public BlockTrait color(int color) {
        this.color = color;
        return this;
    }

    public int color() {
        return color;
    }

    public BlockTrait kilograms(double kilograms) {
        this.kilograms = kilograms;
        return this;
    }

    public Double kilograms() {
        return kilograms;
    }

    public BlockTrait canPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
        return this;
    }

    public Boolean canPickUp() {
        return canPickUp;
    }

    public Double meters() {
        return 1.0D;
    }


    record BlockTraitCodec(Integer color, Double kilograms, Boolean canPickUp) {
    }
}
