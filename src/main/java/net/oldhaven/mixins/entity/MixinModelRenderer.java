package net.oldhaven.mixins.entity;

import net.minecraft.src.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelRenderer.class)
public class MixinModelRenderer {
    @Shadow private int textureOffsetX;
    @Shadow private int textureOffsetY;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(int f, int f2, CallbackInfo ci) {

    }
}
