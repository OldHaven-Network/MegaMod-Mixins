// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.itemkeep;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, GuiSmallButton, TexturePackList, 
//            GuiTexturePackSlot, GuiButton, RenderEngine, FontRenderer

public class GuiItemKeep extends GuiScreen
{
    public GuiItemKeep(GuiScreen guiscreen) {
        guiScreen = guiscreen;
    }

    public void initGui() {
        StringTranslate st = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height - 42, st.translateKey("gui.done")));
        mc.texturePackList.updateAvaliableTexturePacks();
        slotGui = new GuiItemKeepSlot(this);
        slotGui.registerScrollButtons(controlList, 7, 8);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled)
            return;
        if(guibutton.id == 6)
            mc.displayGuiScreen(guiScreen);
        else /*no point in this*/
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

    public void drawScreen(int i, int j, float f) {
        slotGui.drawScreen(i, j, f);
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.drawCenteredString(this.fontRenderer, "All Item IDs & Names", this.width / 2, 16, 16777215);
        super.drawScreen(i, j, f);
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public Minecraft getMinecraft(GuiItemKeep guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    protected GuiScreen guiScreen;
    private GuiItemKeepSlot slotGui;
}
