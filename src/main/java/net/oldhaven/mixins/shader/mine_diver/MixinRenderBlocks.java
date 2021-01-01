package net.oldhaven.mixins.shader.mine_diver;

import net.mine_diver.glsl.Shaders;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBlocks.class)
public class MixinRenderBlocks {
    @Inject(method = "renderBottomFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderBottomFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
    }

    @Inject(method = "renderTopFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderTopFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
    }

    @Inject(method = "renderEastFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderEastFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
    }

    @Inject(method = "renderWestFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderWestFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
    }

    @Inject(method = "renderNorthFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderNorthFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
    }

    @Inject(method = "renderSouthFace(Lnet/minecraft/src/Block;DDDI)V", at = @At("HEAD"))
    private void onRenderSouthFace(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
    }
}
