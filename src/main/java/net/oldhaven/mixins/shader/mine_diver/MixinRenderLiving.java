package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.src.RenderLiving;
import net.mine_diver.glsl.Shaders;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLiving.class)
public class MixinRenderLiving {

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V"))
    private void onGlEnable(int i) {
        if(!Shaders.isShaderEnabled()) {
            GL11.glEnable(i);
            return;
        }
        Shaders.glEnableWrapper(i);
    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
    private void onGlDisable(int i) {
        if(!Shaders.isShaderEnabled()) {
            GL11.glDisable(i);
            return;
        }
        Shaders.glDisableWrapper(i);
    }
}
