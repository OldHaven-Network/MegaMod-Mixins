package net.oldhaven.mixins.entity;

import net.minecraft.src.InventoryPlayer;
import net.oldhaven.customs.util.MMUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public class MixinInventoryPlayer {
    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    private void changeCurrentItem(int i, CallbackInfo ci) {
        if(MMUtil.isZooming)
            ci.cancel();
    }
}
