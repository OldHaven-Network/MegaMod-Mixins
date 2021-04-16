package net.oldhaven.customs.util;

import net.minecraft.src.World;

public class WorldUtil {
    public static int getTopBlockAt(World world, int x, int z) {
        int id = 0;
        for(int y=62;y < 128;y++) {
            int block = world.getBlockId(x, y, z);
            if(block != 0)
                id = block;
        }
        if(id == 0) {
            for(int y=63;y > 0;y--) {
                int block = world.getBlockId(x, y, z);
                if(block != 0) {
                    id = block;
                    break;
                }
            }
        }
        return id;
    }

    public static boolean isBlockStuck(World world, int x, int y, int z) {
        return world.getBlockId(x, y+1, z) != 0 && world.getBlockId(x, y-1, z) != 0;
    }

    public static int getBlockAbove(World world, int x, int y, int z) {
        return world.getBlockId(x, y+1, z);
    }
}
