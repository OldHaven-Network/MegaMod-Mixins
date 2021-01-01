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
    MM_VERSION(
            new ModOption("MM Version", "MegaMod", false),
            MegaMod.version, 0, 5),

//region SETTINGS_MOD
    FIELD_OF_VIEW(
            new ModOption("Field of View", "Mod", false),
            Style.FLOAT, "70", 70.0F, "", 70.0F, 0.1F),
    WORLD_BRIGHTNESS(
            new ModOption("World Brightness", "Mod", false)
                    .setDescription("World Brightness for your client"),
            Style.FLOAT, 0.0F),
    FLY_SPEED(
            new ModOption("Fly Speed", "Mod", false),
            Style.FLOAT, "OFF", 100.0F, "%", 0.0F, 0.25F),
//endregion

//region SETTINGS_SHADER
    SHADERS(
            new ModOption("Shaders", "Shader", false),
            Style.FLOAT, "OFF", 4.0F, "", 0.0F, 0.0F,
            new String[]{
                    "OFF", "GLSL", "Checkerboard", "Faked-Real", "Outline"
            }),
    SHADOW_DENSITY(
            new ModOption("Shadow Density", "ShaderNonGLSL", false)
                .setDescription("How harsh shadows will be on blocks"),
            Style.FLOAT, "DEFAULT", 20.0F, "", 1.0F, 0),
    DYNAMIC_LIGHTING(
            new ModOption("Dynamic Lighting", "ShaderNonGLSL", false)
                .setDescription("Lights can move around (Unstable)"),
            Style.BOOL, false),
    WATER_COLOR(
            new ModOption("Water Color Hex", "Shader", null),
            "", 0, 6),
    LAVA_COLOR(
            new ModOption("Lava Color Hex", "Shader", null),
            "", 0, 6),
//endregion

//region SETTINGS_SKY
    CLOUD_HEIGHT(
            new ModOption("Cloud Height", "Sky", false)
                .setDescription("How high the clouds should be on your client"),
            Style.FLOAT, "OFF", 140.0F, "", 60.0F, 0.75F),
    FORCE_TIME(
            new ModOption("Force Time", "Sky", false)
                .setDescription("Set time on your client to a tick"),
            Style.FLOAT, "OFF", 24000.0F, " Ticks", 0.0F, 0.0F),
    TOGGLE_RAINSNOW(
            new ModOption("Toggle Rain/Snow", "Sky", false)
                .setDescription("Disable or enable rain/snow"),
            Style.BOOL, true),
//endregion

//region SETTINGS_BLOCKS
    RANDOM_TALLERGRASS(
            new ModOption("Random Taller-Grass", "Blocks", false)
                .setDescription("Grass will be taller in some areas"),
            Style.BOOL, false),
    FANCY_TREES(
            new ModOption("Fancy Trees", "Blocks", false)
                .setDescription("Randomized prettier trees"),
            Style.FLOAT, "OFF", 1.0F, "", 0.0F, 0.0F,
            new String[]{
                    "Fast", "Fancy"
            }),
    WATER_ANIMATION(
            new ModOption("Water Animation", "Blocks", false)
                .setDescription("Disable static water flow animation"),
            Style.BOOL, true),
    LADDERS_CLIMBABLE(
            new ModOption("Ladders Climbable", "Blocks", false)
                .setDescription("Make ladders climbable... or choose not to"),
            Style.BOOL, true),
    SIGNS_TEXT_SHADOW(
            new ModOption("Signs Text Shadow", "Blocks", false)
                    .setDescription("Have a shadow on text for all signs"),
            Style.BOOL, true),
//endregion

