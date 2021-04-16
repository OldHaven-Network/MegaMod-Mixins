package net.oldhaven.customs.packets.util;

import net.oldhaven.MMDebug;
import net.oldhaven.customs.packets.*;
import net.oldhaven.customs.packets.all.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum PacketList implements IPacket {
    DISCONNECT(new CPacketDisconnect()),
    PLAYERJOIN(new CPacketPlayerJoin()),
    PLAYERQUIT(new CPacketPlayerQuit()),
    PLAYERTYPING(new CPacketPlayerTyping()),

    ONSCREENTEXT(new CPacketOnScreenText()),
    OPTION(new CPacketOption()),
    OPENGUI(new CPacketOpenGUI());

    private final PacketRunnable packetRunnable;
    PacketList(PacketRunnable packetClass) {
        this.packetRunnable = packetClass;
    }

    public PacketRunnable get() {
        return packetRunnable;
    }

    @Override
    public void onRun(String[] args) {
        get().onRun(args);
    }
    @Override
    public void send(String... args) {
        get().send(args);
    }
    @Nonnull
    @Override
    public String getName() {
        return get().getName();
    }

    // -------------------------------------------

    public static boolean runPacket(Packet195Custom packetCustom) {
        MMDebug.println("Custom Packet: " + packetCustom.packetInfo);
        if(!Packets.canUsePackets)
            return false;
        String utf = packetCustom.packetInfo;
        String[] split = utf.split(";");
        String packetName = split[0].toLowerCase();
        PacketList packet = getPacketByName(packetName);
        if(packet == null) {
            Packets.failPacket(packetName, "Unknown packet");
            return false;
        }

        String[] args = null;
        if(split.length > 1) {
            args = new String[split.length-1];
            System.arraycopy(split, 1, args, 0, split.length - 1);
        }
        packet.get().onRun(args);
        return true;
    }

    @Nullable
    public static PacketList getPacketByName(String name) {
        for(PacketList packet : values()) {
            if(packet.name().equalsIgnoreCase(name))
                return packet;
        }
        return null;
    }
}
