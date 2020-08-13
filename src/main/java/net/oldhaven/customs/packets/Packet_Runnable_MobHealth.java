package net.oldhaven.customs.packets;

import java.util.HashMap;
import java.util.Map;

public class Packet_Runnable_MobHealth extends PacketRunnable {
    public static Map<Integer /*mob id*/, Integer /*health*/> mobIds = new HashMap<>();

    @Override
    public void run(String[] args) {
        int id = Integer.parseInt(args[0]);
        int health = Integer.parseInt(args[1]);
        mobIds.put(id, health);
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
