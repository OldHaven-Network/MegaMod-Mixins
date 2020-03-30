package net.oldhaven;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Vec3D;
import net.oldhaven.customs.*;
import net.oldhaven.modloader.IBaseMod;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Color;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MegaMod {
    public static boolean debug = true;
    private static MegaMod instance;
    public MegaMod() {
        instance = this;
        onScreenTextMap = new LinkedHashMap<>();
        autoLogins = new SavedLogins(this);
        savedServers = new SavedServers(this);
        serverPacketInformation = new ServerPacketInformation(this);
        customGameSettings = new CustomGameSettings();
        customKeybinds = new CustomKeybinds();
        player = new Player();
    }

    public void modLoaderTest() {
        if(null == null)
            return;
        try {
            Class<?> clazz = Class.forName("ModLoader");
            Method m = clazz.getMethod("getLoadedMods");
            List<Class<?>> list = (List<Class<?>>) m.invoke(null);
            for(Class<?> mod : list) {
                System.out.println("a: " + mod.getClass().getSimpleName());
            }
            modLoaderEnabled = true;
        } catch (Exception ignore) {
        }
    }

    private static MMThread mmThread;
    public void BeginThread() {
        new Thread(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(mmThread=new MMThread(), 0, 50);
        }).start();
    }

    public static boolean modLoaderEnabled = false;

    public static float[] getRainbowColor() {
        return null;
    }
    private static class MMThread extends TimerTask {
        MMThread() {
            this.lastRainbow = new float[]{1, 1, 1, 1};
        }
        @Override
        public void run() {
            if(getFontRenderer() != null)
                this.rainbowNext();
        }
        private List<Color> colors;
        private int colorNext = 0;
        private float[] lastRainbow;

        public float[] rainbowNext() {
            if (colors == null) {
                colors = new ArrayList<>();
                for (int r = 0; r < 100; r++) colors.add(new Color(r * 255 / 100, 255, 0));
                for (int g = 100; g > 0; g--) colors.add(new Color(255, g * 255 / 100, 0));
                for (int b = 0; b < 100; b++) colors.add(new Color(255, 0, b * 255 / 100));
                for (int r = 100; r > 0; r--) colors.add(new Color(r * 255 / 100, 0, 255));
                for (int g = 0; g < 100; g++) colors.add(new Color(0, g * 255 / 100, 255));
                for (int b = 100; b > 0; b--) colors.add(new Color(0, 255, b * 255 / 100));
                colors.add(new Color(0, 255, 0));
            }
            colorNext++;
            if (colorNext >= colors.size())
                colorNext = 0;
            Color color = colors.get(colorNext);
            lastRainbow = new float[]{color.getRed()/255, color.getGreen()/255, color.getBlue()/255, 1.0F};
            return lastRainbow;
        }
    }

    public static MegaMod getInstance() {
        return instance;
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public static String version = "0.3.1";
    public static String requiresUpdate = null;

    public static boolean isConnected() {
        return SkinFix.connected;
    }

    public static class Player {
        Player() {}
        public EntityPlayerSP getPlayer() {
            return getMinecraftInstance().thePlayer;
        }
        public boolean doesPlayerExist() {
            return getMinecraftInstance().thePlayer != null;
        }
        public double getPlayerSpeed() {
            if(!doesPlayerExist())
                return -1;
            EntityPlayerSP p = getPlayer();
            return Vec3D.createVector(p.posX, p.posY, p.posZ).distanceTo(Vec3D.createVector(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ));
        }
        public double getPlayerMotion() {
            if(!doesPlayerExist())
                return -1;
            EntityPlayerSP p = getPlayer();
            return (p.motionX * p.motionX + p.motionZ * p.motionZ);
        }
    }
    private static Player player;
    public static Player getPlayerInstance() {
        return player;
    }

    public int signCursorLoc = 0;
    public int chatCursorLoc = 0;
    public int chatScrollUp = 0;

    public boolean isSprinting = false;
    public boolean isFlying = false;
    public boolean flyStill = false;

    public Entity pointingEntity = null;
    public int pointingBlock = 0;

    public void tryFullscreen() {
        if(System.getProperty("org.lwjgl.opengl.Window.undecorated") == null)
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        boolean b = getCustomGameSettings().getOptionI("Borderless") == 1;
        if(b) {
            int w = Display.getWidth();
            int h = Display.getHeight();
        }
    }

    public static class OnScreenText {
        private int color;
        private String text;
        OnScreenText(String text, int color) {
            this.text = text;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public String getText() {
            return text;
        }
    }
    private LinkedHashMap<String, OnScreenText> onScreenTextMap;
    public Map<String, OnScreenText> getOnScreenText() {
        return onScreenTextMap;
    }
    public void replaceOnScreenText(String name, String text, int color) {
        if(onScreenTextMap.containsKey(name))
            onScreenTextMap.get(name).text = text;
        else
            showOnScreenText(name, text, color);
    }
    public void showOnScreenText(String name, String text, int color) {
        if(!onScreenTextMap.containsKey(name))
            onScreenTextMap.put(name, new OnScreenText(text, color));
    }
    public void hideOnScreenText(String name) {
        onScreenTextMap.remove(name);
    }

    private ServerPacketInformation serverPacketInformation;
    public ServerPacketInformation getServerPacketInformation() {
        return serverPacketInformation;
    }

    private CustomKeybinds customKeybinds;
    public CustomKeybinds getCustomKeybinds() {
        return customKeybinds;
    }

    private CustomGameSettings customGameSettings;
    public CustomGameSettings getCustomGameSettings() {
        return customGameSettings;
    }

    private static IFontRenderer fontRenderer;
    public static void setFontRenderer(IFontRenderer fontRendere) {
        fontRenderer = fontRendere;
    }
    public static IFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    private SavedLogins autoLogins;
    public SavedLogins getAutoLogins() {
        return autoLogins;
    }

    private SavedServers savedServers;
    public SavedServers getSavedServers() {
        return savedServers;
    }

    private String connectedServer;
    public void setConnectedServer(String s) {
        connectedServer = s;
    }
    public String getConnectedServer() {
        return connectedServer;
    }

    private static Minecraft mcInstance;
    public static Minecraft getMinecraftInstance() {
        if(mcInstance == null) {
            try {
                ThreadGroup group = Thread.currentThread().getThreadGroup();
                int count = group.activeCount();
                Thread[] threads = new Thread[count];
                group.enumerate(threads);
                for (Thread thread : threads) {
                    if (!thread.getName().equals("Minecraft main thread")) {
                        continue;
                    }
                    mcInstance = (Minecraft) getPrivateValue(Thread.class, thread, "target");
                    break;
                }

            } catch(SecurityException | NoSuchFieldException ex) {
                System.out.println(ex);
            }
        }
        return mcInstance;
    }
    private static Object getPrivateValue(Class instanceClass, Object instance, String field)
            throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field f = instanceClass.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(instance);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
