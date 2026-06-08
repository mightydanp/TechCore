package com.mightydanp.techcore;

import com.mightydanp.techcore.client.config.ConfigRegistries;
import com.mightydanp.techcore.api.registries.RegistriesHandler;
import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.registries.Registries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(CoreRef.MOD_ID)
public class TechCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public TechCore(@NotNull FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        RegistriesHandler.init(modEventBus);

        ConfigRegistries.registerConfigs(context);

        Registries.init();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
