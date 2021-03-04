package net.oldhaven.customs;

import java.nio.IntBuffer;

public interface IFontRenderer {
    IntBuffer buffer = null;

    void renderString(String s, int i, int j, float[] color, boolean flag);
}
