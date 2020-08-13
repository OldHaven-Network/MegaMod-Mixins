package net.oldhaven.mixins.entity;

import net.minecraft.src.Entity;
import net.minecraft.src.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract void handleHealthUpdate(byte b);
    @Shadow public boolean onGround;
    @Shadow private int nextStepDistance;
    @Shadow public float distanceWalkedModified;
    @Shadow public double posX;
    @Shadow public double posZ;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        this.handleHealthUpdate((byte)1);
    }

    private float lastDistanceWalked = 0;
    @Redirect(method = "moveEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Entity;distanceWalkedModified:F", opcode = Opcodes.GETFIELD))
    private float distanceWalkedModified(Entity entity,  double d1, double d2, double d3) {
        if(!onGround)
            return lastDistanceWalked;
        lastDistanceWalked = distanceWalkedModified;
        return distanceWalkedModified;
    }
}
