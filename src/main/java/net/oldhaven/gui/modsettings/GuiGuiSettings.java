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

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = 0;
        CustomEnumOptions aenumoptions[] = videoOptions;
        int j = aenumoptions.length;
        for(int k = 0; k < j; k++)
        {
            CustomEnumOptions enumoptions = videoOptions[k];
            String option = enumoptions.getEnumString();
            if(!enumoptions.getEnumFloat())
            {
                controlList.add(new CustomGuiButton.GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, option));
            } else {
                Float value = MegaMod.getInstance().getCustomGameSettings().getOptionF(enumoptions.getEnumString());
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
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            MegaMod.getInstance().getCustomGameSettings().setOptionBtn(((GuiSmallButton)guibutton).returnEnumOptions().name());
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        if(guibutton.id == 200)
        {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(parentGuiScreen);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        Integer show = gs.getOptionI("Enable Button Outline");
        ((GuiButton) controlList.get(1)).enabled = (show == 1);
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
                CustomEnumOptions.ENABLE_BTN_OUTLINE, CustomEnumOptions.BTN_OUTLINE_COLOR, CustomEnumOptions.SHOW_SPEED, CustomEnumOptions.SHOW_MOTION,
                CustomEnumOptions.SHOW_TOOLTIP, CustomEnumOptions.DEFAULT_MM_BACKGROUND, CustomEnumOptions.SHOW_QUIT_BUTTON
        });
    }
}
