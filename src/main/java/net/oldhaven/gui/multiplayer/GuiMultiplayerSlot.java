// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.multiplayer;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.oldhaven.customs.util.MMUtil;

import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiAutoLogins, TexturePackList, TexturePackBase, 
//            RenderEngine, Tessellator

public class GuiMultiplayerSlot extends GuiSlot
{
    public GuiMultiplayerSlot(GuiScreen gui)
    {
        super(MMUtil.getMinecraftInstance(), gui.width, gui.height, 32, (gui.height - 55) + 4, 36);
        parGui = gui;
    }

    protected int getSize() {
        return MMUtil.getSavedServers().getSavedServersMap().size();
    }

    protected void elementClicked(int i, boolean flag)
    {
        MMUtil.getSavedServers().selectedServer = i;
    }

    protected boolean isSelected(int i) {
        return MMUtil.getSavedServers().selectedServer == i;
    }

    protected int getContentHeight()
    {
        return getSize() * 36;
    }

    protected void drawBackground()
    {
        parGui.drawDefaultBackground();
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
    {
        Map<String, String> savedServers = MMUtil.getSavedServers().getSavedServersMap();
        //TexturePackBase texturepackbase = (TexturePackBase)parGui.getMinecraft(parGui).texturePackList.availableTexturePacks().get(i);
        //texturepackbase.bindThumbnailTexture(parGui.getMinecraft(parentTexturePackGui));
        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xffffff);
        tessellator.addVertexWithUV(j, k + l, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(j + 32, k + l, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(j + 32, k, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(j, k, 0.0D, 0.0D, 0.0D);
        tessellator.draw();*/
        String key = (String)savedServers.keySet().toArray()[i];
        FontRenderer fontRenderer = MMUtil.getMinecraftInstance().fontRenderer;
        parGui.drawString(fontRenderer, key, j + 32 + 2, k + 1, 0xffffff);
        parGui.drawString(fontRenderer, savedServers.get(key), j + 32 + 2, k + 12, 0x808080);
        //parentTexturePackGui.drawString(GuiAutoLogins.getFontRenderer(parentTexturePackGui), texturepackbase.firstDescriptionLine, j + 32 + 2, k + 12, 0x808080);
        //parentTexturePackGui.drawString(GuiAutoLogins.getFontRenderer(parentTexturePackGui), texturepackbase.secondDescriptionLine, j + 32 + 2, k + 12 + 10, 0x808080);
    }

    final GuiScreen parGui; /* synthetic field */
}
