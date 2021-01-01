package net.oldhaven.customs.packets.all;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketOnScreenText extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
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
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "OnScreenText";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Client;
    }
}
