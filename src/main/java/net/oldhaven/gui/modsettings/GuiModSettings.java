// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.modsettings;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

import net.minecraft.src.*;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class GuiModSettings extends ModdedSettingsGui {
    public GuiModSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        super(guiscreen, gamesettings);
    }

    @Nonnull
    @Override
    public String getModSection() {
        return "Mod";
    }

    public void initGui() {
        int i = 0;
        controlList.add(new GuiSmallButton(201, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Sky Settings..."));
        i++;
        controlList.add(new GuiSmallButton(207, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Player Settings..."));
        i++;
        controlList.add(new GuiSmallButton(202, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "UI Settings..."));
        i++;
        controlList.add(new GuiSmallButton(206, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "GuiScreen Settings..."));
        i++;
        controlList.add(new GuiSmallButton(203, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "KeyBind Settings..."));
        i++;
        controlList.add(new GuiSmallButton(204, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Block Settings..."));
        i++;
        controlList.add(new GuiSmallButton(205, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Shader Settings..."));
        i++;
        controlList.add(new GuiSmallButton(208, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Discord Settings..."));
        i++;
        controlList.add(new GuiSmallButton(209, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "MiniMap Settings..."));
        i++;
        super.initGui(i);
        super.addDone();
    }

    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton)) {
            MMUtil.getCustomGameSettings().setOptionBtn(((GuiSmallButton)guibutton).returnEnumOptions().name());
            guibutton.displayString = gameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        } else {
            MMUtil.getCustomGameSettings().saveSettings();
            switch (guibutton.id) {
                case 200:
                    mc.displayGuiScreen(parentScreen);break;
                case 201:
                    mc.displayGuiScreen(new GuiSkySettings(this, gameSettings));break;
                case 202:
                    mc.displayGuiScreen(new GuiGuiSettings(this, gameSettings));break;
                case 203:
                    mc.displayGuiScreen(new GuiKeybindSettings(this, gameSettings));break;
                case 204:
                    mc.displayGuiScreen(new GuiBlockSettings(this, gameSettings));break;
                case 205:
                    mc.displayGuiScreen(new GuiShaderSettings(this, gameSettings));break;
                case 206:
                    mc.displayGuiScreen(new GuiGuiScreenSettings(this, gameSettings));break;
                case 207:
                    mc.displayGuiScreen(new GuiPlayerSettings(this, gameSettings));break;
                case 208:
                    mc.displayGuiScreen(new GuiDiscordSettings(this, gameSettings));break;
                case 209:
                    mc.displayGuiScreen(new GuiMiniMapSettings(this, gameSettings));break;
            }
        }
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Mod Settings";
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
