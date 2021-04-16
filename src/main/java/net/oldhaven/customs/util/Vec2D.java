package net.oldhaven.customs.util;

public class Vec2D {
    public static Vec2D create(int x, int z) {
        return new Vec2D(x, z);
    }

    public final int x, y;
    private Vec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
