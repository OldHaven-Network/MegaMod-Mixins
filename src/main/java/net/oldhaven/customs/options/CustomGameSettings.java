package net.oldhaven.customs.options;

import net.oldhaven.MMDebug;
import net.oldhaven.MegaMod;

import java.io.*;
import java.util.*;

public class CustomGameSettings {
    public File optionsFile;
    private LinkedHashMap<String, Object> map;
    public CustomGameSettings() {
        map = new LinkedHashMap<>();
    }

//region SETTERS
    public void setOptionBtn(String name) {
        int newValue = 1;
        if(map.containsKey(name)) {
            String obj = map.get(name).toString();
            try {
                newValue = Integer.parseInt(obj) == 1 ? 0 : 1;
            } catch(NumberFormatException e) {
                newValue = Boolean.parseBoolean(obj) ? 1 : 0;
            }
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
    public void removeOption(String name) {
        map.remove(name);
    }
//endregion

//region GETTERS
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
//endregion

//region READ_WRITE
    public void saveSettings() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(optionsFile));
            ModOptions.Section currentSection = null;
            Map<String, Object> disabled = new HashMap<>();
            int i = 0;
            for(ModOptions.Section section : ModOptions.getAllSections()) {
                for(ModOptions setting : section.getList()) {
                    if (setting != null) {
                        if (setting.isDisabled) {
                            disabled.put(setting.getName(), setting.getCurrentValue());
                            continue;
                        }
                        if (currentSection != section) {
                            if (i != 0)
                                printwriter.println(" ");
                            printwriter.println("[" + section.getName() + "]");
                            currentSection = section;
                        }
                        if(!setting.getDescription().isEmpty())
                            printwriter.println("# " + setting.getDescription());
                        else
                            printwriter.println("# No description for " + setting.getName());
                        printwriter.println(setting.getName() + ":" + setting.getCurrentValue());
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
                    if(s.startsWith("#") || s.startsWith("SECTION"))
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
                MegaMod.hasUpdated = true;
                map.replace("MM Version", ModOptions.getOptionByName("MM Version").getDefaultValue());
                save = true;
            }
        }
        for(ModOptions opt : ModOptions.getList()) {
            String name = opt.getName();
            if(!map.containsKey(name)) {
                map.put(name, opt.getDefaultValue());
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
        if(MMDebug.enabled)
            System.out.println(" --- SETTINGS READ --- ");
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            ModOptions option = ModOptions.getOptionByName(name);
            if(option != null) {
                String value = entry.getValue().toString();
                if(MMDebug.enabled)
                    System.out.println(option.getName() + " : " + value);
                switch (option.getStyle()) {
                    case BOOL:
                        try {
                            int i = Integer.parseInt(value);
                            option.setCurrentValue(i == 1);
                        } catch(NumberFormatException e) {
                            option.setCurrentValue(Boolean.valueOf(value));
                        }
                        break;
                    case FLOAT:
                        option.setCurrentValue(Float.valueOf(value));
                        break;
                    case INTEGER:
                        option.setCurrentValue(Integer.valueOf(value));
                        break;
                    default:
                        option.setCurrentValue(entry.getValue());
                        break;
                }
            }
        }
        if(MMDebug.enabled)
            System.out.println(" --- SETTINGS END --- ");
        if(save)
            this.saveSettings();
    }
//endregion

}
