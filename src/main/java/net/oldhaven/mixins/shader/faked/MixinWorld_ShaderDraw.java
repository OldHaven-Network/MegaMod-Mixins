package net.oldhaven.mixins.shader.faked;

import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.shaders.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld_ShaderDraw implements IWorld {
    @Shadow public abstract void markBlockNeedsUpdate(int i, int i1, int i2);
    @Shadow public abstract int getBlockId(int i, int i1, int i2);
    @Shadow public abstract boolean canBlockSeeTheSky(int x, int y, int z);

    float tick = 2001;
    float tick2 = 0;
    @Inject(method = "randomDisplayUpdates", at=@At("INVOKE"))
    private void tickInject(CallbackInfo ci) {
        if(tick > 2000) {
            MegaMod.getFakeShaderThread().setWorld(this);
            if (((int) ModOptions.SHADERS.getAsFloat() * 3) > 0) {
                MegaMod.getFakeShaderThread().cleanup();
                if (MegaMod.getMinecraftInstance().thePlayer != null) {
                    Vec3D pos = MegaMod.getMinecraftInstance().thePlayer.getPosition(1.0F);
                    int pX = (int) pos.xCoord;
                    int pY = (int) pos.yCoord;
                    int pZ = (int) pos.zCoord;
                    int up = 150;
                    for (int i = -up; i < up; i++) {
                        int x = pX + i;
                        for (int p = -up; p < up; p++) {
                            int z = pZ + p;
                            if (getBlockId(x, pY, z) <= 0)
                                continue;
                            // This is simply for optimization sake
                            MegaMod.getFakeShaderThread().addBlockToRender(x, pY, z);
                            p+=10;
                        }
                        i+=10;
                    }
                }
            }
            tick = 0;
        }
        tick += 0.01F;
    }
}
