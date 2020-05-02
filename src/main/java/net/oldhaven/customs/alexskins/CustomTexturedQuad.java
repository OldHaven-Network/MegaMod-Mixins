package net.oldhaven.customs.alexskins;

import net.minecraft.src.PositionTextureVertex;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TexturedQuad;
import net.minecraft.src.Vec3D;

public class CustomTexturedQuad extends TexturedQuad {
    public CustomTexturedQuad(PositionTextureVertex[] var1) {
        super(var1);
    }
    public CustomTexturedQuad(PositionTextureVertex[] var1, int var2, int var3, int var4, int var5, int textureWidth, int textureHeight) {
        this(var1);
        float var6 = 0.0F / textureWidth;
        float var7 = 0.0F / textureHeight;
        var1[0] = var1[0].setTexturePosition((float)var4 / textureWidth - var6, (float)var3 / textureHeight + var7);
        var1[1] = var1[1].setTexturePosition((float)var2 / textureWidth + var6, (float)var3 / textureHeight + var7);
        var1[2] = var1[2].setTexturePosition((float)var2 / textureWidth + var6, (float)var5 / textureHeight - var7);
        var1[3] = var1[3].setTexturePosition((float)var4 / textureWidth - var6, (float)var5 / textureHeight - var7);
    }
}
