package net.oldhaven.customs.packets;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayerSP;
import net.oldhaven.customs.util.MMUtil;

public abstract class PacketRunnable implements IPacket {
    private String argOut(String... args) {
        StringBuilder bytesBuild = new StringBuilder();
        for(String arg : args)
            bytesBuild.append(arg).append(";");
        return bytesBuild.toString();
    }

    public void sendOut(String... args) {
        EntityPlayerSP thePlayer = MMUtil.getMinecraftInstance().thePlayer;
        EntityClientPlayerMP clientPlayerMP = (EntityClientPlayerMP) thePlayer;

        String playerName = thePlayer.username;
        String bytes = argOut(getName(), argOut(args));

        Packet195Custom packet = new Packet195Custom(playerName, bytes);
        clientPlayerMP.sendQueue.addToSendQueue(packet);
    }

    public abstract CustomPacketType getType();
}
