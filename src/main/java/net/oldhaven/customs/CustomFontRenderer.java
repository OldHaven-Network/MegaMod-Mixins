package net.oldhaven.customs;

import net.minecraft.src.FontRenderer;

public class CustomFontRenderer {
    public static CustomFontRenderer instance;
    public static CustomFontRenderer getCustomFontRenderer() {
        return instance;
    }

    private static IFontRenderer IFontRenderer;
    public static void setFontRenderer(IFontRenderer fontRenderer) {
        IFontRenderer = fontRenderer;
    }
    public static IFontRenderer getFontRenderer() {
        return IFontRenderer;
    }

    public CustomFontRenderer(FontRenderer renderer) {

    }
}
