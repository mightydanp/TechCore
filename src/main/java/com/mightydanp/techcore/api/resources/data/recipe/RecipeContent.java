package com.mightydanp.techcore.api.resources.data.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeContent {
    private final String modid;
    private final String name;
    private ShapelessRecipeBuilder shapeless = null;
    private ShapedRecipeBuilder shaped = null;
    private JsonObject recipeJson;
    private JsonObject advancementJson;
    public RecipeContent(String modid, String name) {
        this.modid = modid;
        this.name = name;
    }

    public RecipeContent(ResourceLocation resourceLocation) {
        this.modid = resourceLocation.getNamespace();
        this.name = resourceLocation.getPath();
    }

    public String modid() {
        return modid;
    }
    public String name() {
        return name;
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item){
        shapeless = ShapelessRecipeBuilder.shapeless(category, item);
        return shapeless;
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item, int amount){
        shapeless = ShapelessRecipeBuilder.shapeless(category, item, amount);
        return shapeless;
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStack itemStack){
        shapeless = ShapelessRecipeBuilder.shapeless(category, itemStack);
        return shapeless;
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item){
        shaped = ShapedRecipeBuilder.shaped(category, item);
        return shaped;
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item, int amount){
        shaped = ShapedRecipeBuilder.shaped(category, item, amount);
        return shaped;
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemStack itemStack){
        shaped = ShapedRecipeBuilder.shaped(category, itemStack);
        return shaped;
    }

    @SuppressWarnings("removal")
    public RecipeContent save(@Nullable Object optional){
        RecipeOutput recipeOutput = new RecipeOutput() {
            @Override
            public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... conditions) {
                recipeJson = Util.getOrThrow(Recipe.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(recipe, conditions))), IllegalStateException::new).getAsJsonObject();

                if (advancementHolder != null) {
                    advancementJson = Util.getOrThrow(Advancement.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(advancementHolder.value(), conditions))), IllegalStateException::new).getAsJsonObject();
                }
            }

            @Override
            public Advancement.@NotNull Builder advancement() {
                return new Advancement.Builder().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
            }
        };

        if(shapeless != null && shaped == null) {
            if(optional instanceof String string){
                shapeless.save(recipeOutput, string);
            }else if(optional instanceof ResourceLocation resourceLocation) {
                shapeless.save(recipeOutput, resourceLocation);
            }else{
                shapeless.save(recipeOutput, new ResourceLocation(modid(), name()));
            }
        }

        if(shapeless == null && shaped != null) {
            if(optional instanceof String string){
                shaped.save(recipeOutput, string);
            }else if(optional instanceof ResourceLocation resourceLocation) {
                shaped.save(recipeOutput, resourceLocation);
            }else{
                shaped.save(recipeOutput, new ResourceLocation(modid(), name()));
            }
        }

        return this;
    }


    public JsonObject recipeJson(){
        //RecipeProvider.this.recipePathProvider.json(resourceLocation)));
        //need to create an advancement data
        //RecipeProvider.this.advancementPathProvider.json(advancementHolder.id())));
        return recipeJson;
    }

    public JsonObject advancementJson(){
        //RecipeProvider.this.recipePathProvider.json(resourceLocation)));
        //need to create an advancement data
        //RecipeProvider.this.advancementPathProvider.json(advancementHolder.id())));
        return advancementJson;
    }

    public String getHasName(ItemLike p_176603_) {
        return "has_" + getItemName(p_176603_);
    }

    public String getItemName(ItemLike p_176633_) {
        return BuiltInRegistries.ITEM.getKey(p_176633_.asItem()).getPath();
    }

    @SuppressWarnings("ALL")
    protected static Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block) {
        return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger.TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty()));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(MinMaxBounds.Ints minMaxBounds, ItemLike itemLike) {
        return inventoryTrigger(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(itemLike).withCount(minMaxBounds));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike) {
        return inventoryTrigger(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(itemLike));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tagKey) {
        return inventoryTrigger(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(tagKey));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate.Builder... builders) {
        return inventoryTrigger(Arrays.stream(builders).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... itemPredicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(itemPredicates)));
    }

    public void twoByTwoPacker(RecipeCategory category, ItemLike output, ItemLike input) {
        shaped(category, output, 1).define('#', input).pattern("##").pattern("##").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void threeByThreePacker(RecipeCategory category, ItemLike output, ItemLike input, String unlockBy) {
        shapeless = shapeless(category, output).requires(input, 9).unlockedBy(unlockBy, has(input));
        save(null);
    }

    public void threeByThreePacker(RecipeCategory p_259186_, ItemLike output, ItemLike input) {
        threeByThreePacker(p_259186_, output, input, getHasName(input));
    }

    public void planksFromLog(ItemLike output, TagKey<Item> input, int amount) {
        shapeless = shapeless(RecipeCategory.BUILDING_BLOCKS, output, amount).requires(input).group("planks").unlockedBy("has_log", has(input));
        save(null);
    }

    public void planksFromLogs(ItemLike output, TagKey<Item> input, int amount) {
        shapeless = shapeless(RecipeCategory.BUILDING_BLOCKS, output, amount).requires(input).group("planks").unlockedBy("has_logs", has(input));
        save(null);
    }

    public void woodFromLogs(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.BUILDING_BLOCKS, output, 3).define('#', input).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(input));
        save(null);
    }

    public void woodenBoat(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.TRANSPORTATION, output).define('#', input).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER));
        save(null);
    }

    public void chestBoat(ItemLike output, ItemLike input) {
        shapeless = shapeless(RecipeCategory.TRANSPORTATION, output).requires(Blocks.CHEST).requires(input).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS));
        save(null);
    }

    public RecipeBuilder buttonBuilder(ItemLike output, Ingredient input) {
        shapeless = shapeless(RecipeCategory.REDSTONE, output).requires(input);
        save(null);
        return shapeless;
    }

    public RecipeBuilder doorBuilder(ItemLike output, Ingredient input) {
        shaped = shaped(RecipeCategory.REDSTONE, output, 3).define('#', input).pattern("##").pattern("##").pattern("##");
        save(null);
        return shaped;
    }

    public RecipeBuilder fenceBuilder(ItemLike output, Ingredient input) {
        int i = output == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        Item item = output == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK;
        shaped = shaped(RecipeCategory.DECORATIONS, output, i).define('W', input).define('#', item).pattern("W#W").pattern("W#W");
        save(null);
        return shaped;
    }

    public RecipeBuilder fenceGateBuilder(ItemLike output, Ingredient input) {
        shaped = shaped(RecipeCategory.REDSTONE, output).define('#', Items.STICK).define('W', input).pattern("#W#").pattern("#W#");
        save(null);
        return shaped;
    }

    public void pressurePlate(ItemLike output, ItemLike input) {
        pressurePlateBuilder(RecipeCategory.REDSTONE, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
    }

    public RecipeBuilder pressurePlateBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output).define('#', input).pattern("##");
        save(null);
        return shaped;
    }

    public void slab(RecipeCategory category, ItemLike output, ItemLike input) {
        slabBuilder(category, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public RecipeBuilder slabBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output, 6).define('#', input).pattern("###");
        save(null);
        return shaped;
    }

    public RecipeBuilder stairBuilder(ItemLike output, Ingredient input) {
        shaped = shaped(RecipeCategory.BUILDING_BLOCKS, output, 4).define('#', input).pattern("#  ").pattern("## ").pattern("###");
        save(null);
        return shaped;
    }

    public RecipeBuilder trapdoorBuilder(ItemLike output, Ingredient input) {
        shaped = shaped(RecipeCategory.REDSTONE, output, 2).define('#', input).pattern("###").pattern("###");
        save(null);
        return shaped;
    }

    public RecipeBuilder signBuilder(ItemLike output, Ingredient input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output, 3).group("sign").define('#', input).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ");
        save(null);
        return shaped;
    }

    public void hangingSign(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output, 6).group("hanging_sign").define('#', input).define('X', Items.CHAIN).pattern("X X").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(input));
        save(null);
    }

    /*
    public void colorBlockWithDye(List<Item> inputs, List<Item> outputs, String p_289641_) {
        for(int i = 0; i < inputs.size(); ++i) {
            Item input = inputs.get(i);
            Item output = outputs.get(i);
            shapeless = shapeless(RecipeCategory.BUILDING_BLOCKS, output).requires(input).requires(Ingredient.of(outputs.stream().filter((p_288265_) -> !p_288265_.equals(output)).map(ItemStack::new))).group(p_289641_).unlockedBy("has_needed_dye", has(input));
        }
    }
     */

    public void colorBlockWithDye(ItemLike output, ItemLike input, String group) {
        shapeless = shapeless(RecipeCategory.BUILDING_BLOCKS, output).requires(input).requires(output).group(group).unlockedBy("has_needed_dye", has(input));
        save(null);
    }

    public void carpet(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output, 3).define('#', input).pattern("##").group("carpet").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void bedFromPlanksAndWool(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output).define('#', input).define('X', ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void banner(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output).define('#', input).define('|', Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void stainedGlassFromGlassAndDye(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.BUILDING_BLOCKS, output, 8).define('#', Blocks.GLASS).define('X', input).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", has(Blocks.GLASS));
        save(null);
    }

    public void stainedGlassPaneFromStainedGlass(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output, 16).define('#', input).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", has(input));
        save(null);
    }

    public void stainedGlassPaneFromGlassPaneAndDye(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.DECORATIONS, output, 8).define('#', Blocks.GLASS_PANE).define('$', input).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE)).unlockedBy(getHasName(input), has(input));
        save(getConversionRecipeName(output, Blocks.GLASS_PANE));
    }

    public void coloredTerracottaFromTerracottaAndDye(ItemLike output, ItemLike input) {
        shaped = shaped(RecipeCategory.BUILDING_BLOCKS, output, 8).define('#', Blocks.TERRACOTTA).define('X', input).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").unlockedBy("has_terracotta", has(Blocks.TERRACOTTA));
        save(null);
    }

    public void concretePowder(ItemLike output, ItemLike input) {
        shapeless = shapeless(RecipeCategory.BUILDING_BLOCKS, output, 8).requires(input).requires(Blocks.SAND, 4).requires(Blocks.GRAVEL, 4).group("concrete_powder").unlockedBy("has_sand", has(Blocks.SAND)).unlockedBy("has_gravel", has(Blocks.GRAVEL));
        save(null);
    }

    public void candle(ItemLike output, ItemLike input) {
        shapeless = shapeless(RecipeCategory.DECORATIONS, output).requires(Blocks.CANDLE).requires(input).group("dyed_candle").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void wall(RecipeCategory category, ItemLike output, ItemLike input) {
        wallBuilder(category, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
    }

    public RecipeBuilder wallBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output, 6).define('#', input).pattern("###").pattern("###");
        save(null);
        return shaped;
    }

    public void polished(RecipeCategory category, ItemLike output, ItemLike input) {
        polishedBuilder(category, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
    }

    public RecipeBuilder polishedBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output, 4).define('S', input).pattern("SS").pattern("SS");
        save(null);
        return shaped;
    }

    public void cut(RecipeCategory category, ItemLike output, ItemLike input) {
        cutBuilder(category, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
    }

    public ShapedRecipeBuilder cutBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output, 4).define('#', input).pattern("##").pattern("##");
        save(null);
        return shaped;
    }

    public void chiseled(RecipeCategory category, ItemLike output, ItemLike input) {
        shaped = chiseledBuilder(category, output, Ingredient.of(input)).unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void mosaicBuilder(RecipeCategory category, ItemLike output, ItemLike input) {
        shaped = shaped(category, output).define('#', input).pattern("#").pattern("#").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public ShapedRecipeBuilder chiseledBuilder(RecipeCategory category, ItemLike output, Ingredient input) {
        shaped = shaped(category, output).define('#', input).pattern("#").pattern("#");
        save(null);
        return shaped;
    }
    public void nineBlockToOneStorage(RecipeCategory category, ItemLike outputItem, ItemLike inputItem, @javax.annotation.Nullable String group, ResourceLocation resourceLocation) {
        shaped = shaped(category, outputItem).define('#', inputItem).pattern("###").pattern("###").pattern("###").group(group).unlockedBy(getHasName(inputItem), has(inputItem));
        save(resourceLocation);
    }

    public void oneToNineBlockStorage(RecipeCategory category, ItemLike output, ItemLike input, @javax.annotation.Nullable String p_250414_, ResourceLocation resourceLocation) {
        shapeless = shapeless(category, output, 9).requires(input).group(p_250414_).unlockedBy(getHasName(input), has(input));
        save(resourceLocation);
    }

    public void grate(Block output, Block input) {
        shaped = shaped(RecipeCategory.BUILDING_BLOCKS, output, 4).define('M', input).pattern(" M ").pattern("M M").pattern(" M ").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public void copperBulb(Block output, Block input) {
        shaped = shaped(RecipeCategory.REDSTONE, output, 4).define('C', input).define('R', Items.REDSTONE).define('B', Items.BLAZE_ROD).pattern(" C ").pattern("CBC").pattern(" R ").unlockedBy(getHasName(input), has(input));
        save(null);
    }

    public String getSimpleRecipeName(ItemLike itemLike) {
        return getItemName(itemLike);
    }

    public String getConversionRecipeName(ItemLike itemLike, ItemLike itemLike1) {
        String var10000 = getItemName(itemLike);
        return var10000 + "_from_" + getItemName(itemLike1);
    }
}
