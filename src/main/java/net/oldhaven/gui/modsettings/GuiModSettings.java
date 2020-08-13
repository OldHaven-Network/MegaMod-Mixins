// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.modsettings;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;

public class GuiModSettings extends ModdedSettingsGui
{
    private MegaMod megaMod;
    public GuiModSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        megaMod = MegaMod.getInstance();
        screenTitle = "Mod Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    @Override
    public String getModSection() {
        return "Mod";
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
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
        super.initGui(i);
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            megaMod.getCustomGameSettings().setOptionBtn(((GuiSmallButton)guibutton).returnEnumOptions().name());
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        } else {
            megaMod.getCustomGameSettings().saveSettings();
            switch (guibutton.id) {
                case 200:
                    mc.displayGuiScreen(parentGuiScreen);break;
                case 201:
                    mc.displayGuiScreen(new GuiSkySettings(this, guiGameSettings));break;
                case 202:
                    mc.displayGuiScreen(new GuiGuiSettings(this, guiGameSettings));break;
                case 203:
                    mc.displayGuiScreen(new GuiKeybindSettings(this, guiGameSettings));break;
                case 204:
                    mc.displayGuiScreen(new GuiBlockSettings(this, guiGameSettings));break;
                case 205:
                    mc.displayGuiScreen(new GuiShaderSettings(this, guiGameSettings));break;
                case 206:
                    mc.displayGuiScreen(new GuiGuiScreenSettings(this, guiGameSettings));break;
                case 207:
                    mc.displayGuiScreen(new GuiPlayerSettings(this, guiGameSettings));break;
                case 208:
                    mc.displayGuiScreen(new GuiSkinSettings(this, guiGameSettings));break;
            }
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private static ModOptions videoOptions[];
}
