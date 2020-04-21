package net.oldhaven.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;

public class GuiEXP extends GuiScreen {
    public GuiEXP() {}

    public void initGui() {
        controlList.add(new GuiSmallButton(201, width / 2, height / 6, "Frick"));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled)
            return;

    }

    @Override
    public void drawBackground(int i) {
        drawDefaultBackground();
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        this.drawBackground(i);
    }
}
