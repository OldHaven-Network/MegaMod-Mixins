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
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.gui.CustomGuiButton;
import net.oldhaven.gui.GuiYesNo;

public class GuiShaderNonGLSLSettings extends ModdedSettingsGui
{

    private final int lastShader;
    public GuiShaderNonGLSLSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Non-GLSL Settings";
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
        return "ShaderNonGLSL";
    }

    public void initGui()
    {
        this.controlList.clear();
        super.initGui(0);
        StringTranslate stringtranslate = StringTranslate.getInstance();
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
