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
    public CustomModelRenderer bipedLeftArmwear;
    public CustomModelRenderer bipedRightArmwear;
    public CustomModelRenderer bipedLeftLegwear;
    public CustomModelRenderer bipedRightLegwear;
    public CustomModelRenderer bipedBodyWear;
    @Inject(method = "<init>(FF)V", at = @At("RETURN"))
    private void init(float scale, float f2, CallbackInfo ci) {
        this.bipedLeftArm = new ModelRenderer(32, 48);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + f2, 0.0F);

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
