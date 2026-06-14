package com.mightydanp.techcore.command;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class RevealVeinsCommandTest {
    static {
        SharedConstants.tryDetectVersion();
        try {
            Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
            bootstrapped.setAccessible(true);
            bootstrapped.setBoolean(null, true);
            try {
                BuiltInRegistries.bootStrap();
            } catch (RuntimeException ignored) {
            }
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void commandUsesTechcoreNamespaceAndIsDevelopmentOnly() {
        CommandDispatcher<CommandSourceStack> developmentDispatcher = new CommandDispatcher<>();
        CommandDispatcher<CommandSourceStack> productionDispatcher = new CommandDispatcher<>();

        RevealVeinsCommand.register(developmentDispatcher, true);
        RevealVeinsCommand.register(productionDispatcher, false);

        assertNotNull(developmentDispatcher.getRoot().getChild("techcore"));
        assertEquals(null, productionDispatcher.getRoot().getChild("techcore"));
    }

    @Test
    void commandRequiresPermissionLevelFour() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        CommandSourceStack denied = mock(CommandSourceStack.class);
        CommandSourceStack allowed = mock(CommandSourceStack.class);

        RevealVeinsCommand.register(dispatcher, true);

        when(denied.hasPermission(4)).thenReturn(false);
        when(allowed.hasPermission(4)).thenReturn(true);

        assertFalse(dispatcher.getRoot().getChild("techcore").getChild("revealveins").canUse(denied));
        assertTrue(dispatcher.getRoot().getChild("techcore").getChild("revealveins").canUse(allowed));
    }

    @Test
    void grammarSupportsDefaultRadiusOldSyntaxAndAllModes() throws Exception {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        ServerLevel level = mock(ServerLevel.class);
        ServerChunkCache chunkSource = mock(ServerChunkCache.class);
        CommandSourceStack source = commandSource(new Vec3(0.5D, 64.0D, 0.5D), level);

        when(level.getChunkSource()).thenReturn(chunkSource);
        when(level.getMinBuildHeight()).thenReturn(0);
        when(level.getMaxBuildHeight()).thenReturn(1);

        RevealVeinsCommand.register(dispatcher, true);

        assertEquals(0, dispatcher.execute("techcore revealveins", source));
        assertEquals(0, dispatcher.execute("techcore revealveins 0", source));
        assertEquals(0, dispatcher.execute("techcore revealveins all", source));
        assertEquals(0, dispatcher.execute("techcore revealveins no_sparse", source));
        assertEquals(0, dispatcher.execute("techcore revealveins no_regular", source));
        assertEquals(0, dispatcher.execute("techcore revealveins dense", source));
        assertEquals(0, dispatcher.execute("techcore revealveins regular", source));
        assertEquals(0, dispatcher.execute("techcore revealveins sparse", source));
        assertThrows(CommandSyntaxException.class, () -> dispatcher.execute("techcore revealveins invalid", source));
        assertThrows(CommandSyntaxException.class, () -> dispatcher.execute("techcore revealveins 10", source));
    }

    @Test
    void scanUsesFloorSafeNegativeSourceCoordinatesAndOnlyGetChunkNow() {
        ServerLevel level = mock(ServerLevel.class);
        ServerChunkCache chunkSource = mock(ServerChunkCache.class);
        LevelChunk chunk = mock(LevelChunk.class);
        RevealVeinsCommand.OreFormMembershipSets memberships = new RevealVeinsCommand.OreFormMembershipSets(Set.of(Blocks.IRON_ORE), Set.of(), Set.of());

        when(level.getChunkSource()).thenReturn(chunkSource);
        when(level.getMinBuildHeight()).thenReturn(0);
        when(level.getMaxBuildHeight()).thenReturn(1);
        when(chunkSource.getChunkNow(-1, -2)).thenReturn(chunk);
        when(chunk.getPos()).thenReturn(new ChunkPos(-1, -2));
        when(level.getBlockState(any(BlockPos.class))).thenReturn(Blocks.AIR.defaultBlockState());

        RevealVeinsCommand.RevealResult result = RevealVeinsCommand.reveal(
                level,
                new ChunkPos(BlockPos.containing(new Vec3(-0.2D, 64.0D, -16.2D))),
                0,
                RevealVeinsCommand.RevealMode.ALL,
                memberships
        );

        assertEquals(1, result.loadedChunksProcessed());
        verify(chunkSource, times(1)).getChunkNow(-1, -2);
        verify(chunkSource, never()).getChunk(anyInt(), anyInt(), any(), anyBoolean());
    }

    @Test
    void unloadedChunksAreSkippedAndScanStaysInsideSelectedChunksWithInclusiveMinExclusiveMaxY() {
        ServerLevel level = mock(ServerLevel.class);
        ServerChunkCache chunkSource = mock(ServerChunkCache.class);
        LevelChunk center = mock(LevelChunk.class);
        RevealVeinsCommand.OreFormMembershipSets memberships = new RevealVeinsCommand.OreFormMembershipSets(Set.of(), Set.of(), Set.of());
        Set<Long> readPositions = new HashSet<>();

        when(level.getChunkSource()).thenReturn(chunkSource);
        when(level.getMinBuildHeight()).thenReturn(5);
        when(level.getMaxBuildHeight()).thenReturn(7);
        when(chunkSource.getChunkNow(0, 0)).thenReturn(center);
        when(center.getPos()).thenReturn(new ChunkPos(0, 0));
        when(level.getBlockState(any(BlockPos.class))).thenAnswer(invocation -> {
            BlockPos pos = invocation.getArgument(0);
            readPositions.add(pos.asLong());
            return Blocks.STONE.defaultBlockState();
        });
        when(level.setBlock(any(BlockPos.class), eq(Blocks.AIR.defaultBlockState()), eq(Block.UPDATE_CLIENTS))).thenReturn(true);

        RevealVeinsCommand.RevealResult result = RevealVeinsCommand.reveal(level, new ChunkPos(0, 0), 1, RevealVeinsCommand.RevealMode.ALL, memberships);

        assertEquals(1, result.loadedChunksProcessed());
        assertEquals(8, result.unloadedChunksSkipped());
        assertEquals(0, result.regularFormMembershipsPreserved());
        assertEquals(0, result.denseFormMembershipsPreserved());
        assertEquals(0, result.sparseFormMembershipsPreserved());
        assertEquals(0, result.oreBlocksRemovedByFiltering());
        assertEquals(16 * 16 * 2, result.otherNonOreBlocksReplaced());
        for (long packed : readPositions) {
            BlockPos pos = BlockPos.of(packed);
            assertTrue(pos.getX() >= 0 && pos.getX() <= 15);
            assertTrue(pos.getZ() >= 0 && pos.getZ() <= 15);
            assertTrue(pos.getY() >= 5 && pos.getY() < 7);
        }
    }

    @Test
    void modeFilteringPreservesEnabledMembershipsAndCountsWritesExactlyOnce() {
        ServerLevel level = mock(ServerLevel.class);
        ServerChunkCache chunkSource = mock(ServerChunkCache.class);
        LevelChunk chunk = mock(LevelChunk.class);
        Material material = new Material("test_material", null);
        Block regular = Blocks.IRON_ORE;
        Block dense = mock(Block.class);
        BlockState denseState = mock(BlockState.class);
        Block sparse = Blocks.COPPER_ORE;
        Block overlapping = Blocks.GOLD_ORE;
        Map<Long, BlockState> states = new HashMap<>();
        Set<Long> writes = new HashSet<>();

        when(denseState.getBlock()).thenReturn(dense);
        when(denseState.isAir()).thenReturn(false);
        when(denseState.getValue(DenseOre.DENSITY)).thenReturn(5);

        material.ore.oreBlocks.put("host", () -> regular);
        material.ore.oreBlocks.put("shared", () -> overlapping);
        material.ore.denseOreBlocks.put("host", () -> dense);
        material.ore.denseOreBlocks.put("shared", () -> overlapping);
        material.ore.sparseOreBlocks.put("host", () -> sparse);
        RevealVeinsCommand.OreFormMembershipSets memberships = RevealVeinsCommand.oreFormMembershipSets(Set.of(material));

        when(level.getChunkSource()).thenReturn(chunkSource);
        when(level.getMinBuildHeight()).thenReturn(0);
        when(level.getMaxBuildHeight()).thenReturn(1);
        when(chunkSource.getChunkNow(0, 0)).thenReturn(chunk);
        when(chunk.getPos()).thenReturn(new ChunkPos(0, 0));

        states.put(new BlockPos(0, 0, 0).asLong(), regular.defaultBlockState());
        states.put(new BlockPos(1, 0, 0).asLong(), denseState);
        states.put(new BlockPos(2, 0, 0).asLong(), sparse.defaultBlockState());
        states.put(new BlockPos(3, 0, 0).asLong(), overlapping.defaultBlockState());
        states.put(new BlockPos(4, 0, 0).asLong(), Blocks.AIR.defaultBlockState());
        states.put(new BlockPos(5, 0, 0).asLong(), Blocks.STONE.defaultBlockState());

        when(level.getBlockState(any(BlockPos.class))).thenAnswer(invocation -> states.getOrDefault(((BlockPos) invocation.getArgument(0)).asLong(), Blocks.STONE.defaultBlockState()));
        doAnswer(invocation -> {
            BlockPos pos = invocation.getArgument(0);
            writes.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()).asLong());
            return true;
        }).when(level).setBlock(any(BlockPos.class), eq(Blocks.AIR.defaultBlockState()), eq(Block.UPDATE_CLIENTS));

        RevealVeinsCommand.RevealResult denseOnly = RevealVeinsCommand.reveal(level, new ChunkPos(0, 0), 0, RevealVeinsCommand.RevealMode.DENSE, memberships);

        assertEquals(RevealVeinsCommand.RevealMode.DENSE, denseOnly.mode());
        assertEquals(0, denseOnly.radius());
        assertEquals(1, denseOnly.regularFormMembershipsPreserved());
        assertEquals(2, denseOnly.denseFormMembershipsPreserved());
        assertEquals(0, denseOnly.sparseFormMembershipsPreserved());
        assertEquals(2, denseOnly.oreBlocksRemovedByFiltering());
        assertEquals(251, denseOnly.otherNonOreBlocksReplaced());
        assertEquals(
                denseOnly.oreBlocksRemovedByFiltering() + denseOnly.otherNonOreBlocksReplaced(),
                writes.size()
        );
        assertTrue(writes.contains(new BlockPos(0, 0, 0).asLong()));
        assertFalse(writes.contains(new BlockPos(1, 0, 0).asLong()));
        assertTrue(writes.contains(new BlockPos(2, 0, 0).asLong()));
        assertFalse(writes.contains(new BlockPos(3, 0, 0).asLong()));
        assertFalse(writes.contains(new BlockPos(4, 0, 0).asLong()));
        assertEquals(5, denseState.getValue(DenseOre.DENSITY));
    }

    @Test
    void modeBehaviorMatchesRequestedPreservationForms() {
        Block shared = Blocks.GOLD_ORE;
        RevealVeinsCommand.OreFormMembershipSets memberships = new RevealVeinsCommand.OreFormMembershipSets(
                Set.of(Blocks.IRON_ORE, shared),
                Set.of(Blocks.DIAMOND_ORE, shared),
                Set.of(Blocks.COPPER_ORE)
        );

        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.ALL, true, true, true, true);
        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.NO_SPARSE, true, true, false, true);
        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.NO_REGULAR, false, true, true, true);
        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.DENSE, false, true, false, true);
        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.REGULAR, true, false, false, true);
        assertModePreservation(memberships, RevealVeinsCommand.RevealMode.SPARSE, false, false, true, false);
    }

    @Test
    void allModePreservesEveryOreFormAndOverlapMembershipCountersMayOverlap() {
        ServerLevel level = mock(ServerLevel.class);
        ServerChunkCache chunkSource = mock(ServerChunkCache.class);
        LevelChunk chunk = mock(LevelChunk.class);
        Block overlapping = Blocks.GOLD_ORE;
        RevealVeinsCommand.OreFormMembershipSets memberships = new RevealVeinsCommand.OreFormMembershipSets(
                Set.of(Blocks.IRON_ORE, overlapping),
                Set.of(overlapping),
                Set.of(Blocks.COPPER_ORE)
        );

        when(level.getChunkSource()).thenReturn(chunkSource);
        when(level.getMinBuildHeight()).thenReturn(0);
        when(level.getMaxBuildHeight()).thenReturn(1);
        when(chunkSource.getChunkNow(0, 0)).thenReturn(chunk);
        when(chunk.getPos()).thenReturn(new ChunkPos(0, 0));
        when(level.getBlockState(any(BlockPos.class))).thenAnswer(invocation -> {
            BlockPos pos = invocation.getArgument(0);
            if (pos.getZ() != 0) {
                return Blocks.AIR.defaultBlockState();
            }
            if (pos.getX() == 0) {
                return Blocks.IRON_ORE.defaultBlockState();
            }
            if (pos.getX() == 1) {
                return overlapping.defaultBlockState();
            }
            if (pos.getX() == 2) {
                return Blocks.COPPER_ORE.defaultBlockState();
            }
            return Blocks.AIR.defaultBlockState();
        });

        RevealVeinsCommand.RevealResult result = RevealVeinsCommand.reveal(level, new ChunkPos(0, 0), 0, RevealVeinsCommand.RevealMode.ALL, memberships);

        assertEquals(2, result.regularFormMembershipsPreserved());
        assertEquals(1, result.denseFormMembershipsPreserved());
        assertEquals(1, result.sparseFormMembershipsPreserved());
        assertEquals(0, result.oreBlocksRemovedByFiltering());
        assertEquals(0, result.otherNonOreBlocksReplaced());
        verify(level, never()).setBlock(any(BlockPos.class), eq(Blocks.AIR.defaultBlockState()), eq(Block.UPDATE_CLIENTS));
    }

    @Test
    void invalidOreSuppliersFailDescriptively() {
        Material material = new Material("broken", null);
        material.ore.oreBlocks.put("host", null);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> RevealVeinsCommand.oreFormMembershipSets(Set.of(material)));

        assertTrue(exception.getMessage().contains("material=broken"));
        assertTrue(exception.getMessage().contains("oreFormMap=oreBlocks"));
        assertTrue(exception.getMessage().contains("hostKey=host"));
    }

    private static CommandSourceStack commandSource(Vec3 position, ServerLevel level) {
        CommandSourceStack source = mock(CommandSourceStack.class);
        when(source.hasPermission(4)).thenReturn(true);
        when(source.getPosition()).thenReturn(position);
        when(source.getLevel()).thenReturn(level);
        doAnswer(invocation -> null).when(source).sendSuccess(any(), eq(false));
        return source;
    }

    private static void assertModePreservation(
            RevealVeinsCommand.OreFormMembershipSets memberships,
            RevealVeinsCommand.RevealMode mode,
            boolean regularPreserved,
            boolean densePreserved,
            boolean sparsePreserved,
            boolean sharedPreserved
    ) {
        assertEquals(regularPreserved, memberships.regularBlocks().stream().anyMatch(block -> block == Blocks.IRON_ORE && mode.preserveRegular()));
        assertEquals(densePreserved, memberships.denseBlocks().stream().anyMatch(block -> block == Blocks.DIAMOND_ORE && mode.preserveDense()));
        assertEquals(sparsePreserved, memberships.sparseBlocks().stream().anyMatch(block -> block == Blocks.COPPER_ORE && mode.preserveSparse()));
        assertEquals(sharedPreserved, (memberships.regularBlocks().contains(Blocks.GOLD_ORE) && mode.preserveRegular())
                || (memberships.denseBlocks().contains(Blocks.GOLD_ORE) && mode.preserveDense())
                || (memberships.sparseBlocks().contains(Blocks.GOLD_ORE) && mode.preserveSparse()));
    }
}
