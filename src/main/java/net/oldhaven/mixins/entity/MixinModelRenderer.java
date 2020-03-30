package net.oldhaven.mixins.entity;

import net.minecraft.src.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelRenderer.class)
public class MixinModelRenderer {
    @Shadow private int textureOffsetX;
    @Shadow private int textureOffsetY;

    @Redirect(method = "<init>", at = @At("HEAD"))
    private void init(int f, int f2) {
        this.textureOffsetX = 64;
        this.textureOffsetY = 64;
    }
}
