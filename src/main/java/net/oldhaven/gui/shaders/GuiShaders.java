// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.autologins.GuiAutoLoginsEdit;

import java.io.File;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, GuiSmallButton, TexturePackList, 
//            GuiTexturePackSlot, GuiButton, RenderEngine, FontRenderer

public class GuiShaders extends GuiScreen
{
    public GuiShaders(GuiScreen guiscreen)
    {
        refreshTimer = -1;
        fileLocation = "";
        guiScreen = guiscreen;
    }

    private GuiButton viewBtn;
    private GuiButton deleteBtn;
    private GuiButton doneBtn;
    private GuiButton newBtn;
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        //controlList.add(newBtn=new GuiButton(5, this.width / 2 + 4, this.height - 48, 150, 20, stringtranslate.translateKey("New AutoLogin")));
        //controlList.add(viewBtn=new GuiButton(4, this.width / 2 - 154, this.height - 48, 150, 20, "View Selected"));
        //controlList.add(deleteBtn=new GuiButton(3, this.width / 2 - 154, this.height - 22, 150, 20, "Delete All Selected"));
        controlList.add(doneBtn=new GuiButton(6, this.width / 2 + 4, this.height - 22, 150, 20, stringtranslate.translateKey("Cancel")));
        mc.texturePackList.updateAvaliableTexturePacks();
        fileLocation = (new File(Minecraft.getMinecraftDir(), "shaders")).getAbsolutePath();
        slotGui = new GuiShadersSlot(this);
        slotGui.registerScrollButtons(controlList, 7, 8);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        else if(guibutton.id == 4) {
            if(!slotGui.viewingIP.equals(""))
                mc.displayGuiScreen(new GuiAutoLoginsEdit(this, slotGui.viewingIP, slotGui.selected));
            else if(!slotGui.selected.equals(""))
                slotGui.selectIP(slotGui.selected);
        } else if(guibutton.id == 3) {
            if(!slotGui.viewingIP.equals(""))
                MMUtil.getAutoLogins().getSavedLoginsByIP(slotGui.viewingIP).remove(slotGui.selected);
            else if(!slotGui.selected.equals(""))
                MMUtil.getAutoLogins().removeAllLogins(slotGui.selected);
        } else if(guibutton.id == 6) {
            if(!slotGui.viewingIP.equals(""))
                slotGui.selectIP("");
            else
                mc.displayGuiScreen(guiScreen);
        } else if(guibutton.id == 5) {
            mc.displayGuiScreen(new GuiAutoLoginsEdit(this));
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
        deleteBtn.enabled=viewBtn.enabled=!slotGui.selected.equals("");
        viewBtn.displayString = !slotGui.viewingIP.equals("") ? "Edit Selected Login" : "View Selected";
        doneBtn.displayString = slotGui.viewingIP.equals("") ? "Cancel" : "Finish Viewing";
        deleteBtn.displayString = slotGui.viewingIP.equals("") ? "Delete All Selected" : "Delete Selected Name";
        newBtn.enabled = slotGui.viewingIP.equals("");
        StringTranslate stringtranslate = StringTranslate.getInstance();
        boolean flag = !slotGui.viewingIP.equals("");
        drawCenteredString(fontRenderer, stringtranslate.translateKey("Your Auto Logins"), width / 2, flag ? 6 : 16, 0xffffff);
        if(flag)
            drawCenteredString(fontRenderer, stringtranslate.translateKey("For " + slotGui.viewingIP), width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    public void updateScreen()
    {
        super.updateScreen();
        refreshTimer--;
    }

    public Minecraft getMinecraft(GuiShaders guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    public FontRenderer getFontRenderer(GuiShaders guitexturepacks)
    {
        return guitexturepacks.fontRenderer;
    }

    protected GuiScreen guiScreen;
    private int refreshTimer;
    private String fileLocation;
    private GuiShadersSlot slotGui;
}
