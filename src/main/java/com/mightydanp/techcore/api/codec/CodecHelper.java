package com.mightydanp.techcore.api.codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CodecHelper {
    public static <E extends Enum<E>> StreamCodec<FriendlyByteBuf, E> enumCodec(Class<E> enumClass) {
        return new StreamCodec<>() {
            @Override
            public E decode(FriendlyByteBuf buf) {
                return buf.readEnum(enumClass);
            }

            @Override
            public void encode(FriendlyByteBuf buf, E value) {
                buf.writeEnum(value);
            }
        };
    }
}
