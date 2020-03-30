package net.oldhaven.customs.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.oldhaven.MegaMod;

public class CustomPacket_OnScreenText extends CustomPacket {
    @Override
    public void run(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            builder.append(arg).append(" ");
        }
        String s = builder.toString();
        Minecraft mc = MegaMod.getMinecraftInstance();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        mc.fontRenderer.drawString(s, 1, k/2, 0xFFFFFF);
    }
}
