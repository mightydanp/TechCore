package com.mightydanp.techcore.api.resources.data.loottable;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableContent {
    private final String modid;
    private final String name;
    public LootTable lootTable;

    public BlockLootSubProvider helper;

    public LootTableContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public LootTableContent(ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    public String modid() {
        return modid;
    }
    public String name() {
        return name;
    }

    public LootTableContent setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    public JsonObject json(){
        //LootTableProvider
        return Util.getOrThrow(LootTable.CODEC.encodeStart(JsonOps.INSTANCE, lootTable), IllegalStateException::new).getAsJsonObject();

    }

    public static LootTable standardDropTable(Block b) {
        return LootTable.lootTable().withPool(createStandardDrops(b)).build();
    }

    public static LootPool.Builder createStandardDrops(ItemLike itemProvider) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(itemProvider));
    }

}
