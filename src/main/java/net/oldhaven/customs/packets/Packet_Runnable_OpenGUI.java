package net.oldhaven.customs.packets;

import net.minecraft.src.GuiScreen;

import java.util.HashMap;
import java.util.Map;

public class Packet_Runnable_OpenGUI extends PacketRunnable {
    private Map<String, GuiScreen> guis = new HashMap<String, GuiScreen>(){
        {
        }
    };

    @Override
    public void run(String[] args) {

    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.GUI;
    }
}
