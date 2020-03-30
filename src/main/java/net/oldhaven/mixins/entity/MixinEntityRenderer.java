package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.PlayerControllerTest;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.MegaMod;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
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

    private float flyFoV = 0.0F;
    private float sprintFoV() {
        if(MegaMod.getInstance().isSprinting) {
            if(flyFoV >= 5.0F)
                flyFoV = 5.0F;
            else
                flyFoV += 0.45F;
            return flyFoV;
        }
        if(flyFoV != 0.0F)
            flyFoV -= 0.45F;
        if(flyFoV <= 0.0F)
            flyFoV = 0.0F;
        return flyFoV;
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
        Float f = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(f == null)
            return 70.0F;
        return (f * 70) + 70 + sprintFoV();
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 60.0F))
    private float getFoVModifierWater(float var1) {
        Float f = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(f == null)
            return 60.0F;
        return (f * 70) + 70  + sprintFoV() - 10;
    }

    private float getFovModifierDefault(float var1) {
        return 70;
    }

    @Inject(method = "func_4135_b", at = @At("HEAD"))
    private void renderHand(float f, int i, CallbackInfo ci) {
        Float temp = MegaMod.getInstance().getCustomGameSettings().getOptionF("Field of View");
        if(temp == null)
            temp = 0.1F;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        gs.setOption("Field of View", 0.0F);
        GLU.gluPerspective(getFovModifierDefault(f), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        gs.setOption("Field of View", temp);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
