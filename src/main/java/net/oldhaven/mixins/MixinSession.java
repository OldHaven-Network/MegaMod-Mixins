package net.oldhaven.mixins;

import net.minecraft.src.Session;
import net.oldhaven.MegaMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Session.class)
public class MixinSession {
    @Shadow public String username;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        if(MegaMod.debug)
            this.username = "cutezyash";
    }
}
