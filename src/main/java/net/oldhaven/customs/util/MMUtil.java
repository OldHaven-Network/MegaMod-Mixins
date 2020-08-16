package net.oldhaven.customs.util;

import net.minecraft.client.Minecraft;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.IFontRenderer;
import net.oldhaven.customs.ServerPacketInformation;
import net.oldhaven.customs.alexskins.CustomRenderPlayer;
import net.oldhaven.customs.options.*;
import net.oldhaven.customs.shaders.FakeShaderThread;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class MMUtil {
    public static int
            thirdPersonView, signCursorLoc,
            chatCursorLoc, chatScrollUp,
            pointingBlock =                     0;
    public static boolean
            hasLoggedIn, isZooming,
            isSprinting, isFlying,
            flyStill, modLoaderEnabled,
            failedToDrawBG, playerList =        false;

    private static ServerPacketInformation serverPacketInformation;
    private static CustomGameSettings customGameSettings;
    private static FakeShaderThread fakeShaderThread;
    private static PlayerInstanced playerInstanced;
    private static CustomKeybinds customKeybinds;
    private static IFontRenderer fontRenderer;
    private static SavedShaders savedShaders;
    private static SavedServers savedServers;
    private static SavedLogins autoLogins;

    private static Minecraft mcInstance;

    /**
     * Initialize MegaMod Utils, creating new instances
     */
    public static void initialize(MegaMod megaMod) {
        playerInstanced = new PlayerInstanced(new CustomRenderPlayer());
        serverPacketInformation = new ServerPacketInformation(megaMod);
        OnScreenText.onScreenTextMap = new LinkedHashMap<>();
        customGameSettings = new CustomGameSettings();
        fakeShaderThread = new FakeShaderThread();
        savedServers = new SavedServers(megaMod);
        customKeybinds = new CustomKeybinds();
        autoLogins = new SavedLogins(megaMod);
        savedShaders = new SavedShaders();
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

    public static FakeShaderThread getFakeShaderThread() {
        return fakeShaderThread;
    }

    public static PlayerInstanced getPlayer() {
        return playerInstanced;
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

    public static ServerPacketInformation getServerPacketInformation() {
        return serverPacketInformation;
    }
}
