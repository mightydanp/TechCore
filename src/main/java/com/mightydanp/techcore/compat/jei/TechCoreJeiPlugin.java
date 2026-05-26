package com.mightydanp.techcore.compat.jei;

import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import com.mightydanp.techcore.materials.Item.GemItem;
import com.mightydanp.techcore.materials.Item.OreItem;
import com.mightydanp.techcore.materials.Material;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

@JeiPlugin
public class TechCoreJeiPlugin implements IModPlugin {
    private static final IIngredientSubtypeInterpreter<ItemStack> GEM_SUBTYPE = (stack, context) -> {
        if (!(stack.getItem() instanceof GemItem gemItem)) return IIngredientSubtypeInterpreter.NONE;

        return "quality=" + gemItem.getGemQuality(stack);
    };

    private static final IIngredientSubtypeInterpreter<ItemStack> ORE_SUBTYPE = (stack, context) -> {
        if (!(stack.getItem() instanceof OreItem oreItem)) return IIngredientSubtypeInterpreter.NONE;

        String quantity = Float.toString(oreItem.getQuantityLevel(stack));
        String purity = Float.toString(oreItem.getPurityLevel(stack));

        return "quantity=" + quantity + "|purity=" + purity;
    };

    private static final IIngredientSubtypeInterpreter<ItemStack> DUST_SUBTYPE = (stack, context) -> {
        if (!(stack.getItem() instanceof DustItem dustItem)) return IIngredientSubtypeInterpreter.NONE;

        String quantity = Float.toString(dustItem.getQuantityLevel(stack));
        String purity = Float.toString(dustItem.getPurityLevel(stack));

        return "quantity=" + quantity + "|purity=" + purity;
    };

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        Set<Item> gemItems = new LinkedHashSet<>();
        Set<Item> oreItems = new LinkedHashSet<>();
        Set<Item> dustItems = new LinkedHashSet<>();

        for (Material material : RegistriesHandler.getMaterials()) {
            addItem(gemItems, material.ore.gem);

            for (Supplier<Item> oreItem : material.ore.rawOreItems.values()) {
                addItem(oreItems, oreItem);
            }

            addItem(dustItems, material.processed.dust);

            for (Supplier<Item> dustItem : material.processed.dustItems.values()) {
                addItem(dustItems, dustItem);
            }
        }

        for (Item item : gemItems) {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, GEM_SUBTYPE);
        }

        for (Item item : oreItems) {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, ORE_SUBTYPE);
        }

        for (Item item : dustItems) {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, DUST_SUBTYPE);
        }
    }

    private static void addItem(Set<Item> items, Supplier<Item> supplier) {
        if (supplier != null) {
            items.add(supplier.get());
        }
    }
}
