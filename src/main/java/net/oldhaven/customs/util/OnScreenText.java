package net.oldhaven.customs.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class OnScreenText {
    private int color;
    private String text;
    OnScreenText(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    static LinkedHashMap<String, OnScreenText> onScreenTextMap;
    public static Map<String, OnScreenText> getOnScreenText() {
        return onScreenTextMap;
    }
    public static void replaceOnScreenText(String name, String text, int color) {
        if(onScreenTextMap.containsKey(name))
            onScreenTextMap.get(name).setText(text);
        else
            showOnScreenText(name, text, color);
    }
    public static void showOnScreenText(String name, String text, int color) {
        if(!onScreenTextMap.containsKey(name))
            onScreenTextMap.put(name, new OnScreenText(text, color));
    }
    public static void hideOnScreenText(String name) {
        onScreenTextMap.remove(name);
    }
}
