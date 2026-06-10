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
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
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

    public Supplier<Block> stoneBlock, tilesBlock, bricksBlock, cobbleBlock, polishedBlock,
            smallTilesBlock, smallBricksBlock, squareBricksBlock, crackedBricksBlock, chiseledBricksBlock,
            windmillTilesABlock, windmillTilesBBlock, mossyCobbleBlock, mossyBricksBlock, reinforcedBricksBlock;
    public Supplier<SlabBlock> polishedSlabBlock;
    public Supplier<StairBlock> polishedStairsBlock;
    public Supplier<ButtonBlock> stoneButtonBlock;
    public Supplier<PressurePlateBlock> stonePressurePlateBlock;

    public Supplier<Item> stoneItemBlock, polishedSlabItemBlock, tilesItemBlock, bricksItemBlock, cobbleItemBlock,
            polishedItemBlock, smallTilesItemBlock, smallBricksItemBlock, squareBricksItemBlock,
            crackedBricksItemBlock, chiseledBricksItemBlock, windmillTilesAItemBlock,
            windmillTilesBItemBlock, mossyCobbleItemBlock, mossyBricksItemBlock, reinforcedBricksItemBlock,
            polishedStairsItemBlock, stoneButtonItemBlock, stonePressurePlateItemBlock;

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
        this.useExistingRockLayerTexture = false;
        isRockLayer = true;
        return this;
    }

    public RockLayerComponent<A> noOresAllowed(){
        canContainOre = false;
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
            tilesBlock = registerBlock(variantName("tiles"));
            tilesItemBlock = registerItemBlock(variantName("tiles"), tilesBlock);
            bricksBlock = registerBlock(variantName("bricks"));
            bricksItemBlock = registerItemBlock(variantName("bricks"), bricksBlock);
            smallTilesBlock = registerBlock(variantName("small_tiles"));
            smallTilesItemBlock = registerItemBlock(variantName("small_tiles"), smallTilesBlock);
            smallBricksBlock = registerBlock(variantName("small_bricks"));
            smallBricksItemBlock = registerItemBlock(variantName("small_bricks"), smallBricksBlock);
            squareBricksBlock = registerBlock(variantName("square_bricks"));
            squareBricksItemBlock = registerItemBlock(variantName("square_bricks"), squareBricksBlock);
            crackedBricksBlock = registerBlock(variantName("cracked_bricks"));
            crackedBricksItemBlock = registerItemBlock(variantName("cracked_bricks"), crackedBricksBlock);
            chiseledBricksBlock = registerBlock(variantName("chiseled_bricks"));
            chiseledBricksItemBlock = registerItemBlock(variantName("chiseled_bricks"), chiseledBricksBlock);
            windmillTilesABlock = registerBlock(variantName("windmill_tiles_a"));
            windmillTilesAItemBlock = registerItemBlock(variantName("windmill_tiles_a"), windmillTilesABlock);
            windmillTilesBBlock = registerBlock(variantName("windmill_tiles_b"));
            windmillTilesBItemBlock = registerItemBlock(variantName("windmill_tiles_b"), windmillTilesBBlock);
            mossyCobbleBlock = registerBlock(variantName("mossy_cobble"));
            mossyCobbleItemBlock = registerItemBlock(variantName("mossy_cobble"), mossyCobbleBlock);
            mossyBricksBlock = registerBlock(variantName("mossy_bricks"));
            mossyBricksItemBlock = registerItemBlock(variantName("mossy_bricks"), mossyBricksBlock);
            registerReinforcedBricksBlock();

            if(!useExistingRockLayerTexture){
                stoneBlock = registerBlock(material.name);
                stoneItemBlock = registerItemBlock(material.name, stoneBlock);
                cobbleBlock = registerBlock(variantName("cobble"));
                cobbleItemBlock = registerItemBlock(variantName("cobble"), cobbleBlock);
                polishedBlock = registerBlock(variantName("polished"));
                polishedItemBlock = registerItemBlock(variantName("polished"), polishedBlock);
                polishedSlabBlock = registerSlabBlock(variantName("polished_slab"));
                polishedSlabItemBlock = registerItemBlock(variantName("polished_slab"), polishedSlabBlock);
                polishedStairsBlock = registerStairsBlock(variantName("polished_stairs"), polishedBlock);
                polishedStairsItemBlock = registerItemBlock(variantName("polished_stairs"), polishedStairsBlock);
                stoneButtonBlock = registerButtonBlock(variantName("button"));
                stoneButtonItemBlock = registerItemBlock(variantName("button"), stoneButtonBlock);
                stonePressurePlateBlock = registerPressurePlateBlock(variantName("pressure_plate"));
                stonePressurePlateItemBlock = registerItemBlock(variantName("pressure_plate"), stonePressurePlateBlock);
            }
        }

        return this;
    }

    /// Register

    private void registerReinforcedBricksBlock() {
        String name = variantName("reinforced_bricks");

        reinforcedBricksBlock = RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(
                MaterialBlockProperties.of()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
        ));

        reinforcedBricksItemBlock = registerItemBlock(name, reinforcedBricksBlock, true);
    }

    private Supplier<Block> registerBlock(String name) {
        return RegistriesHandler.BLOCKS.register(name, () -> new RockLayerBlock(blockProperties()));
    }

    private Supplier<SlabBlock> registerSlabBlock(String name) {
        return RegistriesHandler.BLOCKS.register(name, () -> new SlabBlock(blockProperties()));
    }

    private Supplier<StairBlock> registerStairsBlock(String name, Supplier<? extends Block> baseBlock) {
        return RegistriesHandler.BLOCKS.register(name, () -> new StairBlock(() -> baseBlock.get().defaultBlockState(), blockProperties()));
    }

    private Supplier<ButtonBlock> registerButtonBlock(String name) {
        return RegistriesHandler.BLOCKS.register(name, () -> new ButtonBlock(blockProperties(), BlockSetType.STONE, 20, false));
    }

    private Supplier<PressurePlateBlock> registerPressurePlateBlock(String name) {
        return RegistriesHandler.BLOCKS.register(name, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, blockProperties(), BlockSetType.STONE));
    }

    private Supplier<Item> registerItemBlock(String name, Supplier<? extends Block> block) {
        return registerItemBlock(name, block, false);
    }

    private Supplier<Item> registerItemBlock(String name, Supplier<? extends Block> block, boolean includeColor) {
        return RegistriesHandler.BLOCK_ITEMS.register(name, () -> new RockLayerItemBlock(block.get(), itemProperties(includeColor)));
    }

    private MaterialBlockProperties blockProperties() {
        return MaterialBlockProperties.of()
                .strength(3.0f, 3.0f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
    }

    private MaterialItemProperties itemProperties(boolean includeColor) {
        MaterialItemProperties properties = new MaterialItemProperties();

        if (includeColor) {
            properties.color(material.physical.getColor());
        }

        return properties
                .symbol(material.chemical.getSymbol())
                .boilingPoint(material.thermal.getBoilingPoint())
                .meltingPoint(material.thermal.getMeltingPoint());
    }

    private String variantName(String variant) {
        return material.name + "_" + variant;
    }

    /// Models

    @Override
    public RockLayerComponent<A> initClient() {
        if (isRockLayer) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;

            saveVariantBlockAssets(modid, name, "tiles", tilesBlock);
            saveVariantBlockAssets(modid, name, "bricks", bricksBlock);
            saveVariantBlockAssets(modid, name, "small_tiles", smallTilesBlock);
            saveVariantBlockAssets(modid, name, "small_bricks", smallBricksBlock);
            saveVariantBlockAssets(modid, name, "square_bricks", squareBricksBlock);
            saveVariantBlockAssets(modid, name, "cracked_bricks", crackedBricksBlock);
            saveVariantBlockAssets(modid, name, "chiseled_bricks", chiseledBricksBlock);
            saveVariantBlockAssets(modid, name, "windmill_tiles_a", windmillTilesABlock);
            saveVariantBlockAssets(modid, name, "windmill_tiles_b", windmillTilesBBlock);
            saveVariantBlockAssets(modid, name, "mossy_cobble", mossyCobbleBlock, "cobble", "mossy_cobble_overlay");
            saveVariantBlockAssets(modid, name, "mossy_bricks", mossyBricksBlock, "bricks", "mossy_bricks_overlay");
            saveVariantBlockAssets(modid, name, "reinforced_bricks", reinforcedBricksBlock, "bricks", "reinforced_bricks_overlay");


            if (!useExistingRockLayerTexture) {
                initLayerBlockModel(modid, name);
                saveVariantBlockAssets(modid, name, "cobble", cobbleBlock);
                saveVariantBlockAssets(modid, name, "polished", polishedBlock);
                initPolishedSlabBlockModel(modid, name);
                initPolishedStairsBlockModel(modid, name);
                initStoneButtonBlockModel(modid, name);
                initStonePressurePlateBlockModel(modid, name);
            }
        }

        return this;
    }

    private void initLayerBlockModel(String modid, String name) {
        ResourceLocation texture = accessoryTexture(modid, name, "stone");
        saveRockLayerBlockAssets(modid, name, stoneBlock, texture, null);
    }

    private void initPolishedSlabBlockModel(String modid, String name) {
        String blockName = name + "_polished_slab";
        ResourceLocation texture = accessoryTexture(modid, name, "polished");
        ResourceLocation doubleSlab = ResourceLocation.fromNamespaceAndPath(modid, BlockModelContent.BLOCK_FOLDER + "/" + name + "_polished");

        new RockLayerModelContent(modid, blockName, null).saveRockLayerSlabBlockModel(polishedSlabBlock.get(), texture, doubleSlab);
    }

    private void initPolishedStairsBlockModel(String modid, String name) {
        String blockName = name + "_polished_stairs";
        ResourceLocation texture = accessoryTexture(modid, name, "polished");

        new RockLayerModelContent(modid, blockName, null).saveRockLayerStairsBlockModel(polishedStairsBlock.get(), texture);
    }

    private void initStoneButtonBlockModel(String modid, String name) {
        String blockName = name + "_button";
        ResourceLocation texture = accessoryTexture(modid, name, "stone");

        new RockLayerModelContent(modid, blockName, null).saveRockLayerButtonBlockModel(stoneButtonBlock.get(), texture);
    }

    private void initStonePressurePlateBlockModel(String modid, String name) {
        String blockName = name + "_pressure_plate";
        ResourceLocation texture = accessoryTexture(modid, name, "stone");

        new RockLayerModelContent(modid, blockName, null).saveRockLayerPressurePlateBlockModel(stonePressurePlateBlock.get(), texture);
    }

    private void saveVariantBlockAssets(String modid, String rockName, String variant, Supplier<Block> block) {
        saveVariantBlockAssets(modid, rockName, variant, block, variant, null);
    }

    private void saveVariantBlockAssets(String modid, String rockName, String blockVariant, Supplier<Block> block, String textureVariant, String overlayVariant) {
        ResourceLocation texture = accessoryTexture(modid, rockName, textureVariant);
        ResourceLocation overlayTexture = overlayVariant == null ? null : accessoryTexture(modid, rockName, overlayVariant);

        saveRockLayerBlockAssets(modid, rockName + "_" + blockVariant, block, texture, overlayTexture);
    }

    private ResourceLocation accessoryTexture(String modid, String rockName, String variant) {
        return ResourceLocation.fromNamespaceAndPath(modid, BlockModelContent.BLOCK_FOLDER + "/" + "stones/" + rockName + "/" + variant);
    }

    private void saveRockLayerBlockAssets(String modid, String blockName, Supplier<Block> block, ResourceLocation texture, ResourceLocation overlayTexture) {
        if (block == null) return;

        RockLayerModelContent model = overlayTexture == null
                ? new RockLayerModelContent(modid, blockName, null).saveRockLayerBlockModel(texture)
                : new RockLayerModelContent(modid, blockName, null).saveRockLayerBlockModel(texture, overlayTexture);

        new BlockStateContent<>(modid, blockName).simpleBlock(block.get(), model.model());
        new MCItemModelContent(modid, blockName, null).saveBlockItem(model);
    }

    /// Languages

    @Override
    public RockLayerComponent<A> initLanguages() {
        if (isRockLayer) {
            String modid = CoreRef.MOD_ID;
            String name = material.name;


            registerVariantLanguage(modid, name, "tiles", tilesItemBlock);
            registerVariantLanguage(modid, name, "bricks", bricksItemBlock);
            registerVariantLanguage(modid, name, "small_tiles", smallTilesItemBlock);
            registerVariantLanguage(modid, name, "small_bricks", smallBricksItemBlock);
            registerVariantLanguage(modid, name, "square_bricks", squareBricksItemBlock);
            registerVariantLanguage(modid, name, "cracked_bricks", crackedBricksItemBlock);
            registerVariantLanguage(modid, name, "chiseled_bricks", chiseledBricksItemBlock);
            registerVariantLanguage(modid, name, "windmill_tiles_a", windmillTilesAItemBlock);
            registerVariantLanguage(modid, name, "windmill_tiles_b", windmillTilesBItemBlock);
            registerVariantLanguage(modid, name, "mossy_cobble", mossyCobbleItemBlock);
            registerVariantLanguage(modid, name, "mossy_bricks", mossyBricksItemBlock);
            registerVariantLanguage(modid, name, "reinforced_bricks", reinforcedBricksItemBlock);

            if (!useExistingRockLayerTexture) {
                registerLanguage(modid, name, stoneItemBlock);
                registerVariantLanguage(modid, name, "cobble", cobbleItemBlock);
                registerVariantLanguage(modid, name, "polished", polishedItemBlock);
                registerVariantLanguage(modid, name, "polished_slab", polishedSlabItemBlock);
                registerVariantLanguage(modid, name, "polished_stairs", polishedStairsItemBlock);
                registerVariantLanguage(modid, name, "button", stoneButtonItemBlock);
                registerVariantLanguage(modid, name, "pressure_plate", stonePressurePlateItemBlock);
            }
        }

        return this;
    }

    private void registerVariantLanguage(String modid, String name, String variant, Supplier<Item> item) {
        registerLanguage(modid, name + "_" + variant, item);
    }

    private void registerLanguage(String modid, String blockName, Supplier<Item> item) {
        AssetPackRegistries.registerSafetyLanguage(item, modid, LanguageCodes.english, BlockModelContent.BLOCK_FOLDER,
                blockName, LanguageContent.toDisplayName(blockName));
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