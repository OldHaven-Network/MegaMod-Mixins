package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.options.CustomGameSettings;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.OnScreenText;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow private Minecraft mc;
    @Shadow private float farPlaneDistance;
    @Shadow private Entity pointedEntity;

    @Shadow private float field_22225_u;
    @Shadow private float field_22226_t;
    @Shadow private float field_22223_w;
    @Shadow private float field_22224_v;
    @Shadow private float field_22227_s;
    @Shadow private float field_22228_r;
    @Shadow private float field_22230_A;
    @Shadow private float field_22220_z;
    @Shadow private boolean cloudFog;
    private float flyFoV = 0.0F;
    private float zoomFoV = 0.0F;
    private float sprintFoV() {
        if (MMUtil.isSprinting) {
            if (flyFoV >= 5.0F)
                flyFoV = 5.0F;
            else
                flyFoV += 0.45F;
            return flyFoV;
        }
        if (flyFoV != 0.0F)
            flyFoV -= 0.45F;
        if (flyFoV <= 0.0F)
            flyFoV = 0.0F;
        return flyFoV;
    }

    private float getZoomFoV() {
        int dwheel = Mouse.getDWheel();
        int times = 1;
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
            times = 5;
        if(dwheel < 0) {
            if(zoomFoV <= 40)
                zoomFoV -= times;
            else
                zoomFoV -= 0.5F * times;
        } else if(dwheel > 0) {
            if(zoomFoV > 40)
                zoomFoV += 0.5F * times;
            else
                zoomFoV += times;
        }
        if(zoomFoV < 0)
            zoomFoV = 0;
        if(zoomFoV > 59)
            zoomFoV = 59;
        return (-10 - zoomFoV);
    }

    @Inject(method = "getMouseOver", at = @At("RETURN"))
    private void getMouseOver(float f, CallbackInfo ci) {
        if(mc.theWorld != null) {
            boolean blockExists = false;
            MovingObjectPosition mob = mc.objectMouseOver;
            if (mc.objectMouseOver != null) {
                if (mc.theWorld.blockExists(mob.blockX, mob.blockY, mob.blockZ)) {
                    MMUtil.pointingBlock = mc.theWorld.getBlockId(mob.blockX, mob.blockY, mob.blockZ);
                    blockExists = true;
                }
            }
            if (!blockExists)
                MMUtil.pointingBlock = 0;
            if (!(mc.playerController instanceof PlayerControllerTest))
                MMUtil.getPlayer().pointingEntity = pointedEntity;
        } else {
            MMUtil.getPlayer().pointingEntity = null;
            MMUtil.pointingBlock = 0;
        }
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 70.0F))
    private float getFovModifier(float var1) {
        if(MMUtil.isZooming) { /* zoom zoom */
            OnScreenText.replaceOnScreenText("zoom", "Zoom (x" + (int) zoomFoV + ")", 0xffffff);
            return (getZoomFoV() + 70);
        }
        OnScreenText.hideOnScreenText("zoom");
        float f = ModOptions.FIELD_OF_VIEW.getAsFloat();
        return ((f * 70) + 70 + sprintFoV());
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 60.0F))
    private float getFoVModifierWater(float var1) {
        if(MMUtil.isZooming)
            return (getZoomFoV() + 70);
        float f = ModOptions.FIELD_OF_VIEW.getAsFloat();
        return (f * 70) + 70  + sprintFoV() - 10;
    }

    private float getFovModifierDefault(float var1) {
        return 70;
    }

    /**
     * @author cutezyash
     * @reason Third person camera rewrite
     * @param partialTick
     */
    @Overwrite
    private void orientCamera(float partialTick) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float yOffset = var2.yOffset - 1.62F;
        double pX = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)partialTick;
        double pY = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)partialTick - (double)yOffset;
        double pZ = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)partialTick;
        GL11.glRotatef(this.field_22230_A + (this.field_22220_z - this.field_22230_A) * partialTick, 0.0F, 0.0F, 1.0F);
        if (var2.isPlayerSleeping()) {
            yOffset = (float)((double)yOffset + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.field_22273_E) {
                int blockId = this.mc.theWorld.getBlockId(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
                if (blockId == Block.blockBed.blockID) {
                    int blockMetadata = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
                    int i = blockMetadata & 3;
                    GL11.glRotatef((float)(i * 90), 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * partialTick + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * partialTick, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView) {
            float distNorm = ModOptions.THIRDPERSON_DISTANCE.getAsFloat();
            float distDiv = distNorm * 5 + 1;
            double dist = (double)(this.field_22227_s + (this.field_22228_r - this.field_22227_s) * partialTick) / distDiv;
            float pit;
            float yaw;
            if (this.mc.gameSettings.field_22273_E) {
                yaw = this.field_22225_u + (this.field_22226_t - this.field_22225_u) * partialTick;
                pit = this.field_22223_w + (this.field_22224_v - this.field_22223_w) * partialTick;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-dist));
                GL11.glRotatef(pit, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
            } else {
                yaw = var2.rotationYaw;
                pit = var2.rotationPitch;
                if(MMUtil.thirdPersonView == 1)
                    pit+=180.0F;
                double var14 = (double)(-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pit / 180.0F * 3.1415927F)) * dist;
                double var16 = (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pit / 180.0F * 3.1415927F)) * dist;
                double var18 = (double)(-MathHelper.sin(pit / 180.0F * 3.1415927F)) * dist;

                for(int i=0;i < 8;i++) {
                    float iX = (float)((i & 1) * 2 - 1) * 0.1F;
                    float iY = (float)((i >> 1 & 1) * 2 - 1) * 0.1F;
                    float iZ = (float)((i >> 2 & 1) * 2 - 1) * 0.1F;
                    MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(
                            Vec3D.createVector(
                                    pX + (double)iX,
                                    pY + (double)iY,
                                    pZ + (double)iZ
                            ), Vec3D.createVector(
                                    pX - var14 + (double)iX + (double)iZ,
                                    pY - var18 + (double)iY,
                                    pZ - var16 + (double)iZ
                            )
                    );
                    if (var24 != null) {
                        double var25 = var24.hitVec.distanceTo(Vec3D.createVector(pX, pY, pZ));
                        if (var25 < dist) {
                            dist = var25;
                        }
                    }
                }

                if(MMUtil.thirdPersonView == 1) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.rotationPitch - pit, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var2.rotationYaw - yaw, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-dist));
                GL11.glRotatef(yaw - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(pit - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.field_22273_E) {
            GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * partialTick, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * partialTick + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, yOffset, 0.0F);
        pX = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)partialTick;
        pY = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)partialTick - (double)yOffset;
        pZ = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)partialTick;
        this.cloudFog = this.mc.renderGlobal.func_27307_a(pX, pY, pZ, partialTick);
    }

    @Inject(method = "renderRainSnow", at = @At("HEAD"), cancellable = true)
    private void renderRainSnow(float v, CallbackInfo ci) {
        if(!ModOptions.TOGGLE_RAINSNOW.getAsBool())
            ci.cancel();
    }

    @Inject(method = "func_4135_b", at = @At("HEAD"))
    private void renderHand(float f, int i, CallbackInfo ci) {
        Float temp = ModOptions.FIELD_OF_VIEW.getAsFloat();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        CustomGameSettings gs = MMUtil.getCustomGameSettings();
        gs.setOption("Field of View", 0.0F);
        float zoom = MMUtil.isZooming ? (getZoomFoV() + 70 - 10) : getFovModifierDefault(f);
        if(MMUtil.isZooming && zoom <= 20)
            zoom = 20;
        GLU.gluPerspective(zoom, (float)this.mc.displayWidth / (float)this.mc.displayHeight,
                0.05F, this.farPlaneDistance * 2.0F);
        gs.setOption("Field of View", temp);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
