package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.blockstate.component.OreBlockStateComponent;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.content.model.block.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.block.component.OreBlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.ItemModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.component.OreItemModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.item.GemItem;
import com.mightydanp.techcore.materials.item.OreBlockItem;
import com.mightydanp.techcore.materials.item.OreItem;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.BedrockOre;
import com.mightydanp.techcore.materials.block.DenseOre;
import com.mightydanp.techcore.materials.block.OreBlock;
import com.mightydanp.techcore.materials.properties.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OreComponent<A extends Material> extends Component<A, OreComponent<A>>{
    private static final String BEDROCK_ORE_KEY = "bedrock";

    public List<Material> sameRockMaterials = new ArrayList<>();

    private OreTypes.OreType oreType;
    private List<RockTypes.RockType> rockTypes = new ArrayList<>();
    private int maxDensity = 1;

    public Supplier<Item> chippedGem, flawedGem, gem, flawlessGem, legendaryGem;

    public Map<String, Supplier<Item>> rawOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> centrifugedOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> crushedOreItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> purifiedOreItems = new LinkedHashMap<>();

    public Map<String, Supplier<Item>> sparseOreBlockItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> oreBlockItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> denseOreBlockItems = new LinkedHashMap<>();
    public Map<String, Supplier<Item>> bedrockOreBlockItems = new LinkedHashMap<>();

    public Map<String, Supplier<Block>> sparseOreBlocks = new LinkedHashMap<>();
    public Map<String, Supplier<Block>> oreBlocks = new LinkedHashMap<>();
    public Map<String, Supplier<Block>> denseOreBlocks = new LinkedHashMap<>();
    public Map<String, Supplier<Block>> bedrockOreBlocks = new LinkedHashMap<>();


    public OreComponent(A material) {
        super("ore", "component", material);
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType , int maxDensity, RockTypes.RockType... rockTypes) {
        this.oreType = oreType;
        this.maxDensity = maxDensity;
        this.rockTypes = Arrays.asList(rockTypes);
        this.sameRockMaterials = RockLayerComponent.getRockLayerMaterials().stream().filter(material1 -> this.rockTypes.contains(material1.rockLayer.rockType) && material1.rockLayer.canContainOre).toList();
        return this;
    }

    public OreComponent<A> setOre(OreTypes.OreType oreType, RockTypes.RockType... rockTypes) {
        this.oreType = oreType;
        this.rockTypes = Arrays.asList(rockTypes);
        this.sameRockMaterials = RockLayerComponent.getRockLayerMaterials().stream().filter(material1 -> this.rockTypes.contains(material1.rockLayer.rockType) && material1.rockLayer.canContainOre).toList();
        return this;
    }

    public OreComponent<A> setMaxDensity(int maxDensity) {
        this.maxDensity = maxDensity;
        return this;
    }

    @Override
    public OreComponent<A> init() {
        if (oreType == OreTypes.GEM.oreType()) {
            String name = material.name + "_gem";

            chippedGem = registerGemItem("chipped_" + name);
            flawedGem = registerGemItem("flawed_" + name);
            gem = registerGemItem(material.name + "_gem");
            flawlessGem = registerGemItem("flawless_" + name);
            legendaryGem = registerGemItem("legendary_" + name);
        }

        if (oreType == OreTypes.ORE.oreType() || oreType == OreTypes.GEM.oreType()) {
            registerBedrockOreBlock(material.name + "_ore");

            for (Material stoneLayer : sameRockMaterials) {
                String stoneName = stoneLayer.name;
                String name = stoneName + "_" + material.name + "_ore";

                rawOreItems.put(stoneName, registerOreItem("raw_" + name));
                centrifugedOreItems.put(stoneName, registerOreItem("centrifuged_" + name));
                crushedOreItems.put(stoneName, registerOreItem("crushed_" + name));
                purifiedOreItems.put(stoneName, registerOreItem("purified_" + name));

                registerSparseOreBlock(stoneName, name);
                registerOreBlock(stoneName, name);
                registerDenseOreBlock(stoneName, name);
            }
        }

        return this;
    }

    ///Register

    private void registerSparseOreBlock(String stoneName, String baseName) {
        String blockName = "sparse_" + baseName;
        Supplier<Block> block = RegistriesHandler.BLOCKS.register(blockName, () -> new OreBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .requiresCorrectToolForDrops()
        ));

        sparseOreBlocks.put(stoneName, block);
        sparseOreBlockItems.put(stoneName, registerOreBlockItem(blockName, block));
    }

    private void registerOreBlock(String stoneName, String baseName) {
        Supplier<Block> block = RegistriesHandler.BLOCKS.register(baseName, () -> new OreBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .requiresCorrectToolForDrops()
        ));

        oreBlocks.put(stoneName, block);
        oreBlockItems.put(stoneName, registerOreBlockItem(baseName, block));
    }

    private void registerDenseOreBlock(String stoneName, String baseName) {
        String blockName = "dense_" + baseName;
        Supplier<Block> block = RegistriesHandler.BLOCKS.register(blockName, () -> new DenseOre(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .requiresCorrectToolForDrops(),
                maxDensity
        ));

        denseOreBlocks.put(stoneName, block);
        denseOreBlockItems.put(stoneName, registerOreBlockItem(blockName, block));
    }

    private void registerBedrockOreBlock(String baseName) {
        String blockName = "bedrock_" + baseName;
        Supplier<Block> block = RegistriesHandler.BLOCKS.register(blockName, () -> new BedrockOre(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .requiresCorrectToolForDrops()
        ));

        bedrockOreBlocks.put(BEDROCK_ORE_KEY, block);
        bedrockOreBlockItems.put(BEDROCK_ORE_KEY, registerOreBlockItem(blockName, block));
    }


    private Supplier<Item> registerOreBlockItem(String itemName, Supplier<Block> block) {
        return RegistriesHandler.BLOCK_ITEMS.register(itemName, () -> new OreBlockItem(block.get(), new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private Supplier<Item> registerGemItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new GemItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private Supplier<Item> registerOreItem(String itemName) {
        return RegistriesHandler.ITEMS.register(itemName, () -> new OreItem(new MaterialItemProperties()
                .color(material.physical.getColor())
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    ///
    @Override
    public OreComponent<A> initClient() {
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        if (gem != null) initGemModels(modid, name, gem);

        initRawOreModels(modid, name, rawOreItems);
        initOreBlockModels(modid, name);

        return this;
    }

    ///Models

    private void initGemModels(String modid, String name, Supplier<Item> item) {
        if(item != null) {
            String[] items = {"chipped_" + name + "_gem", "flawed_" + name + "_gem", name + "_gem", "flawless_" + name + "_gem", "legendary_" + name + "_gem"};
            String[] models = {"chipped_gem", "flawed_gem", "gem", "flawless_gem", "legendary_gem"};

            for (String model : models) {
                new OreItemModelContent(modid, model, "gem")
                        .saveGemModel(material.icon, model);
            }

            for (int i = 0; i < models.length; i++) {
                new OreItemModelContent(modid, items[i], null)
                        .saveGemItemModel(material.icon, models[i]);
            }
        }
    }

    private void initRawOreModels(String modid, String name, @NotNull Map<String, Supplier<Item>> items) {
        if(!items.isEmpty()) {
            String[] stages = {"raw", "centrifuged", "crushed", "purified"};

            for (String stage : stages) {
                new OreItemModelContent(modid, stage + "_" + name + "_ore", "ore")
                        .saveOreItemModel(material.icon, stage);
            }

            for (Material stoneLayer : sameRockMaterials) {
                String rockName = stoneLayer.name;

                new OreItemModelContent(modid, "raw_" + rockName + "_" + name + "_ore", null)
                        .saveStageOreItemModel(material.icon, "raw");
                new OreItemModelContent(modid, "centrifuged_" + rockName + "_" + name + "_ore", null)
                        .saveStageOreItemModel(material.icon, "centrifuged");
                new OreItemModelContent(modid, "crushed_" + rockName + "_" + name + "_ore", null)
                        .saveStageOreItemModel(material.icon, "crushed");
                new OreItemModelContent(modid, "purified_" + rockName + "_" + name + "_ore", null)
                        .saveStageOreItemModel(material.icon, "purified");
            }
        }
    }

    private void initOreBlockModels(String modid, String name) {
        List<Map<String, Supplier<Block>>> blockMaps = List.of(sparseOreBlocks, oreBlocks, denseOreBlocks);
        String[] stages = {"sparse", "ore", "dense"};

        for (Material stoneLayer : sameRockMaterials) {
            Block stoneBaseBlock = getOreBaseBlock(stoneLayer);
            if (stoneBaseBlock == null) continue;

            for (int i = 0; i < stages.length; i++) {
                String stage = stages[i];
                Supplier<Block> oreBlock = blockMaps.get(i).get(stoneLayer.name);

                if (oreBlock == null) continue;

                String blockName = "ore".equals(stage)
                        ? stoneLayer.name + "_" + name + "_ore"
                        : stage + "_" + stoneLayer.name + "_" + name + "_ore";

                saveOreBlockAssets(modid, blockName, stage, stoneLayer, oreBlock);
            }
        }

        Supplier<Block> bedrockOreBlock = bedrockOreBlocks.get(BEDROCK_ORE_KEY);
        if (bedrockOreBlock != null) {
            saveOreBlockAssets(modid, "bedrock_" + name + "_ore", "bedrock", Blocks.BEDROCK, bedrockOreBlock);
        }

    }

    @Contract(pure = true)
    private Block getOreBaseBlock(@NotNull Material stoneLayer) {
        return stoneLayer.rockLayer.existingRocklayerBlock != null
                ? stoneLayer.rockLayer.existingRocklayerBlock
                : stoneLayer.rockLayer.stoneBlock != null ? stoneLayer.rockLayer.stoneBlock.get() : null;
    }

    private void saveOreBlockAssets(String modid, String blockName, @NotNull String textureVariant, Material stoneLayer, @NotNull Supplier<Block> oreBlock) {
        ResourceLocation baseTexture = baseTexture(modid, stoneLayer, "stone");

        saveOreBlockAssets(modid, blockName, textureVariant, baseTexture, oreBlock);
    }

    private void saveOreBlockAssets(String modid, String blockName, @NotNull String textureVariant, Block baseBlock, @NotNull Supplier<Block> oreBlock) {
        ResourceLocation baseTexture = new OreBlockStateComponent(modid, blockName).blockTexture(baseBlock);

        saveOreBlockAssets(modid, blockName, textureVariant, baseTexture, oreBlock);
    }

    private void saveOreBlockAssets(String modid, String blockName, @NotNull String textureVariant, ResourceLocation baseTexture, @NotNull Supplier<Block> oreBlock) {
        new OreBlockModelContent(modid, blockName, "ore")
                .saveOreBlockModel(material.icon, blockName, baseTexture, textureVariant.matches("ore") ? textureVariant : textureVariant + "_ore");

        new OreBlockStateComponent(modid, blockName)
                .saveOreBlockState(oreBlock.get());

        new OreItemModelContent(modid, blockName, null)
                .saveOreBlockItemModel("ore/" + blockName);
    }

    private ResourceLocation baseTexture(String modid, Material stoneLayer, @NotNull String textureVariant) {
        if (stoneLayer.rockLayer.useExistingRockLayerTexture && stoneLayer.rockLayer.existingRocklayerBlock != null) {
            return new OreBlockStateComponent(modid, stoneLayer.name).blockTexture(stoneLayer.rockLayer.existingRocklayerBlock);
        }

        return ResourceLocation.fromNamespaceAndPath(modid,  BlockModelContent.BLOCK_FOLDER + "/stones/" + stoneLayer.name + "/" + textureVariant);
    }

    ///
    @Override
    public OreComponent<A> initItemProperties(){
        registerItemProperty(chippedGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (chippedGem.get())).getGemQuality(stack));

        registerItemProperty(flawedGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (flawedGem.get())).getGemQuality(stack));

        registerItemProperty(gem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (gem.get())).getGemQuality(stack));

        registerItemProperty(flawlessGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (flawlessGem.get())).getGemQuality(stack));

        registerItemProperty(legendaryGem,
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "gem_quality"),
                (stack, level, entity, seed) ->
                        ((GemItem) (legendaryGem.get())).getGemQuality(stack));

        registerOreQuantityProperty(rawOreItems);
        registerOreQuantityProperty(centrifugedOreItems);
        registerOreQuantityProperty(crushedOreItems);
        registerOreQuantityProperty(purifiedOreItems);

        return this;
    }

    private void registerOreQuantityProperty(@NotNull Map<String, Supplier<Item>> oreItems) {
        for (Supplier<Item> oreItem: oreItems.values()) {
            registerItemProperty(oreItem,
                    ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "quantity"),
                    (stack, level, entity, seed) ->
                            ((OreItem) (oreItem.get())).getQuantityLevel(stack));
        }
    }
    ///
    @Override
    public OreComponent<A> initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        registerItemColor(event, chippedGem, material.physical.getColor());
        registerItemColor(event, flawedGem, material.physical.getColor());
        registerItemColor(event, gem, material.physical.getColor());
        registerItemColor(event, flawlessGem, material.physical.getColor());
        registerItemColor(event, legendaryGem, material.physical.getColor());

        registerOreColors(event, rawOreItems);
        registerOreColors(event, centrifugedOreItems);
        registerOreColors(event, crushedOreItems);
        registerOreColors(event, purifiedOreItems);

        registerOreColors(event, sparseOreBlockItems);
        registerOreColors(event, oreBlockItems);
        registerOreColors(event, denseOreBlockItems);
        registerItemColor(event, bedrockOreBlockItems.get(BEDROCK_ORE_KEY), material.physical.getColor());

        return this;
    }

    private void registerOreColors(RegisterColorHandlersEvent.Item event, Map<String, Supplier<Item>> oreItems) {
        for (Material stoneLayer : sameRockMaterials) {
            Supplier<Item> oreItem = oreItems.get(stoneLayer.name);
            if (oreItem != null) {
                registerItemColor(event, oreItem, material.physical.getColor(), stoneLayer.physical.getColor());
            }
        }
    }
    ///

    @Override
    public OreComponent<A> initBlockProperties(RegisterColorHandlersEvent.Block event) {
        registerOreBlockColors(event, sparseOreBlocks);
        registerOreBlockColors(event, oreBlocks);
        registerOreBlockColors(event, denseOreBlocks);
        registerOreBlockColors(event, bedrockOreBlocks);

        return this;
    }

    private void registerOreBlockColors(RegisterColorHandlersEvent.Block event, @NotNull Map<String, Supplier<Block>> oreBlocks) {
        for (Supplier<Block> oreBlock : oreBlocks.values()) {
            registerBlockColor(event, oreBlock, material.physical.getColor());
        }
    }

    ///
    @Override
    public OreComponent<A> initLanguages(){
        String modid = CoreRef.MOD_ID;
        String name = material.name;

        AssetPackRegistries.registerSafetyLanguage(chippedGem, modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER, "chipped_" + name + "_gem", "Chipped " + LanguageContent.toDisplayName(name) + " Gem");
        AssetPackRegistries.registerSafetyLanguage(flawedGem, modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER, "flawed_" + name + "_gem", "Flawed " +LanguageContent.toDisplayName(name) + " Gem");
        AssetPackRegistries.registerSafetyLanguage(gem, modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER, name + "_gem", LanguageContent.toDisplayName(name) + " Gem");
        AssetPackRegistries.registerSafetyLanguage(flawlessGem, modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER, "flawless_" + name + "_gem", "Flawless " + LanguageContent.toDisplayName(name) + " Gem");
        AssetPackRegistries.registerSafetyLanguage(legendaryGem, modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER, "legendary_" + name + "_gem", "Legendary " + LanguageContent.toDisplayName(name) + " Gem");

        for (Material stoneLayer : sameRockMaterials) {
            String rockName = stoneLayer.name;
            String translatedName = LanguageContent.toDisplayName(rockName + "_" + name);
            String blockName = rockName + "_" + name + "_ore";

            AssetPackRegistries.registerSafetyLanguage(rawOreItems.get(rockName), modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER,
                    "raw_" + blockName, "Raw " + translatedName + " Ore");
            AssetPackRegistries.registerSafetyLanguage(centrifugedOreItems.get(rockName), modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER,
                    "centrifuged_" + blockName, "Centrifuged " + translatedName + " Ore");
            AssetPackRegistries.registerSafetyLanguage(crushedOreItems.get(rockName), modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER,
                    "crushed_" + blockName, "Crushed " + translatedName + " Ore");
            AssetPackRegistries.registerSafetyLanguage(purifiedOreItems.get(rockName), modid, LanguageCodes.english, ItemModelContent.ITEM_FOLDER,
                    "purified_" + blockName, "Purified " + translatedName + " Ore");

            AssetPackRegistries.registerSafetyLanguage(sparseOreBlockItems.get(rockName), modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                    "sparse_" + blockName, "Sparse " + translatedName + " Ore");

            AssetPackRegistries.registerSafetyLanguage(oreBlockItems.get(rockName), modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                    blockName, translatedName + " Ore");

            AssetPackRegistries.registerSafetyLanguage(denseOreBlockItems.get(rockName), modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                    "dense_" + blockName, "Dense " + translatedName + " Ore");

        }

        AssetPackRegistries.registerSafetyLanguage(bedrockOreBlockItems.get(BEDROCK_ORE_KEY), modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                "bedrock_" + name + "_ore", "Bedrock " + LanguageContent.toDisplayName(name) + " Ore");

        return this;
    }

    ///

    public boolean hasOreBlocks() {
        return !sparseOreBlocks.isEmpty() || !oreBlocks.isEmpty() || !denseOreBlocks.isEmpty() || !bedrockOreBlocks.isEmpty();
    }

    public OreTypes.OreType getOreType() {
        return oreType;
    }

    public int getMaxDensity() {
        return maxDensity;
    }

    public List<RockTypes.RockType> getRockTypes() {
        return Collections.unmodifiableList(rockTypes);
    }

    public Map<String, Supplier<Block>> getSparseOreBlocks() {
        return Collections.unmodifiableMap(sparseOreBlocks);
    }

    public Map<String, Supplier<Item>> getSparseOreBlockItems() {
        return Collections.unmodifiableMap(sparseOreBlockItems);
    }

    public Map<String, Supplier<Block>> getOreBlocks() {
        return Collections.unmodifiableMap(oreBlocks);
    }

    public Map<String, Supplier<Block>> getDenseOreBlocks() {
        return Collections.unmodifiableMap(denseOreBlocks);
    }
}
