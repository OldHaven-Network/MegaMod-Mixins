package net.oldhaven.mixins.gui;

import net.minecraft.src.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    /*@Inject(method = "drawRect", at = @At("HEAD"))
    private void drawRect(int i, int i1, int i2, int i3, int i4, CallbackInfo ci) {

    }

    @Inject(method = "drawTexturedModalRect", at = @At("HEAD"))
    private void drawTexturedModalRect(int i, int i1, int i2, int i3, int i4, int i5, CallbackInfo ci) {
        System.out.println("draw rect");
    }*/
}
