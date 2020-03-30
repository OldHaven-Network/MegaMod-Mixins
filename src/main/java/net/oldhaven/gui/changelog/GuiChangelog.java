// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.changelog;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.oldhaven.MegaMod;

import java.io.File;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, GuiSmallButton, TexturePackList, 
//            GuiTexturePackSlot, GuiButton, RenderEngine, FontRenderer

public class GuiChangelog extends GuiScreen
{
    public GuiChangelog(GuiScreen guiscreen)
    {
        refreshTimer = -1;
        fileLocation = "";
        guiScreen = guiscreen;
    }

    GuiButton done;
    public void initGui()
    {
        this.controlList.add(done=new GuiButton(6, this.width / 2 - 100, this.height - 38, "Done"));
        mc.texturePackList.updateAvaliableTexturePacks();
        fileLocation = (new File(Minecraft.getMinecraftDir(), "texturepacks")).getAbsolutePath();
        slotGui = new GuiChangelogSlot(this);
        slotGui.registerScrollButtons(controlList, 7, 8);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        else if(guibutton.id == 6) {
            mc.displayGuiScreen(guiScreen);
        } else
            slotGui.actionPerformed(guibutton);
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
    }

    protected void mouseMovedOrUp(int i, int j, int k)
    {
        super.mouseMovedOrUp(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        slotGui.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, "MegaMod Changelog", width / 2, 10, 0xffffff);
        drawCenteredString(fontRenderer, "Drag the screen up or down to scroll", width / 2, 20, 0x666666);
        super.drawScreen(i, j, f);
    }

    public void updateScreen()
    {
        super.updateScreen();
        refreshTimer--;
    }

    public Minecraft getMinecraft(GuiChangelog guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }

    protected GuiScreen guiScreen;
    private int refreshTimer;
    private String fileLocation;
    private GuiChangelogSlot slotGui;
}
