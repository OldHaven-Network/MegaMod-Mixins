package net.oldhaven.customs;

import net.minecraft.src.PositionTextureVertex;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TexturedQuad;
import net.minecraft.src.Vec3D;

public class CustomTexturedQuad extends TexturedQuad {
    private final boolean invertNormal = false;

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

    public void flipFace()
    {
        PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[vertexPositions.length];
        for(int i = 0; i < vertexPositions.length; i++)
        {
            apositiontexturevertex[i] = vertexPositions[vertexPositions.length - i - 1];
        }

        vertexPositions = apositiontexturevertex;
    }

    public void draw(Tessellator tessellator, float f)
    {
        Vec3D vec3d = vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
        Vec3D vec3d1 = vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
        Vec3D vec3d2 = vec3d1.crossProduct(vec3d).normalize();
        tessellator.startDrawingQuads();
        if(invertNormal)
        {
            tessellator.setNormal(-(float)vec3d2.xCoord, -(float)vec3d2.yCoord, -(float)vec3d2.zCoord);
        } else
        {
            tessellator.setNormal((float)vec3d2.xCoord, (float)vec3d2.yCoord, (float)vec3d2.zCoord);
        }
        for(int i = 0; i < 4; i++)
        {
            PositionTextureVertex positiontexturevertex = vertexPositions[i];
            tessellator.addVertexWithUV((float)positiontexturevertex.vector3D.xCoord * f, (float)positiontexturevertex.vector3D.yCoord * f, (float)positiontexturevertex.vector3D.zCoord * f, positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY);
        }

        tessellator.draw();
    }
}
