package net.oldhaven.mixins.entity;

import net.minecraft.src.*;
import net.oldhaven.customs.alexskins.CustomModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLiving.class)
public class MixinRenderLiving {
    @Shadow protected ModelBase mainModel;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ModelBase modelBase, float v, CallbackInfo ci) {
        if(modelBase instanceof ModelBiped && !(modelBase instanceof ModelZombie)) {
            this.mainModel = new CustomModelBiped(0, 0);
        }
    }
}
