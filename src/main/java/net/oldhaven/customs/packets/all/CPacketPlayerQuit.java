package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketPlayerQuit extends PacketRunnable {
    public void onRun(String[] args) {
        System.out.println("Player Quit: " + args[0]);
        MMUtil.getPlayer().removePlayerJoin(args[0]);
    }

    @Override
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "PlayerQuit";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Server;
    }
}