//region SETTINGS_SKIN
    ALEX_SKINS(
            new ModOption("Alex Skins", "Skin", false),
            Style.BOOL, true),
    SKIN_HAT(
            new ModOption("Hat", "Skin", false)
                .setDescription("Disable hat globally"),
            Style.BOOL, true),
    SKIN_CAPE(
            new ModOption("Cape", "Skin", false)
                .setDescription("Disable cape globally"),
            Style.BOOL, true),
    SKIN_EARS(
            new ModOption("Ears", "Skin", false)
                    .setDescription("Disable ears globally"),
            Style.BOOL, true),
    SKIN_JACKET(
            new ModOption("Jacket", "Skin", false)
                .setDescription("Disable jacket globally"),
            Style.BOOL, true),
    SKIN_LEFT_SLEEVE(
            new ModOption("Left Sleeve", "Skin", false)
                .setDescription("Disable outer left sleeve globally"),
            Style.BOOL, true),
    SKIN_RIGHT_SLEEVE(
            new ModOption("Right Sleeve", "Skin", false)
                .setDescription("Disable outer right sleeve globally"),
            Style.BOOL, true),
    SKIN_LEFT_PANTS_LEG(
            new ModOption("Left Pants Leg", "Skin", false)
                .setDescription("Disable outer left pants globally"),
            Style.BOOL, true),
    SKIN_RIGHT_PANTS_LEG(
            new ModOption("Right Pants Leg", "Skin", false)
                .setDescription("Disable outer right pants globally"),
            Style.BOOL, true),
    SKIN_DEFAULT_CAPE(
            new ModOption("Default Cape", "Skin", false)
                .setDescription("Default cape you should see first on anyone"),
            Style.FLOAT, "Minecraft", 2.0F, "", 0.0F, 0.0F,
            new String[]{
                    "Minecraft", "Optifine", "MCCapes"
            }),
//endregion

//region SETTINGS_GUISCREEN
    ANIMATE_MAIN_MENU(
            new ModOption("Animate Main Menu", "GuiScreen", false)
                .setDescription("Animated png on Main Menu background"),
            Style.BOOL, true),
    MAIN_MENU_QUIT_BUTTON(
            new ModOption("Main Menu Quit Btn", "GuiScreen", false)
                .setDescription("Enable/Disable Main Menu quit button"),
            Style.BOOL, true),
    NEW_MULTIPLAYER_GUI(
            new ModOption("New MultiPlayer GUI", "GuiScreen", false)
                .setDescription("Enable/Disable the modernized MultiPlayer GUI"),
            Style.BOOL, true),
//endregion

//region SETTINGS_GUI
    //SUBTITLES(new ModOption("Subtitles", "GUI", false), ModStyle.BOOL, 0),
    MODERN_TOOLTIPS(
            new ModOption("Modern Tooltips", "GUI", false)
                .setDescription("Hover over a tool, or change weapons provides tooltips"),
            Style.BOOL, true),
    CHAT_BG_OPACITY(
            new ModOption("Chat BG Opacity", "GUI", false)
                .setDescription("Chat Background opaqueness (unstable)"),
            Style.FLOAT, 1.0F),
    RAISE_SELECTED_HOTBAR(
            new ModOption("Raise HotBar Selected", "GUI", true)
                    .setDescription("Raises the hotbar a few pixels for the selected item"),
            Style.FLOAT, 0.0F),
    //SHOW_TOOLTIP(new ModOption("Show Tooltip", "GUI", false), ModStyle.BOOL, 1),
    TOGGLE_WAILA(
            new ModOption("Toggle WAILA", "GUI", false)
                .setDescription("\"What Am I Looking At?\""),
            Style.BOOL, true),
    SHOW_MOTION_IN_GAME(
            new ModOption("Show Motion In-Game", "GUI", false)
                .setDescription("Shows your concurrent motion in-game"),
            Style.BOOL, false),
    SHOW_SPEED_IN_GAME(
            new ModOption("Show Speed In-Game", "GUI", false)
                .setDescription("Shows your concurrent speed in-game"),
            Style.BOOL, false),
    BUTTON_TEXT_SHADOW(
            new ModOption("Button Text Shadow", "GUI", false)
                    .setDescription("Enable/Disable text shadow on all buttons"),
            Style.BOOL, true),
    BUTTON_OUTLINE_HEX(
            new ModOption("Button Outline Hex", "GUI", null),
            "", 0, 6),
    BUTTON_TEXT_HEX(
            new ModOption("Button Text Color Hex", "GUI", null),
            "", 0, 6),
//endregion

//region SETTINGS_PLAYER
    THIRDPERSON_DISTANCE(
            new ModOption("ThirdPerson Distance", "Player", false)
                .setDescription("How far do you want the camera from your player?"),
            Style.FLOAT, "FAR", 29.0F, "", 1.0F, 0.0F),
//endregion

