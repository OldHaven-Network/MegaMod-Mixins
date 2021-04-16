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

public class GuiMiniMapSettings extends ModdedSettingsGui {
    public GuiMiniMapSettings(GuiScreen guiscreen, GameSettings gamesettings) {
        super(guiscreen, gamesettings);
    }

    public void initGui() {
        int i = super.initGui(0);
        int buttonTextColor = getColorFor(ModOptions.MINIMAP_BORDER_COLOR_HEX.getAsString());
        controlList.add(new RGBButton(202,
                width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                "MiniMap Border Color...", buttonTextColor));
        i++;
        buttonTextColor = getColorFor(ModOptions.MINIMAP_COMPASS_COLOR_HEX.getAsString());
        controlList.add(new RGBButton(203,
                width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                "MiniMap Compass Colors...", buttonTextColor));
        super.addDone();
    }

    private int getColorFor(String s) {
        if(s.isEmpty())
            return 0xffffff;
        return Integer.decode(s);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled)
            return;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton)) {
            gameSettings.setOptionValue(((GuiSmallButton) guibutton).returnEnumOptions(), 1);
            guibutton.displayString = gameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        if(guibutton.id == 200) {
            MMUtil.getCustomGameSettings().saveSettings();
            mc.displayGuiScreen(parentScreen);
        } else if(guibutton.id == 202) {
            mc.displayGuiScreen(new GuiRGB(ModOptions.MINIMAP_BORDER_COLOR_HEX.getAsString(),
                (int hex) -> {
                    MMUtil.getCustomGameSettings().setOption("MiniMap Border Color Hex", String.valueOf(hex));
                    MMUtil.getCustomGameSettings().saveSettings();
                    mc.displayGuiScreen(this);
                }, (int hex) -> mc.displayGuiScreen(this), () -> {
                    MMUtil.getCustomGameSettings().setOption("MiniMap Border Color Hex", "");
                    MMUtil.getCustomGameSettings().saveSettings();
                    mc.displayGuiScreen(this);
                }
            ));
        } else if(guibutton.id == 203) {
            mc.displayGuiScreen(new GuiRGB(ModOptions.MINIMAP_COMPASS_COLOR_HEX.getAsString(),
                    (int hex) -> {
                        MMUtil.getCustomGameSettings().setOption("MiniMap Compass Color Hex", String.valueOf(hex));
                        MMUtil.getCustomGameSettings().saveSettings();
                        mc.displayGuiScreen(this);
                    }, (int hex) -> mc.displayGuiScreen(this), () -> {
                MMUtil.getCustomGameSettings().setOption("MiniMap Compass Color Hex", "");
                MMUtil.getCustomGameSettings().saveSettings();
                mc.displayGuiScreen(this);
            }
            ));
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
        return "Mod MiniMap Settings";
    }

    @Nonnull
    @Override
    public String getModSection() {
        return "MiniMap";
    }
}
