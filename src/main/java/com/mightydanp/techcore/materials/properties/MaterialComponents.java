package com.mightydanp.techcore.materials.properties;

import com.mightydanp.techcore.materials.Material;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public enum MaterialComponents {
    BLOCK_GEM(new MaterialComponent("block_", "_gem", m -> {}, m -> {})),
    BLOCK_METAL(new MaterialComponent("block_", "_metal", m -> {}, m -> {})),
    DUST(new MaterialComponent("", "_dust", m -> {}, m -> {})),
    FLUID(new MaterialComponent("", "_fluid", m -> {}, m -> {})),
    GAS(new MaterialComponent("", "_gas", m -> {}, m -> {})),
    GEM(new MaterialComponent("", "_gem", m -> {}, m -> {})),
    HARDENED_INGOT(new MaterialComponent("hardened_", "_ingot", m -> {}, m -> {})),
    HOT_INGOT(new MaterialComponent("hot_", "_ingot", m -> {}, m -> {})),
    INGOT(new MaterialComponent("", "_ingot", m -> {}, m -> {})),
    ORE(new MaterialComponent("", "_ore", m -> {}, m -> {})),
    SMALL_ORE(new MaterialComponent("small_", "_ore", m -> {}, m -> {})),
    SOFTENED_INGOT(new MaterialComponent("softened_", "_ingot", m -> {}, m -> {})),
    STONE_LAYER(new MaterialComponent("stone_", "_layer", m -> {}, m -> {})),
    TOOL(new MaterialComponent("", "_tool", m -> {}, m -> {}));

    public final MaterialComponent materialComponent;

    MaterialComponents(MaterialComponent materialComponent) {
        this.materialComponent = materialComponent;
    }

    public String getPrefix() {
        return materialComponent.prefix();
    }

    public String getSuffix() {
        return materialComponent.suffix();
    }

    @Override
    public String toString() {
        return materialComponent.prefix() + materialComponent.suffix();
    }

    public record MaterialComponent(String prefix, String suffix, Consumer<Material> onServerApply, Consumer<Material> onClientApply) {
        public static final Codec<MaterialComponent> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("prefix").forGetter(MaterialComponent::prefix),
                        Codec.STRING.fieldOf("suffix").forGetter(MaterialComponent::suffix)
                ).apply(instance, (prefix, suffix) -> new MaterialComponent(prefix, suffix, m -> {}, m -> {}))
        );

        public static final StreamCodec<FriendlyByteBuf, MaterialComponent> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, MaterialComponent::prefix,
                ByteBufCodecs.STRING_UTF8, MaterialComponent::suffix,
                (prefix, suffix) -> new MaterialComponent(prefix, suffix, m -> {}, m -> {})
        );

        @Override
        public @NotNull String toString() {
            return prefix + suffix;
        }

        public void applyServer(Material material) {
            onServerApply.accept(material);
        }

        public void applyClient(Material material) {
            onClientApply.accept(material);
        }
    }
}
/*
    GEM, ORE, DUST, PART, PIPE, WIRE, FOIL, LENS, PLATE, DOUBLE_INGOT, DOUBLE_PLATE, DENSE_PLATE, GAS, SOLID, FLUID, PLASMA,

    G_ALL                       = new TagData[] {GEMS, ORES, EMPTY, DUSTS, PARTS, PIPES, WIRES, FOILS, RAILS, LENSES, STICKS, ARMORS, INGOTS, INGOTS_HOT, PLATES, PLANTS, PROJECTILES, CONTAINERS, DIRTY_DUSTS, MULTIINGOTS, MULTIPLATES, DENSEPLATES, CONTAINERS_GAS, CONTAINERS_SOLID, CONTAINERS_FLUID, CONTAINERS_PLASMA},
    G_CONTAINERS                = new TagData[] {CONTAINERS, PLANTS},
    G_DUST                      = new TagData[] {DUSTS, PLANTS},
    G_DUST_ORES                 = new TagData[] {DUSTS, PLANTS, ORES},
    G_CRYSTAL                   = new TagData[] {PROJECTILES, DUSTS, PLANTS, GEMS},
    G_CRYSTAL_ORES              = new TagData[] {PROJECTILES, DUSTS, PLANTS, GEMS, ORES},
    G_BLAZE                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, STICKS},
    G_PEARL                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, GEMS},
    G_PEARL_TRANSPARENT         = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, GEMS, LENSES, TD.Properties.TRANSPARENT},
    G_GLASS                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ARMORS, LENSES, TD.Properties.TRANSPARENT},
    G_QUARTZ                    = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS},
    G_QUARTZ_ORES               = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ORES},
    G_GEM                       = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ARMORS},
    G_GEM_ORES                  = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ARMORS, ORES},
    G_GEM_TRANSPARENT           = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ARMORS, LENSES, TD.Properties.TRANSPARENT},
    G_GEM_ORES_TRANSPARENT      = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, GEMS, ARMORS, LENSES, TD.Properties.TRANSPARENT, ORES},
    G_WOOD                      = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, FOILS},
    G_STONE                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, DIRTY_DUSTS},
    G_BRICK                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, INGOTS},
    G_INGOT                     = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS},
    G_INGOT_ORES                = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS, ORES},
    G_INGOT_MACHINE             = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS, PARTS},
    G_INGOT_MACHINE_ORES        = new TagData[] {PROJECTILES, DUSTS, PLANTS, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS, PARTS, ORES},
    G_INGOT_ND                  = new TagData[] {PROJECTILES, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS},
    G_INGOT_ND_MACHINE          = new TagData[] {PROJECTILES, PLATES, STICKS, ARMORS, INGOTS, INGOTS_HOT, MULTIINGOTS, DENSEPLATES, MULTIPLATES, FOILS, PARTS},
    G_MACHINE                   = new TagData[] {PROJECTILES, PLATES, STICKS, ARMORS, WIRES, FOILS, PARTS};

 */