//region SETTINGS_KEYBINDS
    DOUBLE_JUMP_TO_FLY(
            new ModOption("Double Jump To Fly", "Keybinds", false), Style.BOOL, false),
    DOUBLE_TAP_TO_SPRINT(
            new ModOption("Double Tap To Sprint", "Keybinds", false), Style.BOOL, false),
    HOLD_SPRINT(
            new ModOption("Hold Sprint", "Keybinds", false), Style.BOOL, false),
    HOLD_SNEAK(
            new ModOption("Hold Sneak", "Keybinds", false), Style.BOOL, false),
    DISABLE_PLAYERLIST(
            new ModOption("Disable PlayerList", "Keybinds", false), Style.BOOL, false),
    DISABLE_SPRINT(
            new ModOption("Disable Sprint", "Keybinds", false), Style.BOOL, false),
    DISABLE_FLY(
            new ModOption("Disable Fly", "Keybinds", false), Style.BOOL, false),
    DISABLE_ZOOM(
            new ModOption("Disable Zoom", "Keybinds", false), Style.BOOL, false),
//endregion

//region SETTINGS_DISCORD
    RICH_PRESENCE(
            new ModOption("Rich Presence", "Discord", false)
                .setDescription("Disable Discord status completely"),
            Style.BOOL, false),
//endregion

//region SETTINGS_SP
    SP_CHEATS(
            new ModOption("SP Cheats", "SP", false)
                .setDescription("Enable or disable SinglePlayer cheats (/help)"),
            Style.BOOL, false);
//endregion


//region VARIABLES
    private final Style style;
    public boolean isDisabled;

    private final String name;
    private String[] values = null;
    private String description =        "";
    private String startString =        "OFF";
    private String slideEnd =           "%";

    private Object defaultValue =        -1.0158F;
    private float start, add =          0.0F;
    private float times =               100.0F;

    private int defaultValueInt = -83;
    private int minString, ordinal;
    private int maxString =             20;

    private Object currentValue;
    private Section currentSection;
//endregion

//region GETTERS
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

//endregion


//region CONSTRUCTORS
    ModOptions(ModOption modOption, Style style) {
        this.name = modOption.getName();
        this.description = modOption.getDescription();
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
    ModOptions(ModOption modOption, Style style, Object value) {
        this(modOption, style);
        this.defaultValue = value;
    }

    ModOptions(ModOption modOption, String valueString, int minLen, int maxLen) {
        this(modOption, Style.STRING);
        this.defaultValue = valueString;
        this.minString = minLen;
        this.maxString = maxLen;
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
//endregion

    private void setDisabled() {
        this.isDisabled = true;
    }

//region OPT_GETTERS
    private boolean hasUpdated = false;
    private Object updatedObj = -1;
    public Object getCurrentValue() {
        return currentValue;
    }
    public String getStringValue() {
        return getAsString();
    }
    public String getAsString() {
        return this.currentValue+"";
    }
    public int getAsInt() {
        if(!hasUpdated) {
            if(this.style == Style.FLOAT) {
                float f = (float) this.currentValue;
                float add = this.getAdd();
                float times = this.getTimes();
                if (f == 0.0F)
                    this.updatedObj = 0;
                else
                    this.updatedObj = (int) ((f * times) + add) + 1;
            } else if(this.style == Style.BOOL) {
                this.updatedObj = ((int)this.currentValue == 1 ? 1 : 0);
            } else
                this.updatedObj = (int) this.currentValue;
            this.hasUpdated = true;
        }
        return (int) this.updatedObj;
    }
    public boolean getAsBool() {
        if(!this.hasUpdated) {
            if(this.currentValue instanceof Integer) {
                this.updatedObj = (boolean) (((int) this.currentValue) == 1);
            } else
                this.updatedObj = (boolean) this.currentValue;
            this.hasUpdated = true;
        }
        return (boolean) this.updatedObj;
    }
    public float getAsFloat() {
        if(!hasUpdated) {
            this.updatedObj = Float.parseFloat(getAsString());
            this.hasUpdated = true;
        }
        return (float) this.updatedObj;
    }

    void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
        this.hasUpdated = false;
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

    public String getDescription() {
        return description;
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

    public Object getDefaultValue() {
        return defaultValue;
    }
//endregion

    static class ModOption {
        static Map<String, ModOptions> mapToList = new HashMap<>();
        static List<ModOptions> list = new ArrayList<>();
        static List<Section> sectionList = new ArrayList<>();
        static Map<String, Section> sectionMap = new HashMap<>();

        private String description = "";
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

        ModOption setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getDescription() {
            return description;
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
