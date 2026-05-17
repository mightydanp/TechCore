package com.mightydanp.techcore.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraftforge.client.model.pipeline.VertexConsumerWrapper;
import org.jetbrains.annotations.NotNull;

public final class TemperatureVertexConsumer extends VertexConsumerWrapper {
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public TemperatureVertexConsumer(VertexConsumer parent, int rgb, float strength) {
        super(parent);
        this.r = (rgb >> 16) & 0xFF;
        this.g = (rgb >> 8) & 0xFF;
        this.b = rgb & 0xFF;
        this.a = (int) (Mth.clamp(strength, 0.0F, 1.0F) * 255.0F);
    }

    @Override
    public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
        return super.color(this.r, this.g, this.b, this.a);
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        super.defaultColor(this.r, this.g, this.b, this.a);
    }
}
