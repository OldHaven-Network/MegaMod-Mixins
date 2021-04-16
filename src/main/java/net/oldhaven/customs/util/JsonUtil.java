package net.oldhaven.customs.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.oldhaven.devpack.GenericSuccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonUtil {
    public static final Gson gson = new Gson();

    public static <J> J readJsonFromURL(URL url, Class<J> type) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.append(line);
                }
            }
            in.close();
            return gson.fromJson(lines.toString(), type);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GenericSuccess<JsonElement> getElement(JsonObject from, String... toList) {
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
}
