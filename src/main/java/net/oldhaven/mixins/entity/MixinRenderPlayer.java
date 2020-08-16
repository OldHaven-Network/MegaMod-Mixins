package net.oldhaven.mixins.entity;

import net.minecraft.src.*;
import net.oldhaven.customs.alexskins.CustomModelBiped;
import net.oldhaven.customs.util.SkinFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer extends RenderLiving {
    @Shadow private ModelBiped modelBipedMain;

    public MixinRenderPlayer(ModelBase modelBase, float v) { super(modelBase, v); }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(CallbackInfo ci) {
        this.modelBipedMain = (CustomModelBiped)this.mainModel;
    }

    @Inject(method = "renderPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/src/RenderPlayer;modelBipedMain:Lnet/minecraft/src/ModelBiped;", ordinal = 1, shift = At.Shift.AFTER))
    private void isSneak(EntityPlayer entityPlayer, double v, double v1, double v2, float v3, float v4, CallbackInfo ci) {
        CustomModelBiped modelBiped = (CustomModelBiped) this.modelBipedMain;
        modelBiped.isSneak = entityPlayer.isSneaking();
        modelBiped.setAlex(SkinFix.isSkinAlex(entityPlayer.username));
    }

    @Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;postRender(F)V"))
    private void postRenderHand(ModelRenderer modelRenderer, float v) {
        ((CustomModelBiped) this.modelBipedMain).bipedRightArm.postRender(v);
    }

    /**
     * @author cutezyash
     */
    @Overwrite
    public void drawFirstPersonHand() {
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        ((CustomModelBiped)this.modelBipedMain).bipedRightArm.render(0.0625F);
    }
}
