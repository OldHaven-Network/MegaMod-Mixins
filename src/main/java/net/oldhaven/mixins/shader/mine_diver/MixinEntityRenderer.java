package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.RenderGlobal;
import net.mine_diver.glsl.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow protected abstract void func_4135_b(float v, int i);

    @Shadow protected abstract void renderRainSnow(float v);

    @Shadow protected abstract void setupCameraTransform(float v, int i);

    @Inject(method = "renderWorld(FJ)V", at = @At("HEAD"))
    private void beginRender(float var1, long var2, CallbackInfo ci) {
        Shaders.beginRender(mc, var1, var2);
    }

    @Inject(method = "renderWorld(FJ)V", at = @At("RETURN"))
    private void endRender(CallbackInfo ci) {
        Shaders.endRender();
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;setupCameraTransform(FI)V"))
    private void setClearColor(EntityRenderer entityRenderer, float var1, int var18) {
        Shaders.setClearColor(fogColorRed, fogColorGreen, fogColorBlue);
        setupCameraTransform(var1, var18);
        Shaders.setCamera(var1);
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;sortAndRender(Lnet/minecraft/src/EntityLiving;ID)I"))
    private int beginTerrain(RenderGlobal renderGlobal, EntityLiving var4, int i, double var1) {
        int ret;
        if (i == 0) {
            Shaders.beginTerrain();
            ret = renderGlobal.sortAndRender(var4, i, var1);
            Shaders.endTerrain();
        } else if (i == 1) {
            Shaders.beginWater();
            ret = renderGlobal.sortAndRender(var4, i, var1);
            Shaders.endWater();
        } else
            ret = renderGlobal.sortAndRender(var4, i, var1);
        return ret;
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;renderAllRenderLists(ID)V"))
    private void beginWater(RenderGlobal renderGlobal, int i, double var1) {
        Shaders.beginWater();
        renderGlobal.renderAllRenderLists(i, var1);
        Shaders.endWater();
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;renderRainSnow(F)V"))
    private void beginWeather(EntityRenderer entityRenderer, float var1) {
        Shaders.beginWeather();
        renderRainSnow(var1);
        Shaders.endWeather();
    }

    @Redirect(method = "renderWorld(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;func_4135_b(FI)V"))
    private void beginHand(EntityRenderer entityRenderer, float var1, int var18) {
        Shaders.beginHand();
        func_4135_b(var1, var18);
        Shaders.endHand();
    }

    @Shadow
    private Minecraft mc;
    @Shadow
    float
            fogColorRed,
            fogColorGreen,
            fogColorBlue;
}
