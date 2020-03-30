package net.oldhaven.customs.packets;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayerSP;
import net.oldhaven.MegaMod;

public abstract class CustomPacket {
    public abstract void run(String[] args);
    public void sendOut(String packet, String... args) {
        StringBuilder utfBuild = new StringBuilder();
        for(int i=0;i < args.length;i++) {
            if(i >= args.length-1) {
                utfBuild.append(args[i]);
            } else
                utfBuild.append(args[i]).append(";");
        }
        String utf = utfBuild.toString();
        EntityPlayerSP thePlayer = MegaMod.getMinecraftInstance().thePlayer;
        ((EntityClientPlayerMP)thePlayer).sendQueue.addToSendQueue(new Packet195Custom(thePlayer.username, packet+";"+utf));
    }
}
