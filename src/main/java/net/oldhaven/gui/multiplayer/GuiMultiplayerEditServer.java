// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.multiplayer;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.StringTranslate;
import net.oldhaven.customs.util.MMUtil;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiTextField, StringTranslate, GuiButton, 
//            GameSettings, GuiConnecting

public class GuiMultiplayerEditServer extends GuiScreen
{
    private ServerInfo editing;
    public GuiMultiplayerEditServer(GuiScreen guiscreen)
    {
        parentScreen = guiscreen;
        this.editing = null;
    }
    public GuiMultiplayerEditServer(GuiScreen guiScreen, ServerInfo serverInfo) {
        parentScreen = guiScreen;
        editing = serverInfo;
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
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, editing != null ? "Edit" : "Done"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        String s = mc.gameSettings.lastServer.replaceAll("_", ":");
        ((GuiButton)controlList.get(0)).enabled = s.length() > 0;
        serverTextBox = new GuiTextField(this, fontRenderer, width / 2 - 100, (height / 4 - 60+10) + 50 + 18, 200, 20, s);
        serverTextBox.setMaxStringLength(128);
        serverTextBox2 = new GuiTextField(this, fontRenderer, width / 2 - 100, (height / 4 - 10) + 50 + 18, 200, 20, s);
        serverTextBox2.setMaxStringLength(128);
        if(editing != null) {
            serverTextBox.setText(editing.serverName);
            serverTextBox2.setText(editing.info);
            ((GuiButton)controlList.get(0)).enabled = true;
        } else {
            serverTextBox.setText("");
            serverTextBox2.setText("");
            ((GuiButton)controlList.get(0)).enabled = false;
        }
        serverTextBox2.isFocused = false;
        serverTextBox.isFocused = true;
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
            return;
        if(guibutton.id == 1)
            mc.displayGuiScreen(parentScreen);
        else if(guibutton.id == 0) {
            MMUtil.getSavedServers().saveServer(serverTextBox.getText(), serverTextBox2.getText());
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(parentScreen);
        }
    }

    protected void keyTyped(char c, int i)
    {
        if(!serverTextBox2.isFocused)
            serverTextBox.textboxKeyTyped(c, i);
        if(!serverTextBox.isFocused)
            serverTextBox2.textboxKeyTyped(c, i);
        if(c == '\r')
            actionPerformed((GuiButton)controlList.get(0));
        ((GuiButton)controlList.get(0)).enabled = serverTextBox.getText().length() > 0 && serverTextBox2.getText().length() > 0;
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        serverTextBox.mouseClicked(i, j, k);
        serverTextBox2.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, editing != null ? "Edit Server Info" : "Addding Server", width / 2, 16, 0xffffff);
        drawString(fontRenderer, "Server Address", width / 2 - 100, (height / 4 - 50) + 60 + 36, 0xa0a0a0);
        drawString(fontRenderer, "Server Name", width / 2 - 100, (height / 4 - (10+60+20)) + 60 + 36, 0xa0a0a0);
        serverTextBox.drawTextBox();
        serverTextBox2.drawTextBox();
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentScreen;
    private GuiTextField serverTextBox;
    private GuiTextField serverTextBox2;
}
