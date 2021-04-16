package net.oldhaven.gui.onscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.oldhaven.customs.packets.util.Packets;
import net.oldhaven.customs.util.MMUtil;

import java.util.List;

public class PlayerListUI extends OnScreenUI {
    public static int playersOnline = 0;
    public static int maxPlayers = 100;

    public void draw(ScaledResolution sc) {
        Minecraft mc = getMinecraft();
        int width = sc.getScaledWidth();
        int centerW = sc.getScaledWidth()/2;
        if(Packets.canUsePackets()) {
            List<String> names = MMUtil.getPlayer().getJoinedNames();
            int height = sc.getScaledHeight()/2 - (12 * names.size());
            int graidentOH = height - 12;
            int gradientH = height + (12 * names.size()) + 12;
            drawGradientRect(width / 2 - 45, graidentOH,  width / 2 + 45, gradientH, 0xc0101010, 0xd0101010);
            drawCenteredString(mc.fontRenderer, "PLAYERLIST", centerW, graidentOH, 0x4fedff);
            for(String name : names) {
                drawCenteredString(mc.fontRenderer, name, centerW, height, 0xffffff);
                height += 12;
            }
            playersOnline = names.size();
            drawCenteredString(mc.fontRenderer, playersOnline + " players online", centerW, height, 0x4fedff);
        } else {
            drawCenteredString(mc.fontRenderer, playersOnline+"/"+maxPlayers, centerW, width/2, 0x4fedff);
        }
    }

    public void drawBackground() {

    }
}
