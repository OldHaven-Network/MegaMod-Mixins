package net.oldhaven.mixins.chunks;

import net.minecraft.src.ChunkProvider;
import net.minecraft.src.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkProvider.class)
public abstract class MixinChunkProvider implements IChunkProvider {

}
