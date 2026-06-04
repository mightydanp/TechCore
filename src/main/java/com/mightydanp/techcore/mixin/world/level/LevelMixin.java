package com.mightydanp.techcore.mixin.world.level;

import com.mightydanp.techcore.world.level.WasGenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("RETURN"))
    private void techcore$markChangedAfterGeneration(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level) (Object) this;

        if (cir.getReturnValueZ() && level instanceof ServerLevel serverLevel) {
            WasGenerated.markChanged(serverLevel, pos);
        }
    }
}
