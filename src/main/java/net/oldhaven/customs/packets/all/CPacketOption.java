package net.oldhaven.customs.packets.all;

import net.oldhaven.customs.ServerPacketInformation;
import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;
import net.oldhaven.customs.util.MMUtil;

import javax.annotation.Nonnull;

public class CPacketOption extends PacketRunnable {
    @Override
    public void onRun(String[] args) {
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
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "Option";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Client;
    }
}
