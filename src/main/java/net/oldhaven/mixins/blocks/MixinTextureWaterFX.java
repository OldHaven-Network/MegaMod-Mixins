package net.oldhaven.mixins.blocks;

import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureWaterFX;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureWaterFX.class)
public class MixinTextureWaterFX extends TextureFX {
    @Shadow protected float[] field_1157_h;
    @Shadow protected float[] field_1158_g;

    public MixinTextureWaterFX(int i) {
        super(i);
    }

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        /*int f = (int)(ModOptions.WATER_COLOR.getAsFloat() * 5.0F);
        if(f == 5) {
            ci.cancel();
            return;
        }*/
        if(!ModOptions.WATER_ANIMATION.getAsBool()) {
            float[] af = field_1157_h;
            field_1157_h = field_1158_g;
            field_1158_g = af;
            for (int i1 = 0; i1 < 256; i1++) {
                float f1 = 0.5F;
                float f2 = f1 * f1;
                int l1 = (int) (32F + f2 * 32F);
                int j2 = (int) (50F + f2 * 64F);
                int k2 = 255;
                int l2 = (int) (146F + f2 * 50F);
                imageData[i1 * 4 + 0] = (byte) l1;
                imageData[i1 * 4 + 1] = (byte) j2;
                imageData[i1 * 4 + 2] = (byte) k2;
                imageData[i1 * 4 + 3] = (byte) l2;
            }
            ci.cancel();
        }
    }
}
