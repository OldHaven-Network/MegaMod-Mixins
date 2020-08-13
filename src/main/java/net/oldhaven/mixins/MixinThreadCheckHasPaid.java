package net.oldhaven.mixins;

import net.minecraft.src.ThreadCheckHasPaid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadCheckHasPaid.class)
public class MixinThreadCheckHasPaid {
    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false)
    private void run(CallbackInfo info) {
        info.cancel();
    }
}
