package com.mightydanp.techcore.command;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.components.OreComponent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public final class RevealVeinsCommand {
    static final int DEFAULT_RADIUS = 0;
    static final int MIN_RADIUS = 0;
    static final int MAX_RADIUS = 9;

    private RevealVeinsCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        register(dispatcher, !FMLEnvironment.production);
    }

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean developmentMode) {
        if (!developmentMode) {
            return;
        }

        LiteralArgumentBuilder<CommandSourceStack> reveal = Commands.literal("revealveins")
                .requires(source -> source.hasPermission(4))
                .executes(context -> execute(context.getSource(), RevealMode.ALL, DEFAULT_RADIUS))
                .then(
                        Commands.argument("chunkRadius", IntegerArgumentType.integer(MIN_RADIUS, MAX_RADIUS))
                                .executes(context -> execute(context.getSource(), RevealMode.ALL, IntegerArgumentType.getInteger(context, "chunkRadius")))
                );

        for (RevealMode mode : RevealMode.values()) {
            reveal.then(
                    Commands.literal(mode.argument())
                            .executes(context -> execute(context.getSource(), mode, DEFAULT_RADIUS))
                            .then(
                                    Commands.argument("chunkRadius", IntegerArgumentType.integer(MIN_RADIUS, MAX_RADIUS))
                                            .executes(context -> execute(context.getSource(), mode, IntegerArgumentType.getInteger(context, "chunkRadius")))
                            )
            );
        }

        dispatcher.register(Commands.literal(CoreRef.MOD_ID).then(reveal));
    }

    private static int execute(CommandSourceStack source, RevealMode mode, int radius) {
        ServerLevel level = source.getLevel();
        BlockPos sourceBlockPos = BlockPos.containing(source.getPosition());
        ChunkPos sourceChunk = new ChunkPos(sourceBlockPos);
        OreFormMembershipSets membershipSets = oreFormMembershipSets();
        int maxPossibleChunks = maxPossibleChunks(radius);

        source.sendSuccess(() -> Component.literal(
                "Destructive developer command: revealveins mode=" + mode.argument()
                        + ", radius=" + radius
                        + ", maxLoadedChunks=" + maxPossibleChunks
                        + ", unloaded chunks will not be loaded or generated, and disabled ore forms will be permanently removed."
        ), false);

        RevealResult result = reveal(level, sourceChunk, radius, mode, membershipSets);

        source.sendSuccess(() -> Component.literal(
                "Reveal veins: mode=" + result.mode().argument()
                        + ", radius=" + result.radius()
                        + ", loaded chunks processed=" + result.loadedChunksProcessed()
                        + ", unloaded chunks skipped=" + result.unloadedChunksSkipped()
                        + ", regular form memberships preserved=" + result.regularFormMembershipsPreserved()
                        + ", dense form memberships preserved=" + result.denseFormMembershipsPreserved()
                        + ", sparse form memberships preserved=" + result.sparseFormMembershipsPreserved()
                        + ", ore blocks removed by filtering=" + result.oreBlocksRemovedByFiltering()
                        + ", other non-ore blocks replaced with air=" + result.otherNonOreBlocksReplaced()
        ), false);

        return result.loadedChunksProcessed() > 0 ? 1 : 0;
    }

    static int maxPossibleChunks(int radius) {
        int side = radius * 2 + 1;
        return side * side;
    }

    static OreFormMembershipSets oreFormMembershipSets() {
        return oreFormMembershipSets(RegistriesHandler.getMaterials());
    }

    static OreFormMembershipSets oreFormMembershipSets(Iterable<Material> materials) {
        LinkedHashSet<Block> regularBlocks = new LinkedHashSet<>();
        LinkedHashSet<Block> denseBlocks = new LinkedHashSet<>();
        LinkedHashSet<Block> sparseBlocks = new LinkedHashSet<>();

        for (Material material : materials) {
            if (material == null || material.ore == null) {
                continue;
            }

            OreComponent<Material> ore = material.ore;
            addOreBlocks(regularBlocks, material, "oreBlocks", ore.getOreBlocks());
            addOreBlocks(denseBlocks, material, "denseOreBlocks", ore.getDenseOreBlocks());
            addOreBlocks(sparseBlocks, material, "sparseOreBlocks", ore.getSparseOreBlocks());
        }

        return new OreFormMembershipSets(
                Set.copyOf(regularBlocks),
                Set.copyOf(denseBlocks),
                Set.copyOf(sparseBlocks)
        );
    }

    private static void addOreBlocks(Set<Block> blocks, Material material, String mapName, Map<String, Supplier<Block>> blockSuppliers) {
        for (Map.Entry<String, Supplier<Block>> entry : blockSuppliers.entrySet()) {
            String hostKey = entry.getKey();
            Supplier<Block> supplier = entry.getValue();

            if (supplier == null) {
                throw invalidOreBlock(material.name, mapName, hostKey, "null supplier");
            }

            final Block block;
            try {
                block = supplier.get();
            } catch (RuntimeException exception) {
                throw invalidOreBlock(material.name, mapName, hostKey, "supplier threw " + exception.getClass().getSimpleName() + ": " + exception.getMessage(), exception);
            }

            if (block == null) {
                throw invalidOreBlock(material.name, mapName, hostKey, "supplier returned null block");
            }

            blocks.add(block);
        }
    }

    static RevealResult reveal(ServerLevel level, ChunkPos sourceChunk, int radius, RevealMode mode, OreFormMembershipSets membershipSets) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(sourceChunk, "sourceChunk");
        Objects.requireNonNull(mode, "mode");
        Objects.requireNonNull(membershipSets, "membershipSets");

        int loadedChunksProcessed = 0;
        int unloadedChunksSkipped = 0;
        int regularFormMembershipsPreserved = 0;
        int denseFormMembershipsPreserved = 0;
        int sparseFormMembershipsPreserved = 0;
        int oreBlocksRemovedByFiltering = 0;
        int otherNonOreBlocksReplaced = 0;
        int minY = level.getMinBuildHeight();
        int maxYExclusive = level.getMaxBuildHeight();
        ServerChunkCache chunkSource = level.getChunkSource();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int chunkZ = sourceChunk.z - radius; chunkZ <= sourceChunk.z + radius; chunkZ++) {
            for (int chunkX = sourceChunk.x - radius; chunkX <= sourceChunk.x + radius; chunkX++) {
                LevelChunk chunk = chunkSource.getChunkNow(chunkX, chunkZ);

                if (chunk == null) {
                    unloadedChunksSkipped++;
                    continue;
                }

                loadedChunksProcessed++;
                int minX = chunk.getPos().getMinBlockX();
                int minZ = chunk.getPos().getMinBlockZ();

                for (int y = minY; y < maxYExclusive; y++) {
                    for (int z = minZ; z <= minZ + 15; z++) {
                        for (int x = minX; x <= minX + 15; x++) {
                            cursor.set(x, y, z);
                            BlockState state = level.getBlockState(cursor);
                            Block block = state.getBlock();
                            boolean regularMember = membershipSets.regularBlocks().contains(block);
                            boolean denseMember = membershipSets.denseBlocks().contains(block);
                            boolean sparseMember = membershipSets.sparseBlocks().contains(block);
                            boolean preserve = (regularMember && mode.preserveRegular())
                                    || (denseMember && mode.preserveDense())
                                    || (sparseMember && mode.preserveSparse());

                            if (preserve) {
                                if (regularMember) {
                                    regularFormMembershipsPreserved++;
                                }
                                if (denseMember) {
                                    denseFormMembershipsPreserved++;
                                }
                                if (sparseMember) {
                                    sparseFormMembershipsPreserved++;
                                }
                            } else if (!state.isAir()) {
                                level.setBlock(cursor, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

                                if (regularMember || denseMember || sparseMember) {
                                    oreBlocksRemovedByFiltering++;
                                } else {
                                    otherNonOreBlocksReplaced++;
                                }
                            }
                        }
                    }
                }
            }
        }

        return new RevealResult(
                mode,
                radius,
                loadedChunksProcessed,
                unloadedChunksSkipped,
                regularFormMembershipsPreserved,
                denseFormMembershipsPreserved,
                sparseFormMembershipsPreserved,
                oreBlocksRemovedByFiltering,
                otherNonOreBlocksReplaced
        );
    }

    private static IllegalStateException invalidOreBlock(String materialName, String mapName, String hostKey, String reason) {
        return invalidOreBlock(materialName, mapName, hostKey, reason, null);
    }

    private static IllegalStateException invalidOreBlock(String materialName, String mapName, String hostKey, String reason, RuntimeException cause) {
        String message = "Invalid reveal-veins ore block registration:"
                + " material=" + materialName
                + ", oreFormMap=" + mapName
                + ", hostKey=" + hostKey
                + ", reason=" + reason;
        return cause == null ? new IllegalStateException(message) : new IllegalStateException(message, cause);
    }

    enum RevealMode {
        ALL("all", true, true, true),
        NO_SPARSE("no_sparse", true, true, false),
        NO_REGULAR("no_regular", false, true, true),
        DENSE("dense", false, true, false),
        REGULAR("regular", true, false, false),
        SPARSE("sparse", false, false, true);

        private final String argument;
        private final boolean preserveRegular;
        private final boolean preserveDense;
        private final boolean preserveSparse;

        RevealMode(String argument, boolean preserveRegular, boolean preserveDense, boolean preserveSparse) {
            this.argument = argument;
            this.preserveRegular = preserveRegular;
            this.preserveDense = preserveDense;
            this.preserveSparse = preserveSparse;
        }

        String argument() {
            return argument;
        }

        boolean preserveRegular() {
            return preserveRegular;
        }

        boolean preserveDense() {
            return preserveDense;
        }

        boolean preserveSparse() {
            return preserveSparse;
        }
    }

    record OreFormMembershipSets(
            Set<Block> regularBlocks,
            Set<Block> denseBlocks,
            Set<Block> sparseBlocks
    ) {
    }

    record RevealResult(
            RevealMode mode,
            int radius,
            int loadedChunksProcessed,
            int unloadedChunksSkipped,
            int regularFormMembershipsPreserved,
            int denseFormMembershipsPreserved,
            int sparseFormMembershipsPreserved,
            int oreBlocksRemovedByFiltering,
            int otherNonOreBlocksReplaced
    ) {
    }
}
