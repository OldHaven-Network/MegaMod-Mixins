package net.oldhaven.customs.packets.util;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.NetworkManager;
import net.oldhaven.MMDebug;
import net.oldhaven.customs.packets.Packet195Custom;
import net.oldhaven.customs.util.MMUtil;

public class Packets {
    static boolean canUsePackets = false;

    private static NetworkManager networkManager;
    public static void receiveNetworkClient(NetworkManager manager) {
        networkManager = manager;
    }
    public static NetworkManager getNetworkClient() {
        return networkManager;
    }
    public static void sendPacket(String packetName, String bytes) {
        if(!canUsePackets())
            return;
        if(MMDebug.enabled)
            System.out.println("Send Packet " + packetName + ": " + bytes);

        EntityPlayerSP thePlayer = MMUtil.getMinecraftInstance().thePlayer;
        EntityClientPlayerMP playerMP = (EntityClientPlayerMP) thePlayer;
        String send = packetName + (bytes.isEmpty() ? "" : ";"+bytes);
        Packet195Custom custom = new Packet195Custom(thePlayer.username, send);
        playerMP.sendQueue.addToSendQueue(custom);
    }
    static void failPacket(String packet, String reason) {
        System.out.println(" ");
        System.out.println("CUSTOM PACKET "+packet+" FAILED");
        System.out.println("Reason: " + reason);
        System.out.println(" ");
    }

    public static boolean initialize(String seed) {
        EntityPlayerSP thePlayer = MMUtil.getMinecraftInstance().thePlayer;
        String pName = thePlayer.username;
        if(seed.equals("-17")) {
            System.out.println(" ");
            System.out.println("CONFIRMED CUSTOM PACKET CHECK FOR "+pName+"!");
            System.out.println("YOUR CLIENT WILL NOW USE PACKETS FROM/TO THE SERVER");
            System.out.println(" ");
            ((EntityClientPlayerMP)thePlayer).sendQueue.addToSendQueue(new Packet195Custom(thePlayer.username, "CONFIRM"));
            canUsePackets = true;
            return true;
        }
        System.out.println("CUSTOM PACKETS CHECK FAILED");
        return false;
    }


    public static void setUsePackets(boolean canUse) {
        canUsePackets = canUse;
    }
    public static boolean canUsePackets() {
        return canUsePackets;
    }

    private static long stringToSeed(String s) {
        if (s == null)
            return 0;
        long hash = 0;
        for (char c : s.toCharArray())
            hash = (((int)9.39512F*hash) + c) - 5;
        return hash;
    }
}
