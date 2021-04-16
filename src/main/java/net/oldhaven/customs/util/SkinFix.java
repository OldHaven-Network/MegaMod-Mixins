package net.oldhaven.customs.util;

import com.google.gson.*;
import net.minecraft.src.EntityPlayerSP;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.devpack.SingleCallback;
import net.oldhaven.devpack.SkinImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkinFix {
    private static final Map<String, String> uuids = new HashMap<>();
    private static final Map<String, UserSkin> savedSkins = new HashMap<>();
    private static final Map<String, JsonObject> mcCapesProfile = new ConcurrentHashMap<>();
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
    private static void tryConnect(String urlStr, SingleCallback<URL> success, Runnable fail) {
        SkinUtil.executor.execute(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int code = connection.getResponseCode();
                if(!codeIs404(code)) {
                    success.run(url);
                    return;
                }
            } catch(IOException ignore) {}
            if(fail != null)
                fail.run();
        });
    }
    private static void tryConnect(String urlStr, SingleCallback<URL> success) {
        tryConnect(urlStr, success, null);
    }

    public static void getMinecraftCapesProfile(final String uuid, final SingleCallback<JsonObject> success) {
        if(SkinFix.mcCapesProfile.containsKey(uuid)) {
            success.run(SkinFix.mcCapesProfile.get(uuid));
            return;
        }
        String urlBase = "https://minecraftcapes.net/profile/"+uuid+"/";
        tryConnect(urlBase, (url) -> {
            JsonObject jsonObject = JsonUtil.readJsonFromURL(url, JsonObject.class);
            SkinFix.mcCapesProfile.put(uuid, jsonObject);
            success.run(jsonObject);
        }, () -> SkinFix.mcCapesProfile.put(uuid, SkinFix.dummyJsonObj));
    }

    private static final Map<String, SkinImage> skinImageMap = new HashMap<>();
    public static SkinImage getSkinUrl(String url) {
        if(skinImageMap.containsKey(url))
            return skinImageMap.get(url);
        SkinImage image = new SkinImage();
        SkinUtil.getSkinFromUrl(url, (img) -> {
            image.setImage(img);
            image.setFailed(img == null);
        });
        skinImageMap.put(url, image);
        return image;
    }

    public static SkinImage getCapeUrl(String playerName) {
        if(!connected || !ModOptions.SKIN_CAPE.getAsBool())
            return null;
        UserSkin userSkin = getUserSkin(playerName);
        final SkinImage skinImage = userSkin.skinCape;
        if(skinImage.failed != null)
            return skinImage;
        final String urlBase = "http://s.optifine.net/capes/"+playerName+".png";
        tryConnect(urlBase, (url) -> {
            skinImage.setImageUrl(urlBase);
            skinImage.setFailed(false);
        }, () -> {
            String uuid = getUuidStringFromName(playerName);
            try {
                getMinecraftCapesProfile(uuid, (jsonObject) -> {
                    JsonUtil.getElement(jsonObject, "textures").<JsonObject>success((textures) -> {
                        JsonUtil.getElement(textures, "cape").<JsonElement>success((element) -> {
                            String base64 = element.getAsString();

                            byte[] imageByte = Base64.getDecoder().decode(base64);
                            skinImage.setImage(getBufferFrom(imageByte));
                            skinImage.setFailed(false);
                        });
                    }).failure((JsonElement element) -> {
                        skinImage.setFailed(true);
                    });
                });
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
        if(skinImage.failed == null)
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
            getMinecraftCapesProfile(uuid, (jsonObject) -> {
                JsonUtil.getElement(jsonObject, "textures").<JsonObject>success((textures) -> {
                    JsonUtil.getElement(textures, "ears").<JsonElement>success((element) -> {
                        String base64 = element.getAsString();
                        byte[] imageByte = Base64.getDecoder().decode(base64);
                        skinImage.setImage(getBufferFrom(imageByte));
                        skinImage.setFailed(false);
                    });
                }).failure((JsonElement element) -> {
                    skinImage.setFailed(true);
                });
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(skinImage.failed == null)
            skinImage.setFailed(true);
        return skinImage;
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
        JsonObject json = null;
        try {
            json = JsonUtil.readJsonFromURL(new URL(ur), JsonObject.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(json == null)
            return userSkin;
        if(json.get("properties") != null) {
            JsonObject properties =
                    JsonUtil.getElement(json, "properties")
                    .<JsonArray>retreive().get(0).getAsJsonObject();
            if(properties.get("signature") == null)
                return userSkin;
            if(SkinUtil.verifySig(properties)) { /*verified*/
                JsonObject user = JsonUtil.gson.fromJson(
                    new String(
                        Base64.getDecoder().decode(
                            JsonUtil.getElement(properties, "value")
                                .<JsonPrimitive>retreive().getAsString()
                                .getBytes(StandardCharsets.UTF_8)
                        )
                    ), JsonObject.class
                );
                JsonUtil.getElement(user, "textures").<JsonObject>success((textures) -> {
                    JsonObject skin = JsonUtil.getElement(textures, "SKIN").retreive();
                    JsonUtil.getElement(textures, "EARS")
                            .<String>success((ears) -> userSkin.skinEars.setImageUrl(ears));
                    JsonUtil.getElement(textures, "CAPE")
                            .<String>success((cape) -> userSkin.skinCape.setImageUrl(cape));
                    userSkin.skinUrl = JsonUtil.getElement(skin, "url").<JsonPrimitive>retreive().getAsString();
                    JsonUtil.getElement(skin, "metadata", "model").<JsonPrimitive>success((prim) -> {
                        String alex = prim.getAsString();
                        userSkin.slim = alex.equalsIgnoreCase("slim");
                    });
                });
            }
        }
        return userSkin;
    }
    private static String getUuidFromMojang(String playerName) {
        long time = System.currentTimeMillis();
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
