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
import net.oldhaven.customs.options.CustomGameSettings;

public class GuiPlayerSettings extends ModdedSettingsGui {
    public GuiPlayerSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Player Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        CustomGameSettings gs = MegaMod.getCustomGameSettings();
        screenTitle = stringtranslate.translateKey("Player Settings");
        int i = 0;
        controlList.add(new GuiSmallButton(206, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Skin Settings..."));
        i++;
        super.initGui(i);
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            guiGameSettings.setOptionValue(((GuiSmallButton) guibutton).returnEnumOptions(), 1);
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        switch(guibutton.id) {
            case 200:
                MegaMod.getCustomGameSettings().saveSettings();
                mc.displayGuiScreen(new GuiModSettings(this, guiGameSettings));
                break;
            case 206:
                mc.displayGuiScreen(new GuiSkinSettings(this, guiGameSettings));break;
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

    @Override
    public String getModSection() {
        return "Player";
    }
}
