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
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.gui.CustomEnumOptions;
import net.oldhaven.gui.CustomGuiButton;

public class GuiGuiSettings extends GuiScreen
{

    public GuiGuiSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Gui Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    private CustomGuiButton.GuiTextField colorField;
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = 0;
        CustomEnumOptions[] enumOptions = videoOptions;
        int j = enumOptions.length;
        for(int k = 0; k < j; k++)
        {
            CustomEnumOptions enumoptions = videoOptions[k];
            String option = enumoptions.getEnumString();
            if(enumoptions.getEnumBoolean()) {
                controlList.add(new CustomGuiButton.GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, option));
            } else if(enumoptions.getEnumFloat()) {
                Float value = MegaMod.getInstance().getCustomGameSettings().getOptionF(enumoptions.getEnumString());
                controlList.add(new CustomGuiButton.GuiSlider(
                        enumoptions.returnEnumOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumoptions, option, value == null ? 0F : value));
            } else {
                String value = MegaMod.getInstance().getCustomGameSettings().getOptionS(enumoptions.getEnumString());
                controlList.add(colorField=new CustomGuiButton.GuiTextField(this,
                        fontRenderer, enumoptions.returnEnumOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumoptions, value));
            }
            i++;
        }

        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        colorField.enabled = ((int) (gs.getOptionF("Button Outline") * 11.0F)) == 11;
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100) {
             if(guibutton instanceof GuiSmallButton) {
                MegaMod.getInstance().getCustomGameSettings().setOptionBtn(((GuiSmallButton) guibutton).returnEnumOptions().name());
                guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
            }
        } else if (guibutton.id == 200) {
            mc.gameSettings.saveOptions();
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
    private static CustomEnumOptions videoOptions[];

    static 
    {
        videoOptions = (new CustomEnumOptions[] {
                CustomEnumOptions.ThirdPerson_Distance,
                CustomEnumOptions.BTN_OUTLINE_COLOR, CustomEnumOptions.BTN_ADVANCED_COLOR, CustomEnumOptions.SHOW_SPEED,
                CustomEnumOptions.SHOW_MOTION, CustomEnumOptions.SHOW_TOOLTIP, CustomEnumOptions.DEFAULT_MM_BACKGROUND,
                CustomEnumOptions.SHOW_QUIT_BUTTON, CustomEnumOptions.TOGGLE_WAILA
        });
    }
}
