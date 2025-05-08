package com.mightydanp.techcore.api.materials.properties;

import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum MaterialFlags {
    BLOCK_GEM("block_", "_gem"),
    BLOCK_METAL("block_", "_metal"),
    DUST("", "_dust"),
    FLUID("", "_fluid"),
    GAS("", "_gas"),
    GEM("", "_gem"),
    HARDENED_INGOT("hardened_", "_ingot"),
    HOT_INGOT("hot_", "_ingot"),
    INGOT("", "_ingot"),
    ORE("", "_ore"),
    SMALL_ORE("small_", "_ore"),
    SOFTENED_INGOT("softened_", "_ingot"),
    STONE_LAYER("stone_", "_layer"),
    TOOL("", "_tool");

    private final Codec codec;

    MaterialFlags(String prefix, String suffix) {
        this.codec = new Codec(prefix, suffix);

    }

    public Codec getCodec() {
        return this.codec;
    }

    public record Codec(String prefix, String suffix) {
        public static String codecName = "material_flag";

        public static final com.mojang.serialization.Codec<Codec> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                com.mojang.serialization.Codec.STRING.fieldOf("prefix").forGetter(Codec::prefix),
                com.mojang.serialization.Codec.STRING.fieldOf("suffix").forGetter(Codec::suffix)
        ).apply(instance, Codec::new));

        public static String fixesToName(Codec codec) {
            String prefix = codec.prefix().replace("_", "");
            String suffix = codec.suffix().replace("_", "");
            String name = "";

            if (!prefix.isEmpty() && !suffix.isEmpty()) {
                name = prefix + "_" + suffix;
            }

            if (prefix.isEmpty() && !suffix.isEmpty()) {
                name = suffix;
            }

            if (!prefix.isEmpty() && suffix.isEmpty()) {
                name = prefix;
            }

            return name;
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
