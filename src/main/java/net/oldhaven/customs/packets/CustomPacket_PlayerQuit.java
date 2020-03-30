package net.oldhaven.customs.packets;

public class CustomPacket_PlayerQuit extends CustomPacket {
    public void run(String[] args) {
        System.out.println("Player Quit: " + args[0]);
    }
}
