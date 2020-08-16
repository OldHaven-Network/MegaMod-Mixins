package net.oldhaven.customs.packets;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.NetworkManager;
import net.oldhaven.customs.util.MMUtil;

import java.util.HashMap;
import java.util.Map;

public class Packets {
    private static boolean canUsePackets = false;
    private static Map<String, PacketRunnable> customPackets = new HashMap<String, PacketRunnable>(){
        {
            put("onscreentext", new Packet_Runnable_OnScreenText());
            put("playerjoin", new Packet_Runnable_PlayerJoin());
            put("playerquit", new Packet_Runnable_PlayerQuit());
            put("disconnect", new Packet_Runnable_Disconnect());
            put("option", new Packet_Runnable_Option());
            put("mobhealth", new Packet_Runnable_MobHealth());
        }
    };
    public static PacketRunnable getPacketByName(String name) {
        if(customPackets.containsKey(name))
            return customPackets.get(name);
        return null;
    }
    public static boolean doPacketCheck(Packet195Custom packetCustom) {
        if(!canUsePackets)
            return false;
        String utf = packetCustom.utf;
        String[] split = utf.split(";");
        String packet = split[0].toLowerCase();
        if(!customPackets.containsKey(packet)) {
            failPacket(packet, "Unknown packet");
            return false;
        }
        String[] args = null;
        if(split.length > 1) {
            args = new String[split.length-1];
            System.arraycopy(split, 1, args, 0, split.length - 1);
        }
        customPackets.get(packet).run(args);
        return true;
    }
    private static NetworkManager networkManager;
    public static void receiveNetworkClient(NetworkManager manager) {
        networkManager = manager;
    }
    public static NetworkManager getNetworkClient() {
        return networkManager;
    }
    private static void failPacket(String packet, String reason) {
        System.out.println(" ");
        System.out.println("CUSTOM PACKET "+packet+" FAILED");
        System.out.println("Reason: " + reason);
        System.out.println(" ");
    }
    private static boolean seedMatches(String str, String gotSeed) {
        return String.valueOf(stringToSeed(str)).equals(gotSeed);
    }
    public static boolean tryInitalize(String seed) {
        EntityPlayerSP thePlayer = MMUtil.getMinecraftInstance().thePlayer;
        String pName = thePlayer.username;
        if(seed.equals("-17")) {
            System.out.println(" ");
            System.out.println("CONFIRMED CUSTOM PACKET CHECK OF "+seed+" ON "+pName+"!");
            System.out.println("YOUR CLIENT WILL NOW USE PACKETS FROM/TO THE SERVER");
            System.out.println(" ");
            ((EntityClientPlayerMP)thePlayer).sendQueue.addToSendQueue(new Packet195Custom(thePlayer.username, "CONFIRM"));
            canUsePackets = true;
            return true;
        } else
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
