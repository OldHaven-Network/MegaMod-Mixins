package net.oldhaven.customs.packets;

import java.util.HashMap;
import java.util.Map;

public class CustomPacket_MobHealth extends CustomPacket {
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
