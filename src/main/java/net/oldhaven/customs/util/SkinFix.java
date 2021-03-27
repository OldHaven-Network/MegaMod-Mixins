package net.oldhaven.customs.util;

import com.google.gson.*;
import net.minecraft.src.EntityPlayerSP;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.devpack.GenericSuccess;
import net.oldhaven.devpack.SkinImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    private static final Map<String, String> uuids = new HashMap<>();
    private static final Map<String, UserSkin> savedSkins = new HashMap<>();
    private static final Map<String, JsonObject> mcCapesProfile = new HashMap<>();
    private static final JsonObject dummyJsonObj = new JsonObject();

    public static class UserSkin {
        public boolean slim;

        public SkinImage skinEars;
        public SkinImage skinCape;
        public String skinUrl;
        UserSkin() {
            this.slim = false;
            this.skinUrl = null;
            this.skinCape = new SkinImage();
            this.skinEars = new SkinImage();
        }
    }

    public static boolean connected = false;
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

    public static void unload() {
        EntityPlayerSP sp = MMUtil.getMinecraftInstance().thePlayer;
        if(sp != null) {
            SkinFix.UserSkin userSkin = SkinFix.getUserSkin(sp.username);
            if(userSkin != null)
                sp.skinUrl = userSkin.skinUrl;
        }
        savedSkins.clear();
    }
    public static Boolean isSkinAlex(String name) {
        if(!ModOptions.ALEX_SKINS.getAsBool())
            return false;
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
            savedSkins.put(playerName, new UserSkin());
            return null;
        }
        String s = "https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false";
        UserSkin userSkin = getSkinFromUrl(s);
        savedSkins.put(playerName, userSkin);
        return userSkin;
    }

    private static boolean codeIs404(int code) {
        return code == 404 || code == 500 || code == 522;
    }
    private static boolean tryConnect(String urlS) {
        try {
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(!codeIs404(code)) {
                return true;
            } else {
                System.out.println("TRY CONNECT CODE : " + code);
            }
        } catch(IOException ignore) {}
        return false;
    }


    public static JsonObject getMinecraftCapesProfile(String uuid) {
        if(SkinFix.mcCapesProfile.containsKey(uuid))
            return SkinFix.mcCapesProfile.get(uuid);
        String urlBase = "https://minecraftcapes.net/profile/"+uuid+"/";
        if(tryConnect(urlBase)) {
            JsonObject jsonObject = SkinUtil.readJsonFromURL(urlBase);
            SkinFix.mcCapesProfile.put(uuid, jsonObject);
            return jsonObject;
        }
        SkinFix.mcCapesProfile.put(uuid, SkinFix.dummyJsonObj);
        return null;
    }

    public static SkinImage getCapeUrl(String playerName) {
        if(!connected || !ModOptions.SKIN_CAPE.getAsBool())
            return null;
        UserSkin userSkin = getUserSkin(playerName);
        SkinImage skinImage = userSkin.skinCape;
        if(skinImage.failed != null)
            return skinImage;
        System.out.println("2");
        String urlBase = "http://s.optifine.net/capes/"+playerName+".png";
        if(tryConnect(urlBase)) {
            skinImage.setImageUrl(urlBase);
            skinImage.setFailed(false);
            return skinImage;
        }
        System.out.println("3");
        String uuid = getUuidStringFromName(playerName);
        try {
            JsonObject jsonObject = getMinecraftCapesProfile(uuid);
            if (jsonObject != null) {
                getElement(jsonObject, "textures").<JsonObject>success((textures) -> {
                    String base64 = textures.get("cape").getAsString();

                    byte[] imageByte = Base64.getDecoder().decode(base64);
                    skinImage.setImage(getBufferFrom(imageByte));
                    skinImage.setFailed(false);
                }).failure((JsonElement element) -> {
                    skinImage.setFailed(true);
                });
                System.out.println(skinImage.image);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(skinImage.failed != null)
            skinImage.setFailed(true);
        return skinImage;
    }

    public static SkinImage getEarsImage(String playerName) {
        if(!connected || !ModOptions.SKIN_EARS.getAsBool())
            return null;
        UserSkin userSkin = getUserSkin(playerName);
        SkinImage skinImage = userSkin.skinEars;
        if(skinImage.failed != null)
            return skinImage;
        String uuid = getUuidStringFromName(playerName);
        try {
            JsonObject jsonObject = getMinecraftCapesProfile(uuid);
            if (jsonObject != null) {
                getElement(jsonObject, "textures").<JsonObject>success((textures) -> {
                    String base64 = textures.get("ears").getAsString();

                    byte[] imageByte = Base64.getDecoder().decode(base64);
                    skinImage.setImage(getBufferFrom(imageByte));
                    skinImage.setFailed(false);
                }).failure((JsonElement element) -> {
                    skinImage.setFailed(true);
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(skinImage.failed != null)
            skinImage.setFailed(true);
        return skinImage;
    }

    private static GenericSuccess<JsonElement> getElement(JsonObject from, String... toList) {
        JsonElement getter = null;
        for(String to : toList) {
            getter = from.get(to);
            if(getter == null)
                return GenericSuccess._RSC(null, false);
            if(getter.isJsonObject())
                from = getter.getAsJsonObject();
        }
        return GenericSuccess._RSC(getter, getter != null && !getter.isJsonNull());
    }

    private static BufferedImage getBufferFrom(byte[] imgBytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imgBytes);
            BufferedImage bufferIn = ImageIO.read(bis);
            bis.close();
            return bufferIn;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static UserSkin getSkinFromUrl(String ur) {
        UserSkin userSkin = new UserSkin();
        JsonObject json = SkinUtil.readJsonFromURL(ur);
        if(json == null)
            return userSkin;
        if(json.get("properties") != null) {
            JsonObject properties =
                    getElement(json, "properties")
                    .<JsonArray>retreive().get(0).getAsJsonObject();
            if(properties.get("signature") == null)
                return userSkin;
            if(SkinUtil.verifySig(properties)) { /*verified*/
                JsonObject user = MMUtil.gson.fromJson(
                    new String(
                        Base64.getDecoder().decode(
                            getElement(properties, "value")
                                .<JsonPrimitive>retreive().getAsString()
                                .getBytes(StandardCharsets.UTF_8)
                        )
                    ), JsonObject.class
                );
                getElement(user, "textures").<JsonObject>success((textures) -> {
                    JsonObject skin = getElement(textures, "SKIN").retreive();
                    getElement(textures, "EARS")
                            .<String>success((ears) -> userSkin.skinEars.setImageUrl(ears));
                    getElement(textures, "CAPE")
                            .<String>success((cape) -> userSkin.skinCape.setImageUrl(cape));
                    userSkin.skinUrl = getElement(skin, "url").<JsonPrimitive>retreive().getAsString();
                    getElement(skin, "metadata", "model").<JsonPrimitive>success((prim) -> {
                        String alex = prim.getAsString();
                        userSkin.slim = alex.equalsIgnoreCase("slim");
                    });
                });
            }
        }
        return userSkin;
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
