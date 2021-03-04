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

public class GuiPlayerSettings extends ModdedSettingsGui {
    public GuiPlayerSettings(GuiScreen guiscreen, GameSettings gamesettings) {
        super(guiscreen, gamesettings);
    }

    public void initGui() {
        controlList.add(new GuiSmallButton(206, width / 2 - 155, height / 6, "Skin Settings..."));
        super.initGui(1);
        super.addDone();
    }

    protected void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton)) {
            gameSettings.setOptionValue(((GuiSmallButton) guibutton).returnEnumOptions(), 1);
            guibutton.displayString = gameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        switch(guibutton.id) {
            case 200:
                MMUtil.getCustomGameSettings().saveSettings();
                mc.displayGuiScreen(parentScreen);
                break;
            case 206:
                mc.displayGuiScreen(new GuiSkinSettings(this, gameSettings));break;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Mod Player Settings";
    }

    @Nonnull
    @Override
    public String getModSection() {
        return "Player";
    }
}
