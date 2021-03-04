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

public class GuiGuiScreenSettings extends ModdedSettingsGui
{

    public GuiGuiScreenSettings(GuiScreen guiscreen, GameSettings gamesettings) {
        super(guiscreen, gamesettings);
    }

    @Nonnull
    @Override
    public String getModSection() {
        return "GuiScreen";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Mod GuiScreen Settings";
    }

    public void initGui() {
        super.initGui(0);
        super.addDone();
    }

    protected void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton)) {
            MMUtil.getCustomGameSettings().setOptionBtn(((GuiSmallButton) guibutton).returnEnumOptions().name());
            guibutton.displayString = gameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        } else if (guibutton.id == 200) {
            MMUtil.getCustomGameSettings().saveSettings();
            mc.displayGuiScreen(parentScreen);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
