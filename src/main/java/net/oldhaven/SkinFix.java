package net.oldhaven;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
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

    private static Map<String, UserSkin> savedSkins = new HashMap<>();
    public static Boolean isSkinAlex(String name) {
        UserSkin userSkin = getUserSkin(name);
        if(userSkin == null)
            return false;
        return userSkin.slim;
    }

    public static UserSkin getUserSkin(String playerName) {
        if(playerName == null)
            return null;
        if(savedSkins.containsKey(playerName))
            return savedSkins.get(playerName);
        String uuid = getUuidStringFromName(playerName);
        if(uuid == null) {
            savedSkins.put(playerName, new UserSkin(null, null, false));
            return null;
        }
        String s = "https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false";
        UserSkin userSkin = getSkinFromUrl(s);
        savedSkins.put(playerName, userSkin);
        return userSkin;
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
        UserSkin userSkin = getUserSkin(playerName);
        if(userSkin != null && userSkin.capeUrl != null) {
            if(tryConnect(userSkin.capeUrl))
                return userSkin.capeUrl;
        }
        String urlBase;
        String toSend;
        urlBase = "http://s.optifine.net/capes/";
        toSend = playerName + ".png";
        if(tryConnect(urlBase + toSend))
            return urlBase + toSend;
         urlBase = "https://minecraftcapes.co.uk/getCape/";
        toSend = uuid;
        if(tryConnect(urlBase + toSend))
            return urlBase + toSend;
        return null;
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

    /**
     * Some code from here to help with doing signature stuff:
     * https://github.com/Steveice10/MCAuthLib/blob/master/src/main/java/com/github/steveice10/mc/auth/data/GameProfile.java
     */
    private static final PublicKey SIGNATURE_KEY;
    static {
        try(InputStream in = SkinFix.class.getResourceAsStream("/yggdrasil_session_pubkey.der")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int length = -1;
            while((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            out.close();

            SIGNATURE_KEY = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
        } catch(Exception e) {
            throw new ExceptionInInitializerError("Missing/invalid yggdrasil public key.");
        }
    }

    public static class UserSkin {
        public final boolean slim;
        public final String skinUrl;
        private final String capeUrl;
        UserSkin(String skinUrl, String capeUrl, boolean slim) {
            this.slim = slim;
            this.skinUrl = skinUrl;
            this.capeUrl = capeUrl;
        }
    }
    private static Gson gson = new Gson();
    private static UserSkin getSkinFromUrl(String ur) {
        try {
            URL url = new URL(ur);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder lines = new StringBuilder();
            String line;
            while((line = in.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.append(line);
                }
            }
            in.close();

            JsonObject json = gson.fromJson(lines.toString(), JsonObject.class);
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(SIGNATURE_KEY);
            if(json.get("properties") != null) {
                JsonObject properties = ((JsonArray)json.get("properties")).get(0).getAsJsonObject();
                String value = properties.get("value").getAsString();
                if(properties.get("signature") == null)
                    return null;
                String yourSig = properties.get("signature").getAsString();
                sig.update(value.getBytes());
                boolean b = sig.verify(Base64.getDecoder().decode(yourSig.getBytes(StandardCharsets.UTF_8)));
                if(b) { /*verified*/
                    String toJson = new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)));
                    JsonObject user = gson.fromJson(toJson, JsonObject.class);
                    JsonObject textures = user.get("textures").getAsJsonObject();
                    JsonObject skin = textures.get("SKIN").getAsJsonObject();
                    String capeUrl = null;
                    if(textures.get("CAPE") != null) {
                        capeUrl = textures.get("CAPE").getAsJsonObject().get("url").getAsString();
                    }
                    String skinUrl = skin.get("url").getAsString();
                    if(skin.get("metadata") != null) {
                        String alex = skin.get("metadata").getAsJsonObject().get("model").getAsString();
                        if(alex.equalsIgnoreCase("slim")) {
                            return new UserSkin(skinUrl, capeUrl,true);
                        }
                    }
                    return new UserSkin(skinUrl, capeUrl, false);
                }
            }
            return null;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return new UserSkin(null, null, false);
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
