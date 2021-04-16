package net.oldhaven.mixins.chunks;

import net.minecraft.src.ChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ChunkLoader.class)
public class MixinChunkLoader {
    long time;

    @Inject(method = "chunkFileForXZ", at = @At("HEAD"))
    private void loadStart(int i, int i1, CallbackInfoReturnable<File> cir) {
        this.time = System.currentTimeMillis();
    }

    @Redirect(method = "chunkFileForXZ", at = @At(value = "INVOKE", target = "Ljava/io/File;exists()Z"))
    private boolean loadEnd(File file) {
        System.out.println("Loaded chunk in " + (System.currentTimeMillis() - time));
        return file.exists();
    }
}
