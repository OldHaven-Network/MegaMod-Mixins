package net.oldhaven.mixins.world;

import net.minecraft.src.WorldInfo;
import net.oldhaven.customs.options.CustomGameSettings;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldInfo.class)
public class MixinWorldInfo {
    @Shadow private long worldTime;

    @Inject(method = "setWorldTime", at = @At("HEAD"), cancellable = true)
    private void setWorldTime(long i, CallbackInfo ci) {
        CustomGameSettings gs = MegaMod.getCustomGameSettings();
        float f = ModOptions.FORCE_TIME.getAsFloat();
        if(f != 0.0F) {
            this.worldTime = (int) (f * 24000.0F);
            ci.cancel();
        }
    }
}
