package net.oldhaven.customs.packets;

import net.oldhaven.MegaMod;

public class CustomPacket_PlayerQuit extends CustomPacket {
    public void run(String[] args) {
        System.out.println("Player Quit: " + args[0]);
        MegaMod.getInstance().removePlayerJoin(args[0]);
    }
}
