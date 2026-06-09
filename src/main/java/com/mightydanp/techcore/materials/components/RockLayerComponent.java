package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.api.resources.assets.AssetPackRegistries;
import com.mightydanp.techcore.api.resources.assets.content.blockstate.BlockStateContent;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageCodes;
import com.mightydanp.techcore.api.resources.assets.content.language.LanguageContent;
import com.mightydanp.techcore.api.resources.assets.content.model.block.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.block.component.RockLayerModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.MCItemModelContent;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.item.RockLayerItemBlock;
import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.block.RockLayerBlock;
import com.mightydanp.techcore.materials.properties.MaterialBlockProperties;
import com.mightydanp.techcore.materials.properties.MaterialItemProperties;
import com.mightydanp.techcore.materials.properties.RockTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.function.Supplier;


public class RockLayerComponent<A extends Material> extends Component<A, RockLayerComponent<A>> {
    public boolean isRockLayer;
    public boolean canContainOre = true;

    public RockTypes.RockType rockType;
    public List<RockTypes.RockType> rockTypesFoundIn;

    public boolean useExistingRockLayerTexture;

    public Block existingRocklayerBlock;

    public Supplier<Block> stoneBlock, smoothSlabBlock, tilesBlock, bricksBlock, cobbleBlock, smoothBlock,
            small_tilesBlock, small_bricksBlock, square_bricksBlock, cracked_bricksBlock, chiseled_bricksBlock,
            windmill_tiles_aBlock, windmill_tiles_bBlock, mossyCobbleBlock, mossyBricksBlock, reinforcedBricksBlock;

    public Supplier<Item> stoneItemBlock, smoothSlabItemBlock, tilesItemBlock, bricksItemBlock, cobbleItemBlock,
            smoothItemBlock, small_tilesItemBlock, small_bricksItemBlock, square_bricksItemBlock,
            cracked_bricksItemBlock, chiseled_bricksItemBlock, windmill_tiles_aItemBlock,
            windmill_tiles_bItemBlock, mossyCobbleItemBlock, mossyBricksItemBlock, reinforcedBricksItemBlock;

    public RockLayerComponent(A material) {
        super("rock_layer", "component", material);
    }

    public RockLayerComponent<A> rockLayer(Block existingRocklayerBlock, RockTypes.RockType rockType) {
        this.rockType = rockType;
        this.existingRocklayerBlock = existingRocklayerBlock;
        isRockLayer = true;
        return this;
    }

    public RockLayerComponent<A> rockLayer(RockTypes.RockType rockType) {
        this.rockType = rockType;
        isRockLayer = true;
        return this;
    }

    public RockLayerComponent<A> noOreAllowed(){
        canContainOre = false;
        return this;
    }

    public RockLayerComponent<A> useExistingRockLayerTexture() {
        useExistingRockLayerTexture = true;
        return this;
    }

    public static @NotNull @Unmodifiable List<Material> getRockLayerMaterials() {
        return RegistriesHandler.getMaterials().stream()
                .filter(material -> material.rockLayer.isRockLayer)
                .toList();
    }

    @Override
    public RockLayerComponent<A> init() {
        if (isRockLayer) {
            if (!useExistingRockLayerTexture) {
                registerLayerBlock();
                registerSmoothSlabBlock();
                registerTilesBlock();
                registerBricksBlock();
                registerCobbleBlock();
                registerSmoothBlock();
                registerSmallTilesBlock();
                registerSmallBricksBlock();
                registerSquareBricksBlock();
                registerCrackedBricksBlock();
                registerChiseledBricksBlock();
                registerWindmillTilesABlock();
                registerWindmillTilesBBlock();
                registerMossyCobbleBlock();
                registerMossyBricksBlock();
                registerReinforcedBricksBlock();
            }
        }

        return this;
    }

    /// Register

