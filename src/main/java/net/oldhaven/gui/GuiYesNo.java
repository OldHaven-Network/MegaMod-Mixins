package net.oldhaven.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;

public class GuiYesNo extends GuiScreen {
    private final String[] text;
    private final Runnable yesAction;
    private final Runnable noAction;
    public GuiYesNo(Runnable yesAction, Runnable noAction, String... text) {
        super();
        this.yesAction = yesAction;
        this.noAction = noAction;
        this.text = text;
    }

    @Override
    public void initGui() {
        this.controlList.add(new GuiSmallButton(0, this.width / 2 - 155 + 0, this.height / 6 + 96, "Yes"));
        this.controlList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, "No"));
    }

    @Override
    protected void actionPerformed(GuiButton var1) {
        if(var1.id == 0)
            this.yesAction.run();
        else
            this.noAction.run();
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        this.drawDefaultBackground();
        int loc = 70;
        for(String str : this.text) {
            this.drawCenteredString(this.fontRenderer, str, this.width / 2, loc, 16777215);
            loc+=20;
        }
        super.drawScreen(i, i1, v);
    }
}
