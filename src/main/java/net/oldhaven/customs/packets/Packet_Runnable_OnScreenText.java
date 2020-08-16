package net.oldhaven.customs.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.oldhaven.customs.util.MMUtil;

public class Packet_Runnable_OnScreenText extends PacketRunnable {
    @Override
    public void run(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            builder.append(arg).append(" ");
        }
        String s = builder.toString();
        Minecraft mc = MMUtil.getMinecraftInstance();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        mc.fontRenderer.drawString(s, 1, k/2, 0xFFFFFF);
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Client;
    }
}
