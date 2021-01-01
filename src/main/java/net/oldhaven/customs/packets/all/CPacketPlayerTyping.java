package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.packets.util.Packets;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketPlayerTyping extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
        if(args[0].equals("true")) {
            MMUtil.playersTyping.add(args[1]);
        } else {
            MMUtil.playersTyping.remove(args[1]);
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
