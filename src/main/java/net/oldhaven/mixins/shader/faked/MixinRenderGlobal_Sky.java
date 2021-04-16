package net.oldhaven.mixins.shader.faked;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal_Sky {
    @Shadow private Minecraft mc;
    @Shadow private World worldObj;
    @Shadow private RenderEngine renderEngine;

    @Inject(method = "renderSky", at=@At("HEAD"))
    private void renderSky(float v, CallbackInfo ci) {
        //renderSkyBox(v);
    }

    @Inject(method = "renderSky", at=@At(value = "INVOKE", target = "Lnet/minecraft/src/World;getStarBrightness(F)F", shift = At.Shift.BEFORE))
    private void renderShaderSky(float v, CallbackInfo ci) {
        if (((int) ModOptions.SHADERS.getAsFloat() * 4) > 0)
            return;
    }

    public void renderSkyBox(float f) {
        GL11.glDisable(3553);
        Vec3D vec3d = worldObj.func_4079_a(mc.renderViewEntity, f);
        float f1 = (float)vec3d.xCoord;
        float f2 = (float)vec3d.yCoord;
        float f3 = (float)vec3d.zCoord;
        if(mc.gameSettings.anaglyph)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f7 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f7;
        }
        GL11.glColor3f(f1, f2, f3);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(2912);
        GL11.glColor3f(f1, f2, f3);
        GL11.glDisable(2912);
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(3553);
        GL11.glBlendFunc(770, 1);
        GL11.glPushMatrix();
        float f6 = 1.0F - worldObj.func_27162_g(f);
        float f9 = 0.0F;
        float f11 = 0.0F;
        float f13 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
        GL11.glTranslatef(f9, f11, f13);
        GL11.glRotatef(0F, 0F, 0F, 1F);
        float f15 = 100F;
        GL11.glRotatef(90F, 1F, 0F, 0F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky3.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glRotatef(-90F, 0F, 0F, 1F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky1.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glRotatef(180F, 0F, 0F, 1F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky4.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glRotatef(90F, 0F, 0F, 1F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky5.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glRotatef(-180F, 0F, 0F, 1F);
        GL11.glRotatef(-90F, 1F, 0F, 0F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky2.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glRotatef(-180F, 1F, 0F, 0F);
        GL11.glBindTexture(3553, renderEngine.getTexture("/net/mine_diver/skyboxapi/resources/sky6.png"));
        drawSkyboxQuads(tessellator, f15);
        GL11.glDisable(3553);
        GL11.glPopMatrix();
    }

    private void drawSkyboxQuads(Tessellator tessellator, float f15) {
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f15, 100D, f15, 0.0D, 1.0D);
        tessellator.addVertexWithUV(-f15, 100D, f15, 1.0D, 1.0D);
        tessellator.addVertexWithUV(-f15, 100D, -f15, 1.0D, 0.0D);
        tessellator.addVertexWithUV(f15, 100D, -f15, 0.0D, 0.0D);
        tessellator.draw();
    }
}
