package net.oldhaven;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MegaModDiscord {
    private static boolean isDisabled;
    private static DiscordRichPresence richPresence;
    static void handOverTheGoods() {
        final DiscordRPC dLib = DiscordRPC.INSTANCE;
        richPresence = new DiscordRichPresence();
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if(!isDisabled)
                    dLib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }
    public static boolean ip(String text) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(text);
        return m.find();
    }
    public static void setTimestampCurrent() {
        richPresence.startTimestamp = System.currentTimeMillis() / 1000;
    }
    public static void setTimestamp(final long start, final long end) {
        richPresence.startTimestamp = start;
        richPresence.endTimestamp = end;
    }
    public enum Images {
        MainMenu("minecraft-icon", "Minecraft", null, null),
        SinglePlayer("singleplayer", "Singleplayer", "minecraft-icon", "Minecraft"),
        betaoldhavennet("betaoldhavennet", "Beyta MC", "minecraft-icon", "Minecraft"),
        beytaoldhavennet("betaoldhavennet", "Beyta MC", "minecraft-icon", "Minecraft"),
        oldschoolminecraftcom("osm", "OldSchoolMinecraft", "minecraft-icon", "Minecraft");

        private String largeID;
        private String smallID;
        private String largeText;
        private String smallText;
        Images(String largeID, String largeText, String smallID, String smallText) {
            this.largeID = largeID;
            this.smallID = smallID;
            this.largeText = largeText;
            this.smallText = smallText;
        }
    }
    public static void setImages(Images images) {
        setLargeImage(images.largeID, images.largeText);
        setSmallImage(images.smallID, images.smallText);
    }
    public static void setSmallImage(final String id, final String text) {
        if(richPresence == null)
            return;
        richPresence.smallImageKey = id;
        richPresence.smallImageText = text;
    }
    public static void setLargeImage(final String id, final String text) {
        if(richPresence == null)
            return;
        richPresence.largeImageKey = id;
        richPresence.largeImageText = text;
    }
    public static void setDetails(final String details) {
        if(richPresence == null)
            return;
        if(ip(details))
            richPresence.details = "IP Address";
        else
            richPresence.details = details;
    }
    public static void setState(final String state) {
        if(richPresence == null)
            return;
        if(ip(state))
            richPresence.state = "IP Address";
        else
            richPresence.state = state;
        richPresence.partySize = 0;
        richPresence.partyMax = 0;
    }
    public static void setParty(final String state, final int size, final int max) {
        if(richPresence == null)
            return;
        setState(state);
        richPresence.partySize = size;
        richPresence.partyMax = max;
    }
    public static void updatePresence() {
        if(isDisabled)
            return;
        if(richPresence == null)
            return;
        DiscordRPC.INSTANCE.Discord_UpdatePresence(richPresence);
    }
    public static void setEnabled(boolean b) {
        isDisabled = !b;
        if(!b)
            DiscordRPC.INSTANCE.Discord_ClearPresence();
        else
            MegaModDiscord.updatePresence();
    }
}
