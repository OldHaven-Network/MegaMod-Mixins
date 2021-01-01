package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketPlayerJoin extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
        System.out.println("Player Join: " + args[0]);
        MMUtil.getPlayer().addPlayerJoin(args[0]);
    }

    @Override
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "PlayerJoin";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
