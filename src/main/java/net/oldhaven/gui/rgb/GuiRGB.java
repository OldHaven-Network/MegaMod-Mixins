package net.oldhaven.gui.rgb;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;

import java.awt.*;

public class GuiRGB extends GuiScreen {
    @FunctionalInterface
    public interface RunnableWithHex {
        void run(int hex);
    }
    private final RunnableWithHex yesAction;
    private final RunnableWithHex noAction;
    private final Runnable toDefault;
    public GuiRGB(RunnableWithHex yesAction, RunnableWithHex noAction, Runnable toDefault) {
        super();
        this.yesAction = yesAction;
        this.noAction = noAction;
        this.toDefault = toDefault;
    }

    private RGBSlider r;
    private RGBSlider g;
    private RGBSlider b;
    @Override
    public void initGui() {
        int height = this.height / 3 - 50;
        this.controlList.add(r=new RGBSlider(3, this.width / 2 - 155, height, "Red", 1.0F, 0));
        this.controlList.add(g=new RGBSlider(4, this.width / 2 - 155, height+22, "Green", 1.0F, 1));
        this.controlList.add(b=new RGBSlider(5, this.width / 2 - 155, height+22+22, "Blue", 1.0F, 2));
        this.controlList.add(new GuiSmallButton(0, this.width / 2 - (155/2), this.height / 3 + 55, "Reset to Default / Remove"));
        this.controlList.add(new GuiSmallButton(1, this.width / 2 - 155 + 0, this.height / 3 + 96, "Yes"));
        this.controlList.add(new GuiSmallButton(2, this.width / 2 - 155 + 160, this.height / 3 + 96, "No"));
    }

    @Override
    protected void actionPerformed(GuiButton var1) {
        int hex = Integer.parseInt(Integer.toHexString(new Color(r.sliderValue, g.sliderValue, b.sliderValue).getRGB()).substring(2), 16);
        if(var1.id == 1)
            this.yesAction.run(hex);
        else if(var1.id == 2)
            this.noAction.run(hex);
        else if(var1.id == 0)
            this.toDefault.run();
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        this.drawDefaultBackground();
        int hex = Integer.parseInt(Integer.toHexString(new Color(r.sliderValue, g.sliderValue, b.sliderValue).getRGB()).substring(2), 16);
        int loc = this.height / 3 + 80;
        this.drawCenteredString(this.fontRenderer, "Are you okay with this color?", this.width / 2, loc, hex);
        super.drawScreen(i, i1, v);
    }
}
