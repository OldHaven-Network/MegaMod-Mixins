package net.oldhaven;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Vec3D;
import net.oldhaven.customs.*;
import net.oldhaven.customs.options.*;
import net.oldhaven.customs.shaders.FakeShaderThread;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class MegaMod {
    public static int thirdPersonView = 0;
    public static boolean debug = false;
    private static MegaMod instance;
    public MegaMod() {
        instance = this;
        onScreenTextMap = new LinkedHashMap<>();
        autoLogins = new SavedLogins(this);
        savedServers = new SavedServers(this);
        savedShaders = new SavedShaders();
        serverPacketInformation = new ServerPacketInformation(this);
        customGameSettings = new CustomGameSettings();
        customKeybinds = new CustomKeybinds();
        player = new Player();
        joinedNames = new ArrayList<>();
        fakeShaderThread = new FakeShaderThread();
        //BeginThread();
    }

    public static boolean modLoaderEnabled = false;
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

    //private static MMThread mmThread;
    /**
     * Deprecated because use is no longer needed.
     */
    @Deprecated
    public void BeginThread() {
        /*new Thread(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(mmThread=new MMThread(), 0, 2000);
        }).start();*/
    }

    /*public static float[] getRainbowColor() {
        return mmThread.lastRainbow;
    }*/
    private static class MMThread extends TimerTask {
        private int seconds = 0;
        MMThread() {
            this.lastRainbow = new float[]{1, 1, 1, 1};
        }
        @Override
        public void run() {
            //if(seconds >= 1) {
                //if (renderBlocks != null) {
                    //System.out.println("compute shaders 1?");
                    //renderBlocks.computeShaders();
                //}
            //    seconds = 0;
            //}
            if(getFontRenderer() != null)
                this.rainbowNext();
            seconds++;
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
            lastRainbow = new float[]{color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1.0F};
            return lastRainbow;
        }
    }

    public static MegaMod getInstance() {
        return instance;
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public static String version = "0.6.0";
    public static boolean hasUpdated = false;
    public static String requiresUpdate = null;

    public boolean playerList;
    private List<String> joinedNames;
    public void addPlayerJoin(String name) {
        joinedNames.add(name);
    }
    public void removePlayerJoin(String name) {
        joinedNames.remove(name);
    }
    public List<String> getJoinedNames() {
        return joinedNames;
    }
    public void clearJoinedNames() {
        joinedNames.clear();
    }

    public static boolean isConnected() {
        return SkinFix.connected;
    }

    public static class Player {
        Player() {}
        private double exp;
        private int level;
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
        public double getExp() {
            return exp;
        }
        public int getLevel() {
            return level;
        }
    }
    private static Player player;
    public static Player getPlayerInstance() {
        return player;
    }

    public int signCursorLoc = 0;
    public int chatCursorLoc = 0;
    public int chatScrollUp = 0;

    public boolean isZooming = false;
    public boolean isSprinting = false;
    public boolean isFlying = false;
    public boolean flyStill = false;

    public Entity pointingEntity = null;
    public int pointingBlock = 0;

    /**
     * @deprecated un-used, impossible.
     * @throws NullPointerException Invalid ModOption: Borderless
     * @see ModOptions
     */
    @Deprecated
    public void tryFullscreen() throws NullPointerException {
        if(System.getProperty("org.lwjgl.opengl.Window.undecorated") == null)
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        boolean b = getCustomGameSettings().getOptionI("Borderless") == 1;
        if(b) {
            int w = Display.getWidth();
            int h = Display.getHeight();
        }
    }

    private static FakeShaderThread fakeShaderThread;
    public static FakeShaderThread getFakeShaderThread() {
        return fakeShaderThread;
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

    private static ServerPacketInformation serverPacketInformation;
    public static ServerPacketInformation getServerPacketInformation() {
        return serverPacketInformation;
    }

    private static SavedShaders savedShaders;
    public static SavedShaders getSavedShaders() {
        return savedShaders;
    }

    private static CustomKeybinds customKeybinds;
    public static CustomKeybinds getCustomKeybinds() {
        return customKeybinds;
    }

    private static CustomGameSettings customGameSettings;
    public static CustomGameSettings getCustomGameSettings() {
        return customGameSettings;
    }

    private static IFontRenderer fontRenderer;
    public static void setFontRenderer(IFontRenderer fontRendere) {
        fontRenderer = fontRendere;
    }
    public static IFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    private static SavedLogins autoLogins;
    public static SavedLogins getAutoLogins() {
        return autoLogins;
    }

    private static SavedServers savedServers;
    public static SavedServers getSavedServers() {
        return savedServers;
    }

    private String connectedServer;
    public boolean hasLoggedIn = false;
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
