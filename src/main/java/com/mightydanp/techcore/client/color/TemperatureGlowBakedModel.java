package com.mightydanp.techcore.client.color;

import com.mightydanp.techcore.client.config.ClientConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class TemperatureGlowBakedModel extends BakedModelWrapper<BakedModel> {

    private final IdentityHashMap<BakedModel, TemperatureGlowBakedModel> overrideCache = new IdentityHashMap<>();
    private ItemOverrides wrappingOverrides = null;

    public TemperatureGlowBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        ItemOverrides original = originalModel.getOverrides();
        if (original == ItemOverrides.EMPTY) return ItemOverrides.EMPTY;
        if (wrappingOverrides == null) {
            wrappingOverrides = new ItemOverrides() {
                @Override
                public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack,
                                          @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
                    BakedModel resolved = original.resolve(originalModel, stack, level, entity, seed);
                    if (resolved == null || resolved instanceof TemperatureGlowBakedModel) return resolved;
                    return overrideCache.computeIfAbsent(resolved, TemperatureGlowBakedModel::new);
                }
            };
        }
        return wrappingOverrides;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext cameraTransformType, @NotNull PoseStack poseStack, boolean applyLeftHandTransform) {
        originalModel.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    public @NotNull List<BakedModel> getRenderPasses(@NotNull ItemStack itemStack, boolean fabulous) {
        return List.of(this);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return retint(super.getQuads(state, side, rand));
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        return retint(super.getQuads(state, side, rand, extraData, renderType));
    }

    private List<BakedQuad> retint(List<BakedQuad> original) {
        List<BakedQuad> result = new ArrayList<>(original.size());
        for (BakedQuad quad : original) {
            if (quad.getTintIndex() == -1 || quad.getTintIndex() == 0) {
                result.add(new BakedQuad(quad.getVertices(), ClientConfig.TEMPERATURE_TINT_INDEX, quad.getDirection(), quad.getSprite(), quad.isShade(), quad.hasAmbientOcclusion()));
            } else {
                result.add(quad);
            }
        }
        return result;
    }
}
