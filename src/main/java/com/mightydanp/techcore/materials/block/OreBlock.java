package com.mightydanp.techcore.materials.block;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.Impurities;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import com.mightydanp.techcore.world.item.properties.Purity;
import com.mightydanp.techcore.world.item.properties.Quantity;
import com.mightydanp.techcore.world.level.levelgen.vein.OreVeinResolvedCellResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class OreBlock extends MaterialBlock {
    private static final ThreadLocal<HarvestContext> ACTIVE_HARVEST = new ThreadLocal<>();

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
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        ACTIVE_HARVEST.set(new HarvestContext(pos));

        try {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
            afterSuccessfulHarvest(level, pos, state, player, tool);
        } finally {
            ACTIVE_HARVEST.remove();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, @NotNull LootParams.Builder params) {
        ServerLevel level = params.getLevel();
        OriginContext originContext = originContext(params);
        if (originContext == null) return super.getDrops(state, params);


        if (isExplosionContext(params)) return buildExplosionDrops(state, params, level, originContext.pos());


        HarvestContext harvestContext = ACTIVE_HARVEST.get();
        if (harvestContext == null || !harvestContext.pos().equals(originContext.pos())) return super.getDrops(state, params);


        ItemStack stack = buildResolvedStack(level, originContext.pos(), baseRockMultiplier());
        return stack.isEmpty() ? List.of() : List.of(stack);
    }

    protected double baseRockMultiplier() {
        return 1.0D;
    }

    protected void afterSuccessfulHarvest(Level level, BlockPos pos, BlockState state, Player player, ItemStack tool) {
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


                Material contributing = resolved.replacement() ?
                        resolved.winningOreCellResult().selectedMaterial()
                        : resolved.originalHostMaterial();
                materialMasses.merge(contributing, 1.0D, Double::sum);
            }
        }

        if (baseRockMultiplier != 1.0D) materialMasses.replaceAll((material, mass) -> material == oreMaterial ? mass : mass * baseRockMultiplier);

        double primaryMass = materialMasses.getOrDefault(oreMaterial, 0.0D);
        double totalMass = materialMasses.values().stream().mapToDouble(Double::doubleValue).sum();
        return new Composition(materialMasses, primaryMass, totalMass);
    }

    private boolean isExplosionContext(LootParams.@NotNull Builder params) {
        return params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS) != null;
    }

    private @Nullable OriginContext originContext(LootParams.@NotNull Builder params) {
        net.minecraft.world.phys.Vec3 origin = params.getOptionalParameter(LootContextParams.ORIGIN);
        return origin == null ? null : new OriginContext(BlockPos.containing(origin));
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation materialId(@NotNull Material material) {
        return ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, material.name);
    }

    private record HarvestContext(BlockPos pos) { }

    private record OriginContext(BlockPos pos) { }

    protected record Composition(Map<Material, Double> materialMasses, double primaryMass, double totalMass) { }
}

