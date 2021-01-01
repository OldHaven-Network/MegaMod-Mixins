// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.changelog;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Tessellator;
import net.oldhaven.customs.util.MMUtil;

import java.util.LinkedList;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiAutoLogins, TexturePackList, TexturePackBase, 
//            RenderEngine, Tessellator

class GuiChangelogSlot extends GuiSlotEdit
{
    private LinkedList<ChangeLog.TextField> textFields;
    public GuiChangelogSlot(GuiChangelog gui)
    {
        super(gui.getMinecraft(gui), gui.width, gui.height, 32, (gui.height - 55) + 4, 36);
        parGui = gui;
        textFields = ChangeLog.getChangelog();
    }

    private void addString(String s, int color) {
        /*int i = parGui.getFontRenderer().getStringWidth(s);
        if(i > 20) {
            addString(s.substring(0, 20), color);
            addString(s.substring(20), color);
            return;
        }*/
        //textFields.addLast(new TextField(s, color));
    }

    protected int getSize() {
        return textFields.size();
    }

    protected void elementClicked(int i, boolean flag) {
    }

    protected boolean isSelected(int i) {
        return false;
    }

    protected int getContentHeight()
    {
        return getSize() * 12;
    }

    protected void drawBackground()
    {
        parGui.drawDefaultBackground();
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        super.drawScreen(i, i1, v);
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        FontRenderer fontRenderer = MMUtil.getMinecraftInstance().fontRenderer;
        String str = textFields.get(i).string;
        int color = textFields.get(i).color;
        parGui.drawString(fontRenderer, str, 10, k, color);
    }

    private final GuiChangelog parGui; /* synthetic field */
}
