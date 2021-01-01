package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CPacketMobHealth extends PacketRunnable {
    public static Map<Integer /*mob id*/, Integer /*health*/> mobIds = new HashMap<>();

    @Override
    public void onRun(String[] args) {
        int id = Integer.parseInt(args[0]);
        int health = Integer.parseInt(args[1]);
        mobIds.put(id, health);
    }

    @Override
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "MobHealth";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
