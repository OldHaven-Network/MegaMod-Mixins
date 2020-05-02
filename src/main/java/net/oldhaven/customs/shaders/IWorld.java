package net.oldhaven.customs.shaders;

import net.minecraft.src.EnumSkyBlock;

public interface IWorld {
    void markBlockNeedsUpdate(int i, int i1, int i2);
    int getBlockId(int x, int y, int z);
    boolean canBlockSeeTheSky(int x, int y, int z);
    void updateLighting();
    void scheduleLightingUpdate(EnumSkyBlock skyBlock, int x, int y, int z, int x1, int y1, int z1);
}
