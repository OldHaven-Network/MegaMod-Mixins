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
import net.oldhaven.customs.options.ModOptions;

public class GuiKeybindSettings extends ModdedSettingsGui
{

    public GuiKeybindSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Gui Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        super.initGui(0);

        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            MegaMod.getCustomGameSettings().setOptionBtn(((GuiSmallButton)guibutton).returnEnumOptions().name());
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        if(guibutton.id == 200)
        {
            MegaMod.getCustomGameSettings().saveSettings();
            MegaMod.getCustomKeybinds().keyCheck();
            MegaMod.getCustomKeybinds().saveIntegers();
            mc.displayGuiScreen(parentGuiScreen);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        CustomGameSettings gs = MegaMod.getCustomGameSettings();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private static ModOptions videoOptions[];

    @Override
    public String getModSection() {
        return "Keybinds";
    }
}
