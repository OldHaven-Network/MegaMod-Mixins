// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.modsettings;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

import net.minecraft.src.*;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.MegaMod;
import net.oldhaven.gui.CustomEnumOptions;
import net.oldhaven.gui.CustomGuiButton;

public class GuiSkySettings extends GuiScreen {
    public GuiSkySettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Sky Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        screenTitle = stringtranslate.translateKey("Sky Settings");
        int i = 0;
        int j = options.length;
        for(int k = 0; k < j; k++)
        {
            CustomEnumOptions enumoptions = options[k];
            String option = enumoptions.getEnumString();
            if(!enumoptions.getEnumFloat()) {
                controlList.add(new CustomGuiButton.GuiSmallButton(
                        enumoptions.returnEnumOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumoptions, option));
            } else {
                Float value = gs.getOptionF(enumoptions.getEnumString());
                controlList.add(new CustomGuiButton.GuiSlider(
                        enumoptions.returnEnumOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumoptions, option, value == null ? 0F : value));
            }
            i++;
        }

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
        if(guibutton.id == 200)
        {
            MegaMod.getInstance().getCustomGameSettings().saveSettings();
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
    private static CustomEnumOptions options[];

    static 
    {
        options = (new CustomEnumOptions[] {
            CustomEnumOptions.CLOUD_HEIGHT, CustomEnumOptions.RAIN_TOGGLE, CustomEnumOptions.FORCE_TIME
        });
    }
}
