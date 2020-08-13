package net.oldhaven;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Vec3D;
import net.oldhaven.customs.IFontRenderer;
import net.oldhaven.customs.ServerPacketInformation;
import net.oldhaven.customs.alexskins.CustomRenderPlayer;
import net.oldhaven.customs.options.*;
import net.oldhaven.customs.shaders.FakeShaderThread;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MegaMod {
    private static Minecraft mcInstance;
    private static MegaMod instance;

    public static int
            thirdPersonView, signCursorLoc,
            chatCursorLoc, chatScrollUp,
            pointingBlock =                     0;

    private String connectedServer;
    public static String version =              "0.7.0";
    public static String requiresUpdate =       null;

    public boolean
            hasLoggedIn, isZooming,
            isSprinting, isFlying,
            flyStill, modLoaderEnabled,
            failedToDrawBG, playerList =        false;
    public static boolean hasUpdated, debug =   false;

    private static ServerPacketInformation serverPacketInformation;
    private static CustomGameSettings customGameSettings;
    private static CustomKeybinds customKeybinds;
    private static IFontRenderer fontRenderer;
    private static SavedShaders savedShaders;
    private static SavedServers savedServers;
    private static SavedLogins autoLogins;

    private LinkedHashMap<String, OnScreenText> onScreenTextMap;
    private static PlayerInstanced playerInstanced;
    private static FakeShaderThread fakeShaderThread;
    public Entity pointingEntity = null;

    private List<String> joinedNames;


    public static MegaMod getInstance() {
        return instance;
    }
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public MegaMod() {
        DiscordRPC dLib = DiscordRPC.INSTANCE;
        String applicationId = "588975277733052432";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("DiscordRPC started");
        dLib.Discord_Initialize(applicationId, handlers, true, null);
        MegaModDiscord.handOverTheGoods();
        MegaModDiscord.setImages(MegaModDiscord.Images.MainMenu);
        MegaModDiscord.setDetails("In main menu");
        //MegaModDiscord.setState(null);
        //MegaModDiscord.setParty("With", 1, 100);
        MegaModDiscord.updatePresence();
        instance = this;
        onScreenTextMap = new LinkedHashMap<>();
        autoLogins = new SavedLogins(this);
        savedServers = new SavedServers(this);
        savedShaders = new SavedShaders();
        serverPacketInformation = new ServerPacketInformation(this);
        customGameSettings = new CustomGameSettings();
        customKeybinds = new CustomKeybinds();
        playerInstanced = new PlayerInstanced(new CustomRenderPlayer());
        joinedNames = new ArrayList<>();
        fakeShaderThread = new FakeShaderThread();
        //BeginThread();
    }

    public void onMinecraftStarted() {
        System.out.println("" +
                "YOU ARE USING A DEVELOPMENT VERSION OF MEGAMOD\n" +
                "IF YOU HAVE ANY SUGGESTIONS PLEASE REPORT THEM ON OUR GITHUB PAGE!\n\n" +
                "MegaMod Version: " + version + "\n" +
                "Thank you for testing MegaMod. - cutezyash#7654\n"
        );
    }

    public void modLoaderTest() { }

    /* -- PLAYER JOIN PACKETS -- */
    public void addPlayerJoin(String name) {
        joinedNames.add(name);
        MegaModDiscord.setState("With " + joinedNames.size() + " players");
        MegaModDiscord.updatePresence();
    }
    public void removePlayerJoin(String name) {
        joinedNames.remove(name);
        MegaModDiscord.setState("With " + joinedNames.size() + " players");
        MegaModDiscord.updatePresence();
    }
    public List<String> getJoinedNames() {
        return joinedNames;
    }
    public void clearJoinedNames() {
        joinedNames.clear();
    }
    /* -- PLAYER JOIN PACKETS END */

    /**
     * Is connected online?
     * @return bool
     */
    public static boolean isOnline() {
        return SkinFix.connected;
    }
    public static class PlayerInstanced {
        public final CustomRenderPlayer customRenderPlayer;
        PlayerInstanced(CustomRenderPlayer crp) {
            this.customRenderPlayer = crp;
        }
        private double exp;
        private int level;
        public EntityPlayerSP getPlayerSP() {
            return getMinecraftInstance().thePlayer;
        }
        public boolean doesPlayerExist() {
            return getMinecraftInstance().thePlayer != null;
        }
        public double getPlayerSpeed() {
            if(!doesPlayerExist())
                return -1;
            EntityPlayerSP p = getPlayerSP();
            return Vec3D.createVector(p.posX, p.posY, p.posZ).distanceTo(Vec3D.createVector(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ));
        }
        public double getPlayerMotion() {
            if(!doesPlayerExist())
                return -1;
            EntityPlayerSP p = getPlayerSP();
            return (p.motionX * p.motionX + p.motionZ * p.motionZ);
        }
        public double getExp() {
            return exp;
        }
        public int getLevel() {
            return level;
        }
    }
    public static PlayerInstanced getPlayer() {
        return playerInstanced;
    }

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

    public static ServerPacketInformation getServerPacketInformation() {
        return serverPacketInformation;
    }

    public static SavedShaders getSavedShaders() {
        return savedShaders;
    }

    public static CustomKeybinds getCustomKeybinds() {
        return customKeybinds;
    }

    public static CustomGameSettings getCustomGameSettings() {
        return customGameSettings;
    }

    public static void setFontRenderer(IFontRenderer fontRendere) {
        fontRenderer = fontRendere;
    }
    public static IFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public static SavedLogins getAutoLogins() {
        return autoLogins;
    }

    public static SavedServers getSavedServers() {
        return savedServers;
    }

    public void setConnectedServer(String s) {
        connectedServer = s;
        if(s != null) {
            String server = s.replaceAll("\\.", "").toLowerCase().split(":")[0];
            String noport = s.split(":")[0];
            for(MegaModDiscord.Images images : MegaModDiscord.Images.values()) {
                if(images.name().toLowerCase().equals(server)) {
                    MegaModDiscord.setImages(images);
                    break;
                }
            }
            MegaModDiscord.setDetails("Playing " + noport);
            MegaModDiscord.setState("");
        } else {
            MegaModDiscord.setDetails("In main menu");
            MegaModDiscord.setState("");
            MegaModDiscord.setImages(MegaModDiscord.Images.MainMenu);
        }
        MegaModDiscord.updatePresence();
    }
    public String getConnectedServer() {
        return connectedServer;
    }

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
    private static Object getPrivateValue(Class<?> instanceClass, Object instance, String field)
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
