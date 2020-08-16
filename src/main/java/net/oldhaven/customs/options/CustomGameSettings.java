package net.oldhaven.customs.options;

import net.oldhaven.MegaMod;

import java.io.*;
import java.util.*;

public class CustomGameSettings {
    public File optionsFile;
    private LinkedHashMap<String, Object> map;
    public CustomGameSettings() {
        map = new LinkedHashMap<>();
    }

    public void setOptionBtn(String name) {
        int newValue = 1;
        if(map.containsKey(name)) {
            newValue = Integer.parseInt(map.get(name).toString()) == 1 ? 0 : 1;
        }
        ModOptions option = ModOptions.getOptionByName(name);
        if(option != null)
            option.setCurrentValue(newValue);
        map.replace(name, newValue);
    }
    public void setOption(String name, Object obj) {
        ModOptions option = ModOptions.getOptionByName(name);
        if(option != null)
            option.setCurrentValue(obj);
        map.remove(name);
        map.put(name, obj);
    }
    public String getOptionS(String name) {
        Object obj = getOption(name);
        if(obj != null)
            return String.valueOf(obj);
        return null;
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
            return Integer.parseInt(String.valueOf(obj));
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
            ModOptions.Section currentSection = null;
            Map<String, Object> disabled = new HashMap<>();
            int i = 0;
            for(ModOptions.Section section : ModOptions.getAllSections()) {
                for(ModOptions enumOption : section.getList()) {
                    if (enumOption != null) {
                        if (enumOption.isDisabled) {
                            disabled.put(enumOption.getName(), enumOption.getCurrentValue());
                            continue;
                        }
                        if (section != null && currentSection != section) {
                            if (i != 0)
                                printwriter.println(" ");
                            printwriter.println("[" + section.getName() + "]");
                            currentSection = section;
                        }
                        printwriter.println(enumOption.getName() + ":" + enumOption.getCurrentValue());
                        i++;
                    }
                }
            }
            printwriter.println(" ");
            printwriter.println("[DISABLED]");
            for(Map.Entry<String, Object> entry : disabled.entrySet()) {
                printwriter.println(entry.getKey() + ":" + entry.getValue());
            }
            printwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readSettings() {
        if(optionsFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
                for (String s; (s = reader.readLine()) != null; ) {
                    s = s.trim();
                    if (s.startsWith("SECTION"))
                        continue;
                    String[] as = s.split(":");
                    if (as.length > 1) {
                        map.put(as[0], as[1]);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean save = false;
        if(!map.containsKey("MM Version")) {
            MegaMod.hasUpdated = true;
        } else {
            if(!String.valueOf(map.get("MM Version")).equals(MegaMod.version)) {
                System.out.println(map.get("MM Version"));
                MegaMod.hasUpdated = true;
                map.replace("MM Version", ModOptions.getOptionByName("MM Version").getDefaultValueString());
                save = true;
            }
        }
        ModOptions.Section currentSection = null;
        for(ModOptions enu : ModOptions.getList()) {
            String name = enu.getName();
            float f = enu.getDefaultValue();
            if(f != -1.0158F && !map.containsKey(name)) {
                switch(enu.getStyle()) {
                    case BOOL:
                    case INTEGER:
                        map.put(name, (int) f);break;
                    case FLOAT:
                        map.put(name, f);break;
                    default:
                        map.put(name, enu.getDefaultValueString());break;
                }
                save = true;
            }
        }
        /* 0.4.0 fixes
        if(getOptionS("Button ADV Color").equalsIgnoreCase("(NOTWORKING)")) {
            setOption("Button ADV Color", "0xffffff");
            save = true;
        } */
        if(getOptionI("Enable WAILA") != null) {
            setOption("Toggle WAILA", getOptionI("Enable WAILA"));
            removeOption("Enable WAILA");
            save = true;
        }
        /* end 0.4.0 fixes */
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            ModOptions option = ModOptions.getOptionByName(name);
            if(option != null)
                option.setCurrentValue(entry.getValue());
        }
        if(save)
            this.saveSettings();
    }
}
