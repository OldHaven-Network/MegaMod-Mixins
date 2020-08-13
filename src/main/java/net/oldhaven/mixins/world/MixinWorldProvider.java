package net.oldhaven.mixins.world;

import net.minecraft.src.WorldProvider;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldProvider.class)
public class MixinWorldProvider {
    @Inject(method = "getCloudHeight", at = @At("HEAD"), cancellable = true)
    private void getCloudHeight(CallbackInfoReturnable<Float> ci) {
        float f = ModOptions.CLOUD_HEIGHT.getAsFloat();
        if(f == 0.0F) {
            ci.setReturnValue(-50.0F);
            return;
        }
        ci.setReturnValue((f * 140.0F) + 60.0F);
    }
}
