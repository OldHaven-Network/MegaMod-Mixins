package net.oldhaven.gui.controls;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public interface IGuiControls {
    void drawDefaultBackground();
    void drawString(FontRenderer var1, String var2, int var3, int var4, int var5);
    void onAction(GuiButton guiButton, GuiControlsSlot.ButtonInfo isNew);
}
