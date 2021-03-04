package net.oldhaven;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import org.lwjgl.Sys;

public class MegaMod {
    private static MegaMod instance;

    public static String version =
            "0.7.1";
    public static String requiresUpdate = null;

    public static boolean devVersion = false;
    public static boolean hasUpdated = false;

    public static MegaMod getInstance() {
        return instance;
    }
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public MegaMod() {
        instance = this;
        DiscordRPC dLib = DiscordRPC.INSTANCE;
        String applicationId = "588975277733052432";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("DiscordRPC started");
        dLib.Discord_Initialize(applicationId, handlers, true, null);
        MegaModDiscord.handOverTheGoods();
        MMUtil.initialize(this);
        //BeginThread();
    }

    private void startDiscord() {
        MegaModDiscord.setEnabled(ModOptions.RICH_PRESENCE.getAsBool());
        MegaModDiscord.setImages(MegaModDiscord.Images.MainMenu);
        MegaModDiscord.setDetails("In main menu");
        //MegaModDiscord.setState(null);
        //MegaModDiscord.setParty("With", 1, 100);
        MegaModDiscord.updatePresence();
    }

    public void onMinecraftStarted() {
        this.startDiscord();
        System.out.println("" +
                "YOU ARE USING A DEVELOPMENT VERSION OF MEGAMOD\n" +
                "IF YOU HAVE ANY SUGGESTIONS PLEASE REPORT THEM ON OUR GITHUB PAGE!\n\n" +
                "MegaMod Version: " + version + "\n" +
                "Thank you for testing MegaMod. - cutezyash#7654\n"
        );
    }

    public void modLoaderTest() { }

    /**
     * Is connected online?
     * @return bool
     */
    public static boolean isOnline() {
        return SkinFix.connected;
    }
}
