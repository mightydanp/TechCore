package com.mightydanp.techcore.world.level.levelgen.feature;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinChunkPlan;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinResolvedCell;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class OreVeinFeatureTest {
    static {
        SharedConstants.tryDetectVersion();
        try {
            java.lang.reflect.Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
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
    void applyPlanUsesExactHostMatchAndUpdateClientsFlag() {
        BlockPos replace = new BlockPos(1, 2, 3);
        BlockPos skip = new BlockPos(4, 5, 6);
        BlockState host = Blocks.STONE.defaultBlockState();
        BlockState ore = Blocks.IRON_ORE.defaultBlockState();
        Material material = new Material("granite", null);
        Map<BlockPos, BlockState> states = new HashMap<>();
        Map<BlockPos, Integer> flags = new HashMap<>();
        states.put(replace, host);
        states.put(skip, Blocks.DIRT.defaultBlockState());
        WorldGenLevel level = proxyLevel(states, flags);
        OreVeinResolvedCell replaceCell = new OreVeinResolvedCell(replace, material, host, null, ore, true, false, false, List.of());
        OreVeinResolvedCell skipCell = new OreVeinResolvedCell(skip, material, host, null, ore, true, false, false, List.of());
        OreVeinChunkPlan plan = new OreVeinChunkPlan(new ChunkPos(0, 0), ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, ResourceLocation.withDefaultNamespace("overworld")), List.of(
                new OreVeinChunkPlan.PlannedReplacement(replace, replaceCell),
                new OreVeinChunkPlan.PlannedReplacement(skip, skipCell)
        ));

        boolean changed = OreVeinFeature.applyPlan(level, plan);

        assertTrue(changed);
        assertEquals(ore, states.get(replace));
        assertEquals(Blocks.DIRT.defaultBlockState(), states.get(skip));
        assertEquals(Block.UPDATE_CLIENTS, flags.get(replace));
        assertFalse(flags.containsKey(skip));
    }

    private static WorldGenLevel proxyLevel(Map<BlockPos, BlockState> states, Map<BlockPos, Integer> flags) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> switch (method.getName()) {
            case "getBlockState" -> states.getOrDefault(args[0], Blocks.AIR.defaultBlockState());
            case "setBlock" -> {
                states.put(((BlockPos) args[0]).immutable(), (BlockState) args[1]);
                flags.put(((BlockPos) args[0]).immutable(), (Integer) args[2]);
                yield true;
            }
            case "hashCode" -> System.identityHashCode(proxy);
            case "equals" -> proxy == args[0];
            case "toString" -> "ProxyWorldGenLevel";
            default -> defaultValue(method.getReturnType());
        };

        return (WorldGenLevel) Proxy.newProxyInstance(
                OreVeinFeatureTest.class.getClassLoader(),
                new Class<?>[]{WorldGenLevel.class},
                handler
        );
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0.0F;
        }
        if (type == double.class) {
            return 0.0D;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }
}
