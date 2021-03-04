package net.oldhaven.customs.packets.all;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnectFailed;
import net.minecraft.src.NetworkManager;
import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.packets.util.Packets;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketDisconnect extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
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
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "Disconnect";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
