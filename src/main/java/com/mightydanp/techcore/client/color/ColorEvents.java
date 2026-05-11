package com.mightydanp.techcore.client.color;

import com.mightydanp.techcore.client.config.ClientConfig;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.materials.Item.MaterialItem;
import com.mightydanp.techcore.materials.properties.Temperature;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CoreRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorEvents {

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ClientConfig.registerVanillaDefaults();
        ClientConfig.registerTechCoreDefaults();

        Map<ResourceLocation, BakedModel> models = event.getModels();
        for (Item item : ClientConfig.getRegistered()) {
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
            if (rl == null) continue;
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
            BakedModel model = models.get(mrl);
            if (model != null && !(model instanceof TemperatureGlowBakedModel)) {
                models.put(mrl, new TemperatureGlowBakedModel(model));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        ClientConfig.registerVanillaDefaults();
        ClientConfig.registerTechCoreDefaults();

        ItemColors itemColors = event.getItemColors();

        for (Item item : ClientConfig.getRegistered()) {
            ItemColor existingHandler = captureExistingHandler(itemColors, item);
            event.register((stack, tintIndex) -> {
                Item i = stack.getItem();

                if (tintIndex != ClientConfig.TEMPERATURE_TINT_INDEX) {
                    return existingHandler != null ? existingHandler.getColor(stack, tintIndex) : -1;
                }

                boolean shouldGlow = ClientConfig.TEMPERATURE_ALL_ITEMS.get() ? !ClientConfig.isBlacklisted(i) : ClientConfig.isRegistered(i);

                if (!shouldGlow) return existingHandler != null ? existingHandler.getColor(stack, 0) : -1;

                if (!Temperature.hasTemperature(stack)) return existingHandler != null ? existingHandler.getColor(stack, 0) : -1;

                Double temperature = Temperature.getTemperature(stack);

                if (temperature == null) return existingHandler != null ? existingHandler.getColor(stack, 0) : -1;

                if(item instanceof MaterialItem){
                    Item item2 = item;
                }

                int[] rgb = new Temperature(temperature, Temperature.getScale()).getRGBColor();
                return (0xFF << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
            }, item);
        }
    }

    private static ItemColor captureExistingHandler(ItemColors colors, Item item) {
        try {
            Field f = ItemColors.class.getDeclaredField("itemColors");
            f.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<Object, ItemColor> map = (java.util.Map<Object, ItemColor>) f.get(colors);
            return map.get(ForgeRegistries.ITEMS.getDelegateOrThrow(item));
        } catch (Exception e) {
            return null;
        }
    }
}
