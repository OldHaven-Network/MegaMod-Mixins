package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.MegaMod;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLiving.class)
public class MixinEntityLiving extends Entity {
    int sprintTimeout = 0;
    public MixinEntityLiving(World world) {
        super(world);
    }
    @Override
    public void entityInit() {

    }

    /*@Inject(method = "moveEntityWithHeading", at = @At(""))
    private void moveEntityWithHeading(CallbackInfo ci) {

    }*/

    private boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayerSP;
    }
    private boolean isFlying(Entity entity) {
        return MegaMod.getInstance().isFlying;
    }
    private boolean isSprinting(Entity entity) {
        return MegaMod.getInstance().isSprinting;
    }

    @Redirect(method = "moveEntityWithHeading",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;isInWater()Z", ordinal = 0, opcode = Opcodes.INVOKEVIRTUAL))
    public boolean isInWater(EntityLiving entity) {
        Boolean flyB = isFlying(this);
        if(flyB != null && flyB)
            return false;
        return entity.isInWater();
    }

    private double[] lastFly;
    private boolean wasLastFlying = false;
    private float flySpeed = 0;
    private long lastFlyTime = 0;
    @Redirect(method = "moveEntityWithHeading",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;moveEntity(DDD)V", ordinal = 2, opcode = Opcodes.INVOKEVIRTUAL))
    public void handleYValue(EntityLiving entity, double x, double y, double z) {
        if(!isPlayer(this)) {
            this.moveEntity(x, y, z);
            return;
        }
        CustomGameSettings cgs = MegaMod.getInstance().getCustomGameSettings();
        GameSettings gs = MegaMod.getMinecraftInstance().gameSettings;
        float gsFlySpeed = (cgs.getOptionF("Fly Speed") * 5) + 1;
        Boolean flyB = isFlying(this);
        Boolean sprintB = isSprinting(this);
        if(flyB) {
            MegaMod.getInstance().showOnScreenText("flying", "Flying", 0xFFFFFF);
            Minecraft mc = MegaMod.getMinecraftInstance();
            if(mc.currentScreen == null) {
                if(!wasLastFlying)
                    wasLastFlying = true;
                if (Keyboard.isKeyDown(gs.keyBindJump.keyCode)) {
                    this.motionY = y = this.flySpeed * gsFlySpeed;
                    if (this.flySpeed < 0.0F)
                        this.flySpeed += 0.035F;
                    else
                        this.flySpeed += 0.015F;
                } else if (Keyboard.isKeyDown(gs.keyBindSneak.keyCode)) {
                    this.motionY = y = this.flySpeed * gsFlySpeed;
                    if (this.flySpeed > 0.0F)
                        this.flySpeed -= 0.035F;
                    else
                        this.flySpeed -= 0.015F;
                } else {
                    if (this.flySpeed > 0.015F) {
                        this.flySpeed -= 0.015F;
                    } else if (this.flySpeed < -0.015F) {
                        this.flySpeed += 0.015F;
                    } else if (this.flySpeed >= -0.015F || this.flySpeed <= 0.015F)
                        this.flySpeed = 0.0F;
                    this.motionY = y = this.flySpeed * gsFlySpeed;
                }
                if (this.flySpeed > 0.1F)
                    this.flySpeed = 0.1F;
                else if (this.flySpeed < -0.1F)
                    this.flySpeed = -0.1F;
                x *= gsFlySpeed;
                z *= gsFlySpeed;
                if(sprintB) {
                    x *= 1.5F;
                    z *= 1.5F;
                }
                lastFly = new double[]{x, z};
            }
        } else {
            if(wasLastFlying) {
                if(!isOver(motionX, -0.25F, 0.25F) && !isOver(motionZ, -0.25F, 0.25F)) {
                    motionX = lastFly[0];
                    motionZ = lastFly[1];
                } else if(!isOver(motionX, -0.32F, 0.32F) && !isOver(motionZ, -0.32F, 0.32F)) {
                    motionX = (lastFly[0] /= 2);
                    motionZ = (lastFly[1] /= 2);
                }
                wasLastFlying = false;
            } else {
                if(!isOver(motionX, -0.25F, 0.25F) && !isOver(motionZ, -0.25F, 0.25F)) {
                    if (sprintB) {
                        x *= 1.55F;
                        z *= 1.55F;
                    }
                }
            }
            this.flySpeed = 0.0F;
            MegaMod.getInstance().hideOnScreenText("flying");
        }
        this.moveEntity(x, y, z);
    }

    private boolean isOver(double f, float min, float max) {
        boolean b = false;
        if(f < min)
            b = true;
        if(f > max)
            b = true;
        return b;
    }
    private double minMax(double f, float min, float max) {
        if(f < min)
            f = min;
        if(f > max)
            f = max;
        return f;
    }

    @Redirect(method = "moveEntityWithHeading",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;handleLavaMovement()Z", ordinal = 0, opcode = Opcodes.INVOKEVIRTUAL))
    public boolean handleLavaMovement(EntityLiving entity) {
        if(isPlayer(this) && isFlying(this))
            return false;
        return entity.handleLavaMovement();
    }

    @ModifyConstant(method = "moveEntityWithHeading", constant = @Constant(floatValue = 0.91F, ordinal = 2))
    public float editYValue(float f) {
        if(!isPlayer(this))
            return f;
        if(MegaMod.getInstance().isSprinting) {
            if(MegaMod.getPlayerInstance().getPlayer().movementInput.moveForward == 0.0F) {
                MegaMod.getInstance().isSprinting = false;
            }
        }
        Integer blockId = null;
        boolean flyB = isFlying(this);
        boolean sprintB = isSprinting(this);
        if(onGround && !flyB)
            blockId = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
        if(blockId == null && flyB) {
            if(motionY > 0.1f) {
                motionY -= 0.05f;
            } else if(motionY < -0.1f) {
                motionY += 0.05f;
            } else if(motionY > -0.1f || motionY < 0.1f) {
                motionY = 0;
            }
        }
        if(sprintB) {
            if(!flyB) {
                if (onGround && !isCollidedHorizontally) {
                    f += 0.15F;
                    if(f > 0.98F)
                        f = 0.98F;
                    sprintTimeout = 0;
                } else {
                    if (isCollidedHorizontally)
                        MegaMod.getInstance().isSprinting = false;
                    if (!onGround)
                        sprintTimeout++;
                    if (sprintTimeout > 20)
                        MegaMod.getInstance().isSprinting = false;
                }
            } else {
                if(isCollidedHorizontally)
                    MegaMod.getInstance().isSprinting = false;
            }
        }
        return f;
    }



    @Inject(method = "isOnLadder", at = @At("HEAD"), cancellable = true)
    private void isOnLadder(CallbackInfoReturnable<Boolean> ci) {
        if(isPlayer(this)) {
            Integer i = MegaMod.getInstance().getCustomGameSettings().getOptionI("Disable Ladders");
            boolean flyB = isFlying(this);
            if (i != null && i == 1 || flyB)
                ci.setReturnValue(false);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }
}
