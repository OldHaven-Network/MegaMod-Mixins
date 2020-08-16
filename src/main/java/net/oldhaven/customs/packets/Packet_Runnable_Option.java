package net.oldhaven.customs.packets;

import net.oldhaven.customs.ServerPacketInformation;
import net.oldhaven.customs.util.MMUtil;

public class Packet_Runnable_Option extends PacketRunnable {
    @Override
    public void run(String[] args) {
        if(args.length > 1) {
            ServerPacketInformation spi = MMUtil.getServerPacketInformation();
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