    private void registerLayerBlock() {
        stoneBlock = RegistriesHandler.BLOCKS.register(material.name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        stoneItemBlock = RegistriesHandler.BLOCK_ITEMS.register(material.name, () -> new RockLayerItemBlock(
                stoneBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerSmoothSlabBlock() {
        String name = material.name + "_smooth_slab";

        smoothSlabBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        smoothSlabItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                smoothSlabBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerTilesBlock() {
        String name = material.name + "_tiles";

        tilesBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        tilesItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                tilesBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerBricksBlock() {
        String name = material.name + "_bricks";

        bricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        bricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                bricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerCobbleBlock() {
        String name = material.name + "_cobble";

        cobbleBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        cobbleItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                cobbleBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerSmoothBlock() {
        String name = material.name + "_smooth";

        smoothBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        smoothItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                smoothBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerSmallTilesBlock() {
        String name = material.name + "_small_tiles";

        small_tilesBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        small_tilesItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                small_tilesBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerSmallBricksBlock() {
        String name = material.name + "_small_bricks";

        small_bricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        small_bricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                small_bricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerSquareBricksBlock() {
        String name = material.name + "_square_bricks";

        square_bricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        square_bricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                square_bricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerCrackedBricksBlock() {
        String name = material.name + "_cracked_bricks";

        cracked_bricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        cracked_bricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                cracked_bricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerChiseledBricksBlock() {
        String name = material.name + "_chiseled_bricks";

        chiseled_bricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        chiseled_bricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                chiseled_bricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerWindmillTilesABlock() {
        String name = material.name + "_windmill_tiles_a";

        windmill_tiles_aBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        windmill_tiles_aItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                windmill_tiles_aBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerWindmillTilesBBlock() {
        String name = material.name + "_windmill_tiles_b";

        windmill_tiles_bBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        windmill_tiles_bItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                windmill_tiles_bBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerMossyCobbleBlock() {
        String name = material.name + "_mossy_cobble";

        mossyCobbleBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        mossyCobbleItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                mossyCobbleBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerMossyBricksBlock() {
        String name = material.name + "_mossy_bricks";

        mossyBricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        mossyBricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                mossyBricksBlock.get(),
                new MaterialItemProperties()
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    private void registerReinforcedBricksBlock() {
        String name = material.name + "_reinforced_bricks";

        reinforcedBricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        reinforcedBricksItemBlock = RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(
                reinforcedBricksBlock.get(),
                new MaterialItemProperties()
                        .color(material.physical.getColor())
                        .symbol(material.chemical.getSymbol())
                        .boilingPoint(material.thermal.getBoilingPoint())
                        .meltingPoint(material.thermal.getMeltingPoint())
        ));
    }

    /// Models

    @Override
    public RockLayerComponent<A> initClient() {
        if (isRockLayer && stoneBlock != null) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;

            initLayerBlockModel(modid, name);

            if (!useExistingRockLayerTexture) {
                initSmoothSlabBlockModel(modid, name);
                initTilesBlockModel(modid, name);
                initBricksBlockModel(modid, name);
                initCobbleBlockModel(modid, name);
                initSmoothBlockModel(modid, name);
                initSmallTilesBlockModel(modid, name);
                initSmallBricksBlockModel(modid, name);
                initSquareBricksBlockModel(modid, name);
                initCrackedBricksBlockModel(modid, name);
                initChiseledBricksBlockModel(modid, name);
                initWindmillTilesABlockModel(modid, name);
                initWindmillTilesBBlockModel(modid, name);
                initMossyCobbleBlockModel(modid, name);
                initMossyBricksBlockModel(modid, name);
                initReinforcedBricksBlockModel(modid, name);
            }
        }

        return this;
    }

    private void initLayerBlockModel(String modid, String name) {
        ResourceLocation texture = accessoryTexture(modid, name, "stone");
        saveRockLayerBlockAssets(modid, name, stoneBlock, texture, null);
    }

    private void initSmoothSlabBlockModel(String modid, String name) {
        String blockName = name + "_smooth_slab";
        ResourceLocation texture = accessoryTexture(modid, name, "smooth");

        saveRockLayerBlockAssets(modid, blockName, smoothSlabBlock, texture, null);
    }

    private void initTilesBlockModel(String modid, String name) {
        String blockName = name + "_tiles";
        ResourceLocation texture = accessoryTexture(modid, name, "tiles");

        saveRockLayerBlockAssets(modid, blockName, tilesBlock, texture, null);
    }

    private void initBricksBlockModel(String modid, String name) {
        String blockName = name + "_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "bricks");

        saveRockLayerBlockAssets(modid, blockName, bricksBlock, texture, null);
    }

    private void initCobbleBlockModel(String modid, String name) {
        String blockName = name + "_cobble";
        ResourceLocation texture = accessoryTexture(modid, name, "cobble");

        saveRockLayerBlockAssets(modid, blockName, cobbleBlock, texture, null);
    }

    private void initSmoothBlockModel(String modid, String name) {
        String blockName = name + "_smooth";
        ResourceLocation texture = accessoryTexture(modid, name, "smooth");

        saveRockLayerBlockAssets(modid, blockName, smoothBlock, texture, null);
    }

    private void initSmallTilesBlockModel(String modid, String name) {
        String blockName = name + "_small_tiles";
        ResourceLocation texture = accessoryTexture(modid, name, "small_tiles");

        saveRockLayerBlockAssets(modid, blockName, small_tilesBlock, texture, null);
    }

    private void initSmallBricksBlockModel(String modid, String name) {
        String blockName = name + "_small_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "small_bricks");

        saveRockLayerBlockAssets(modid, blockName, small_bricksBlock, texture, null);
    }

    private void initSquareBricksBlockModel(String modid, String name) {
        String blockName = name + "_square_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "square_bricks");

        saveRockLayerBlockAssets(modid, blockName, square_bricksBlock, texture, null);
    }

    private void initCrackedBricksBlockModel(String modid, String name) {
        String blockName = name + "_cracked_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "cracked_bricks");

        saveRockLayerBlockAssets(modid, blockName, cracked_bricksBlock, texture, null);
    }

    private void initChiseledBricksBlockModel(String modid, String name) {
        String blockName = name + "_chiseled_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "chiseled_bricks");

        saveRockLayerBlockAssets(modid, blockName, chiseled_bricksBlock, texture, null);
    }

    private void initWindmillTilesABlockModel(String modid, String name) {
        String blockName = name + "_windmill_tiles_a";
        ResourceLocation texture = accessoryTexture(modid, name, "windmill_tiles_a");

        saveRockLayerBlockAssets(modid, blockName, windmill_tiles_aBlock, texture, null);
    }

    private void initWindmillTilesBBlockModel(String modid, String name) {
        String blockName = name + "_windmill_tiles_b";
        ResourceLocation texture = accessoryTexture(modid, name, "windmill_tiles_b");

        saveRockLayerBlockAssets(modid, blockName, windmill_tiles_bBlock, texture, null);
    }

    private void initMossyCobbleBlockModel(String modid, String name) {
        String blockName = name + "_mossy_cobble";
        ResourceLocation texture = accessoryTexture(modid, name, "cobble");
        ResourceLocation overlayTexture = accessoryTexture(modid, name, "mossy_cobble_overlay");

        saveRockLayerBlockAssets(modid, blockName, mossyCobbleBlock, texture, overlayTexture);
    }

    private void initMossyBricksBlockModel(String modid, String name) {
        String blockName = name + "_mossy_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "bricks");
        ResourceLocation overlayTexture = accessoryTexture(modid, name, "mossy_bricks_overlay");

        saveRockLayerBlockAssets(modid, blockName, mossyBricksBlock, texture, overlayTexture);
    }

    private void initReinforcedBricksBlockModel(String modid, String name) {
        String blockName = name + "_reinforced_bricks";
        ResourceLocation texture = accessoryTexture(modid, name, "bricks");
        ResourceLocation overlayTexture = accessoryTexture(modid, name, "reinforced_bricks_overlay");

        saveRockLayerBlockAssets(modid, blockName, reinforcedBricksBlock, texture, overlayTexture);
    }

    private ResourceLocation accessoryTexture(String modid, String rockName, String variant) {
        return ResourceLocation.fromNamespaceAndPath(modid, BlockModelContent.BLOCK_FOLDER + "/" + "stones/" + rockName + "/" + variant);
    }

    private void saveRockLayerBlockAssets(String modid, String blockName, Supplier<Block> block, ResourceLocation texture, ResourceLocation overlayTexture) {
        if (block == null) return;

        RockLayerModelContent model = overlayTexture == null
                ? new RockLayerModelContent(modid, blockName, null).saveRockLayerBlockModel(texture)
                : new RockLayerModelContent(modid, blockName, null).saveRockLayerBlockModel(texture, overlayTexture);

        try {
            new BlockStateContent<>(modid, blockName).simpleBlock(block.get(), model.model());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate rock layer blockstate for " + blockName, e);
        }

        new MCItemModelContent(modid, blockName, null).saveBlockItem(model);
    }

    /// Languages

    @Override
    public RockLayerComponent<A> initLanguages() {
        if (isRockLayer) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;

            registerLayerLanguage(modid, name);

            if (!useExistingRockLayerTexture) {
                registerSmoothSlabLanguage(modid, name);
                registerTilesLanguage(modid, name);
                registerBricksLanguage(modid, name);
                registerCobbleLanguage(modid, name);
                registerSmoothLanguage(modid, name);
                registerSmallTilesLanguage(modid, name);
                registerSmallBricksLanguage(modid, name);
                registerSquareBricksLanguage(modid, name);
                registerCrackedBricksLanguage(modid, name);
                registerChiseledBricksLanguage(modid, name);
                registerWindmillTilesALanguage(modid, name);
                registerWindmillTilesBLanguage(modid, name);
                registerMossyCobbleLanguage(modid, name);
                registerMossyBricksLanguage(modid, name);
                registerReinforcedBricksLanguage(modid, name);
            }
        }

        return this;
    }

    private void registerLayerLanguage(String modid, String name) {
        AssetPackRegistries.registerSafetyLanguage(stoneItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                name, LanguageContent.toDisplayName(name));
    }

    private void registerSmoothSlabLanguage(String modid, String name) {
        String blockName = name + "_smooth_slab";

        AssetPackRegistries.registerSafetyLanguage(smoothSlabItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerTilesLanguage(String modid, String name) {
        String blockName = name + "_tiles";

        AssetPackRegistries.registerSafetyLanguage(tilesItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerBricksLanguage(String modid, String name) {
        String blockName = name + "_bricks";

        AssetPackRegistries.registerSafetyLanguage(bricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerCobbleLanguage(String modid, String name) {
        String blockName = name + "_cobble";

        AssetPackRegistries.registerSafetyLanguage(cobbleItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerSmoothLanguage(String modid, String name) {
        String blockName = name + "_smooth";

        AssetPackRegistries.registerSafetyLanguage(smoothItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerSmallTilesLanguage(String modid, String name) {
        String blockName = name + "_small_tiles";

        AssetPackRegistries.registerSafetyLanguage(small_tilesItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerSmallBricksLanguage(String modid, String name) {
        String blockName = name + "_small_bricks";

        AssetPackRegistries.registerSafetyLanguage(small_bricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerSquareBricksLanguage(String modid, String name) {
        String blockName = name + "_square_bricks";

        AssetPackRegistries.registerSafetyLanguage(square_bricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerCrackedBricksLanguage(String modid, String name) {
        String blockName = name + "_cracked_bricks";

        AssetPackRegistries.registerSafetyLanguage(cracked_bricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerChiseledBricksLanguage(String modid, String name) {
        String blockName = name + "_chiseled_bricks";

        AssetPackRegistries.registerSafetyLanguage(chiseled_bricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerWindmillTilesALanguage(String modid, String name) {
        String blockName = name + "_windmill_tiles_a";

        AssetPackRegistries.registerSafetyLanguage(windmill_tiles_aItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerWindmillTilesBLanguage(String modid, String name) {
        String blockName = name + "_windmill_tiles_b";

        AssetPackRegistries.registerSafetyLanguage(windmill_tiles_bItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerMossyCobbleLanguage(String modid, String name) {
        String blockName = name + "_mossy_cobble";

        AssetPackRegistries.registerSafetyLanguage(mossyCobbleItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerMossyBricksLanguage(String modid, String name) {
        String blockName = name + "_mossy_bricks";

        AssetPackRegistries.registerSafetyLanguage(mossyBricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    private void registerReinforcedBricksLanguage(String modid, String name) {
        String blockName = name + "_reinforced_bricks";

        AssetPackRegistries.registerSafetyLanguage(reinforcedBricksItemBlock, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER, blockName, LanguageContent.toDisplayName(blockName));
    }

    /// Item Properties

    @Override
    public RockLayerComponent<A> initItemProperties() {
        return this;
    }

    /// Render Layers

    @Override
    public RockLayerComponent<A> initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        if (isRockLayer && reinforcedBricksItemBlock != null) {
            registerItemColor(event, reinforcedBricksItemBlock, material.physical.getColor());
        }

        return this;
    }
}