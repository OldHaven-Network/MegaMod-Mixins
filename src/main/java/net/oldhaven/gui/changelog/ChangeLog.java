package net.oldhaven.gui.changelog;

import net.oldhaven.MegaMod;
import net.oldhaven.SkinFix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class ChangeLog {
    private int green = 0x50b571;
    private int white = 0xffffff;
    private static LinkedList<TextField> textFields;
    static class TextField {
        int color;
        String string;
        TextField(String s, int color) {
            this.string = s;
            this.color = color;
        }
    }
    private boolean hasCheckedVersion = false;
    public static LinkedList<TextField> getChangelog() {
        return textFields;
    }
    public ChangeLog() {
        if(!MegaMod.isConnected())
            return;
        textFields = new LinkedList<>();
        try {
            URL url = new URL("https://www.oldhaven.net/megamod.txt");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while((str = in.readLine()) != null) {
                if(str.startsWith("#"))
                    continue;
                if(str.length() > 4) {
                    String sub = str.substring(0, 5);
                    if (sub.contains(".")) {
                        String ver = sub.replaceAll("[A-z ]", "");
                        if (ver.equals(sub)) {
                            if(!hasCheckedVersion && !MegaMod.version.equals(sub)) {
                                MegaMod.requiresUpdate = sub;
                            }
                            hasCheckedVersion = true;
                            textFields.addLast(new TextField(str, green));
                            continue;
                        }
                    }
                }
                textFields.addLast(new TextField(str, white));
            }
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
