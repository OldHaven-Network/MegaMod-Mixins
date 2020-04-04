// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.autologins;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.StringTranslate;
import net.oldhaven.MegaMod;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiTextField, StringTranslate, GuiButton, 
//            GameSettings, GuiConnecting

public class GuiAutoLoginsEdit extends GuiScreen
{
    private boolean editing = false;
    private String editingForIP = "";
    private String editingName = "";
    private String editingPass = "";
    public GuiAutoLoginsEdit(GuiScreen guiscreen) {
        parentScreen = guiscreen;
    }
    public GuiAutoLoginsEdit(GuiScreen guiScreen, String ip, String username) {
        parentScreen = guiScreen;
        this.editing = true;
        this.editingForIP = ip;
        this.editingName = username;
        this.editingPass = MegaMod.getInstance().getAutoLogins().getSavedLoginsByIP(ip).getName(username);
    }

    public void updateScreen()
    {
        username.updateCursorCounter();
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, editing ? "Edit" : "Done"));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        String s = mc.gameSettings.lastServer.replaceAll("_", ":");
        ((GuiButton)controlList.get(0)).enabled = s.length() > 0;
        username = new GuiTextField(this, fontRenderer, width / 2 - 100, (height / 4 - 60+10) + 50 + 18, 200, 20, s);
        username.setMaxStringLength(128);
        password = new GuiTextField(this, fontRenderer, width / 2 - 100, (height / 4 - 10) + 50 + 18, 200, 20, s);
        password.setMaxStringLength(128);
        if(editing) {
            username.setText(editingName);
            password.setText(editingPass);
            ((GuiButton)controlList.get(0)).enabled = true;
        } else {
            username.setText("");
            password.setText("");
            ((GuiButton)controlList.get(0)).enabled = false;
        }
        password.isFocused = false;
        username.isFocused = true;
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
            MegaMod megaMod = MegaMod.getInstance();
            if(editing)
                megaMod.getAutoLogins().getSavedLoginsByIP(editingForIP).remove(editingName);
            megaMod.getAutoLogins().saveLogin(username.getText(), password.getText());
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(parentScreen);
        }
    }

    protected void keyTyped(char c, int i)
    {
        if(!password.isFocused)
            username.textboxKeyTyped(c, i);
        if(!username.isFocused)
            password.textboxKeyTyped(c, i);
        if(c == '\r')
            actionPerformed((GuiButton)controlList.get(0));
        ((GuiButton)controlList.get(0)).enabled = username.getText().length() > 0 && password.getText().length() > 0;
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        username.mouseClicked(i, j, k);
        password.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        String server = MegaMod.getInstance().getConnectedServer();
        drawCenteredString(fontRenderer, editing ? "Edit Login Info" : "Adding Auto-Login", width / 2, 16, 0xffffff);
        drawCenteredString(fontRenderer, "For IP " + (editing ? editingForIP : server), width / 2, 34, 0xffffff);
        drawString(fontRenderer, "Username", width / 2 - 100, (height / 4 - (10+60+20)) + 60 + 36, 0xa0a0a0);
        String name = username.getText();
        if(name.length() > 0)
            drawString(fontRenderer, name+"'s Password", width / 2 - 100, (height / 4 - 50) + 60 + 36, 0xa0a0a0);
        else
            drawString(fontRenderer, "Password", width / 2 - 100, (height / 4 - 50) + 60 + 36, 0xa0a0a0);
        password.drawTextBox();
        username.drawTextBox();
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentScreen;
    private GuiTextField username;
    private GuiTextField password;
}
