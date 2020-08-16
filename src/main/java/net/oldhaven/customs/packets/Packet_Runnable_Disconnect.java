package net.oldhaven.customs.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnectFailed;
import net.minecraft.src.NetworkManager;
import net.oldhaven.customs.util.MMUtil;

public class Packet_Runnable_Disconnect extends PacketRunnable {
    @Override
    public void run(String[] args) {
        Minecraft mc = MMUtil.getMinecraftInstance();
        NetworkManager handler = Packets.getNetworkClient();
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

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
