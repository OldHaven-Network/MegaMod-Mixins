package net.oldhaven.customs.shaders.water;

import java.util.LinkedList;

public class CustomLavaColor {
    public static LinkedList<CustomLavaColor> list = new LinkedList<>();
    static {
        new CustomLavaColor("Deep Red", 0xff3224, 1.00F, 0.00F, 0.00F);
        new CustomLavaColor("Dark Red", 0x99003b, 1.75F, 0.00F, 0.25F);
        new CustomLavaColor("Blue", 0x449e9e, 0.00F, 0.00F, 0.25F);
        new CustomLavaColor("Yellow", 0xe3e356, 0.77F, 0.77F, 0.25F);
        new CustomLavaColor("Gray", 0x454545, 0.5F, 0.5F, 0.5F);
        new CustomLavaColor("Transparent", 0x000000, 0.00F, 0.00F, 0.00F);
    }

    public static String[] toNameArray() {
        String[] array = new String[list.size()+1];
        array[0] = "OFF";
        int i = 1;
        for(CustomLavaColor lavaColor : list) {
            array[i] = lavaColor.getName();
            i++;
        }
        return array;
    }

    private String name;
    private float[] rgb;
    private int hex;
    private CustomLavaColor(String name, int hex, float r, float g, float b) {
        this.hex = hex;
        this.rgb = new float[]{r,g,b};
        this.name = name;
        list.add(this);
    }

    public String getName() {
        return name;
    }
    public int getHex() {
        return hex;
    }
    public float[] getRgb() {
        return rgb;
    }
}
