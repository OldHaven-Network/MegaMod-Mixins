package net.oldhaven.customs.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnectFailed;
import net.minecraft.src.NetworkManager;
import net.oldhaven.MegaMod;

public class CustomPacket_Disconnect extends CustomPacket {
    @Override
    public void run(String[] args) {
        Minecraft mc = MegaMod.getMinecraftInstance();
        NetworkManager handler = CustomPackets.getNetworkClient();
        handler.networkShutdown("disconnect.kicked", new Object[0]);
        mc.changeWorld1(null);
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            builder.append(arg).append(" ");
        }
        mc.displayGuiScreen(new GuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", (Object) new String[] {
                builder.toString()
        }));
    }
}
