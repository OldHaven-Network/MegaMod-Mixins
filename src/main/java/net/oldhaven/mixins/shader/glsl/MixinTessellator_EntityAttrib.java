package net.oldhaven.mixins.shader.glsl;

import net.minecraft.src.Tessellator;
import net.oldhaven.customs.shaders.Shader;
import org.lwjgl.opengl.ARBVertexProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tessellator.class)
public class MixinTessellator_EntityAttrib {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void tessShader10(CallbackInfo ci) {
        if (Shader.entityAttrib >= 0) {
            ARBVertexProgram.glEnableVertexAttribArrayARB(Shader.entityAttrib);
            ARBVertexProgram.glVertexAttribPointerARB(Shader.entityAttrib, 2, false, false, 4, Shader.shadersShortBuffer.position(0));
        }
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V", ordinal = 0, shift = At.Shift.AFTER))
    private void tessShader11(CallbackInfo ci) {
        if (Shader.entityAttrib >= 0) {
            ARBVertexProgram.glDisableVertexAttribArrayARB(Shader.entityAttrib);
        }
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void tessShader20(CallbackInfo ci) {
        if (Shader.entityAttrib >= 0) {
            ARBVertexProgram.glEnableVertexAttribArrayARB(Shader.entityAttrib);
            ARBVertexProgram.glVertexAttribPointerARB(Shader.entityAttrib, 2, false, false, 4, Shader.shadersShortBuffer.position(0));
        }
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V", ordinal = 1, shift = At.Shift.AFTER))
    private void tessShader21(CallbackInfo ci) {
        if (Shader.entityAttrib >= 0) {
            ARBVertexProgram.glDisableVertexAttribArrayARB(Shader.entityAttrib);
        }
    }
}
