package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.src.GLAllocation;
import net.minecraft.src.Tessellator;
import net.mine_diver.glsl.Shaders;
import net.mine_diver.glsl.util.TessellatorShaders;
import org.lwjgl.opengl.ARBVertexProgram;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

@Mixin(Tessellator.class)
public class MixinTessellator implements TessellatorShaders {
    @Shadow private int drawMode;
    @Shadow private static boolean convertQuadsToTriangles;
    @Shadow private int addedVertices;
    @Shadow private boolean hasNormals;
    @Shadow private int[] rawBuffer;
    @Shadow private int rawBufferIndex;

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    private void onCor(int var1, CallbackInfo ci) {
        shadersData = new short[] {-1, 0};
        shadersBuffer = GLAllocation.createDirectByteBuffer(var1 / 8 * 4);
        shadersShortBuffer = shadersBuffer.asShortBuffer();
    }

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V"))
    private void onDraw(int mode, int first, int vertexCount) {
        if(!Shaders.isShaderEnabled()) {
            GL11.glDrawArrays(mode, first, vertexCount);
            return;
        }
        if (Shaders.entityAttrib >= 0) {
            ARBVertexProgram.glEnableVertexAttribArrayARB(Shaders.entityAttrib);
            ARBVertexProgram.glVertexAttribPointerARB(Shaders.entityAttrib, 2, false, false, 4, (ShortBuffer)((Buffer)shadersShortBuffer).position(0));
        }
        GL11.glDrawArrays(mode, first, vertexCount);
        if (Shaders.entityAttrib >= 0)
            ARBVertexProgram.glDisableVertexAttribArrayARB(Shaders.entityAttrib);
    }

    @Inject(method = "reset", at = @At(value = "RETURN"))
    private void onReset(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        ((Buffer)shadersBuffer).clear();
    }

    @Inject(method = "addVertex(DDD)V", at = @At(value = "HEAD"))
    private void onAddVertex(CallbackInfo ci) {
        if(!Shaders.isShaderEnabled())
            return;
        if (drawMode == 7 && convertQuadsToTriangles && (addedVertices + 1) % 4 == 0 && hasNormals) {
            rawBuffer[rawBufferIndex + 6] = rawBuffer[(rawBufferIndex - 24) + 6];
            shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
            rawBuffer[rawBufferIndex + 8 + 6] = rawBuffer[(rawBufferIndex + 8 - 16) + 6];
        }
        shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
    }

    @Override
    public void setEntity(int id) {
        if(!Shaders.isShaderEnabled())
            return;
        shadersData[0] = (short) id;
    }

    public ByteBuffer shadersBuffer;
    public ShortBuffer shadersShortBuffer;
    public short[] shadersData;
}
