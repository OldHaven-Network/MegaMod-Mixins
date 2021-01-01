package net.oldhaven.customs.packets;

public enum CustomPacketType {
    GUI("Packet used to create custom GUI", 0),
    Message("Packet used to communicate with chat", 1),
    Server("Packet used to communicate with server-stuff", 2),
    Client("Packet used to communicate with client-stuff", 3),
    Unknown("Unknown packet type", -1);

    private String desc;
    private int number;
    CustomPacketType(String desc, int num) {
        this.desc = desc;
        this.number = num;
    }

    public String getDesc() {
        return desc;
    }

    public int getTypeInt() {
        return this.number;
    }

    @Override
    public String toString() {
        return "CustomPacketType{" +
                "desc='" + desc + '\'' +
                ", number=" + number +
                '}';
    }

    public static CustomPacketType compare(int num) {
        for(int i=0;i < CustomPacketType.values().length;i++) {
            if(CustomPacketType.values()[i].number == num)
                return CustomPacketType.values()[i];
        }
        return CustomPacketType.Unknown;
    }
}
