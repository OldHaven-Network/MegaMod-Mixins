// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   SourceFile

package net.oldhaven.gui.modsettings;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.rgb.GuiRGB;
import net.oldhaven.gui.rgb.RGBButton;

import javax.annotation.Nonnull;

public class GuiGuiSettings extends ModdedSettingsGui {
    public GuiGuiSettings(GuiScreen guiscreen, GameSettings gamesettings) {
        super(guiscreen, gamesettings);
    }

    public void initGui()
    {
        int i = super.initGui(0);
        int buttonColor = getColorFor(ModOptions.BUTTON_OUTLINE_HEX.getAsString());
        int buttonTextColor = getColorFor(ModOptions.BUTTON_TEXT_HEX.getAsString());
        controlList.add(new RGBButton(201,
                width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                "Button Outline Color...", buttonColor));
        i++;
        controlList.add(new RGBButton(202,
                width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                "Button Text Color...", buttonTextColor));
        super.addDone();
    }

    private int getColorFor(String s) {
        if(s.isEmpty())
            return 0xffffff;
        return Integer.decode(s);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100) {
             if(guibutton instanceof GuiSmallButton) {
                MMUtil.getCustomGameSettings().setOptionBtn(((GuiSmallButton) guibutton).returnEnumOptions().name());
                guibutton.displayString = gameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
            }
        } else if (guibutton.id == 200) {
            MMUtil.getCustomGameSettings().saveSettings();
            mc.displayGuiScreen(parentScreen);
        } else if(guibutton.id == 201) {
            mc.displayGuiScreen(new GuiRGB(
                (int hex) -> {
                    MMUtil.getCustomGameSettings().setOption("Button Outline Hex", String.valueOf(hex));
                    MMUtil.getCustomGameSettings().saveSettings();
                    mc.displayGuiScreen(this);
                }, (int hex) -> {
                    mc.displayGuiScreen(this);
                }, () -> {
                    MMUtil.getCustomGameSettings().setOption("Button Outline Hex", "");
                    MMUtil.getCustomGameSettings().saveSettings();
                    mc.displayGuiScreen(this);
                }
            ));
        } else if(guibutton.id == 202) {
            mc.displayGuiScreen(new GuiRGB(
                    (int hex) -> {
                        MMUtil.getCustomGameSettings().setOption("Button Text Color Hex", String.valueOf(hex));
                        MMUtil.getCustomGameSettings().saveSettings();
                        mc.displayGuiScreen(this);
                    }, (int hex) -> {
                mc.displayGuiScreen(this);
            }, () -> {
                MMUtil.getCustomGameSettings().setOption("Button Text Color Hex", "");
                MMUtil.getCustomGameSettings().saveSettings();
                mc.displayGuiScreen(this);
            }
            ));
        }
    }

    @Nonnull
    @Override
    public String getModSection() {
        return "GUI";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Mod Gui Settings";
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
