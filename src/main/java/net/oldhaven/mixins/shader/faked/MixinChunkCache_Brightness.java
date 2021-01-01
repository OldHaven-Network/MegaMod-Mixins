package net.oldhaven.mixins.shader.faked;

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkCache.class)
public abstract class MixinChunkCache_Brightness {
    @Shadow public abstract int getLightValue(int i, int i1, int i2);
    @Shadow private World worldObj;
    @Shadow public abstract int getBlockId(int i, int i1, int i2);

    @Shadow public abstract int getLightValueExt(int i, int i1, int i2, boolean b);

    @Inject(method = "getLightValue", at=@At("HEAD"), cancellable = true)
    private void getLightValue(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        int shaders = (int)(ModOptions.SHADERS.getAsFloat()*ModOptions.SHADERS.getTimes());
        if(shaders <= 1 || !ModOptions.DYNAMIC_LIGHTING.getAsBool())
            return;
        Object i = MMUtil.getFakeShaderThread().calculateLightRender(x, y, z, this.getLightValueExt(x, y, z, true));
        if ((int) i != 0)
            cir.setReturnValue((int) i);
    }

    @Inject(method = "getBrightness", at=@At("HEAD"), cancellable = true)
    private void returnBrightness(int x, int y, int z, int min, CallbackInfoReturnable<Float> cir) {
        int f = (int)(ModOptions.SHADERS.getAsFloat()*4);
        if(f == 3) {
            float density = ModOptions.SHADOW_DENSITY.getAsFloat()/2;
            long time = MMUtil.getMinecraftInstance().theWorld.getWorldTime();
            Vec3D curVec = Vec3D.createVector(x, y, z);
            if (time < 14000 || time > 22000) {
                // my math here is terrible, someone help pls
                float angle = MMUtil.getMinecraftInstance().theWorld.getCelestialAngle(0.0F);
                float toCos = MathHelper.cos(-angle * 3.141593F * 2.0F);
                float zi = (toCos - -0F) * 0.5F + 0.5F;
                double toSin = 1.0F - (1.0F - Math.sin(zi * 3.141593F)) * 0.99F;
                if (time < 6000)
                    toSin -= 0.3F;
                Vec3D toSun = Vec3D.createVector(x, y + 50, z + (toSin * 60));
                int blockID = getBlockId(x, y, z);
                if(blockID != Block.leaves.blockID) {
                    if (getBlockId(x, y + 1, z) != 0)
                        return;
                }
                MovingObjectPosition mOP = MMUtil.getMinecraftInstance().theWorld.rayTraceBlocks(curVec, toSun);
                if (mOP != null) {
                    float i = 0.5F-density;
                    if (i < min)
                        i = min;
                    /*float nB = i-0.1F;
                    if(nB > 0.0F)
                        updateNearby(x, y, z, nB);*/
                    cir.setReturnValue(i);
                }
            }
        }
        //return this.worldObj.worldProvider.lightBrightnessTable[var5];
    }
}
