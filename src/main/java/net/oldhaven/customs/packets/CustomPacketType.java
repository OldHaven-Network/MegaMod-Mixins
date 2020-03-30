package net.oldhaven.customs.packets;

public enum CustomPacketType {
    GUI, Message, Unknown;
    CustomPacketType() {};
    public static CustomPacketType compare(int i) {
        switch (i) {
            case 0: return CustomPacketType.GUI;
            case 1: return CustomPacketType.Message;
            default: return CustomPacketType.Unknown;
        }
    }
}
