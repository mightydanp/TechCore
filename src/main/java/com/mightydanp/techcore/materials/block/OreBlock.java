package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.Impurities;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quantity;
import com.mightydanp.techcore.world.level.WasGenerated;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinOreCellEvaluator;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinResolvedCellResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class OreBlock extends MaterialBlock {
    private static final ThreadLocal<PendingHarvestOperation> ACTIVE_HARVEST = new ThreadLocal<>();
    private static final ThreadLocal<Deque<SuppressedGeneratedChange>> SUPPRESSED_GENERATED_CHANGES = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ConcurrentHashMap<UUID, PendingHarvestOperation> PENDING_HARVESTS = new ConcurrentHashMap<>();

    protected final Material oreMaterial;
    protected final Material hostMaterial;
    protected final Supplier<Item> rawOreItem;

    public OreBlock(MaterialBlockProperties properties, Material oreMaterial, Material hostMaterial, Supplier<Item> rawOreItem) {
        super(properties);
        this.oreMaterial = Objects.requireNonNull(oreMaterial, "oreMaterial");
        this.hostMaterial = Objects.requireNonNull(hostMaterial, "hostMaterial");
        this.rawOreItem = Objects.requireNonNull(rawOreItem, "rawOreItem");
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        PendingHarvestOperation operation = beginHarvestOperation(state, level, pos, player, willHarvest);
        if (operation != null) {
            ACTIVE_HARVEST.set(operation);
            TransitionKind transitionKind = operation.removalTransitionKind();
            if (transitionKind != null) suppressGeneratedChange(operation, state, fluid.createLegacyBlock(), transitionKind);
        }

        boolean removed = false;

        try {
            removed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
            return removed;
        } finally {
            if ((!removed || operation == null) && ACTIVE_HARVEST.get() == operation) {
                ACTIVE_HARVEST.remove();
            }
        }
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        PendingHarvestOperation operation = ACTIVE_HARVEST.get();
        if (!matchesOperation(operation, level, player, pos, state, tool)) {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
            return;
        }

        try {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
            operation.expectedRemovalObserved = removalObserved(level, pos, state);
            if (!operation.expectedRemovalObserved) {
                failHarvest(operation);
                return;
            }

            afterSuccessfulHarvest(operation.level, operation.pos, operation.originalState, operation.player, operation.toolSnapshot.copy(), operation);
        } catch (RuntimeException | Error exception) {
            operation.expectedRemovalObserved = operation.expectedRemovalObserved || removalObserved(level, pos, state);
            failHarvest(operation);
            throw exception;
        } finally {
            if (ACTIVE_HARVEST.get() == operation) {
                ACTIVE_HARVEST.remove();
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, @NotNull LootParams.Builder params) {
        ServerLevel level = params.getLevel();
        OriginContext originContext = originContext(params);
        if (originContext == null) return super.getDrops(state, params);

        if (isExplosionContext(params)) return buildExplosionDrops(state, params, level, originContext.pos());

        PendingHarvestOperation operation = ACTIVE_HARVEST.get();
        if (!matchesLootContext(operation, state, params, originContext)) return super.getDrops(state, params);

        ItemStack stack = buildResolvedStack(level, originContext.pos(), baseRockMultiplier());
        return stack.isEmpty() ? List.of() : List.of(stack);
    }

    protected double baseRockMultiplier() {
        return 1.0D;
    }

    protected void afterSuccessfulHarvest(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ServerPlayer player, @NotNull ItemStack tool, @NotNull PendingHarvestOperation operation) {
        finishPermanentHarvest(operation);
    }

    protected int explosionCandidateCount(BlockState state, LootParams.Builder params) {
        return 1;
    }

    protected List<ItemStack> buildExplosionDrops(BlockState state, LootParams.Builder params, ServerLevel level, BlockPos pos) {
        int candidateCount = explosionCandidateCount(state, params);
        if (candidateCount <= 0) return List.of();

        Float explosionRadius = params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
        if (explosionRadius == null || explosionRadius <= 0.0F) explosionRadius = 1.0F;

        ItemStack prototype = buildResolvedStack(level, pos, baseRockMultiplier());
        if (prototype.isEmpty()) return List.of();

        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < candidateCount; i++) {
            if (level.random.nextFloat() <= (1.0F / explosionRadius)) drops.add(prototype.copy());
        }

        return List.copyOf(drops);
    }

    protected @NotNull ItemStack buildResolvedStack(ServerLevel level, BlockPos center, double baseRockMultiplier) {
        Composition composition = scanComposition(level, center, baseRockMultiplier);
        if (composition.totalMass() <= 0.0D || composition.primaryMass() <= 0.0D) return ItemStack.EMPTY;

        Item item = rawOreItem.get();
        if (item == null) return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(item);
        Quantity.stack(stack).set(Quantity.DEFAULT_MAX, Quantity.DEFAULT_MAX);

        double purityValue = 100.0D * composition.primaryMass() / composition.totalMass();
        Purity.stack(stack).set(purityValue);

        Map<ResourceLocation, Double> impurities = new TreeMap<>();
        composition.materialMasses().forEach((material, mass) -> {
            if (material == oreMaterial || mass <= 0.0D) return;
            impurities.put(materialId(material), 100.0D * mass / composition.totalMass());
        });

        if (impurities.isEmpty()) Impurities.stack(stack).remove();
        else Impurities.stack(stack).set(impurities);

        return stack;
    }

    protected @NotNull Composition scanComposition(ServerLevel level, BlockPos center, double baseRockMultiplier) {
        Map<Material, Double> materialMasses = new TreeMap<>(Comparator.comparing(left -> left.name));

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                BlockPos scanPos = center.offset(offsetX, 0, offsetZ);
                OreVeinResolvedCellResolver.ResolvedCell resolved = OreVeinResolvedCellResolver.resolve(level.getSeed(), level.dimension(), scanPos).orElse(null);
                if (resolved == null) continue;

                Material contributing = resolved.replacement()
                        ? resolved.winningOreCellResult().selectedMaterial()
                        : resolved.originalHostMaterial();
                materialMasses.merge(contributing, 1.0D, Double::sum);
            }
        }

        if (baseRockMultiplier != 1.0D) materialMasses.replaceAll((material, mass) -> material == oreMaterial ? mass : mass * baseRockMultiplier);

        double primaryMass = materialMasses.getOrDefault(oreMaterial, 0.0D);
        double totalMass = materialMasses.values().stream().mapToDouble(Double::doubleValue).sum();
        return new Composition(materialMasses, primaryMass, totalMass);
    }

    protected static void suppressRestoration(@NotNull PendingHarvestOperation operation, @NotNull BlockState restoredState) {
        operation.restorationAttempted = true;
        suppressGeneratedChange(
                operation,
                operation.level.getBlockState(operation.pos),
                restoredState,
                TransitionKind.AIR_TO_DENSE_DECREMENT
        );
    }

    protected static void finishPermanentHarvest(@NotNull PendingHarvestOperation operation) {
        if (!operation.expectedRemovalObserved) {
            failHarvest(operation);
            return;
        }

        markChangedOnce(operation);
        completeOperation(operation);
    }

    protected static void finishDenseHarvest(@NotNull PendingHarvestOperation operation, @NotNull BlockState restoredState, boolean restored) {
        operation.restorationSucceeded = restored && restoredState.equals(operation.level.getBlockState(operation.pos));
        if (!operation.restorationSucceeded) {
            failHarvest(operation);
            return;
        }

        completeOperation(operation);
    }

    protected static void failHarvest(@NotNull PendingHarvestOperation operation) {
        if (operation.expectedRemovalObserved || operation.restorationAttempted) markChangedOnce(operation);
        completeOperation(operation);
    }

    public static void expireStaleHarvests(@NotNull MinecraftServer server) {
        Iterator<PendingHarvestOperation> iterator = PENDING_HARVESTS.values().iterator();
        while (iterator.hasNext()) {
            PendingHarvestOperation operation = iterator.next();
            if (operation.level.getServer() != server) continue;
            if (operation.level.getGameTime() <= operation.createdGameTime) continue;
            if (operation.expectedRemovalObserved || operation.restorationAttempted) markChangedOnce(operation);
            if (ACTIVE_HARVEST.get() == operation) ACTIVE_HARVEST.remove();

            iterator.remove();
        }
    }

    public static void repairLevelUnload(@NotNull ServerLevel level) {
        Iterator<PendingHarvestOperation> iterator = PENDING_HARVESTS.values().iterator();
        while (iterator.hasNext()) {
            PendingHarvestOperation operation = iterator.next();
            if (operation.level != level) continue;
            if (operation.expectedRemovalObserved || operation.restorationAttempted) markChangedOnce(operation);
            if (ACTIVE_HARVEST.get() == operation) ACTIVE_HARVEST.remove();
            iterator.remove();
        }
    }

    public static void repairServerStop(@NotNull MinecraftServer server) {
        Iterator<PendingHarvestOperation> iterator = PENDING_HARVESTS.values().iterator();
        while (iterator.hasNext()) {
            PendingHarvestOperation operation = iterator.next();
            if (operation.level.getServer() != server) continue;
            if (operation.expectedRemovalObserved || operation.restorationAttempted) markChangedOnce(operation);
            if (ACTIVE_HARVEST.get() == operation) ACTIVE_HARVEST.remove();
            iterator.remove();
        }
    }

    public static void clearTransientHarvestState() {
        ACTIVE_HARVEST.remove();
        SUPPRESSED_GENERATED_CHANGES.remove();
        PENDING_HARVESTS.clear();
    }

    public static boolean consumeSuppressedGeneratedChange(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState oldState, @NotNull BlockState newState) {
        Deque<SuppressedGeneratedChange> suppressedChanges = SUPPRESSED_GENERATED_CHANGES.get();
        SuppressedGeneratedChange suppressed = suppressedChanges.peek();
        if (suppressed == null || !suppressed.matches(level, pos, oldState, newState)) return false;
        suppressedChanges.pop();
        if (suppressedChanges.isEmpty()) SUPPRESSED_GENERATED_CHANGES.remove();
        return true;
    }

    private static void suppressGeneratedChange(@NotNull PendingHarvestOperation operation, @NotNull BlockState oldState, @NotNull BlockState newState, @NotNull TransitionKind kind) {
        SUPPRESSED_GENERATED_CHANGES.get().push(new SuppressedGeneratedChange(
                operation.token,
                operation.level,
                operation.pos,
                oldState,
                newState,
                kind
        ));
    }

    private @Nullable PendingHarvestOperation beginHarvestOperation(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest) {
        if (!(level instanceof ServerLevel serverLevel)) return null;
        if (!(player instanceof ServerPlayer serverPlayer)) return null;
        if (!willHarvest || serverPlayer.isCreative()) return null;
        if (ACTIVE_HARVEST.get() != null) return null;

        ItemStack tool = serverPlayer.getMainHandItem();
        if (!isSupportedMiningTool(tool)) return null;
        if (!WasGenerated.wasGenerated(serverLevel, pos)) return null;

        OreVeinResolvedCellResolver.ResolvedCell resolved = OreVeinResolvedCellResolver.resolve(serverLevel.getSeed(), serverLevel.dimension(), pos).orElse(null);
        if (!matchesResolvedCenter(state, resolved)) return null;

        PendingHarvestOperation operation = new PendingHarvestOperation(
                UUID.randomUUID(),
                serverLevel,
                pos.immutable(),
                serverPlayer,
                state,
                tool.copy(),
                resolved.winningOreCellResult(),
                state.getBlock() instanceof DenseOre ? state.getValue(DenseOre.DENSITY) : 0,
                serverLevel.getGameTime()
        );

        PENDING_HARVESTS.put(operation.token, operation);
        return operation;
    }

    private boolean matchesResolvedCenter(@NotNull BlockState state, @Nullable OreVeinResolvedCellResolver.ResolvedCell resolved) {
        if (resolved == null || !resolved.replacement() || resolved.overlapGapWon()) return false;
        if (!hostMaterial.equals(resolved.originalHostMaterial())) return false;
        if (!state.equals(resolved.resolvedBlockState())) return false;

        OreVeinOreCellEvaluator.OreCellResult winner = resolved.winningOreCellResult();
        if (winner == null) return false;
        if (!oreMaterial.equals(winner.selectedMaterial())) return false;
        if (winner.variant() != expectedVariant()) return false;
        return winner.variant() != OreVeinOreCellEvaluator.OreCellResult.OreVariant.DENSE_ORE
                || state.hasProperty(DenseOre.DENSITY) && state.getValue(DenseOre.DENSITY) == winner.finalDensity();
    }

    private boolean matchesLootContext(@Nullable PendingHarvestOperation operation, @NotNull BlockState state, LootParams.@NotNull Builder params, @NotNull OriginContext originContext) {
        if (operation == null || operation.level != params.getLevel()) return false;
        if (!operation.pos.equals(originContext.pos())) return false;
        if (!operation.originalState.equals(state)) return false;

        ItemStack lootTool = params.getOptionalParameter(LootContextParams.TOOL);
        if (lootTool == null || !ItemStack.isSameItemSameTags(operation.toolSnapshot, lootTool)) return false;

        Entity entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        return entity instanceof ServerPlayer serverPlayer && serverPlayer.getUUID().equals(operation.player.getUUID());
    }

    private boolean matchesOperation(@Nullable PendingHarvestOperation operation, @NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ItemStack tool) {
        return operation != null
                && operation.level == level
                && operation.player == player
                && operation.pos.equals(pos)
                && operation.originalState.equals(state)
                && ItemStack.isSameItemSameTags(operation.toolSnapshot, tool);
    }

    private boolean removalObserved(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState originalState) {
        return !level.getBlockState(pos).equals(originalState);
    }

    private @NotNull OreVeinOreCellEvaluator.OreCellResult.OreVariant expectedVariant() {
        if (this instanceof DenseOre) return OreVeinOreCellEvaluator.OreCellResult.OreVariant.DENSE_ORE;
        if (this instanceof SparseOre) return OreVeinOreCellEvaluator.OreCellResult.OreVariant.SPARSE_ORE;
        return OreVeinOreCellEvaluator.OreCellResult.OreVariant.REGULAR_ORE;
    }

    private boolean isExplosionContext(LootParams.@NotNull Builder params) {
        return params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS) != null;
    }

    private static boolean isSupportedMiningTool(@NotNull ItemStack tool) {
        return tool.is(ItemTags.PICKAXES);
    }

    private @Nullable OriginContext originContext(LootParams.@NotNull Builder params) {
        net.minecraft.world.phys.Vec3 origin = params.getOptionalParameter(LootContextParams.ORIGIN);
        return origin == null ? null : new OriginContext(BlockPos.containing(origin));
    }

    private static void markChangedOnce(@NotNull PendingHarvestOperation operation) {
        if (operation.finalChangedMarkApplied) return;
        WasGenerated.markChanged(operation.level, operation.pos);
        operation.finalChangedMarkApplied = true;
    }

    private static void completeOperation(@NotNull PendingHarvestOperation operation) {
        PENDING_HARVESTS.remove(operation.token);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation materialId(@NotNull Material material) {
        return ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, material.name);
    }

    protected static final class PendingHarvestOperation {
        private final UUID token;
        private final ServerLevel level;
        private final BlockPos pos;
        private final ServerPlayer player;
        private final BlockState originalState;
        private final ItemStack toolSnapshot;
        private final OreVeinOreCellEvaluator.OreCellResult winner;
        private final int originalDenseDensity;
        private final long createdGameTime;
        private boolean expectedRemovalObserved;
        private boolean restorationAttempted;
        private boolean restorationSucceeded;
        private boolean finalChangedMarkApplied;

        private PendingHarvestOperation(UUID token, ServerLevel level, BlockPos pos, ServerPlayer player, BlockState originalState, ItemStack toolSnapshot, OreVeinOreCellEvaluator.OreCellResult winner, int originalDenseDensity, long createdGameTime) {
            this.token = Objects.requireNonNull(token, "token");
            this.level = Objects.requireNonNull(level, "level");
            this.pos = Objects.requireNonNull(pos, "pos");
            this.player = Objects.requireNonNull(player, "player");
            this.originalState = Objects.requireNonNull(originalState, "originalState");
            this.toolSnapshot = Objects.requireNonNull(toolSnapshot, "toolSnapshot");
            this.winner = Objects.requireNonNull(winner, "winner");
            this.originalDenseDensity = originalDenseDensity;
            this.createdGameTime = createdGameTime;
        }

        private @Nullable TransitionKind removalTransitionKind() {
            if (winner.variant() == OreVeinOreCellEvaluator.OreCellResult.OreVariant.DENSE_ORE && originalDenseDensity <= 1) return null;
            return winner.variant() == OreVeinOreCellEvaluator.OreCellResult.OreVariant.DENSE_ORE
                    ? TransitionKind.DENSE_ORE_TO_AIR
                    : TransitionKind.PERMANENT_ORE_TO_AIR;
        }
    }

    private record OriginContext(BlockPos pos) { }

    private record SuppressedGeneratedChange(UUID token, ServerLevel level, BlockPos pos, BlockState oldState, BlockState newState, TransitionKind kind) {
        private boolean matches(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState oldState, @NotNull BlockState newState) {
            return this.level == level
                    && this.pos.equals(pos)
                    && this.oldState.equals(oldState)
                    && this.newState.equals(newState);
        }
    }

    private enum TransitionKind {
        PERMANENT_ORE_TO_AIR,
        DENSE_ORE_TO_AIR,
        AIR_TO_DENSE_DECREMENT
    }

    protected record Composition(Map<Material, Double> materialMasses, double primaryMass, double totalMass) { }
}

