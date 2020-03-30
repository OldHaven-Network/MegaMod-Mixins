package net.oldhaven.customs;

import net.oldhaven.gui.CustomEnumOptions;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomGameSettings {
    public File optionsFile;
    private Map<String, Object> map;
    public CustomGameSettings() {
        map = new HashMap<>();
    }

    public void setOptionBtn(String name) {
        int newValue = 1;
        if(map.containsKey(name))
            newValue = (Integer)map.get(name) == 1 ? 0 : 1;
        map.replace(name, newValue);
    }
    public void setOption(String name, Object obj) {
        map.remove(name);
        map.put(name, obj);
    }
    public Float getOptionF(String name) {
        Object obj = getOption(name);
        if(obj != null)
            return Float.valueOf(String.valueOf(obj));
        return null;
    }
    public Integer getOptionI(String name) {
        Object obj = getOption(name);
        if(obj != null)
            return Integer.valueOf(String.valueOf(obj));
        return null;
    }
    public Object getOption(String name) {
        if(!map.containsKey(name))
            return null;
        return map.get(name);
    }
    public void removeOption(String name) {
        map.remove(name);
    }

    public void saveSettings() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(optionsFile));
            for(Map.Entry<String, Object> entry : map.entrySet())
                printwriter.println(entry.getKey() + ":" + entry.getValue());
            printwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readSettings() {
        if(!optionsFile.exists())
            return;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            for (String s = ""; (s = reader.readLine()) != null; ) {
                String[] as = s.split(":");
                System.out.println(as[1].getClass().getName());
                map.put(as[0], as[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean save = false;
        for(int i=0;i < CustomEnumOptions.values().length;i++) {
            CustomEnumOptions enu = CustomEnumOptions.values()[i];
            String name = enu.getEnumString();
            float def = enu.getDefaultValue();
            if(def != -1.0158F && !map.containsKey(name)) {
                if(enu.getEnumBoolean() && !enu.getEnumFloat())
                    map.put(name, (int)def);
                else
                    map.put(name, def);
                save = true;
            }
        }
        if(save)
            this.saveSettings();
    }
}
