package net.oldhaven.mixins.shader.glsl;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Minecraft.class)
public class MixinMinecraft_CreateShaders {
    /*@Shadow public int displayWidth;
    @Shadow public int displayHeight;

    @Redirect(method = "run", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;update()V", ordinal = 0))
    private void updateDisplay1() {
        Shader.updateDisplay(MegaMod.getMinecraftInstance());
    }

    @Redirect(method = "run", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;update()V", ordinal = 1))
    private void updateDisplay2() {
        Shader.updateDisplay(MegaMod.getMinecraftInstance());
    }

    @Redirect(method = "toggleFullscreen", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;update()V"))
    private void updateDisplayFullscreen() {
        Shader.updateDisplay(MegaMod.getMinecraftInstance());
    }

    @Inject(method = "resize", at=@At("RETURN"))
    private void resize(int i, int i1, CallbackInfo ci) {
        Shader.setUpBuffers(MegaMod.getMinecraftInstance());
    }

    @Inject(method = "startGame", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V", shift = At.Shift.AFTER, ordinal = 0))
    private void createShaders1(CallbackInfo ci) {
        Shader.setUpBuffers(MegaMod.getMinecraftInstance());
    }

    @Inject(method = "startGame", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V", shift = At.Shift.AFTER, ordinal = 1))
    private void createShaders2(CallbackInfo ci) {
        Shader.setUpBuffers(MegaMod.getMinecraftInstance());
    }

    @Inject(method = "startGame", at=@At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;effectRenderer:Lnet/minecraft/src/EffectRenderer;", shift = At.Shift.BEFORE))
    private void createShaders3(CallbackInfo ci) {
        Shader.viewport(0, 0, this.displayWidth, this.displayHeight);
    }

    @Inject(method = "loadScreen", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V"))
    private void shaderLoad(CallbackInfo ci) {
        Shader.viewport(0, 0, this.displayWidth, this.displayHeight);
    }*/
}
