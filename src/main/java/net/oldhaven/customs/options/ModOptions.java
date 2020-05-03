package net.oldhaven.customs.options;

import net.oldhaven.MegaMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ModOptions {
    MM_VERSION(new ModOption("MM Version", "MegaMod", false), MegaMod.version, 0, 5),

    BRIGHTNESS(new ModOption("Brightness", "Mod", true), true, false),
    FIELD_OF_VIEW(new ModOption("Field of View", "Mod", false), true, false, "70", 70.0F, "", 70.0F, 0.1F),
    FLY_SPEED(new ModOption("Fly Speed", "Mod", false), true, false, "OFF", 100.0F, "%", 0.0F, 0.25F),

    SHADOW_DENSITY(new ModOption("Shadow Density", "Shader", false), true, false, "DEFAULT", 20.0F, "", 1.0F, 0),
    SHADERS(new ModOption("Shaders", "Shader", false), true, false, "OFF", 3.0F, "", 0.0F, 0.0F,
            new String[]{
                    "OFF", "Checkerboard", "Faked-Real", "Outline"
            }),
    WATER_COLOR(new ModOption("Water Color Hex", "Shader", null), "", 0, 6),
    LAVA_COLOR(new ModOption("Lava Color Hex", "Shader", null), "", 0, 6),
    DYNAMIC_LIGHTING(new ModOption("Dynamic Lighting", "Shader", false), false, true, 0),

    CLOUD_HEIGHT(new ModOption("Cloud Height", "Sky", false), true, false, "OFF", 140.0F, "", 60.0F, 0.75F),
    FORCE_TIME(new ModOption("Force Time", "Sky", false), true, false, "OFF", 24000.0F, " Ticks", 0.0F, 0.0F),
    TOGGLE_RAINSNOW(new ModOption("Toggle Rain/Snow", "Sky", false), false, true, 1),

    ENABLE_FANCY_TREES(new ModOption("Enable Fancy Trees", "Blocks", false), false, true, 1),
    DISABLE_WATER_ANIMATION(new ModOption("Disable Water Animation", "Blocks", false), false, true, 0),
    DISABLE_LADDERS(new ModOption("Disable Ladders", "Blocks", false), false, true, 0),

    SKIN_HAT(new ModOption("Hat", "Skin", false), false, true, 1),
    SKIN_CAPE(new ModOption("Cape", "Skin", false), false, true, 1),
    SKIN_JACKET(new ModOption("Jacket", "Skin", false), false, true, 1),
    SKIN_LEFT_SLEEVE(new ModOption("Left Sleeve", "Skin", false), false, true, 1),
    SKIN_RIGHT_SLEEVE(new ModOption("Right Sleeve", "Skin", false), false, true, 1),
    SKIN_LEFT_PANTS_LEG(new ModOption("Left Pants Leg", "Skin", false), false, true, 1),
    SKIN_RIGHT_PANTS_LEG(new ModOption("Right Pants Leg", "Skin", false), false, true, 1),
    SKIN_DEFAULT_CAPE(new ModOption("Default Cape", "Skin", false), true, false, "Minecraft", 2.0F, "", 0.0F, 0.0F,
            new String[]{
                    "Minecraft", "Optifine", "MCCapes"
            }),

    SUBTITLES(new ModOption("Subtitles", "GUI", false), false, true, 0),
    CHAT_BG_OPACITY(new ModOption("Chat BG Opacity", "GUI", false), true, false, 0.0F),

    SHOW_TOOLTIP(new ModOption("Show Tooltip", "GUI", false), false, true, 1),
    TOGGLE_WAILA(new ModOption("Toggle WAILA", "GUI", false), false, true, 1),
    TOGGLE_XPBAR(new ModOption("Toggle XP-Bar", "GUI", true), false, true, 1),
    SHOW_MAIN_MENU_QUIT_BUTTON(new ModOption("Show Main Menu Quit Btn", "GUI", false), false, true, 1),
    SHOW_MOTION_IN_GAME(new ModOption("Show Motion In-Game", "GUI", false), false, true, 0),
    SHOW_SPEED_IN_GAME(new ModOption("Show Speed In-Game", "GUI", false), false, true, 0),
    DEFAULT_MAIN_MENU_BG(new ModOption("Default Main Menu BG", "GUI", false), false, true, 0),
    BUTTON_OUTLINE(new ModOption("Button Outline", "GUI", false), true, false, "OFF", 11.0F, "", 0.0F, 0.0F,
            new String[]{
                    "White", "Blue", "Purple", "Red", "Aqua", "Green", "Yellow", "Orange", "Light Grey", "Grey", "Black", "ADVANCED"
            }),
    BUTTON_ADV_COLOR(new ModOption("Button ADV Color", "GUI", false), "0xffffff", 2, 8),
    THIRDPERSON_DISTANCE(new ModOption("ThirdPerson Distance", "GUI", false), true, false, "FAR", 29.0F, "", 1.0F, 1.0F),

    DOUBLE_JUMP_TO_FLY(new ModOption("Double Jump to Fly", "Keybinds", false), false, true, 0),
    HOLD_SPRINT(new ModOption("Hold Sprint", "Keybinds", false), false, true, 0),
    HOLD_SNEAK(new ModOption("Hold Sneak", "Keybinds", false), false, true, 0),
    DISABLE_PLAYERLIST(new ModOption("Disable PlayerList", "Keybinds", false), false, true, 0),
    DISABLE_SPRINT(new ModOption("Disable Sprint", "Keybinds", false), false, true, 0),
    DISABLE_FLY(new ModOption("Disable Fly", "Keybinds", false), false, true, 0),
    DISABLE_ZOOM(new ModOption("Disable Zoom", "Keybinds", false), false, true, 0);

    private final boolean isFloat;
    private final boolean isBoolean;
    private final String name;
    private int defaultValueInt = -83;
    private float defaultValue = -1.0158F;
    private String defaultValueString = "";
    private String startString = "OFF";
    private float start = 0.0F;
    private float times = 100.0F;
    private String slideEnd = "%";
    private float add = 0.0F;
    private String[] values = null;
    private int minString = 0;
    private int maxString = 20;
    private int ordinal = 0;
    private Object currentValue;

    private Section currentSection = null;
    public static class Section {
        private List<ModOptions> list;
        private String name;
        Section(String name) {
            list = new ArrayList<>();
            this.name = name;
        }
        Section add(ModOptions option) {
            this.list.add(option);
            return this;
        }
        public List<ModOptions> getList() {
            return list;
        }
        public String getName() {
            return name;
        }
    }
    public boolean disabled;
    private ModOptions setDisabled() {
        this.disabled = true;
        return this;
    }
    public static List<Section> getAllSections() {
        return ModOption.sectionList;
    }
    public Section getSection() {
        return currentSection;
    }
    public static Section getSectionByName(String name) {
        if(!ModOption.sectionMap.containsKey(name))
            return null;
        return ModOption.sectionMap.get(name);
    }
    public static Map<String, Section> getModOption() {
        return ModOption.sectionMap;
    }

    public static ModOptions getOptionByInt(int var0) {
        for(ModOptions enumOption : ModOption.list) {
            if(enumOption.getOrdinal() == var0)
                return enumOption;
        }
        return null;
    }
    public static List<ModOptions> getList() {
        return  ModOption.list;
    }

    public static ModOptions getOptionByName(String name) {
        return  ModOption.mapToList.get(name);
    }
    

    private ModOptions(ModOption modOption, boolean floa, boolean bool) {
        this.name = modOption.getName();
        this.isFloat = floa;
        this.isBoolean = bool;
        update(this.name);
        String name = modOption.getSection();
        if(ModOption.sectionMap.containsKey(name))
            ModOption.sectionMap.get(name).list.add(this);
        else
            ModOption.sectionMap.put(name, new Section(name).add(this));
        if(!ModOption.sectionList.contains(ModOption.sectionMap.get(name)))
            ModOption.sectionList.add(ModOption.sectionMap.get(name));
        this.ordinal = ModOption.sectionMap.get(name).list.size();
        this.currentSection = ModOption.sectionMap.get(name);
        if(modOption.disabled == null || modOption.isDisabled()) {
            modOption.disabled = true;
            this.setDisabled();
        }
    }
    private void update(String name) {
        ModOption.mapToList.put(name, this);
        ModOption.list.add(this);
    }
    private ModOptions(ModOption modOption, boolean floa, boolean bool, int valueInt) {
        this(modOption, floa, bool);
        this.defaultValue = valueInt;
    }
    private ModOptions(ModOption modOption, boolean floa, boolean bool, float valueFloat) {
        this(modOption, floa, bool);
        this.defaultValue = valueFloat;
    }
    private ModOptions(ModOption modOption, String valueString, int min, int max) {
        this(modOption, false, false);
        this.defaultValueString = valueString;
        this.defaultValue = 0.0F;
        this.minString = min;
        this.maxString = max;
    }
    private ModOptions(ModOption modOption, boolean floa, boolean bool, String startString, Float times, String end, Float add, float defaultValue) {
        this(modOption, floa, bool);
        this.startString = startString;
        if(times != null)
            this.times = times;
        if(end != null)
            this.slideEnd = end;
        if(add != null)
            this.add = add;
        this.defaultValue = defaultValue;
    }
    private ModOptions(ModOption modOption, boolean floa, boolean bool, String startString, Float times, String end, Float add, float defaultValue, String[] values) {
        this(modOption, floa, bool, startString, times, end, add, defaultValue);
        this.values = values;
    }

    public Object getCurrentValue() {
        return currentValue;
    }
    public String getAsString() {
        return String.valueOf(currentValue);
    }
    public int getAsInt() {
        return Integer.parseInt(getAsString());
    }
    public float getAsFloat() {
        return Float.parseFloat(getAsString());
    }

    void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }

    public int getMaxString() {
        return maxString;
    }

    public int getMinString() {
        return minString;
    }

    public String[] getValues() {
        return values;
    }

    public boolean getFloat() {
        return this.isFloat;
    }

    public boolean getBoolean() {
        return this.isBoolean;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public String getName() {
        return this.name;
    }

    public String getSlideEnd() {
        return slideEnd;
    }

    public float getTimes() {
        return times;
    }

    public String getStartString() {
        return startString;
    }

    public float getAdd() { return add; }

    public String getDefaultValueString() {
        return defaultValueString;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    static class ModOption {
        static Map<String, ModOptions> mapToList = new HashMap<>();
        static List<ModOptions> list = new ArrayList<>();
        static List<Section> sectionList = new ArrayList<>();
        static Map<String, Section> sectionMap = new HashMap<>();

        private String name;
        private String section;
        private Boolean disabled;
        public ModOption(String s, String section, Boolean disabled) {
            this.name = s;
            this.section = section;
            this.disabled = disabled;
        }


        public String getName() {
            return name;
        }

        public String getSection() {
            return section;
        }

        public Boolean isDisabled() {
            return disabled;
        }
    }
}
