package com.mightydanp.techcore.materials.Components;

public enum MaterialComponents {
    BLOCK_GEM(new Component("block_", "_gem", m -> {}, m -> {})),
    BLOCK_METAL(new Component("block_", "_metal", m -> {}, m -> {})),
    DUST(new Component("", "_dust", m -> {}, m -> {})),
    FLUID(new Component("", "_fluid", m -> {}, m -> {})),
    GAS(new Component("", "_gas", m -> {}, m -> {})),
    GEM(new Component("", "_gem", m -> {}, m -> {})),
    HARDENED_INGOT(new Component("hardened_", "_ingot", m -> {}, m -> {})),
    HOT_INGOT(new Component("hot_", "_ingot", m -> {}, m -> {})),
    INGOT(new Component("", "_ingot", m -> {}, m -> {})),
    ORE(new Component("", "_ore", m -> {}, m -> {})),
    SMALL_ORE(new Component("small_", "_ore", m -> {}, m -> {})),
    SOFTENED_INGOT(new Component("softened_", "_ingot", m -> {}, m -> {})),
    STONE_LAYER(new Component("stone_", "_layer", m -> {}, m -> {})),
    TOOL(new Component("", "_tool", m -> {}, m -> {}));

    public final Component simpleComponent;

    MaterialComponents(Component simpleComponent) {
        this.simpleComponent = simpleComponent;
    }

    public String getPrefix() {
        return simpleComponent.prefix();
    }

    public String getSuffix() {
        return simpleComponent.suffix();
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
