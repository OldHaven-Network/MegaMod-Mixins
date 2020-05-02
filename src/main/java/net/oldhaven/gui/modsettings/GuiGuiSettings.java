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
import net.oldhaven.gui.CustomGuiButton;

public class GuiGuiSettings extends ModdedSettingsGui
{

    public GuiGuiSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Gui Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    private CustomGuiButton.GuiTextField colorField;

    @Override
    public String getModSection() {
        return "GUI";
    }

    public void initGui()
    {
        super.initGui(0);
        StringTranslate stringtranslate = StringTranslate.getInstance();
        CustomGameSettings gs = MegaMod.getCustomGameSettings();
        //colorField.enabled = ((int) (gs.getOptionF("Button Outline") * 11.0F)) == 11;
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100) {
             if(guibutton instanceof GuiSmallButton) {
                MegaMod.getCustomGameSettings().setOptionBtn(((GuiSmallButton) guibutton).returnEnumOptions().name());
                guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
            }
        } else if (guibutton.id == 200) {
            MegaMod.getCustomGameSettings().saveSettings();
            mc.displayGuiScreen(parentGuiScreen);
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
