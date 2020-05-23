package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.packets.CustomPackets;
import net.oldhaven.gui.GuiYesNo;
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

    private GuiButton MM_CL;
    private boolean aetherEnabled = false;

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    private void initGuiFirst(CallbackInfo ci) {
        if(MegaMod.hasUpdated) {
            mc.displayGuiScreen(new GuiYesNo(
                () -> { /* yes */
                    MegaMod.hasUpdated = false;
                    mc.displayGuiScreen(new GuiChangelog(new GuiMainMenu()));
                }, () -> {
                    MegaMod.hasUpdated = false;
                    mc.displayGuiScreen(new GuiMainMenu());
                }, "MegaMod has updated", "Would you like to view the changelog?"
            ));
            ci.cancel();
            return;
        }
        mc.hideQuitButton = ModOptions.SHOW_MAIN_MENU_QUIT_BUTTON.getAsInt() != 1;
    }

    @Inject(method = "initGui", at = @At("RETURN"), cancellable = true)
    private void initGui(CallbackInfo ci) {
        field_35358_g = mc.renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(256, 256, 2));
        CustomPackets.setUsePackets(false);
        MegaMod.getInstance().hasLoggedIn = false;
        MegaMod.getInstance().clearJoinedNames();
        MegaMod.getInstance().setConnectedServer(null);
        MegaMod.getInstance().modLoaderTest();
        MegaMod.getServerPacketInformation().reset();
        for(Object o : controlList) {
            GuiButton guiButton = (GuiButton) o;
            //System.out.println(guiButton.displayString + ", x" + guiButton.xPosition + " y" + guiButton.yPosition);
            if (guiButton.displayString.equals("W") || guiButton.displayString.equals("T")) {
                aetherEnabled = true;
                break;
            }
        }
        int w = width-(35+2);
        if(aetherEnabled)
            w = 403 - 24 - (35+2);
        controlList.add(MM_CL=new GuiButton(26, w, 4, 35, 20, "MM-CL"));
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

    private void func_35355_b(int i, int j, float f) {
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

    private void func_35356_c(int i, int j, float f) {
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

    private boolean isMouseOver(int i, int j, int xPosition, int yPosition, int width, int height) {
        return i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", shift = At.Shift.AFTER))
    private void drawScreen(int i, int j, float f, CallbackInfo ci) {
        if(isMouseOver(i, j, MM_CL.xPosition, MM_CL.yPosition, 35, 20)) {
            if(aetherEnabled)
                this.drawCenteredString(this.fontRenderer, "MegaMod ChangeLog", MM_CL.xPosition + (35/2), MM_CL.yPosition + 22+2, 0xffffff);
            else
                this.drawCenteredString(this.fontRenderer, "MegaMod ChangeLog", MM_CL.xPosition-15, MM_CL.yPosition + 22+2, 0xffffff);
        }
        if(MegaMod.requiresUpdate != null) {
            this.drawString(fontRenderer, "UPDATE FROM " + MegaMod.version + " TO " + MegaMod.requiresUpdate, 2, height - 10, 0xff7575);
            this.drawString(fontRenderer, "MegaMod v"+ MegaMod.version + "-Mixins", 2, height - 20, 0xFFFFFF);
        } else
            this.drawString(fontRenderer, "MegaMod v"+ MegaMod.version + "-Mixins", 2, height - 10, 0xFFFFFF);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", ordinal = 0))
    public void drawMCString(GuiMainMenu mainMenu, FontRenderer fontRenderer, String s, int i, int i1, int i2) {
        int o = ModOptions.DEFAULT_MAIN_MENU_BG.getAsInt();
        if(o != 1) {
            mainMenu.drawString(fontRenderer, "Minecraft Beta 1.7.3", i, i1, 0xffffff);
        } else {
            mainMenu.drawString(fontRenderer, "Minecraft Beta 1.7.3", i, i1, i2);
        }
    }


    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiMainMenu;drawDefaultBackground()V"))
    public void drawDefaultBackground(GuiMainMenu mainMenu, int i, int j, float f) {
        int o = ModOptions.DEFAULT_MAIN_MENU_BG.getAsInt();
        if(MegaMod.getInstance().failedToDrawBG || o == 1) {
            if(MegaMod.getInstance().failedToDrawBG) {
                this.drawCenteredString(fontRenderer, "OpenGL errored drawing background", width/2, height/7, 0xf54242);
                this.drawCenteredString(fontRenderer, "Could be a possible incompatibility with OptiFine", width/2, height/7+25, 0xf54242);
            }
            this.drawDefaultBackground();
        } else {
            try {
                func_35356_c(i, j, f);
                drawGradientRect(0, 0, width, height, 0xaaffffff, 0xffffff);
                drawGradientRect(0, 0, width, height, 0, 0xaa000000);
            } catch(Exception openglException) {
                MegaMod.getInstance().failedToDrawBG = true;
            }
        }
    }

    private int field_35357_f;
    private int field_35358_g;

    //@Inject(method = "drawScreen", at = @At("RETURN"))
    //private void drawScreen(int i, int  j, float f, CallbackInfo ci) {
        //drawString(fontRenderer, "MegaMod v"+mc.mmVersion, 2, height - 20, FontRenderer.rainbowColor);
    //}
}
