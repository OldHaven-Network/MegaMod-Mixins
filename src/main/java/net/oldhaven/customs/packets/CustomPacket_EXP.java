package net.oldhaven.customs.packets;

public class CustomPacket_EXP extends CustomPacket {

    @Override
    public void run(String[] args) {
        for(String s : args) {
            System.out.println(s);
        }
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.Client;
    }
}
