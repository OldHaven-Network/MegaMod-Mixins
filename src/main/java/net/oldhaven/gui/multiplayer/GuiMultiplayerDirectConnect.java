// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.multiplayer;

import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiTextField, StringTranslate, GuiButton, 
//            GameSettings, GuiConnecting

public class GuiMultiplayerDirectConnect extends GuiScreen
{

    public GuiMultiplayerDirectConnect(GuiScreen guiscreen)
    {
        parentScreen = guiscreen;
    }

    public void updateScreen()
    {
        serverTextBox.updateCursorCounter();
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, "Join Server"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        String s = mc.gameSettings.lastServer.replaceAll("_", ":");
        ((GuiButton)controlList.get(0)).enabled = s.length() > 0;
        serverTextBox = new GuiTextField(this, fontRenderer, width / 2 - 100, (height / 4 - 10) + 50 + 18, 200, 20, s);
        serverTextBox.isFocused = true;
        serverTextBox.setMaxStringLength(128);
    }

    private GuiButton btnEditServer;
    private GuiButton btnDeleteServer;
    private GuiButton btnSelectServer;

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 1)
        {
            mc.displayGuiScreen(parentScreen);
        } else
        if(guibutton.id == 0)
        {
            String s = serverTextBox.getText().trim();
            mc.gameSettings.lastServer = s.replaceAll(":", "_");
            mc.gameSettings.saveOptions();
            String as[] = s.split(":");
            if(s.startsWith("["))
            {
                int i = s.indexOf("]");
                if(i > 0)
                {
                    String s1 = s.substring(1, i);
                    String s2 = s.substring(i + 1).trim();
                    if(s2.startsWith(":") && s2.length() > 0)
                    {
                        s2 = s2.substring(1);
                        as = new String[2];
                        as[0] = s1;
                        as[1] = s2;
                    } else
                    {
                        as = new String[1];
                        as[0] = s1;
                    }
                }
            }
            if(as.length > 2)
            {
                as = new String[1];
                as[0] = s;
            }
            mc.displayGuiScreen(new GuiConnecting(mc, as[0], as.length <= 1 ? 25565 : parseIntWithDefault(as[1], 25565)));
        }
    }

    private int parseIntWithDefault(String s, int i)
    {
        try
        {
            return Integer.parseInt(s.trim());
        }
        catch(Exception exception)
        {
            return i;
        }
    }

    protected void keyTyped(char c, int i)
    {
        serverTextBox.textboxKeyTyped(c, i);
        if(c == '\r')
        {
            actionPerformed((GuiButton)controlList.get(0));
        }
        ((GuiButton)controlList.get(0)).enabled = serverTextBox.getText().length() > 0;
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        serverTextBox.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Direct Connect", width / 2, 16, 0xffffff);
        drawString(fontRenderer, "Server Address", width / 2 - 100, (height / 4 - 60) + 60 + 36, 0xa0a0a0);
        serverTextBox.drawTextBox();
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentScreen;
    private GuiTextField serverTextBox;
}
