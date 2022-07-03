package me.limelier.sterilize.mixin;

import net.minecraft.block.VineBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VineBlock.class)
public class VineBlockMixin {
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void randomTick(CallbackInfo info) {
        info.cancel();
    }
}
