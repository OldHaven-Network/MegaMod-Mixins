package net.oldhaven.gui.rgb;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.CustomGameSettings;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.gui.CustomGuiButton;
import org.lwjgl.opengl.GL11;

public class RGBButton extends GuiButton {
    private int customColor;
    public RGBButton(int var1, int var2, int var3, String var5, int color) {
        super(var1, var2, var3, 150, 20, var5);
        this.customColor = color;
    }

    public void keyTyped(char var1, int var2) {

    }

    @Override
    public void mouseReleased(int i, int i1) {
        super.mouseReleased(i, i1);
    }

    @Override
    public void drawButton(Minecraft var1, int i, int j) {
        if (this.enabled2) {
            String onOff = "";

            FontRenderer var4 = var1.fontRenderer;
            GL11.glBindTexture(3553, var1.renderEngine.getTexture("/gui/gui.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
            int var6 = this.getHoverState(var5);
            CustomGameSettings gs = MegaMod.getCustomGameSettings();
            String value = ModOptions.BUTTON_OUTLINE_HEX.getAsString();
            if(!value.isEmpty()) {
                int color = Integer.decode(value);
                /*switch(value) {
                    case 1: color = 0x3232a8;break;
                    case 2: color = 0xa032a8;break;
                    case 3: color = 0xa8324e;break;
                    case 4: color = 0x32a2a8;break;
                    case 5: color = 0x32a86d;break;
                    case 6: color = 0xa8a432;break;
                    case 7: color = 0xa87532;break;
                    case 8: color = 0xbdbdbd;break;
                    case 9: color = 0x636363;break;
                    case 10: color = 0x000000;break;
                    case 11: color = Integer.decode(gs.getOptionS("Button ADV Color"));break;
                    default: color = 0xffffff;break;
                }*/
                if (i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height)
                    drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, CustomGuiButton.adjustAlpha(color, 255));
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var6 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.mouseDragged(var1, i, j);
            if (!this.enabled) {
                this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
            } else if (var5) {
                this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
            } else {
                this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, customColor);
            }

        }
    }
}
