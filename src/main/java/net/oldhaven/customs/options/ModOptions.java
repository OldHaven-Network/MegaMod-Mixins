package net.oldhaven.customs.options;
import net.oldhaven.MegaMod;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>MegaMod ModOptions</h1>
 * <p>
 *     This class controls everything about
 *     MegaMod's settings and options, including
 *     turning on and off each option, all
 *     option names, how they react, and more
 * </p>
 * {@code aaaaaaaaaaaaa}
 * @see ModOption
 * @see Section
 * @see Style
 */
public enum ModOptions {
    MM_VERSION(new ModOption("MM Version", "MegaMod", false), MegaMod.version, 0, 5),

    BRIGHTNESS(new ModOption("Brightness", "Mod", true), Style.FLOAT),
    FIELD_OF_VIEW(new ModOption("Field of View", "Mod", false), Style.FLOAT, "70", 70.0F, "", 70.0F, 0.1F),
    FLY_SPEED(new ModOption("Fly Speed", "Mod", false), Style.FLOAT, "OFF", 100.0F, "%", 0.0F, 0.25F),

    SHADERS(new ModOption("Shaders", "Shader", false), Style.FLOAT, "OFF", 4.0F, "", 0.0F, 0.0F,
            new String[]{
                    "OFF", "GLSL", "Checkerboard", "Faked-Real", "Outline"
            }),
    SHADOW_DENSITY(new ModOption("Shadow Density", "ShaderNonGLSL", false), Style.FLOAT, "DEFAULT", 20.0F, "", 1.0F, 0),
    DYNAMIC_LIGHTING(new ModOption("Dynamic Lighting", "ShaderNonGLSL", false), Style.BOOL, 0),
    WATER_COLOR(new ModOption("Water Color Hex", "Shader", null), "", 0, 6),
    LAVA_COLOR(new ModOption("Lava Color Hex", "Shader", null), "", 0, 6),

    CLOUD_HEIGHT(new ModOption("Cloud Height", "Sky", false), Style.FLOAT, "OFF", 140.0F, "", 60.0F, 0.75F),
    FORCE_TIME(new ModOption("Force Time", "Sky", false), Style.FLOAT, "OFF", 24000.0F, " Ticks", 0.0F, 0.0F),
    TOGGLE_RAINSNOW(new ModOption("Toggle Rain/Snow", "Sky", false), Style.BOOL, 1),

    RANDOM_TALLERGRASS(new ModOption("Random Taller-Grass", "Blocks", false), Style.BOOL, 0),
    ENABLE_FANCY_TREES(new ModOption("Fancy Trees", "Blocks", false), Style.FLOAT, "OFF", 1.0F, "", 0.0F, 0.0F,
            new String[]{
                    "Fast", "Fancy"
            }),
    DISABLE_WATER_ANIMATION(new ModOption("Disable Water Animation", "Blocks", false), Style.BOOL, 0),
    DISABLE_LADDERS(new ModOption("Disable Ladders", "Blocks", false), Style.BOOL, 0),

    CUSTOM_SKIN(new ModOption("Alex Skins", "Skin", false), Style.BOOL, 1),
    SKIN_HAT(new ModOption("Hat", "Skin", false), Style.BOOL, 1),
    SKIN_CAPE(new ModOption("Cape", "Skin", false), Style.BOOL, 1),
    SKIN_JACKET(new ModOption("Jacket", "Skin", false), Style.BOOL, 1),
    SKIN_LEFT_SLEEVE(new ModOption("Left Sleeve", "Skin", false), Style.BOOL, 1),
    SKIN_RIGHT_SLEEVE(new ModOption("Right Sleeve", "Skin", false), Style.BOOL, 1),
    SKIN_LEFT_PANTS_LEG(new ModOption("Left Pants Leg", "Skin", false), Style.BOOL, 1),
    SKIN_RIGHT_PANTS_LEG(new ModOption("Right Pants Leg", "Skin", false), Style.BOOL, 1),
    SKIN_DEFAULT_CAPE(new ModOption("Default Cape", "Skin", false), Style.FLOAT, "Minecraft", 2.0F, "", 0.0F, 0.0F,
            new String[]{
                    "Minecraft", "Optifine", "MCCapes"
            }),

    DEFAULT_MAIN_MENU_BG(new ModOption("Default Main Menu BG", "GuiScreen", false), Style.BOOL, 0),
    SHOW_MAIN_MENU_QUIT_BUTTON(new ModOption("Show Main Menu Quit Btn", "GuiScreen", false), Style.BOOL, 1),
    DISABLE_MULTIPLAYER_GUI(new ModOption("Disable Custom MP GUI", "GuiScreen", false), Style.BOOL, 0),

    //SUBTITLES(new ModOption("Subtitles", "GUI", false), ModStyle.BOOL, 0),
    MODERN_TOOLTIPS(new ModOption("Modern Tooltips", "GUI", false), Style.BOOL, 1),
    CHAT_BG_OPACITY(new ModOption("Chat BG Opacity", "GUI", false), Style.FLOAT, 0.0F),
    //SHOW_TOOLTIP(new ModOption("Show Tooltip", "GUI", false), ModStyle.BOOL, 1),
    TOGGLE_WAILA(new ModOption("Toggle WAILA", "GUI", false), Style.BOOL, 1),
    TOGGLE_XPBAR(new ModOption("Toggle XP-Bar", "GUI", true), Style.BOOL, 1),
    SHOW_MOTION_IN_GAME(new ModOption("Show Motion In-Game", "GUI", false), Style.BOOL, 0),
    SHOW_SPEED_IN_GAME(new ModOption("Show Speed In-Game", "GUI", false), Style.BOOL, 0),
    BUTTON_OUTLINE_HEX(new ModOption("Button Outline Hex", "GUI", null), "", 0, 6),

