package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.MegaMod;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
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
    private float flyFoV = 0.0F;
    private float zoomFoV = 0.0F;
    private float sprintFoV() {
        if (MegaMod.getInstance().isSprinting) {
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
                    MegaMod.getInstance().pointingBlock = mc.theWorld.getBlockId(mob.blockX, mob.blockY, mob.blockZ);
                    blockExists = true;
                }
            }
            if (!blockExists)
                MegaMod.getInstance().pointingBlock = 0;
            if (!(mc.playerController instanceof PlayerControllerTest))
                MegaMod.getInstance().pointingEntity = pointedEntity;
        } else {
            MegaMod.getInstance().pointingEntity = null;
            MegaMod.getInstance().pointingBlock = 0;
        }
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 70.0F))
    private float getFovModifier(float var1) {
        if(MegaMod.getInstance().isZooming) { /* zoom zoom */
            MegaMod.getInstance().replaceOnScreenText("zoom", "Zoom (x" + (int) zoomFoV + ")", 0xffffff);
            return (getZoomFoV() + 70);
        }
        MegaMod.getInstance().hideOnScreenText("zoom");
        Float f = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(f == null)
            return 70.0F;
        return ((f * 70) + 70 + sprintFoV());
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 60.0F))
    private float getFoVModifierWater(float var1) {
        if(MegaMod.getInstance().isZooming)
            return (getZoomFoV() + 70);
        Float f = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(f == null)
            return 60.0F;
        return (f * 70) + 70  + sprintFoV() - 10;
    }

    private float getFovModifierDefault(float var1) {
        return 70;
    }

    @Inject(method = "orientCamera", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 9, shift = At.Shift.AFTER))
    public void redirectCatchThirdPersonView(float var1, CallbackInfo ci) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float var3 = var2.yOffset - 1.62F;
        double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var1;
        double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var1 - (double)var3;
        double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var1;
        double var27 = (double)(this.field_22227_s + (this.field_22228_r - this.field_22227_s) * var1) /
                ((MegaMod.getInstance().getCustomGameSettings().getOptionF("ThirdPerson Distance") * 30 + 1));
        float var13;
        float var28;
        var28 = var2.rotationYaw;
        var13 = var2.rotationPitch;
        if(MegaMod.thirdPersonView == 1)
            var13 += 180.0F;
        double var14 = (double)(-MathHelper.sin(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
        double var16 = (double)(MathHelper.cos(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
        double var18 = (double)(-MathHelper.sin(var13 / 180.0F * 3.1415927F)) * var27;

        for(int i = 0; i < 8; ++i) {
            float var21 = (float)((i & 1) * 2 - 1);
            float var22 = (float)((i >> 1 & 1) * 2 - 1);
            float var23 = (float)((i >> 2 & 1) * 2 - 1);
            var21 *= 0.1F;
            var22 *= 0.1F;
            var23 *= 0.1F;
            MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(var4 + (double) var21, var6 + (double) var22, var8 + (double) var23), Vec3D.createVector(var4 - var14 + (double) var21 + (double) var23, var6 - var18 + (double) var22, var8 - var16 + (double) var23));
            if (var24 != null) {
                double var25 = var24.hitVec.distanceTo(Vec3D.createVector(var4, var6, var8));
                if (MegaMod.thirdPersonView == 1)
                    var25 *= -1;
                if (var25 < var27) {
                    var27 = var25;
                }
            }
        }

        if(MegaMod.thirdPersonView == 1) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glRotatef(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var2.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
        GL11.glRotatef(var28 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    /*@Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityLiving;rotationPitch:F", opcode = Opcodes.GETFIELD))
    private float redirectPitch(EntityLiving entityLiving) {
        float pitch = entityLiving.rotationPitch;
        if(MegaMod.thirdPersonView == 1)
            return pitch - 6F;
        return pitch;
    }

    @Inject(method = "orientCamera", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", shift = At.Shift.BEFORE, ordinal = 6))
    private void orientThirdTwo(float v, CallbackInfo ci) {
        if(MegaMod.thirdPersonView == 1) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
    }*/

    @Inject(method = "func_4135_b", at = @At("HEAD"))
    private void renderHand(float f, int i, CallbackInfo ci) {
        Float temp = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(temp == null)
            temp = 0.1F;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        gs.setOption("Field of View", 0.0F);
        float zoom = MegaMod.getInstance().isZooming ? (getZoomFoV() + 70 - 10) : getFovModifierDefault(f);
        if(MegaMod.getInstance().isZooming && zoom <= 20)
            zoom = 20;
        GLU.gluPerspective(zoom, (float)this.mc.displayWidth / (float)this.mc.displayHeight,
                0.05F, this.farPlaneDistance * 2.0F);
        gs.setOption("Field of View", temp);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
