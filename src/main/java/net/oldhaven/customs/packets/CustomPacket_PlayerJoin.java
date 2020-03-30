package net.oldhaven.customs.packets;

public class CustomPacket_PlayerJoin extends CustomPacket {
    @Override
    public void run(String[] args) {
        System.out.println("Player Join: " + args[0]);
    }
}
