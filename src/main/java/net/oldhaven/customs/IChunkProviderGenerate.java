package net.oldhaven.customs;

import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

import java.util.Random;

public interface IChunkProviderGenerate {
    void generate(WorldGenerator worldGenerator, World world, Random rand, int i, int i1, int i2);
    void passTemps(double[] temps);
}
