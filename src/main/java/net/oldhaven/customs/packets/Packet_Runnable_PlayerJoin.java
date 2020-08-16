package net.oldhaven.customs.packets;

import net.oldhaven.customs.util.MMUtil;

public class Packet_Runnable_PlayerJoin extends PacketRunnable {
    @Override
    public void run(String[] args) {
        System.out.println("Player Join: " + args[0]);
        MMUtil.getPlayer().addPlayerJoin(args[0]);
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
