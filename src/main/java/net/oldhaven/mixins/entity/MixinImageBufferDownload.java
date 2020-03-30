package net.oldhaven.mixins.entity;

import net.minecraft.src.ImageBufferDownload;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ImageBufferDownload.class)
public class MixinImageBufferDownload {
    @Shadow private int imageHeight;

    @Redirect(method = "parseUserSkin", at=@At(value = "FIELD", target = "Lnet/minecraft/src/ImageBufferDownload;imageHeight:I", opcode = Opcodes.PUTFIELD))
    private void getWidth(ImageBufferDownload download, int i) {
        this.imageHeight = i;
    }
}
