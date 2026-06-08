package com.mightydanp.techcore.network;

import com.mightydanp.techcore.client.ref.CoreRef;
import com.mightydanp.techcore.network.protocol.game.ClientBoundSplitQuantityResultPacket;
import com.mightydanp.techcore.network.protocol.game.ServerBoundSplitQuantityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class TCNetworkChannel {
    public static SimpleChannel INSTANCE;

    private static boolean initialized = false;
    private static int packetID = 0;
    private static final String PROTOCOL_VERSION = CoreRef.MOD_ID + "_" + "network_channel";


    public static void init() {
        if (initialized) return;
        initialized = true;

        INSTANCE = NetworkRegistry.newSimpleChannel(
                ResourceLocation.fromNamespaceAndPath(CoreRef.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.registerMessage(
                nextId(),
                ServerBoundSplitQuantityPacket.class,
                ServerBoundSplitQuantityPacket::encode,
                ServerBoundSplitQuantityPacket::decode,
                ServerBoundSplitQuantityPacket::handle
        );

        INSTANCE.registerMessage(
                nextId(),
                ClientBoundSplitQuantityResultPacket.class,
                ClientBoundSplitQuantityResultPacket::encode,
                ClientBoundSplitQuantityResultPacket::decode,
                ClientBoundSplitQuantityResultPacket::handle
        );


    }

    private static int nextId() { return packetID++; }


}