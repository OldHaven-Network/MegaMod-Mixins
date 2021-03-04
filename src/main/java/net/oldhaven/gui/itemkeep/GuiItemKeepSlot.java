// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.itemkeep;

import net.minecraft.src.*;
import net.oldhaven.customs.ItemKeep;
import net.oldhaven.customs.util.MMUtil;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiAutoLogins, TexturePackList, TexturePackBase, 
//            RenderEngine, Tessellator

class GuiItemKeepSlot extends GuiSlot
{
    private static RenderItem itemRenderer = new RenderItem();
    private final SortedSet<String> keys;
    private final HashMap<String, ItemKeep.ItemFulfill> hashMap;
    public GuiItemKeepSlot(GuiItemKeep gui) {
        super(gui.getMinecraft(gui), gui.width, gui.height, 32, (gui.height - 55) + 4, 36);
        this.hashMap = ItemKeep.getFullMap();
        this.keys = new TreeSet<>(this.hashMap.keySet());
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
        String name = (String) keys.toArray()[i];
        ItemKeep.ItemFulfill fulfill = hashMap.get(name);
        ItemStack stack = fulfill.stack;
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItemIntoGUI(
                fontRenderer, MMUtil.getMinecraftInstance().renderEngine,
                stack, j + 32 + 2, k + 6);
        RenderHelper.disableStandardItemLighting();
        int id = fulfill.id;
        float hardness = fulfill.hardness;
        float damage = fulfill.damage;
        parGui.drawString(fontRenderer, name, j + 64 + 2, k + 6, 0xffffff);
        parGui.drawString(fontRenderer, "ID: " + id, j + 64 + 2, k + 16, 0xffffff);
        if(hardness != -1)
            parGui.drawString(fontRenderer, "  ;  Hardness: " + hardness, j + 64 + 2+35, k + 16, 0x5cd166);
        else if(damage != -1)
            parGui.drawString(fontRenderer, "  ;  Damage: " + damage, j + 64 + 2+35, k + 16, 0xd15c5e);
        else
            parGui.drawString(fontRenderer, "  ;  No stats", j + 64 + 2+35, k + 16, 0xffffff);
    }

    public String viewingIP = "";
    protected String selected = "";
    private final GuiItemKeep parGui; /* synthetic field */
}
