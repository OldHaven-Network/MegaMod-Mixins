package net.oldhaven.customs.packets;

import net.minecraft.client.Minecraft;
import net.oldhaven.MegaMod;

public class CustomPacket_Option extends CustomPacket {
    @Override
    public void run(String[] args) {
        if(args[0].equals("canFly")) {
            MegaMod.getInstance().getServerPacketInformation().setCanFly(true);
        }
    }
}
