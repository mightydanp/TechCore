package com.mightydanp.techcore.mixin.world.level;

import com.mightydanp.techcore.materials.block.OreBlock;
import com.mightydanp.techcore.world.level.WasGenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.Deque;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Unique
    private static final ThreadLocal<Deque<SetBlockTransition>> TECHCORE_TRANSITIONS = ThreadLocal.withInitial(ArrayDeque::new);

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("HEAD"))
    private void techcore$captureGeneratedTransition(BlockPos pos, BlockState state, int flags, int recursionLeft, @NotNull CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level) (Object) this;
        if (level instanceof ServerLevel) TECHCORE_TRANSITIONS.get().push(new SetBlockTransition(pos.immutable(), level.getBlockState(pos), state));
    }

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("RETURN"))
    private void techcore$markChangedAfterGeneration(BlockPos pos, BlockState state, int flags, int recursionLeft, @NotNull CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level) (Object) this;
        if (!(level instanceof ServerLevel serverLevel)) return;

        Deque<SetBlockTransition> transitions = TECHCORE_TRANSITIONS.get();
        SetBlockTransition transition = transitions.isEmpty() ? null : transitions.pop();
        if (transitions.isEmpty()) TECHCORE_TRANSITIONS.remove();

        if (!cir.getReturnValueZ() || transition == null) return;
        if (OreBlock.consumeSuppressedGeneratedChange(serverLevel, transition.pos(), transition.oldState(), transition.newState())) return;

        WasGenerated.markChanged(serverLevel, pos);
    }

    private record SetBlockTransition(BlockPos pos, BlockState oldState, BlockState newState) {
    }
}
