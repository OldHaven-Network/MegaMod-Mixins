package net.oldhaven.customs.packets;

import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import net.oldhaven.customs.packets.util.PacketList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet195Custom extends Packet {
    public String packetInfo;
    public String userName;
    public Packet195Custom() {}
    public Packet195Custom(String userName, String s) {
        this.userName = userName;
        this.packetInfo = s;
    }

    @Override
    public void readPacketData(DataInputStream dataInputStream) {
        try {
            this.packetInfo = dataInputStream.readUTF();
            this.userName = dataInputStream.readUTF();
        } catch (IOException ignored) {}
    }

    @Override
    public void writePacketData(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(packetInfo);
            dataOutputStream.writeUTF(userName);
        } catch (IOException ignored) {}
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        PacketList.runPacket(this);
    }

    @Override
    public int getPacketSize() {
        return this.packetInfo.length() + this.userName.length();
    }
}