    THIRDPERSON_DISTANCE(new ModOption("ThirdPerson Distance", "Player", false), Style.FLOAT, "FAR", 29.0F, "", 1.0F, 0.0F),

    DOUBLE_JUMP_TO_FLY(new ModOption("Double Jump To Fly", "Keybinds", false), Style.BOOL, 0),
    DOUBLE_TAP_TO_SPRINT(new ModOption("Double Tap To Sprint", "Keybinds", false), Style.BOOL, 0),
    HOLD_SPRINT(new ModOption("Hold Sprint", "Keybinds", false), Style.BOOL, 0),
    HOLD_SNEAK(new ModOption("Hold Sneak", "Keybinds", false), Style.BOOL, 0),
    DISABLE_PLAYERLIST(new ModOption("Disable PlayerList", "Keybinds", false), Style.BOOL, 0),
    DISABLE_SPRINT(new ModOption("Disable Sprint", "Keybinds", false), Style.BOOL, 0),
    DISABLE_FLY(new ModOption("Disable Fly", "Keybinds", false), Style.BOOL, 0),
    DISABLE_ZOOM(new ModOption("Disable Zoom", "Keybinds", false), Style.BOOL, 0),

    RICH_PRESENCE(new ModOption("Rich Presence", "Discord", false), Style.BOOL, 0),

    SP_CHEATS(new ModOption("SP Cheats", "SP", false), Style.BOOL, 0);

    private final Style style;
    public boolean isDisabled;

    private final String name;
    private String[] values = null;
    private String defaultValueString = "";
    private String startString =        "OFF";
    private String slideEnd =           "%";

    private float defaultValue =        -1.0158F;
    private float start, add =          0.0F;
    private float times =               100.0F;

    private int defaultValueInt = -83;
    private int minString, ordinal;
    private int maxString =             20;

    private Object currentValue;
    private Section currentSection;

    public Style getStyle() {
        return style;
    }

    /**
     * MegaMod's ModOptions Style is how
     * mods are used in GUI, if they
     * should be a slider, button or
     * something else.
     */
    public enum Style {
        BOOL, INTEGER, FLOAT, STRING
    }

    /**
     * MegaMod's ModOptions Section
     * controls how each option is
     * visible to which gui
     */
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

    /**
     * Get all concurring sections in MegaMod
     * @return List of all sections
     * @see Section
     */
    public static List<Section> getAllSections() {
        return ModOption.sectionList;
    }

    /**
     * Get section of this current option
     * @return Section.class
     * @see Section
     */
    public Section getSection() {
        return currentSection;
    }

    /**
     * Get a section by name, if exists
     * @param name sectionName
     * @return Section.class
     * @see Section
     */
    public static Section getSectionByName(String name) {
        if(!ModOption.sectionMap.containsKey(name))
            return null;
        return ModOption.sectionMap.get(name);
    }

    /**
     * Get all ModOptions
     * @return List of ModOptions
     */
    public static List<ModOptions> getList() {
        return  ModOption.list;
    }

    /**
     * Get a ModOption by name
     * @param name modOptionName
     * @return self of enum
     */
    public static ModOptions getOptionByName(String name) {
        return  ModOption.mapToList.get(name);
    }

    ModOptions(ModOption modOption, Style style) {
        this.name = modOption.getName();
        this.style = style;
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
        if(modOption.isDisabled()) {
            this.setDisabled();
        }
    }
    private void update(String name) {
        ModOption.mapToList.put(name, this);
        ModOption.list.add(this);
    }
    ModOptions(ModOption modOption, Style style, int valueInt) {
        this(modOption, style);
        this.defaultValue = valueInt;
    }
    ModOptions(ModOption modOption, Style style, float valueFloat) {
        this(modOption, style);
        this.defaultValue = valueFloat;
    }
    ModOptions(ModOption modOption, String valueString, int min, int max) {
        this(modOption, Style.STRING);
        this.defaultValueString = valueString;
        this.defaultValue = 0.0F;
        this.minString = min;
        this.maxString = max;
    }
    ModOptions(ModOption modOption, Style style, String startString, Float times, String end, Float add, float defaultValue) {
        this(modOption, style);
        this.startString = startString;
        if(times != null)
            this.times = times;
        if(end != null)
            this.slideEnd = end;
        if(add != null)
            this.add = add;
        this.defaultValue = defaultValue;
    }
    ModOptions(ModOption modOption, Style style, String startString, Float times, String end, Float add, float defaultValue, String[] values) {
        this(modOption, style, startString, times, end, add, defaultValue);
        this.values = values;
    }

    private void setDisabled() {
        this.isDisabled = true;
    }

    public Object getCurrentValue() {
        return currentValue;
    }
    public String getStringValue() {
        switch(style) {
            case FLOAT:
                float f = Float.parseFloat(getAsString());
                return values[(int) (f * this.times)];
            case INTEGER:
            case BOOL:
                int i = Integer.parseInt(getAsString());
                return values[(int) (i * this.times)];
            default:
                return getAsString();
        }
    }
    public String getAsString() {
        return this.currentValue+"";
    }
    public int getAsInt() {
        if(style == Style.FLOAT) {
            float f = Float.parseFloat(getAsString());
            float add = this.getAdd();
            float times = this.getTimes();
            if(f == 0.0F)
                return 0;
            return (int) ((f * times) + add)+1;
        }
        return Integer.parseInt(getAsString());
    }
    public boolean getAsBool() {
        return getAsInt() == 1;
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

        private final String name;
        private final String section;
        private final Boolean disabled;
        public ModOption(@Nonnull String s, @Nonnull String section, Boolean disabled) {
            if(disabled == null)
                disabled = true;
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
