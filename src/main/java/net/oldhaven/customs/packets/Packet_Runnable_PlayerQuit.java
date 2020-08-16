package net.oldhaven.customs.packets;

import net.oldhaven.customs.util.MMUtil;

public class Packet_Runnable_PlayerQuit extends PacketRunnable {
    public void run(String[] args) {
        System.out.println("Player Quit: " + args[0]);
        MMUtil.getPlayer().removePlayerJoin(args[0]);
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
