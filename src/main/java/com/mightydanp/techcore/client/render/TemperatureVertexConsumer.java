package com.mightydanp.techcore.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class TemperatureVertexConsumer implements VertexConsumer {
    private final VertexConsumer parent;
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public TemperatureVertexConsumer(VertexConsumer parent, int rgb, float strength) {
        this.parent = parent;
        this.r = (rgb >> 16) & 0xFF;
        this.g = (rgb >> 8) & 0xFF;
        this.b = rgb & 0xFF;
        this.a = (int) (Mth.clamp(strength, 0.0F, 1.0F) * 255.0F);
    }

    @Override
    public @NotNull VertexConsumer vertex(double x, double y, double z) {
        return this.parent.vertex(x, y, z);
    }

    @Override
    public @NotNull VertexConsumer vertex(@NotNull Matrix4f matrix, float x, float y, float z) {
        return this.parent.vertex(matrix, x, y, z);
    }

    @Override
    public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
        return this.parent.color(this.r, this.g, this.b, this.a);
    }

    @Override
    public @NotNull VertexConsumer uv(float u, float v) {
        return this.parent.uv(u, v);
    }

    @Override
    public @NotNull VertexConsumer overlayCoords(int u, int v) {
        return this.parent.overlayCoords(u, v);
    }

    @Override
    public @NotNull VertexConsumer uv2(int u, int v) {
        return this.parent.uv2(u, v);
    }

    @Override
    public @NotNull VertexConsumer normal(float x, float y, float z) {
        return this.parent.normal(x, y, z);
    }

    @Override
    public @NotNull VertexConsumer normal(@NotNull Matrix3f matrix, float x, float y, float z) {
        return this.parent.normal(matrix, x, y, z);
    }

    @Override
    public void endVertex() {
        this.parent.endVertex();
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        this.parent.defaultColor(this.r, this.g, this.b, this.a);
    }

    @Override
    public void unsetDefaultColor() {
        this.parent.unsetDefaultColor();
    }
}
