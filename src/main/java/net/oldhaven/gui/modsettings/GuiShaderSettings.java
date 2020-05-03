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
import net.oldhaven.gui.GuiYesNo;
import net.oldhaven.gui.rgb.GuiRGB;
import net.oldhaven.gui.rgb.RGBButton;

public class GuiShaderSettings extends ModdedSettingsGui
{

    private final int lastShader;
    public GuiShaderSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Mod Shader Settings";
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
        this.lastShader = this.getShaderInt();
        System.out.println(this.lastShader);
    }

    private int getShaderInt() {
        return (int)(ModOptions.SHADERS.getAsFloat()*ModOptions.SHADERS.getTimes());
    }

    private CustomGuiButton.GuiTextField colorField;

    @Override
    public String getModSection() {
        return "Shader";
    }

    public void initGui()
    {
        int i = 0;
        String waterStr = ModOptions.WATER_COLOR.getAsString();
        int waterColor;
        if(waterStr.isEmpty())
            waterColor = 0xffffff;
        else
            waterColor  = Integer.decode(waterStr);
        controlList.add(new RGBButton(201, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Water Color", waterColor));
        i++;
        String lavaStr = ModOptions.LAVA_COLOR.getAsString();
        int lavaColor;
        if(lavaStr.isEmpty())
            lavaColor = 0xffffff;
        else
            lavaColor  = Integer.decode(lavaStr);
        controlList.add(new RGBButton(202, width / 2 - 155 + (i % 2) * 160, height / 6 + 24 * (i >> 1), "Lava Color", lavaColor));
        super.initGui(i+1);
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
            int nS = getShaderInt();
            if(nS != this.lastShader) {
                if(nS == 1 || nS == 3) {
                    if(!mc.gameSettings.ambientOcclusion) {
                        mc.displayGuiScreen(new GuiYesNo(
                            () -> { /* yes */
                                mc.gameSettings.ambientOcclusion = true;
                                mc.gameSettings.saveOptions();
                                if(MegaMod.getMinecraftInstance().renderGlobal != null)
                                    MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
                                mc.displayGuiScreen(parentGuiScreen);
                            }, () -> {
                                MegaMod.getCustomGameSettings().setOption(ModOptions.SHADERS.getName(), (float)this.lastShader/ModOptions.SHADERS.getTimes());
                                mc.displayGuiScreen(this);
                            }, "Smooth Lighting is required for these shaders.", " Would you like to turn it on?"
                        ));
                        return;
                    }
                }
            }
            if(MegaMod.getMinecraftInstance().renderGlobal != null)
                MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
            mc.displayGuiScreen(parentGuiScreen);
        } else if(guibutton.id == 201) {
            mc.displayGuiScreen(new GuiRGB(
                (int hex) -> {
                    MegaMod.getCustomGameSettings().setOption("Water Color Hex", String.valueOf(hex));
                    MegaMod.getCustomGameSettings().saveSettings();
                    if(MegaMod.getMinecraftInstance().renderGlobal != null)
                        MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
                    mc.displayGuiScreen(this);
                }, (int hex) -> {
                    mc.displayGuiScreen(this);
                }, () -> {
                    MegaMod.getCustomGameSettings().setOption("Water Color Hex", "");
                    MegaMod.getCustomGameSettings().saveSettings();
                    if(MegaMod.getMinecraftInstance().renderGlobal != null)
                        MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
                    mc.displayGuiScreen(this);
                }
            ));
        } else if(guibutton.id == 202) {
            mc.displayGuiScreen(new GuiRGB(
                (int hex) -> {
                    MegaMod.getCustomGameSettings().setOption("Lava Color Hex", String.valueOf(hex));
                    MegaMod.getCustomGameSettings().saveSettings();
                    if(MegaMod.getMinecraftInstance().renderGlobal != null)
                        MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
                    mc.displayGuiScreen(this);
                }, (int hex) -> {
                    mc.displayGuiScreen(this);
                }, () -> {
                    MegaMod.getCustomGameSettings().setOption("Lava Color Hex", "");
                    MegaMod.getCustomGameSettings().saveSettings();
                    if(MegaMod.getMinecraftInstance().renderGlobal != null)
                        MegaMod.getMinecraftInstance().renderGlobal.loadRenderers();
                    mc.displayGuiScreen(this);
                }
            ));
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        drawCenteredString(fontRenderer, "Shaders are in alpha stage", width / 2, 30, 0xeb4034);
        //drawCenteredString(fontRenderer, "Smooth Lighting in Video Settings is needed", width / 2, height / 6 + 168-20, 0x4287f5);
        //drawCenteredString(fontRenderer, "for anything other then Faked-Real shading!!", width / 2, height / 6 + 168-10, 0x4287f5);
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private static ModOptions videoOptions[];
}
