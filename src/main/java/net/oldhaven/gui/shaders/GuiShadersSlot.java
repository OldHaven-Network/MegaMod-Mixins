// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.shaders;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.oldhaven.customs.options.SavedLogins;
import net.oldhaven.customs.util.MMUtil;

import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiAutoLogins, TexturePackList, TexturePackBase, 
//            RenderEngine, Tessellator

class GuiShadersSlot extends GuiSlot
{
    public GuiShadersSlot(GuiShaders gui)
    {
        super(gui.getMinecraft(gui), gui.width, gui.height, 32, (gui.height - 55) + 4, 36);
        parGui = gui;
    }

    protected int getSize() {
        if(!viewingIP.equals(""))
            return MMUtil.getAutoLogins().getSavedLoginsMap().get(viewingIP).getSize();
        return MMUtil.getAutoLogins().getSavedLoginsSize();
    }

    protected void elementClicked(int i, boolean flag) {
        if(!viewingIP.equals("")) {
            this.selected = MMUtil.getAutoLogins().getSavedLoginsMap().get(viewingIP).getIndex(i);
        } else {
            Map<String, SavedLogins.SavedLogin> savedLoginMap = MMUtil.getAutoLogins().getSavedLoginsMap();
            this.selected = (String) savedLoginMap.keySet().toArray()[i];
        }
    }

    protected boolean isSelected(int i)
    {
        if(!viewingIP.equals(""))
            return this.selected.equals(MMUtil.getAutoLogins().getSavedLoginsMap().get(viewingIP).getIndex(i));
        Map<String, SavedLogins.SavedLogin> savedLoginMap = MMUtil.getAutoLogins().getSavedLoginsMap();
        String ip = (String)savedLoginMap.keySet().toArray()[i];
        return this.selected.equals(ip);
    }

    protected int getContentHeight()
    {
        return getSize() * 36;
    }

    protected void drawBackground()
    {
        parGui.drawDefaultBackground();
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        FontRenderer fontRenderer = MMUtil.getMinecraftInstance().fontRenderer;
        if(!viewingIP.equals("")) {
            String name = MMUtil.getAutoLogins().getSavedLoginsMap().get(viewingIP).getIndex(i);
            parGui.drawString(fontRenderer, name, j + 32 + 2, k + 1, 0xffffff);
            parGui.drawString(fontRenderer, "Edit to view password", j + 32 + 2, k + 12, 0x808080);
        } else {
            Map<String, SavedLogins.SavedLogin> savedLoginMap = MMUtil.getAutoLogins().getSavedLoginsMap();
            String[] fullIp = ((String) savedLoginMap.keySet().toArray()[i]).split(":");
            String ip = fullIp[0];
            String port = fullIp[1];
            parGui.drawString(fontRenderer, "IP For " + ip, j + 32 + 2, k + 1, 0xffffff);
            parGui.drawString(fontRenderer, "Port " + port, j + 32 + 2, k + 12, 0x808080);
        }
        //parentTexturePackGui.drawString(GuiAutoLogins.getFontRenderer(parentTexturePackGui), texturepackbase.firstDescriptionLine, j + 32 + 2, k + 12, 0x808080);
        //parentTexturePackGui.drawString(GuiAutoLogins.getFontRenderer(parentTexturePackGui), texturepackbase.secondDescriptionLine, j + 32 + 2, k + 12 + 10, 0x808080);
    }

    public void selectIP(String s) {
        this.viewingIP = s;
        this.selected = "";
    }

    public String viewingIP = "";
    protected String selected = "";
    private final GuiShaders parGui; /* synthetic field */
}
