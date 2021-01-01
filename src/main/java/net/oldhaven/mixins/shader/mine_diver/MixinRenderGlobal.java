package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.src.RenderGlobal;
import net.minecraft.src.World;
import net.mine_diver.glsl.Shaders;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Redirect(method = "renderSky(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getStarBrightness(F)F"))
    private float onGetStarBrightness(World world, float f) {
        if(!Shaders.isShaderEnabled())
            return world.getStarBrightness(f);
        float ret = world.getStarBrightness(f);
        Shaders.setCelestialPosition();
        return ret;
    }

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
