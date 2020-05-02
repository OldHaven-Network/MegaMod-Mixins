package net.oldhaven.mixins.shader.faked;

import net.minecraft.src.Chunk;
import net.oldhaven.customs.shaders.IChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
public abstract class MixinChunk implements IChunk {
    @Shadow protected abstract void func_1003_g(int i, int i1, int i2);

    @Override
    public void forceUpdateLighting(int x, int y, int z) {
        this.func_1003_g(x, y, z);
    }
}
