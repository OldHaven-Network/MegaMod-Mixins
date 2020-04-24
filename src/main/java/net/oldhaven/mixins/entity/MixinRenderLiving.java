package net.oldhaven.mixins.entity;

import net.minecraft.src.*;
import net.oldhaven.SkinFix;
import net.oldhaven.customs.CustomModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLiving.class)
public class MixinRenderLiving {
    @Shadow protected ModelBase mainModel;

    @Shadow protected ModelBase renderPassModel;

    @Inject(method = "doRenderLiving", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", shift = At.Shift.AFTER))
    private void doRenderLiving1(EntityLiving entityLiving, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
        if(entityLiving instanceof EntityPlayer) {
            boolean isAlex = SkinFix.isSkinAlex(((EntityPlayer) entityLiving).username);
            if(!mainModel.getClass().getSimpleName().equals("fh") && mainModel instanceof ModelBiped) {
                CustomModelBiped newMainModel = (CustomModelBiped) mainModel;
                newMainModel.isSneak = entityLiving.isSneaking();
                newMainModel.setAlex(isAlex);
                this.mainModel = newMainModel;
            }

            if(renderPassModel != null) {
                if(!renderPassModel.getClass().getSimpleName().equals("fh") && renderPassModel instanceof ModelBiped) {
                    CustomModelBiped renderPass = (CustomModelBiped) renderPassModel;
                    renderPass.isSneak = entityLiving.isSneaking();
                    renderPass.setAlex(isAlex);
                    this.renderPassModel = renderPass;
                }
            }
        }
    }
}
