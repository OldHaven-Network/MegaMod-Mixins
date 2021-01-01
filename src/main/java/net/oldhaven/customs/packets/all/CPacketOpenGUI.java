package net.oldhaven.customs.packets.all;

import net.minecraft.src.GuiScreen;
import net.oldhaven.customs.packets.PacketRunnable;
import net.oldhaven.customs.packets.CustomPacketType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CPacketOpenGUI extends PacketRunnable {
    private Map<String, GuiScreen> guis = new HashMap<String, GuiScreen>(){
        {
        }
    };

    @Override
    public void onRun(String[] args) {
    }

    @Override
    public void send(String... args) {
    }

    @Nonnull
    @Override
    public String getName() {
        return "OpenGUI";
    }

    @Override
    public CustomPacketType getType() {
        return CustomPacketType.GUI;
    }
}
