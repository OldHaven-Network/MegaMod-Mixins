package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.src.RenderLiving;
import net.mine_diver.glsl.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLiving.class)
public class MixinRenderLiving {

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V"))
    private void onGlEnable(int i) {
        Shaders.glEnableWrapper(i);
    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
    private void onGlDisable(int i) {
        Shaders.glDisableWrapper(i);
    }
}
