package net.oldhaven.customs.shaders;

import net.minecraft.src.Block;
import net.minecraft.src.Vec3D;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ShaderedBlock {
    private static Map<Vec3D, ShaderedBlock> shaderedBlockMap = new HashMap<>();
    public static void removeShaderedBlock(Vec3D vec3D) {
        shaderedBlockMap.remove(vec3D);
    }
    public static boolean hasVec(Vec3D vec3D) {
        return shaderedBlockMap.containsKey(vec3D);
    }
    public static ShaderedBlock getFromVec(Vec3D vec3D) {
        //if(shaderedBlockMap.containsKey(vec3D))
            return shaderedBlockMap.get(vec3D);
        //return null;
    }

    private Vec3D vec3D;
    private Block block;
    public ShaderedBlock(int x, int y, int z, Block block) {
        vec3D = Vec3D.createVector(x, y, z);
        this.block = block;
        shaderedBlockMap.put(this.vec3D, this);
    }

    public Vec3D getVec3D() {
        return vec3D;
    }

    public Block getBlock() {
        return block;
    }
}
