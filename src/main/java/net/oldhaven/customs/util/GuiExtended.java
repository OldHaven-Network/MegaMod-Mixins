package net.oldhaven.customs.util;

import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiExtended extends Gui {
    @Override
    protected void drawRect(int i, int i1, int i2, int i3, int i4) {
        super.drawRect(i, i1, i2, i3, i4);
    }

    protected void drawRect(double x1, double y1, double x2, double y2, int col) {
        double var6;
        if (x1 < x2) {
            var6 = x1;
            x1 = x2;
            x2 = var6;
        }
        if (y1 < y2) {
            var6 = y1;
            y1 = y2;
            y2 = var6;
        }

        float a = (float)(col >> 24 & 255) / 255.0F;
        float r = (float)(col >> 16 & 255) / 255.0F;
        float g = (float)(col >> 8 & 255) / 255.0F;
        float b = (float)(col & 255) / 255.0F;
        Tessellator x0 = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(r, g, b, a);
        x0.startDrawingQuads();
        x0.addVertex(x1, y2, 0.0D);
        x0.addVertex(x2, y2, 0.0D);
        x0.addVertex(x2, y1, 0.0D);
        x0.addVertex((double)x1, y1, 0.0D);
        x0.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
}
