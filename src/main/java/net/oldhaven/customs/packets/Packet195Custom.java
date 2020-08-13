package net.oldhaven.customs.packets;

import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet195Custom extends Packet {
    public String utf;
    public String userName;
    public Packet195Custom() {}
    public Packet195Custom(String userName, String s) {
        this.userName = userName;
        this.utf = s;
    }

    @Override
    public void readPacketData(DataInputStream dataInputStream) {
        try {
            this.utf = dataInputStream.readUTF();
            this.userName = dataInputStream.readUTF();
        } catch (IOException ignored) {}
    }

    @Override
    public void writePacketData(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(utf);
            dataOutputStream.writeUTF(userName);
        } catch (IOException ignored) {}
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        Packets.doPacketCheck(this);
    }

    @Override
    public int getPacketSize() {
        return this.utf.length() + this.userName.length();
    }
}
