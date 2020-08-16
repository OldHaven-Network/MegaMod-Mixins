package net.oldhaven.customs;

import java.util.HashMap;

public class ItemKeep {
    private static HashMap<String, Integer> listID = new HashMap<>();
    private static HashMap<String, Integer> mapByStr = new HashMap<>();
    public static HashMap<String, Integer> getFullMap() {
        return mapByStr;
    }

    public static void add(String name, int id) {
        System.out.println(name + " " + id);
        mapByStr.put(name, id);
        listID.put(id+"", id);
    }
    public static int get(String s) {
        int id;
        if((id= getByStr(s)) != 0)
            return id;
        if((id= getByID(s)) != 0)
            return id;
        return 0;
    }
    public static int getByStr(String blockName) {
        if(mapByStr.containsKey(blockName))
            return mapByStr.get(blockName);
        return 0;
    }
    public static int getByID(String id) {
        if(listID.containsKey(id))
            return listID.get(id);
        return 0;
    }
}
