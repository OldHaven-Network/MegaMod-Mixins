package net.oldhaven.gui;

public enum CustomEnumOptions {
    BRIGHTNESS("Brightness", true, false),
    FoV("Field of View", true, false, "70", 70.0F, "", 70.0F, 0.1F),

    CLOUD_HEIGHT("Cloud Height", true, false, "OFF", 140.0F, "", 60.0F, 0.75F),
    RAIN_TOGGLE("Toggle Rain/Snow", false, true, 1),
    SHOW_TOOLTIP("Show Tooltip", false, true, 1),
    DOUBLEJUMPTOFLY("Double Jump to Fly", false, true, 0),
    FORCE_TIME("Force Time", true, false, "OFF", 24000.0F, " Ticks", 0.0F, 0.0F),
    FLY_SPEED("Fly Speed", true, false, "OFF", 100.0F, "%", 0.0F, 0.25F),

    ENABLE_FANCY_TREES("Enable Fancy Trees", false, true, 1),

    DISABLE_LADDERS("Disable Ladders", false, true, 0),
    HOLD_SPRINT("Hold Sprint", false, true, 0),
    HOLD_SNEAK("Hold Sneak", false, true, 0),
    SUBTITLES("Subtitles", false, true, 0),
    CHAT_BACKGROUND_OPACITY("Chat BG Opacity", true, false, 0.0F),

    DEFAULT_MM_BACKGROUND("Default Main Menu BG", false, true, 0),
    TOGGLE_WAILA("Toggle WAILA", false, true, 1),
    SHOW_QUIT_BUTTON("Show Main Menu Quit Btn", false, true, 1),
    SHOW_MOTION("Show Motion In-Game", false, true, 0),
    SHOW_SPEED("Show Speed In-Game", false, true, 0),
    BTN_OUTLINE_COLOR("Button Outline", true, false, "OFF", 11.0F, "", 0.0F, 0.0F,
        new String[]{
                "White", "Blue", "Purple", "Red", "Aqua", "Green", "Yellow", "Orange", "Light Grey", "Grey", "Black", "ADVANCED"
        }),
    BTN_ADVANCED_COLOR("Button ADV Color", false, false, "0xffffff", 2, 8),

    Disable_PlayerList("Disable PlayerList", false, true, 0),
    DISABLE_SPRINT("Disable Sprint", false, true, 0),
    DISABLE_FLY("Disable Fly", false, true, 0);

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;
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

    public static CustomEnumOptions getEnumOptions(int var0) {
        CustomEnumOptions[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CustomEnumOptions var4 = var1[var3];
            if (var4.returnEnumOrdinal() == var0) {
                return var4;
            }
        }

        return null;
    }

    private CustomEnumOptions(String name, boolean floa, boolean bool) {
        this.enumString = name;
        this.enumFloat = floa;
        this.enumBoolean = bool;
    }
    private CustomEnumOptions(String name, boolean floa, boolean bool, int valueInt) {
        this(name, floa, bool);
        this.defaultValue = valueInt;
    }
    private CustomEnumOptions(String name, boolean floa, boolean bool, float valueFloat) {
        this(name, floa, bool);
        this.defaultValue = valueFloat;
    }
    private CustomEnumOptions(String name, boolean floa, boolean bool, String valueString, int min, int max) {
        this(name, floa, bool);
        this.defaultValueString = valueString;
        this.defaultValue = 0.0F;
        this.minString = min;
        this.maxString = max;
    }
    private CustomEnumOptions(String name, boolean floa, boolean bool, String startString, Float times, String end, Float add, float defaultValue) {
        this(name, floa, bool);
        this.startString = startString;
        if(times != null)
            this.times = times;
        if(end != null)
            this.slideEnd = end;
        if(add != null)
            this.add = add;
        this.defaultValue = defaultValue;
    }
    private CustomEnumOptions(String name, boolean floa, boolean bool, String startString, Float times, String end, Float add, float defaultValue, String[] values) {
        this(name, floa, bool, startString, times, end, add, defaultValue);
        this.values = values;
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

    public boolean getEnumFloat() {
        return this.enumFloat;
    }

    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }

    public int returnEnumOrdinal() {
        return this.ordinal();
    }

    public String getEnumString() {
        return this.enumString;
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
}
