package net.oldhaven.mixins.entity;

import net.minecraft.src.PositionTextureVertex;
import net.minecraft.src.TexturedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TexturedQuad.class)
public class MixinTexturedQuad {
    @Inject(method = "<init>([Lnet/minecraft/src/PositionTextureVertex;IIII)V", at = @At("RETURN"))
    private void texturedVertex(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, CallbackInfo ci) {
        /*float f = 0.0F;
        float f1 = 0.0F;
        int textureWidth = 64;
        int textureHeight = 64;
        vertices[0] = vertices[0].setTexturePosition((float)texcoordU2 / textureWidth - f, (float)texcoordV1 / textureHeight + f1);
        vertices[1] = vertices[1].setTexturePosition((float)texcoordU1 / textureWidth + f, (float)texcoordV1 / textureHeight + f1);
        vertices[2] = vertices[2].setTexturePosition((float)texcoordU1 / textureWidth + f, (float)texcoordV2 / textureHeight - f1);
        vertices[3] = vertices[3].setTexturePosition((float)texcoordU2 / textureWidth - f, (float)texcoordV2 / textureHeight - f1);*/
    }
}
