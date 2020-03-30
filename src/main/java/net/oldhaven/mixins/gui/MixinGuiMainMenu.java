package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.gui.changelog.GuiChangelog;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {
    @Shadow private String splashText;

    @Inject(method = "initGui", at = @At("HEAD"))
    private void initGuiFirst(CallbackInfo ci) {
        mc.hideQuitButton = MegaMod.getInstance().getCustomGameSettings().getOptionI("Show Main Menu Quit Btn") != 1;
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        controlList.add(new GuiButton(26, width-37, 3, 35, 20, "MM-CL"));
        field_35358_g = mc.renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(256, 256, 2));
        MegaMod.getInstance().setConnectedServer(null);
        MegaMod.getInstance().modLoaderTest();
    }

    @Inject(method = "updateScreen", at = @At("RETURN"))
    public void updateScreen(CallbackInfo ci) {
        field_35357_f++;
    }

    @Inject(method = "actionPerformed", at=@At("RETURN"))
    private void actionPerformed(GuiButton var1, CallbackInfo ci) {
        if(var1.id == 26) {
            mc.displayGuiScreen(new GuiChangelog(this));
        }
    }

    private void func_35355_b(int i, int j, float f)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120F, 1.0F, 0.05F, 10F);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        int k = 8;
        for(int l = 0; l < k * k; l++)
        {
            GL11.glPushMatrix();
            float f1 = ((float)(l % k) / (float)k - 0.5F) / 64F;
            float f2 = ((float)(l / k) / (float)k - 0.5F) / 64F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin(((float)field_35357_f + f) / 400F) * 25F + 20F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)field_35357_f + f) * 0.1F, 0.0F, 1.0F, 0.0F);
            for(int i1 = 0; i1 < 6; i1++)
            {
                GL11.glPushMatrix();
                if(i1 == 1)
                {
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                }
                if(i1 == 2)
                {
                    GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                }
                if(i1 == 3)
                {
                    GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                }
                if(i1 == 4)
                {
                    GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                }
                if(i1 == 5)
                {
                    GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                }
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture((new StringBuilder()).append("/title/bg/panorama").append(i1).append(".png").toString()));
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(0xffffff, 255 / (l + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1D, -1D, 1.0D, 0.0F + f4, 0.0F + f4);
                tessellator.addVertexWithUV(1.0D, -1D, 1.0D, 1.0F - f4, 0.0F + f4);
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f4, 1.0F - f4);
                tessellator.addVertexWithUV(-1D, 1.0D, 1.0D, 0.0F + f4, 1.0F - f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

    private void func_35354_a(float f)
    {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.field_35358_g);
        GL11.glCopyTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        byte byte0 = 3;
        for(int i = 0; i < byte0; i++)
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = width;
            int k = height;
            float f1 = (float)(i - byte0 / 2) / 256F;
            tessellator.addVertexWithUV(j, k, zLevel, 0.0F + f1, 0.0D);
            tessellator.addVertexWithUV(j, 0.0D, zLevel, 1.0F + f1, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + f1, 1.0D);
            tessellator.addVertexWithUV(0.0D, k, zLevel, 0.0F + f1, 1.0D);
        }

        tessellator.draw();
        GL11.glColorMask(true, true, true, true);
    }

    private void func_35356_c(int i, int j, float f)
    {
        GL11.glViewport(0, 0, 256, 256);
        func_35355_b(i, j, f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        func_35354_a(f);
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = width <= height ? 120F / (float)height : 120F / (float)width;
        float f2 = ((float)height * f1) / 256F;
        float f3 = ((float)width * f1) / 256F;
        GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
        GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = width;
        int l = height;
        tessellator.addVertexWithUV(0.0D, l, zLevel, 0.5F - f2, 0.5F + f3);
        tessellator.addVertexWithUV(k, l, zLevel, 0.5F - f2, 0.5F - f3);
        tessellator.addVertexWithUV(k, 0.0D, zLevel, 0.5F + f2, 0.5F - f3);
        tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + f2, 0.5F + f3);
        tessellator.draw();
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", shift = At.Shift.BEFORE))
    private void drawScreen(int i, int j, float f, CallbackInfo ci) {
        if(MegaMod.requiresUpdate != null) {
            this.drawString(fontRenderer, "UPDATE FROM " + MegaMod.version + " TO " + MegaMod.requiresUpdate, 2, height - 10, 0xff7575);
            this.drawString(fontRenderer, "MegaMod v"+ MegaMod.version + "-Mixins", 2, height - 20, 0xFFFFFF);
        } else
            this.drawString(fontRenderer, "MegaMod v"+ MegaMod.version + "-Mixins", 2, height - 10, 0xFFFFFF);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawDefaultBackground()V"))
    public void drawDefaultBackground(GuiMainMenu mainMenu, int i, int j, float f) {
        int o = MegaMod.getInstance().getCustomGameSettings().getOptionI("Default Main Menu BG");
        if(o == 1) {
            this.drawDefaultBackground();
        } else {
            func_35356_c(i, j, f);
            drawGradientRect(0, 0, width, height, 0xaaffffff, 0xffffff);
            drawGradientRect(0, 0, width, height, 0, 0xaa000000);
        }
    }

    private int field_35357_f;
    private int field_35358_g;

    //@Inject(method = "drawScreen", at = @At("RETURN"))
    //private void drawScreen(int i, int  j, float f, CallbackInfo ci) {
        //drawString(fontRenderer, "MegaMod v"+mc.mmVersion, 2, height - 20, FontRenderer.rainbowColor);
    //}
}
