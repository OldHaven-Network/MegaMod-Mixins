package net.oldhaven.mixins.entity;

import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;
import net.oldhaven.customs.CustomModelRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped extends ModelBase {
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedBody;
    @Shadow public ModelRenderer bipedHead;
    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedCloak;
    public CustomModelRenderer bipedLeftArmwear;
    public CustomModelRenderer bipedRightArmwear;
    public CustomModelRenderer bipedLeftLegwear;
    public CustomModelRenderer bipedRightLegwear;
    public CustomModelRenderer bipedBodyWear;
    @Inject(method = "<init>(FF)V", at = @At("RETURN"))
    private void init(float scale, float f, CallbackInfo ci) {
        this.bipedCloak = new CustomModelRenderer(0, 0);
        ((CustomModelRenderer)this.bipedCloak).addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, scale, 64, 64);

        this.bipedHead = new CustomModelRenderer(0, 0);
        ((CustomModelRenderer)this.bipedHead).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale, 64, 64);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        this.bipedHeadwear = new CustomModelRenderer(32, 0);
        ((CustomModelRenderer)this.bipedHeadwear).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F, 64, 64);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);

        this.bipedBody = new CustomModelRenderer(16, 16);
        ((CustomModelRenderer)this.bipedBody).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale, 64, 64);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        this.bipedRightArm = new CustomModelRenderer(40, 16);
        ((CustomModelRenderer)this.bipedRightArm).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + f, 0.0F);
        this.bipedLeftArm = new CustomModelRenderer(40, 16);
        this.bipedLeftArm.mirror = true;
        ((CustomModelRenderer)this.bipedLeftArm).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
        this.bipedRightLeg = new CustomModelRenderer(0, 16);
        ((CustomModelRenderer)this.bipedRightLeg).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
        this.bipedLeftLeg = new CustomModelRenderer(0, 16);
        this.bipedLeftLeg.mirror = true;
        ((CustomModelRenderer)this.bipedLeftLeg).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
        this.bipedLeftArm = new CustomModelRenderer(32, 48);
        ((CustomModelRenderer)this.bipedLeftArm).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightArm = new CustomModelRenderer(40, 16);
        ((CustomModelRenderer)this.bipedRightArm).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        this.bipedLeftLeg = new CustomModelRenderer(16, 48);
        ((CustomModelRenderer)this.bipedLeftLeg).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLeg = new CustomModelRenderer(0, 16);
        ((CustomModelRenderer)this.bipedRightLeg).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + f, 0.0F);

        this.bipedLeftArmwear = new CustomModelRenderer(48, 48);
        this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F, 64, 64);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightArmwear = new CustomModelRenderer(40, 32);
        this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F, 64, 64);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        this.bipedLeftLegwear = new CustomModelRenderer(0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F, 64, 64);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new CustomModelRenderer(0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F, 64, 64);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

        this.bipedBodyWear = new CustomModelRenderer(16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.25F, 64, 64);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(float var1, float var2, float var3, float var4, float var5, float scale, CallbackInfo ci) {
        GL11.glPushMatrix();
        /*if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }*/
        copyModelAngles(bipedHeadwear, bipedHead);
        this.bipedLeftLegwear.render(scale);
        this.bipedRightLegwear.render(scale);
        this.bipedLeftArmwear.render(scale);
        this.bipedRightArmwear.render(scale);
        this.bipedBodyWear.render(scale);
        GL11.glPopMatrix();
    }

    public void copyModelAngles(ModelRenderer source, ModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    private void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, CallbackInfo ci) {
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }
}
