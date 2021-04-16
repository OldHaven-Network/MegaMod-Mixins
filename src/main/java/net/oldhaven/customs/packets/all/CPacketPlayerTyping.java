package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.OnlinePlayer;
import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.packets.util.Packets;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketPlayerTyping extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
        if(args[0].equals("true")) {
            OnlinePlayer.get(args[1]).setTyping(true);
        } else {
            OnlinePlayer.get(args[1]).setTyping(false);
        }
    }

    @Override
    public void send(String... args) {
        Packets.sendPacket(args[0], "");
    }

    @Nonnull
    @Override
    public String getName() {
        return "PlayerTyping";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.GUI;
    }
}
