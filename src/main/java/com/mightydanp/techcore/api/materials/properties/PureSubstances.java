package com.mightydanp.techcore.api.materials.properties;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum PureSubstances {
    COMPOUND("compound"),
    ELEMENT("element");

    public final String name;

    PureSubstances(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Codec using getName()
    public static final Codec<PureSubstances> CODEC = Codec.STRING.xmap(
            str -> switch (str.toLowerCase()) {
                case "compound" -> COMPOUND;
                case "element" -> ELEMENT;
                default -> throw new IllegalArgumentException("Unknown substance: " + str);
            },
            PureSubstances::getName // Use getName instead of toString
    );

    public static final StreamCodec<FriendlyByteBuf, PureSubstances> STREAM_CODEC = StreamCodec.of(
            (buffer, substance) -> buffer.writeUtf(substance.getName()), // Encoding: Write the name to the buffer
            buffer -> {
                String name = buffer.readUtf(); // Decoding: Read the name from the buffer
                return switch (name.toLowerCase()) { // Map the name to the corresponding enum
                    case "compound" -> COMPOUND;
                    case "element" -> ELEMENT;
                    default -> throw new IllegalArgumentException("Unknown substance: " + name);
                };
            }
    );
}
