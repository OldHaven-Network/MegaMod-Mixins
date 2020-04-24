package net.oldhaven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkinFix {
    private static final Map<String, String> uuids = new HashMap();

    static boolean connected = false;
    public static void tryConnection() {
        try {
            URL url = new URL("http://google.com");
            url.openConnection().connect();
        } catch(IOException e) {
            System.out.println("Failed to connect online");
            connected = false;
            return;
        }
        System.out.println("Connected online");
        connected = true;
    }
    public SkinFix() {

    }

    public static Boolean isSkinAlex(String name) {
        if(getUuidStringFromName(name) == null)
            return false;
        UUID uuid = UUID.fromString(getUuidStringFromName(name).replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        return (uuid.hashCode() & 1) != 0;
    }

    private static Map<String, String> savedUrl = new HashMap<>();
    public static String getSkinUrl(String playerName) {
        if(playerName == null)
            return null;
        /*String uuid = getUuidStringFromName(playerName);
        if(uuid == null)
            return null;*/
        if(savedUrl.containsKey(playerName))
            return savedUrl.get(playerName);
        /*String s = "https://sessionserver.mojang.com/session/minecraft/profile/"+uuid;
        String skinUrl = getSkinFromUrl(s);
        System.out.println(skinUrl);
        savedUrl.put(playerName, skinUrl);*/
        return "https://minotar.net/skin/"+playerName;
    }

    private static boolean codeIs404(int code) {
        return code == 404 || code == 500;
    }
    private static boolean tryConnect(String urlS) {
        try {
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(!codeIs404(code)) {
                return true;
            }
        } catch(IOException ignore) {}
        return false;
    }
    public static String[] capeList = new String[]{
            "https://crafatar.com/capes/%UUID%",
            "http://s.optifine.net/capes/%NAME%",
            "https://minecraftcapes.co.uk/getCape/%UUID%"
    };
    private static String capeListing = "0|1|2";
    public static String getCapeUrl(String playerName) {
        if(!connected)
            return null;
        String uuid = getUuidStringFromName(playerName);
        /*String[] capeListings = capeListing.split("|");
        for(int i=0;i < capeListings.length;i++) {
            int o = Integer.parseInt(capeListings[i]);
            String urlBase = capeList[o];
        }*/
        String urlBase = "https://crafatar.com/capes/";
        String toSend = uuid;
        if(uuid != null && tryConnect(urlBase + toSend))
            return urlBase + toSend;
        else {
            urlBase = "http://s.optifine.net/capes/";
            toSend = playerName + ".png";
            if(tryConnect(urlBase + toSend))
                return urlBase + toSend;
            else {
                urlBase = "https://minecraftcapes.co.uk/getCape/";
                toSend = uuid;
                if(tryConnect(urlBase + toSend))
                    return urlBase + toSend;
                return null;
            }
        }
    }

    private static String getUuidStringFromName(String playerName) {
        if (uuids.containsKey(playerName)) {
            return uuids.get(playerName);
        } else {
            String uuid = getUuidFromMojang(playerName);
            if (uuid != null) {
                uuids.put(playerName, uuid);
            }
            return uuid;
        }
    }

    private static String getSkinFromUrl(String ur) {
        try {
            URL url = new URL(ur);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Pattern pattern = Pattern.compile("(\"value\": \"(.*)\")");
            String uuidString = null;

            String line;
            while((line = in.readLine()) != null && uuidString == null) {
                if (!line.isEmpty()) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        uuidString = matcher.group(2);
                    }
                }
            }

            in.close();
            return uuidString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String getUuidFromMojang(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Pattern pattern = Pattern.compile("([0-9a-f]{32})");
            String uuidString = null;

            String line;
            while((line = in.readLine()) != null && uuidString == null) {
                if (!line.isEmpty()) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        uuidString = matcher.group(1);
                    }
                }
            }

            in.close();
            return uuidString;
        } catch (IOException var7) {
            return null;
        }
    }
}
