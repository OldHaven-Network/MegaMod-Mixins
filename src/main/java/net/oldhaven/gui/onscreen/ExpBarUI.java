package net.oldhaven.gui.onscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ExpBarUI extends OnScreenUI {
    public void draw(ScaledResolution sc) {
        Minecraft mc = getMinecraft();
        int width = sc.getScaledWidth();
        int height = sc.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int o = mc.renderEngine.getTexture("/gui/alphabg.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(o);

        int i = 10;
        int h = height-29;
        drawTexturedModalRect(width/2-(182/2), h, 0, 26, 1, 5);
        drawTexturedModalRect(width/2-(182/2)+1+i, h, 1, 26, 179-i, 5);
        drawTexturedModalRect(width/2-(182/2)+180, h, 180, 26, 1, 5);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
