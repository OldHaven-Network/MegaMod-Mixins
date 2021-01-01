package net.oldhaven.mixins.entity;

import net.minecraft.src.*;
import net.oldhaven.customs.alexskins.CustomModelBiped;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer extends RenderLiving {
    @Shadow private ModelBiped modelBipedMain;
    @Shadow private ModelBiped modelArmorChestplate;
    @Shadow private ModelBiped modelArmor;
    private CustomModelBiped cMP;

    public MixinRenderPlayer(ModelBase modelBase, float v) { super(modelBase, v); }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(CallbackInfo ci) {
        this.modelBipedMain = this.cMP = (CustomModelBiped)this.mainModel;
    }

    /**
     * @author cutezyash
     * @reason new render
     */
    @Overwrite
    public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9) {
        ItemStack var10 = var1.inventory.getCurrentItem();
        if(ModOptions.ALEX_SKINS.getAsBool())
            cMP.setAlex(SkinFix.isSkinAlex(var1.username));
        this.modelArmorChestplate.field_1278_i = this.modelArmor.field_1278_i = cMP.field_1278_i = var10 != null;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = cMP.isSneak = var1.isSneaking();
        double var11 = var4 - (double)var1.yOffset;
        if (var1.isSneaking() && !(var1 instanceof EntityPlayerSP)) {
            var11 -= 0.125D;
        }

        super.doRenderLiving(var1, var2, var11, var6, var8, var9);
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = cMP.isSneak = false;
        this.modelArmorChestplate.field_1278_i = this.modelArmor.field_1278_i = cMP.field_1278_i = false;
    }

    private EntityPlayer entityPlayer;
    @Inject(method = "renderSpecials", at = @At("HEAD"))
    private void renderSpecials(EntityPlayer entityPlayer, float v, CallbackInfo ci) {
        this.entityPlayer = entityPlayer;
    }

    @Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean isDeadMau(String s, Object anObject) {
        return true;
    }

    @Redirect(method = "renderSpecials", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/RenderPlayer;"+
                        "loadDownloadableImageTexture(Ljava/lang/String;Ljava/lang/String;)Z",
            opcode = Opcodes.INVOKEVIRTUAL,
            ordinal = 0))
    private boolean loadEars(RenderPlayer renderPlayer, String s, String s1) {
        RenderEngine engine = MMUtil.getMinecraftInstance().renderEngine;
        BufferedImage image = SkinFix.getEarsImage(entityPlayer.username);
        if(image == null)
            return false;
        int i = engine.allocateAndSetupTexture(image);
        engine.bindTexture(i);
        return true;
    }

    @Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;postRender(F)V", ordinal = 0))
    private void postRenderHead(ModelRenderer modelRenderer, float v) {
        cMP.bipedHead.postRender(v);
    }

    @Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;postRender(F)V", ordinal = 1))
    private void postRenderHand(ModelRenderer modelRenderer, float v) {
        cMP.bipedRightArm.postRender(v);
    }

    /**
     * @author cutezyash
     * @reason Custom model for alex skins n such
     */
    @Overwrite
    public void drawFirstPersonHand() {
        cMP.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        cMP.bipedRightArm.render(0.0625F);
    }
}
