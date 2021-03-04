package net.oldhaven.customs.shaders.water;

import java.util.LinkedList;

public class CustomWaterColor {
    public static LinkedList<CustomWaterColor> list = new LinkedList<>();
    static {
        new CustomWaterColor("Bright Green", 0x03fc39, 0.00F, 0.02F, 0.00F);
        new CustomWaterColor("Red", 0xfc0303, 0.02F, 0.00F, 0.00F);
        new CustomWaterColor("Light Blue", 0x03dffc, 0.00F, 0.00F, 0.04F);
        new CustomWaterColor("Dark Blue", 0x2f5257, 0.00F, 0.00F, 0.01F);
        new CustomWaterColor("Transparent", 0x000000, 0.00F, 0.00F, 0.00F);
    }

    public static String[] toNameArray() {
        String[] array = new String[list.size()+1];
        array[0] = "OFF";
        int i = 1;
        for(CustomWaterColor waterColor : list) {
            array[i] = waterColor.getName();
            i++;
        }
        return array;
    }

    private String name;
    private float[] rgb;
    private int hex;
    private CustomWaterColor(String name, int hex, float r, float g, float b) {
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
