package net.oldhaven.mixins;

import net.minecraft.src.FontRenderer;
import net.oldhaven.customs.IFontRenderer;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements IFontRenderer {
    @Shadow public abstract void drawString(String s, int i, int i1, int i2);

    @Inject(method = "drawStringWithShadow", at = @At(value = "HEAD"), cancellable = true)
    private void drawStrWithShadow(String s, int i, int i1, int i2, CallbackInfo ci) {
        if(!ModOptions.BUTTON_TEXT_SHADOW.getAsBool()) {
            this.drawString(s, i, i1, i2);
            ci.cancel();
        }
    }
}
