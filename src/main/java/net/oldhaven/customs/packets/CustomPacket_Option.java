package net.oldhaven.customs.packets;

import net.oldhaven.MegaMod;
import net.oldhaven.customs.ServerPacketInformation;

public class CustomPacket_Option extends CustomPacket {
    @Override
    public void run(String[] args) {
        if(args.length > 1) {
            System.out.println(args[0] + " " + args[1]);
            ServerPacketInformation spi = MegaMod.getServerPacketInformation();
            boolean b = args[1].equalsIgnoreCase("true");
            if (args[0].equals("canFly"))
                spi.setCanFly(b);
            if (args[0].equals("customChat"))
                spi.setCustomChat(b);
        }
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Client;
    }
}
