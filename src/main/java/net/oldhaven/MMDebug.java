package net.oldhaven;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.oldhaven.customs.util.JsonUtil;

import java.net.URL;

public class MMDebug {
    public static boolean enabled;
    public static String debugUserName;

    static boolean retrieveJson() {
        Class<MegaMod> mm = MegaMod.class;
        URL jsonFile = mm.getResource("/fabric.mod.json");
        if(jsonFile == null)
            return false;
        JsonObject j = JsonUtil.readJsonFromURL(jsonFile, JsonObject.class);
        if(j == null)
            return false;
        System.out.println(j.toString());
        JsonUtil.getElement(j, "version").<JsonPrimitive>success((obj) -> {
            System.out.println("VERSION: " + obj.getAsString());
            MegaMod.setVersion(obj.getAsString());
        });
        JsonUtil.getElement(j, "development_mode").<JsonPrimitive>success((obj) -> {
            MegaMod.setDevBuild(obj.getAsBoolean());
        });
        return true;
    }

    public static void println(Object obj) {
        if(MMDebug.enabled) {
            String s = String.valueOf(obj);
            System.out.println("(MMDebug) " + s);
        }
    }
    public static void printe() {
        if(MMDebug.enabled)
            System.out.println();
    }
}
