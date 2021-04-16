package net.oldhaven.gui.onscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.ScaledResolution;
import net.oldhaven.customs.util.GuiExtended;
import net.oldhaven.customs.util.MMUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class OnScreenUI extends GuiExtended {
    protected OnScreenUI() {}

    private static Map<String/*name*/, OnScreenUI> onScreenMap = new HashMap<>();
    public static <T extends OnScreenUI> T getUI(Class<T> clazz) {
        String name = clazz.getName();
        if(onScreenMap.containsKey(name))
            return (T) onScreenMap.get(name);
        T t = null;
        try {
            t = clazz.newInstance();
            onScreenMap.put(name, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    public Minecraft getMinecraft() {
        return MMUtil.getMinecraftInstance();
    }

    //public abstract void draw(ScaledResolution sc, Object... passedArgs);
    //public abstract void drawBackground(ScaledResolution sc);
}
