package net.oldhaven.customs.packets;

import net.oldhaven.MegaMod;

public class CustomPacket_PlayerJoin extends CustomPacket {
    @Override
    public void run(String[] args) {
        System.out.println("Player Join: " + args[0]);
        MegaMod.getInstance().addPlayerJoin(args[0]);
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
