package net.oldhaven.mixins.shader.glsl;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderLiving;
import net.oldhaven.customs.shaders.Shader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLiving.class)
public class MixinRenderLiving_ShaderUse {
    @Inject(method = "doRenderLiving", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V", shift = At.Shift.AFTER))
    private void doRenderShader1(EntityLiving entityLiving, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
        Shader.useProgram(Shader.baseProgramNoT2D);
    }

    @Inject(method = "doRenderLiving", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDepthFunc(I)V", shift = At.Shift.AFTER, ordinal = 0))
    private void doRenderShader2(EntityLiving entityLiving, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
        //Shader.useProgram(Shader.baseProgram);
    }

    @Inject(method = "doRenderLiving", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDepthFunc(I)V", shift = At.Shift.AFTER, ordinal = 1))
    private void doRenderShader3(EntityLiving entityLiving, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
        Shader.useProgram(Shader.baseProgram);
    }
}
