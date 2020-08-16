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
    @Shadow protected ModelBase renderPassModel;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ModelBase modelBase, float v, CallbackInfo ci) {
        if(modelBase instanceof ModelBiped && !(modelBase instanceof ModelZombie)) {
            this.mainModel = new CustomModelBiped(0, 0);
        }
    }

    @Inject(method = "doRenderLiving", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", shift = At.Shift.AFTER))
    private void doRenderLiving1(EntityLiving entityLiving, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
            /*try {
                if (entityLiving instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entityLiving;
                    boolean fh = false;
                    if (!mainModel.getClass().getSimpleName().equals("fh") && mainModel instanceof ModelBiped)
                        fh = true;
                    if(fh)
                        ((ModelBiped)mainModel).isSneak = entityLiving.isSneaking();
                    String name = player.username;
                    if(!name.equals(MMUtil.getMinecraftInstance().thePlayer.username))
                        return;
                    boolean isAlex = SkinFix.isSkinAlex(name);
                    if (fh) {
                        CustomModelBiped newMainModel = (CustomModelBiped) mainModel;
                        newMainModel.setAlex(isAlex);
                    }

                    if (renderPassModel != null) {
                        if (!renderPassModel.getClass().getSimpleName().equals("fh") && renderPassModel instanceof ModelBiped) {
                            CustomModelBiped renderPass = (CustomModelBiped) renderPassModel;
                            renderPass.isSneak = entityLiving.isSneaking();
                            renderPass.setAlex(isAlex);
                        }
                    }
                }
            } catch(ClassCastException ignore){}*/
        //new Thread(runnable).start();
    }
}
