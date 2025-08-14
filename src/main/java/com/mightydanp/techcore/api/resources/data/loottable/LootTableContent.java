package com.mightydanp.techcore.api.resources.data.loottable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.lang.reflect.Type;

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

    public JsonObject json() {
        //LootTableProvider
        return new LootTable.Serializer().serialize(lootTable, LootTable.class, new com.google.gson.JsonSerializationContext() {
            public JsonElement serialize(Object src) { return com.google.gson.JsonParser.parseString(new com.google.gson.Gson().toJson(src)); }
            public JsonElement serialize(Object src, Type typeOfSrc) { return serialize(src); }
        }).getAsJsonObject();

    }

    public static LootTable standardDropTable(Block b) {
        return LootTable.lootTable().withPool(createStandardDrops(b)).build();
    }

    public static LootPool.Builder createStandardDrops(ItemLike itemProvider) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion()).add(LootItem.lootTableItem(itemProvider));
    }
}
