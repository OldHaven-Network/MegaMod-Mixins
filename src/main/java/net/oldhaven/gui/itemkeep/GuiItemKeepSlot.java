// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.itemkeep;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.oldhaven.customs.ItemKeep;
import net.oldhaven.customs.util.MMUtil;

import java.util.HashMap;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiAutoLogins, TexturePackList, TexturePackBase, 
//            RenderEngine, Tessellator

class GuiItemKeepSlot extends GuiSlot
{
    private final HashMap<String, Integer> hashMap;
    public GuiItemKeepSlot(GuiItemKeep gui) {
        super(gui.getMinecraft(gui), gui.width, gui.height, 32, (gui.height - 55) + 4, 36);
        this.hashMap = ItemKeep.getFullMap();
        parGui = gui;
    }

    protected int getSize() {
        return hashMap.size();
    }

    protected void elementClicked(int i, boolean flag) {
    }

    protected boolean isSelected(int i) {
        return false;
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
        String name = (String) hashMap.keySet().toArray()[i];
        int id = hashMap.get(name);
        parGui.drawString(fontRenderer, name + ": " + id, j + 32 + 2, k + 6, 0xffffff);
    }

    public String viewingIP = "";
    protected String selected = "";
    private final GuiItemKeep parGui; /* synthetic field */
}
