package net.oldhaven.gui.rgb;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import org.lwjgl.opengl.GL11;

public class RGBSlider extends GuiButton {
    private final String old;
    public float sliderValue;
    public boolean dragging = false;
    private int colorType;
    public RGBSlider(final int var1, final int var2, final int var3, final String var5, final float var6, int colorType) {
        super(var1, var2, var3, 150+160, 20, var5);
        this.sliderValue = var6;
        this.old = var5;
        this.colorType = colorType;
        this.doDisplayString();
    }

    private void doDisplayString() {
        this.displayString = old + ": " + (int)(this.sliderValue*255);
    }

    protected int getHoverState(boolean var1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft minecraft, int var2, int var3) {
        if (this.enabled2) {
            if (this.dragging) {
                setSliderValue(var2);
            }
            if(colorType == 0)
                GL11.glColor4f(this.sliderValue, 0.0F, 0.0F, 1.0F);
            else if(colorType == 1)
                GL11.glColor4f(0.0F, this.sliderValue, 0.0F, 1.0F);
            else if(colorType == 2)
                GL11.glColor4f(0.0F, 0.0F, this.sliderValue, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3) {
        if (super.mousePressed(var1, var2, var3)) {
            setSliderValue(var2);
            this.dragging = true;
            return true;
        }
        return false;
    }
    public void mouseReleased(int var1, int var2) {
        this.dragging = false;
    }

    private void setSliderValue(int var2) {
        this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
        if (this.sliderValue < 0.0F) {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F) {
            this.sliderValue = 1.0F;
        }

        this.doDisplayString();
    }
}
